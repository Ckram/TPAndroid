package com.ensim.crakm.monbudget.Activites;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ensim.crakm.monbudget.Database.DatabaseContract;
import com.ensim.crakm.monbudget.Database.DatabaseHelper;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                ArrayList<Transaction> transactions = new ArrayList<Transaction>();
                DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = helper.getReadableDatabase();
                String[] projection = {
                        DatabaseContract.TableTransaction._ID,
                        DatabaseContract.TableTransaction.COLUMN_NAME_DESCRIPTION,
                        DatabaseContract.TableTransaction.COLUMN_NAME_CATEGORIE,
                        DatabaseContract.TableTransaction.COLUMN_NAME_MONTANT
                };
                Cursor c = db.query(
                        DatabaseContract.TableTransaction.TABLE_NAME,  // The table to query
                        projection,                               // The columns to return
                        null,                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );
                while (c.moveToNext()) {
                    float montant = c.getFloat(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction.COLUMN_NAME_MONTANT));
                    String categorie = c.getString(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction.COLUMN_NAME_CATEGORIE));
                    String description = c.getString(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction.COLUMN_NAME_DESCRIPTION));

                    Transaction transTemp = new Transaction(new Date(), montant, description, Categorie.GetCategorie(categorie));
                    Transaction.transactions.add(transTemp);
                    Log.d(TAG, transTemp.toString());
                }
                return null;

            }

            @Override
            protected void onPostExecute(Object o) {
                Log.d(TAG,Transaction.transactions.toString());
                super.onPostExecute(o);
            }
        };
        asyncTask.execute();



        com.github.clans.fab.FloatingActionButton fabCreer = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabAjouterTransactionPositive);
        Log.d(TAG,"Il y a rien");
        if (fabCreer != null) {
            Log.d(TAG,"Il y a qqch");
            fabCreer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"Appuie fab +");
                    Intent intentCreerTransaction = new Intent(MainActivity.this,CreateTransactionActivity.class);
                    startActivity(intentCreerTransaction);

                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if(drawer == null)
        {
            Log.d(TAG,"NULL A LA CON");
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Resources resources = getResources();
        String[] categories = resources.getStringArray(R.array.categories_array);
        for (String s : categories)
        {
            Categorie c = Categorie.GetCategorie(s);
        }
        for(String s : Categorie.categories.keySet())
        {
            Log.d(TAG,Categorie.categories.get(s).toString());
        }


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this,ListTransactionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {



        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

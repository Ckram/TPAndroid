package com.ensim.crakm.monbudget.Activites;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String TAG = "MainActivity";
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private com.github.clans.fab.FloatingActionButton nouvelleTransacPos;
    com.github.clans.fab.FloatingActionButton nouvelleTransacNeg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = helper.getReadableDatabase();
                String[] projectionTransaction = {
                        DatabaseContract.TableTransaction._ID,
                        DatabaseContract.TableTransaction.COLUMN_NAME_DESCRIPTION,
                        DatabaseContract.TableTransaction.COLUMN_NAME_CATEGORIE,
                        DatabaseContract.TableTransaction.COLUMN_NAME_MONTANT,
                        DatabaseContract.TableTransaction.COLUMN_NAME_DATE
                };
                Cursor c = db.query(
                        DatabaseContract.TableTransaction.TABLE_NAME,  // The table to query
                        projectionTransaction,                               // The columns to return
                        null,                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        DatabaseContract.TableTransaction.COLUMN_NAME_DATE +" DESC"                // The sort order
                );
                while (c.moveToNext()) {
                    float montant = c.getFloat(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction.COLUMN_NAME_MONTANT));
                    String categorie = c.getString(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction.COLUMN_NAME_CATEGORIE));
                    String description = c.getString(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction.COLUMN_NAME_DESCRIPTION));
                    long dateInLong = c.getLong(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction.COLUMN_NAME_DATE));
                    Date date = new Date(dateInLong);

                    Transaction transTemp = new Transaction(date, montant, description, Categorie.GetCategorie(categorie));
                    Transaction.addTransaction(transTemp);
                }
                String[] projectionCategorie = { DatabaseContract.TableCategories.COLUMN_NAME_NOMCATEGORIE};
                c = db.query(DatabaseContract.TableCategories.TABLE_NAME,
                        projectionCategorie,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                while (c.moveToNext())
                {
                    Categorie.GetCategorie(c.getString(c.getColumnIndexOrThrow(DatabaseContract.TableCategories.COLUMN_NAME_NOMCATEGORIE)));
                }
                return null;

            }

            @Override
            protected void onPostExecute(Object o) {

                super.onPostExecute(o);
            }
        };
        asyncTask.execute();



        nouvelleTransacPos = (FloatingActionButton) findViewById(R.id.fabAjouterTransactionPositive);
        if (nouvelleTransacPos != null) {

            nouvelleTransacPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentCreerTransaction = new Intent(MainActivity.this,CreateTransactionActivity.class);
                    intentCreerTransaction.putExtra("pos",true);
                    startActivity(intentCreerTransaction);

                }
            });
        }
        nouvelleTransacNeg = (FloatingActionButton) findViewById(R.id.fabAjouterTransactionNegative);
        if (nouvelleTransacNeg != null) {

            nouvelleTransacNeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentCreerTransaction = new Intent(MainActivity.this,CreateTransactionActivity.class);
                    intentCreerTransaction.putExtra("pos",false);
                    startActivity(intentCreerTransaction);

                }
            });
        }

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        if(mDrawer == null)
        {
            Log.d(TAG,"NULL A LA CON");
        }
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        nvDrawer.setNavigationItemSelectedListener(this);
        //setupDrawerContent(nvDrawer);
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
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(item.getItemId()) {
            case R.id.nav_camera:
                fragmentClass = ListTransactionActivity.class;
                break;
            case R.id.nav_gallery:
                fragmentClass = PieChartActivity.class;
                break;
            case R.id.nav_slideshow:
                fragmentClass = CreateCategorieActivity.class;
                break;
            default:
                fragmentClass = ListTransactionActivity.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
        return true;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


}

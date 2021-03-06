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
import com.ensim.crakm.monbudget.Model.Budget;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;
import com.github.clans.fab.FloatingActionButton;

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
        populerListes();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        Class startingFragment = ListTransactionActivity.class;
        try {
            fragmentManager.beginTransaction().replace(R.id.flContent, (Fragment)startingFragment.newInstance()).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

        }
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        nvDrawer.setNavigationItemSelectedListener(this);
        //setupDrawerContent(nvDrawer);
        Resources resources = getResources();
        String[] categories = resources.getStringArray(R.array.categories_array);
        for (String c : categories)
        {
            Log.d(TAG, "onCreate: " + c);
            Categorie.GetCategorie(c);
        }




    }
    private void populerListes()
    {
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
                    long id = c.getLong(c.getColumnIndexOrThrow(DatabaseContract.TableTransaction._ID));
                    Date date = new Date(dateInLong);

                    Transaction transTemp = new Transaction(date, montant, description, Categorie.GetCategorie(categorie));
                    transTemp.setId(id);
                    Transaction.addTransaction(transTemp);
                    Log.d(TAG, "doInBackground: " + Transaction.getAllTransactions());
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
                String[] projectionBudgets = {
                        DatabaseContract.TableBudgets._ID,
                        DatabaseContract.TableBudgets.COLUMN_NAME_CATEGORIE,
                        DatabaseContract.TableBudgets.COLUMN_NAME_MONTANT
                };
                c = db.query(
                        DatabaseContract.TableBudgets.TABLE_NAME,  // The table to query
                        projectionBudgets,                               // The columns to return
                        null,                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                // The sort order
                );
                while (c.moveToNext())
                {
                    float montantBudget = c.getFloat(c.getColumnIndexOrThrow(DatabaseContract.TableBudgets.COLUMN_NAME_MONTANT));
                    Categorie categorie = Categorie.GetCategorie(c.getString(c.getColumnIndexOrThrow(DatabaseContract.TableBudgets.COLUMN_NAME_CATEGORIE)));
                    //Budget.budgets.add(new Budget(Transaction.getTransactions(categorie),montantBudget,categorie));
                    Budget budget = new Budget(Transaction.getTransactions(categorie),montantBudget,categorie);
                }
                Log.d(TAG, "doInBackground: " + Budget.budgets.toString());
                return null;

            }

            @Override
            protected void onPostExecute(Object o) {

                super.onPostExecute(o);
            }
        };
        asyncTask.execute();
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(item.getItemId()) {
            case R.id.list_transactions:
                fragmentClass = ListTransactionActivity.class;
                break;
            case R.id.pie_chart:
                fragmentClass = PieChartActivity.class;
                break;
            case R.id.add_category:
                fragmentClass = CreateCategorieActivity.class;
                break;
            case R.id.mes_budgets:
                fragmentClass = MyBudgets.class;
                break;
            case R.id.settings:
                fragmentClass = Settings.class;
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

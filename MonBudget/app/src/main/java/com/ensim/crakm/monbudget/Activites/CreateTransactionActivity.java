package com.ensim.crakm.monbudget.Activites;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ensim.crakm.monbudget.Database.DatabaseContract;
import com.ensim.crakm.monbudget.Database.DatabaseHelper;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;

import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateTransactionActivity extends AppCompatActivity {

    TextView textViewDatePicker;
    DatePickerDialog datePickerDialog;
    Spinner spinnerCategorie;
    Toolbar toolbar;
    EditText revenu;
    EditText description;
    private int annee;
    private int mois;
    private int jour;
    String TAG = "CreationTransac";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        revenu = (EditText) findViewById(R.id.revenu);
        description = (EditText) findViewById(R.id.editTextDescription);
        toolbar = (Toolbar) findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);
        textViewDatePicker = (TextView) findViewById(R.id.textViewDatePicker);
        spinnerCategorie = (MaterialSpinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array,R.layout.adapter_categorie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);
        initialiserTextDatePicker();
        com.github.clans.fab.FloatingActionButton fabValider = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.validerTransaction);
        if (fabValider != null) {
            fabValider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"Appuie fab +");
                    creerTransaction();

                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("yolo","yolo");
        getMenuInflater().inflate(R.menu.menu_new_transac, menu);
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
    void initialiserTextDatePicker()
    {
        Calendar c = Calendar.getInstance();
        annee = c.get(Calendar.YEAR);
        mois = c.get(Calendar.MONTH);
        jour = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this,datePickerListener,annee,mois,jour);
        textViewDatePicker.setText(new StringBuilder().append(jour).append("/")
                .append(mois+1).append("/").append(annee));

    }
    public void afficherDateDialog(View v)
    {
        datePickerDialog.show();
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            annee = year;
            mois = monthOfYear;
            jour = dayOfMonth;
            textViewDatePicker.setText(new StringBuilder().append(jour).append("/")
                    .append(mois+1).append("/").append(annee));
        }

    };
    public void creerTransaction()
    {
        Log.d(TAG,"BOUTON");
        Transaction temp = new Transaction(new Date(annee,mois,jour),
                Float.parseFloat(revenu.getText().toString()),
                description.getText().toString(),
                Categorie.categories.get(spinnerCategorie.getSelectedItem()));
        Transaction.transactions.add(temp);
        DatabaseHelper helper = new DatabaseHelper(CreateTransactionActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_DESCRIPTION,temp.getDescription());
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_MONTANT,temp.getMontant());
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_CATEGORIE,temp.getCategorie().toString());
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.TableTransaction.TABLE_NAME,
                "null",
                values);

        Log.d(TAG,temp.toString());
        Log.d(TAG,Transaction.transactions.toString());
    }

}

package com.ensim.crakm.monbudget.Activites;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ensim.crakm.monbudget.Database.DatabaseContract;
import com.ensim.crakm.monbudget.Database.DatabaseHelper;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateTransactionActivity extends AppCompatActivity {

    TextView textViewDatePicker;
    DatePickerDialog datePickerDialog;
    Spinner spinnerCategorie;
    Toolbar toolbar;
    EditText montant;
    EditText description;
    private RelativeLayout relativeLayout;
    private int annee;
    private int mois;
    private int jour;
    private boolean signe;
    private int  positionUpdate;
    private ArrayAdapter<CharSequence> adapter;
    List <String> list;
    String TAG = "CreationTransac";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signe = this.getIntent().getBooleanExtra("pos",true);
        positionUpdate = this.getIntent().getIntExtra("positionUpdate",-1);
        setContentView(R.layout.activity_create_transaction);
        montant = (EditText) findViewById(R.id.revenu);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutCreationTransaction);
        TextInputLayout layout = (TextInputLayout) findViewById(R.id.layoutRevenu);
        if (!signe)
        {
            layout.setHint("Dépense");
        }
        description = (EditText) findViewById(R.id.editTextDescription);
        toolbar = (Toolbar) findViewById(R.id.toolbarTrans);
        setSupportActionBar(toolbar);
        textViewDatePicker = (TextView) findViewById(R.id.textViewDatePicker);
        spinnerCategorie = (MaterialSpinner) findViewById(R.id.spinner);
        list = new ArrayList<>(Categorie.categories.keySet());
        adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,list);
                /*ArrayAdapter.createFromResource(this,
                R.array.categories_array,R.layout.adapter_categorie);*/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(adapter);


        initialiserTextDatePicker();
        if (positionUpdate != -1)
        {
            layout.setHint("Montant");
            beforeUpdate();
        }
        com.github.clans.fab.FloatingActionButton fabValider = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.validerTransaction);
        if (fabValider != null) {
            fabValider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (positionUpdate == - 1)
                        creerTransaction();
                    else
                        modifierTransaction();
                }
            });
        }
    }

    private void modifierTransaction() {
        Date dateTransac = new Date(annee -1900,mois,jour);
        float montantTmp = Float.parseFloat(montant.getText().toString());
        String descString = description.getText().toString();
        Categorie categorie = Categorie.categories.get(spinnerCategorie.getSelectedItem());
        DatabaseHelper helper = new DatabaseHelper(CreateTransactionActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_DESCRIPTION,descString);
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_MONTANT,montantTmp);
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_CATEGORIE,categorie.getNomCategorie());
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_DATE,dateTransac.getTime());
        long updateRowId;
        updateRowId = db.update(
                DatabaseContract.TableTransaction.TABLE_NAME,values,"_id="+Transaction.getAllTransactions().get(positionUpdate).getId(),null);
        Transaction.removeTransaction(positionUpdate);
        Transaction temp = new Transaction(dateTransac,montantTmp,descString,categorie);
        temp.setId(updateRowId);
        Transaction.addTransaction(temp);
        Log.d(TAG,temp.toString());
        Log.d(TAG,Transaction.getAllTransactions().toString());

    }

    /**
     * Fonction qui permet de remplir les champs de la création de la transaction lorsque l'on veut faire une mise à jour
     */
    private void beforeUpdate()
    {
        Transaction transToUpdate = Transaction.getAllTransactions().get(positionUpdate);
        montant.setText(Float.toString(transToUpdate.getMontant()));
        spinnerCategorie.setSelection(list.indexOf(transToUpdate.getCategorie().getNomCategorie())+1,true);
        description.setText(transToUpdate.getDescription());
        Date dateTmp = transToUpdate.getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTmp);
        annee = cal.get(Calendar.YEAR);
        mois = cal.get(Calendar.MONTH);
        jour = cal.get(Calendar.DAY_OF_MONTH);
        writeDate();
    }

    /**
     * Fonction permettant de réecrire la date dans le textViewCorrespondant
     */
    private void writeDate()
    {
        textViewDatePicker.setText(new StringBuilder().append(jour).append("/")
                .append(mois+1).append("/").append(annee));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_new_transac, menu);
        if (positionUpdate!=-1)
            menu.findItem(R.id.action_supprimer).setIcon(R.drawable.ic_delete_white_24dp);
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
        writeDate();


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
            writeDate();
        }

    };
    public void creerTransaction()
    {
        Date dateTransac = new Date(annee -1900,mois,jour);
        float montantTmp = Float.parseFloat(montant.getText().toString());
        String descString = description.getText().toString();
        Categorie categorie = Categorie.categories.get(spinnerCategorie.getSelectedItem());

        if (!signe)
        {
            montantTmp = -montantTmp;
        }
        DatabaseHelper helper = new DatabaseHelper(CreateTransactionActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_DESCRIPTION,descString);
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_MONTANT,montantTmp);
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_CATEGORIE,categorie.getNomCategorie());
        values.put(DatabaseContract.TableTransaction.COLUMN_NAME_DATE,dateTransac.getTime());
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.TableTransaction.TABLE_NAME,
                "null",
                values);
        Transaction temp = new Transaction(dateTransac,montantTmp,descString,categorie);
        temp.setId(newRowId);
        Transaction.addTransaction(temp);
        Snackbar.make(relativeLayout,"Nouvelle Transaction ajouté",Snackbar.LENGTH_LONG).show();



    }

}

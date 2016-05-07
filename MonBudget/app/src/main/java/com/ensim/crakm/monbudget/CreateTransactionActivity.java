package com.ensim.crakm.monbudget;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class CreateTransactionActivity extends AppCompatActivity {

    TextView textViewDatePicker;
    DatePickerDialog datePickerDialog;
    Spinner spinnerCategorie;
    int annee;
    int mois;
    int jour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        textViewDatePicker = (TextView) findViewById(R.id.textViewDatePicker);
        spinnerCategorie = (Spinner) findViewById(R.id.spinnerCategorie);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array,R.layout.adapter_categorie);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCategorie.setAdapter(adapter);
        initialiserTextDatePicker();





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

}

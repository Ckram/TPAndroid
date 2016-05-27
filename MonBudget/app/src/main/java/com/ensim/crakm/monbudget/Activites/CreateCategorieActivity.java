package com.ensim.crakm.monbudget.Activites;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ensim.crakm.monbudget.Database.DatabaseContract;
import com.ensim.crakm.monbudget.Database.DatabaseHelper;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.R;

public class CreateCategorieActivity extends AppCompatActivity {

    String TAG = "CreationCategorie";
    EditText nomCategorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_categorie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nomCategorie = (EditText) findViewById(R.id.nomCategorie);
        com.github.clans.fab.FloatingActionButton fabValider = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.validerNouvelleCategorie);
        if (fabValider != null) {
            fabValider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"Appuie fab");
                    creerCategorie();

                }
            });
        }
    }

    private void creerCategorie()
    {
        if (Categorie.categories.containsKey(nomCategorie.getText().toString()))
        {
            Toast.makeText(CreateCategorieActivity.this, "La catégorie existe déjà", Toast.LENGTH_SHORT).show();
            return;
        }
        Categorie categorieTmp = Categorie.GetCategorie(nomCategorie.getText().toString());

        DatabaseHelper helper = new DatabaseHelper(CreateCategorieActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableCategories.COLUMN_NAME_NOMCATEGORIE,categorieTmp.getNomCategorie());
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.TableCategories.TABLE_NAME,
                "null",
                values);
        Toast.makeText(CreateCategorieActivity.this, "Catégorie "+ categorieTmp + "bien ajouté", Toast.LENGTH_SHORT).show();

    }

}

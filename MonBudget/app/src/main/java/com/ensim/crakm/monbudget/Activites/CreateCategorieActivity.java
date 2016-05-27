package com.ensim.crakm.monbudget.Activites;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ensim.crakm.monbudget.Database.DatabaseContract;
import com.ensim.crakm.monbudget.Database.DatabaseHelper;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.R;

public class CreateCategorieActivity extends Fragment {

    String TAG = "CreationCategorie";
    EditText nomCategorie;
    Button boutonCreer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nomCategorie = (EditText) view.findViewById(R.id.nomCategorie);
        boutonCreer = (Button) view.findViewById(R.id.validerNouvelleCategorie);
        boutonCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creerCategorie();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.activity_create_categorie,container,false);
    }

    private void creerCategorie()
    {
        if (Categorie.categories.containsKey(nomCategorie.getText().toString()))
        {
            Toast.makeText(CreateCategorieActivity.this.getActivity(), "La catégorie existe déjà", Toast.LENGTH_SHORT).show();
            return;
        }
        Categorie categorieTmp = Categorie.GetCategorie(nomCategorie.getText().toString());

        DatabaseHelper helper = new DatabaseHelper(CreateCategorieActivity.this.getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableCategories.COLUMN_NAME_NOMCATEGORIE,categorieTmp.getNomCategorie());
        long newRowId;
        newRowId = db.insert(
                DatabaseContract.TableCategories.TABLE_NAME,
                "null",
                values);
        Toast.makeText(CreateCategorieActivity.this.getActivity(), "Catégorie "+ categorieTmp + "bien ajouté", Toast.LENGTH_SHORT).show();

    }

}

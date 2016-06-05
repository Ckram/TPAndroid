package com.ensim.crakm.monbudget.Activites;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ensim.crakm.monbudget.Database.DatabaseContract;
import com.ensim.crakm.monbudget.Database.DatabaseHelper;
import com.ensim.crakm.monbudget.Model.Budget;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Marc on 03/06/2016.
 */
public class MyBudgets extends Fragment {
    static String TAG = "My Budgets";
    Button buttonNewBudget;
    MaterialDialog dialog;
    MaterialSpinner spinnerCategorie;
    EditText montantNouveauBudget;
    ArrayAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my_budgets,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new MaterialDialog.Builder(getActivity())
                .title("Nouveau Budget")
                .customView(R.layout.dialog_nouveau_budget, false)
                .backgroundColor(getResources().getColor(R.color.colorPrimaryLight))
                .positiveText("Valider nouveau budget").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Budget budget = new Budget();
                        Categorie categorie = Categorie.categories.get(spinnerCategorie.getSelectedItem());
                        budget.setTransactions(Transaction.getTransactions(categorie));
                        budget.setCategorie(categorie);
                        budget.setMontantBudget(Float.parseFloat(montantNouveauBudget.getText().toString()));
                        Budget.getBudgets().add(budget);
                        DatabaseHelper helper = new DatabaseHelper(getActivity());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseContract.TableBudgets.COLUMN_NAME_MONTANT,budget.getMontantBudget());
                        values.put(DatabaseContract.TableBudgets.COLUMN_NAME_CATEGORIE,budget.getCategorie().getNomCategorie());
                        long newRowId;
                        newRowId = db.insert(
                                DatabaseContract.TableBudgets.TABLE_NAME,
                                "null",
                                values);
                        Snackbar.make(getView(),"Nouveau budget bien ajouté",Snackbar.LENGTH_SHORT).show();

                        Log.d(TAG,Budget.getBudgets().toString());

                    }
                }).build();
        buttonNewBudget = (Button)view.findViewById(R.id.nouveauBudget);

        buttonNewBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                View viewDialog = dialog.getCustomView();
                spinnerCategorie = (MaterialSpinner) viewDialog.findViewById(R.id.choixCategorieBudget);
                ArrayList list = new ArrayList(Categorie.categories.keySet());
                adapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategorie.setAdapter(adapter);
                montantNouveauBudget =(EditText) viewDialog.findViewById(R.id.montantBudget);


            }
        });
    }



}

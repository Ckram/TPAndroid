package com.ensim.crakm.monbudget.Activites;

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
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ensim.crakm.monbudget.Database.DatabaseContract;
import com.ensim.crakm.monbudget.Database.DatabaseHelper;
import com.ensim.crakm.monbudget.Model.Budget;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;

/**
 * Created by Marc on 06/06/2016.
 */
public class Settings extends Fragment{
    Button resetDatabase;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout,container,false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resetDatabase = (Button) view.findViewById(R.id.resetDatabase);
        resetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dsq", "onClick: dsqd");
                Toast.makeText(getActivity(), "dsds", Toast.LENGTH_SHORT).show();
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).
                        title("Validation").
                        content("Voulez vous vraiment réintialiser la base de données ?")
                        .positiveText("oui")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DatabaseHelper helper = new DatabaseHelper(getActivity());
                                SQLiteDatabase db = helper.getWritableDatabase();
                                db.execSQL(DatabaseContract.TableCategories.SQL_DELETE_ENTRIES);
                                db.execSQL(DatabaseContract.TableTransaction.SQL_DELETE_ENTRIES);
                                db.execSQL(DatabaseContract.TableBudgets.SQL_DELETE_ENTRIES);
                                db.execSQL(DatabaseContract.TableTransaction.SQL_CREATE_ENTRIES);
                                db.execSQL(DatabaseContract.TableCategories.SQL_CREATE_ENTRIES);
                                db.execSQL(DatabaseContract.TableBudgets.SQL_CREATE_ENTRIES);
                                Snackbar.make(view,"Base de données supprimé",Snackbar.LENGTH_SHORT);
                                Transaction.getAllTransactions().clear();
                                Transaction.getTransactionsNeg().clear();
                                Transaction.getTransactionsPos().clear();
                                Categorie.categories.clear();
                                Budget.budgets.clear();
                            }
                        })
                        .negativeText("non")
                        .show();
            }
        });
    }
}

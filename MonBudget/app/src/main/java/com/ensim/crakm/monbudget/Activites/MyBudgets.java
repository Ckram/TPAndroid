package com.ensim.crakm.monbudget.Activites;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import java.util.List;

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
    StaggeredGridLayoutManager gridLayoutManager;

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
                        db.close();
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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //recyclerView.setHasFixedSize(true);
        gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(gridLayoutManager);
        BudgetRecyclerViewAdapter recyclerViewAdapter = new BudgetRecyclerViewAdapter(getContext(),Budget.budgets);
        recyclerView.setAdapter(recyclerViewAdapter);
        //adapterBudgets = new BudgetsArrayAdapter(getContext(),R.layout.budget_item_view,(ArrayList)Budget.budgets);

    }



}

class BudgetRecyclerViewAdapter extends RecyclerView.Adapter<BudgetViewHolders>
{
    private List<Budget> itemList;
    private Context context;

    public BudgetRecyclerViewAdapter(Context context, List<Budget> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public BudgetViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgets_list, null);
        BudgetViewHolders rcv = new BudgetViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(BudgetViewHolders holder, int position) {
        float montantTotal =itemList.get(position).getMontantBudget();
        float montantDepense = itemList.get(position).getCategorie().getSpendingsInMonth();
        float reste = montantTotal - Math.abs(montantDepense);
        holder.montantTotal.setText(Float.toString(montantTotal)+" €");
        holder.montantDepense.setText(Float.toString(montantDepense)+" €");
        holder.reste.setText(Float.toString(reste)+" €");
        holder.categorie.setText(itemList.get(position).getCategorie().getNomCategorie());

        if (reste>=0)
        {
            int green = holder.reste.getResources().getColor(R.color.colorPrimary);
            holder.categorie.setBackgroundColor(green);
            holder.reste.setTextColor(green);
        }
        else
        {
            int red = holder.reste.getResources().getColor(R.color.colorAccent);
            holder.categorie.setBackgroundColor(red);
            holder.reste.setTextColor(red);
        }

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}


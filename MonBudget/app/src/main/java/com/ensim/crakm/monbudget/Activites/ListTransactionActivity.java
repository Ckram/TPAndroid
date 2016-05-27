package com.ensim.crakm.monbudget.Activites;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;

import java.util.ArrayList;

public class ListTransactionActivity extends android.support.v4.app.Fragment {
    private TransactionArrayAdapter adapter;
    ListView listViewTransac;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        transactions.addAll(Transaction.getTransactionsNeg());
        transactions.addAll(Transaction.getTransactionsPos());
        adapter = new TransactionArrayAdapter(getActivity(),R.layout.list_item_transaction, transactions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_list_transaction, parent, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        listViewTransac = (ListView) view.findViewById(R.id.listViewTransactions);
        listViewTransac.setAdapter(adapter);
        super.onViewCreated(view,savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}

class TransactionArrayAdapter extends ArrayAdapter<Transaction>
{
    Context context;
    int layoutResourceId;
       ArrayList<Transaction> transactions;
    public TransactionArrayAdapter (Context context,int layoutResourceId,ArrayList<Transaction> transactions)
    {
        super(context, layoutResourceId, transactions);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.transactions = transactions;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ligne = convertView;
        TransactionHolder holder = null;
        if(ligne == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            ligne = inflater.inflate(layoutResourceId, parent, false);

            holder = new TransactionHolder();
            holder.montant = (TextView)ligne.findViewById(R.id.montantTransaction);
            holder.description = (TextView)ligne.findViewById(R.id.descriptionTransaction);
            holder.categorie = (TextView)ligne.findViewById(R.id.categorieTransaction);

            ligne.setTag(holder);
        }
        else
        {
            holder = (TransactionHolder) ligne.getTag();
        }

        Transaction transaction = transactions.get(position);

        holder.montant.setText(Float.toString(transaction.getMontant()));
        holder.description.setText(transaction.getDescription());
        holder.categorie.setText(transaction.getCategorie().getNomCategorie());


        /*
        holder.imgIcon.setImageResource(weather.icon);
        */
        return ligne;

    }



    static class TransactionHolder
    {
        TextView montant;
        TextView description;
        TextView categorie;
    }
}

package com.ensim.crakm.monbudget.Activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ListTransactionActivity extends android.support.v4.app.Fragment {
    private TransactionArrayAdapter adapter;
    private ArrayAdapter categorieAdapter;
    private ArrayList<Transaction> transactionsToPrint;
    String [] mois;
    Calendar calendar;

    ListView listViewTransac;
    MaterialSpinner spinnerChoix;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         calendar = Calendar.getInstance();

        transactionsToPrint = Transaction.getTransactionsInMonth(calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

        mois = DateFormatSymbols.getInstance(Locale.FRANCE).getMonths();
        for (int i=0; i<mois.length;i++)
        {
            mois[i]= StringUtils.capitalize(mois[i]);
            Log.d("Transactions",mois[i]);
        }
        adapter = new TransactionArrayAdapter(getActivity(),R.layout.list_item_transaction, transactionsToPrint);

        categorieAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,mois);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_list_transaction, parent, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        listViewTransac = (ListView) view.findViewById(R.id.listViewTransactions);
        listViewTransac.setAdapter(adapter);
        listViewTransac.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),CreateTransactionActivity.class);
                intent.putExtra("positionUpdate",position);
                startActivity(intent);
                return false;
            }
        });
        spinnerChoix = (MaterialSpinner) view.findViewById(R.id.spinnerChoixTri);
        spinnerChoix.setAdapter(categorieAdapter);
        spinnerChoix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transactionsToPrint.clear();
                transactionsToPrint.addAll(Transaction.getTransactionsInMonth(position,2016));

                Log.d("Transactions",transactionsToPrint.toString());

                adapter.notifyDataSetInvalidated();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerChoix.setSelection(calendar.get(Calendar.MONTH)+1);
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
            holder.date = (TextView)ligne.findViewById(R.id.dateTransaction);
            holder.logo = (ImageView) ligne.findViewById(R.id.logoTransaction);

            ligne.setTag(holder);
        }
        else
        {
            holder = (TransactionHolder) ligne.getTag();
        }

        Transaction transaction = transactions.get(position);

        holder.montant.setText(Float.toString(transaction.getMontant())+" €");
        holder.description.setText(transaction.getDescription());
        holder.categorie.setText(transaction.getCategorie().getNomCategorie());
        holder.date.setText(transaction.getDateString());
        if (transaction.getMontant() > 0)
        {
            holder.logo.setImageResource(R.drawable.plus);
        }else
        {
            holder.logo.setImageResource(R.drawable.minus);
        }


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
        TextView date;
        ImageView logo;
    }
}

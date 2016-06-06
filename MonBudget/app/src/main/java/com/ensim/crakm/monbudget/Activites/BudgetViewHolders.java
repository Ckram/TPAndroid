package com.ensim.crakm.monbudget.Activites;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ensim.crakm.monbudget.R;

/**
 * Created by Marc on 06/06/2016.
 */
public class BudgetViewHolders extends RecyclerView.ViewHolder{

    public TextView montantDepense;
    public TextView montantTotal;
    public TextView reste;
    public TextView categorie;

    public BudgetViewHolders(View itemView) {
        super(itemView);
        montantDepense = (TextView) itemView.findViewById(R.id.montantDepense);
        montantTotal = (TextView) itemView.findViewById(R.id.montantBudget);
        reste = (TextView) itemView.findViewById(R.id.reste);
        categorie = (TextView) itemView.findViewById(R.id.titreCarte);
    }


}

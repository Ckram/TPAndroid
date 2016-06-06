package com.ensim.crakm.monbudget.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 04/06/2016.
 */
public class Budget  {
    public static List<Budget> budgets = new ArrayList<Budget>();
    private List<Transaction> transactions;
    Categorie categorie;
    private float montantBudget;

    public Budget(List<Transaction> transactions, float montantBudget, Categorie categorie) {
        this.transactions = transactions;
        this.montantBudget = montantBudget;
        this.categorie = categorie;
        budgets.add(this);
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "transactions=" + transactions +
                ", categorie=" + categorie +
                ", montantBudget=" + montantBudget +
                '}';
    }
    public void addBudget()
    {
        budgets.add(this);
    }

    public Budget() {
        montantBudget = 0;
    }

    public static List<Budget> getBudgets() {
        return budgets;
    }

    public static void setBudgets(List<Budget> budgets) {

        Budget.budgets = budgets;
    }

    public float getMontantBudget() {
        return montantBudget;
    }

    public void setMontantBudget(float montantBudget) {
        this.montantBudget = montantBudget;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        for (Transaction transaction :transactions)
        {
            montantBudget+=transaction.getMontant();
        }
        this.transactions = transactions;
    }


}

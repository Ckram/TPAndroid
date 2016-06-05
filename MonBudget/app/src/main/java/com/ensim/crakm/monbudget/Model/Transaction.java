package com.ensim.crakm.monbudget.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Moi on 06/05/2016.
 */
public class Transaction implements Parcelable {
    private static ArrayList<Transaction> transactionsPos = new ArrayList<Transaction>();
    private static ArrayList<Transaction> transactionsNeg = new ArrayList<Transaction>();
    private static ArrayList<Transaction> allTransactions = new ArrayList<Transaction>();
    private static float sommmeRevenu =0;
    private  static float sommeDepense = 0;
    private float montant;
    private Date date;
    private String description;
    private Categorie categorie;
    private long id;


    /**
     * Fonction permettant d'ajouter une transaction et qui gère l'ajout dans les listes correspondantes
     * @param transac
     * @return la transaction ajouté
     */
    public static Transaction addTransaction(Transaction transac)
    {
        allTransactions.add(transac);
        if (transac.montant>=0)
        {
            transactionsPos.add(transac);
            sommmeRevenu += transac.getMontant();
        }

        else
        {
            transactionsNeg.add(transac);
            sommeDepense += transac.getMontant();
        }
        return transac;

    }

    public static void removeTransaction(int index)
    {
        Transaction transactionToDelete =allTransactions.get(index) ;
        if (transactionToDelete.montant>=0)
        {
            sommmeRevenu -= transactionToDelete.montant;
            transactionsPos.remove(transactionToDelete);
        }else
        {
            sommeDepense -= transactionToDelete.montant;
            transactionsNeg.remove(transactionToDelete);
        }
        Transaction.allTransactions.remove(index);
    }

    public static ArrayList<Transaction> getTransactionsInMonth(int month,int year)
    {
        ArrayList transactionsInMonth = new ArrayList();
        for (Transaction transaction : Transaction.allTransactions)
        {

            if (transaction.getDate().getMonth() == month && (transaction.getDate().getYear()+1900) ==year)
                transactionsInMonth.add(transaction);
        }
        return transactionsInMonth;
    }


    //region constructor
    public Transaction(Date date, float montant, String description, Categorie categorie) {
        this.date = date;
        this.montant = montant;
        this.description = description;
        this.categorie = categorie;
    }
    public Transaction()
    {}
    //endregion

    //region Getters & Setters
    public static ArrayList<Transaction> getTransactions(Categorie categorie)
    {
        ArrayList<Transaction> transactionsInCategory = new ArrayList<>();
        for (Transaction transaction : allTransactions)
        {
            if (transaction.getCategorie().equals(categorie))
                transactionsInCategory.add(transaction);
        }
        return transactionsInCategory;
    }

    public static ArrayList<Transaction> getAllTransactions() {
        Collections.sort(allTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction lhs, Transaction rhs) {
                if (lhs.getDate().getTime()>rhs.getDate().getTime())
                    return -1;
                else return 0;
            }
        });
        return allTransactions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateString()
    {
        DateFormat dfl = DateFormat.getDateInstance(DateFormat.FULL);
        return dfl.format(date);

    }

    public static ArrayList<Transaction> getTransactionsNeg() {
        return transactionsNeg;
    }

    public static void setTransactionsNeg(ArrayList<Transaction> transactionsNeg) {
        Transaction.transactionsNeg = transactionsNeg;
    }

    public static ArrayList<Transaction> getTransactionsPos() {
        return transactionsPos;
    }

    public static void setTransactionsPos(ArrayList<Transaction> transactionsPos) {
        Transaction.transactionsPos = transactionsPos;
    }

    public static float getSommmeRevenu() {
        return sommmeRevenu;
    }

    public static void setSommmeRevenu(float sommmeRevenu) {
        Transaction.sommmeRevenu = sommmeRevenu;
    }

    public static float getSommeDepense() {
        return sommeDepense;
    }

    public static void setSommeDepense(float sommeDepense) {
        Transaction.sommeDepense = sommeDepense;
    }
    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "montant=" + montant +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", categorie=" + categorie +
                ", id=" + id +
                '}';
    }
    //endregion

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.montant);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeString(this.description);
        dest.writeParcelable(this.categorie, flags);
        dest.writeLong(this.id);
    }

    protected Transaction(Parcel in) {
        this.montant = in.readFloat();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.description = in.readString();
        this.categorie = in.readParcelable(Categorie.class.getClassLoader());
        this.id = in.readLong();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    //endregion
}

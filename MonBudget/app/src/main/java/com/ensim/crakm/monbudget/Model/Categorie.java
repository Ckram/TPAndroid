package com.ensim.crakm.monbudget.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Moi on 07/05/2016.
 */
public class Categorie implements Parcelable {
    public static HashMap<String,Categorie> categories = new HashMap<String, Categorie>();
    String nomCategorie;
    Budget budget;


    public static Categorie GetCategorie(String nomCategorie) {
        if (!categories.containsKey(nomCategorie))
            categories.put(nomCategorie, new Categorie(nomCategorie));
        return  categories.get(nomCategorie);
    }

    private  Categorie(String s)
    {
        this.nomCategorie =s;
        budget = new Budget();
        budget.setTransactions(Transaction.getTransactions(this));
    };

    public float getSpendingsInMonth()
    {
        float spendingInMonth = 0;
        Calendar cal = Calendar.getInstance();
        int moisCourant = cal.get(Calendar.MONTH);
        int anneeCourante = cal.get(Calendar.YEAR);
        for (Transaction transaction : Transaction.getTransactions(this))
        {
            cal.setTime(transaction.getDate());
            if (cal.get(Calendar.MONTH)== moisCourant && cal.get(Calendar.YEAR)==anneeCourante)
            {
                spendingInMonth+=transaction.getMontant();
            }
        }
        return spendingInMonth;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "nomCategorie='" + nomCategorie + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nomCategorie);
    }

    protected Categorie(Parcel in) {
        this.nomCategorie = in.readString();
    }

    public static final Parcelable.Creator<Categorie> CREATOR = new Parcelable.Creator<Categorie>() {
        @Override
        public Categorie createFromParcel(Parcel source) {
            return new Categorie(source);
        }

        @Override
        public Categorie[] newArray(int size) {
            return new Categorie[size];
        }
    };
}

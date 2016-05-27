package com.ensim.crakm.monbudget.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Moi on 06/05/2016.
 */
public class Transaction implements Parcelable {
    private static ArrayList<Transaction> transactionsPos = new ArrayList<Transaction>();
    private static ArrayList<Transaction> transactionsNeg = new ArrayList<Transaction>();
    private static float sommmeRevenu =0;
    private  static float sommeDepense = 0;
    private float montant;
    private Date date;
    private String description;
    private Categorie categorie;


    /**
     * Fonction permettant d'ajouter une transaction et qui gère l'ajout dans les listes correspondantes
     * @param transac
     * @return la transaction ajouté
     */
    public static Transaction addTransaction(Transaction transac)
    {

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
    }

    protected Transaction(Parcel in) {
        this.montant = in.readFloat();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.description = in.readString();
        this.categorie = in.readParcelable(Categorie.class.getClassLoader());
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

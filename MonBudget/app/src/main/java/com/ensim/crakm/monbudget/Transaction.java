package com.ensim.crakm.monbudget;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Moi on 06/05/2016.
 */
public class Transaction implements Parcelable {

    float montant;
    Date date;

    public Transaction(Date date, float montant) {
        this.date = date;
        this.montant = montant;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "montant=" + montant +
                ", date=" + date +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.montant);
        dest.writeLong(date != null ? date.getTime() : -1);
    }

    protected Transaction(Parcel in) {
        this.montant = in.readFloat();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}

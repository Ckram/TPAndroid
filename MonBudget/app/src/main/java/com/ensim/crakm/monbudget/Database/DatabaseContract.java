package com.ensim.crakm.monbudget.Database;

import android.provider.BaseColumns;

/**
 * Created by Moi on 21/05/2016.
 */
public class DatabaseContract {

    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "database.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String VARCHAR_TYPE = " CHAR(255)";
    private static final String FLOAT_TYPE = " FLOAT";

    private static final String COMMA_SEP = ",";
    private DatabaseContract(){}

    public static abstract class TableTransaction implements BaseColumns {
        public static final String TABLE_NAME = "transactions";
        public static final String COLUMN_NAME_ENTRY_ID = "idTransaction";
        public static final String COLUMN_NAME_MONTANT = "montant";
        public static final String COLUMN_NAME_CATEGORIE = "categorie";
        public static final String COLUMN_NAME_DESCRIPTION = "description";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TableTransaction.TABLE_NAME + " (" +
                        TableTransaction._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        TableTransaction.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        TableTransaction.COLUMN_NAME_MONTANT + FLOAT_TYPE + COMMA_SEP +
                        TableTransaction.COLUMN_NAME_CATEGORIE + VARCHAR_TYPE +COMMA_SEP +
                        TableTransaction.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TableTransaction.TABLE_NAME;

    }
}

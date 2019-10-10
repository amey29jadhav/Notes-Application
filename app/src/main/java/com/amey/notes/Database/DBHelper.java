package com.amey.notes.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "notes_manager.db";
    private static final int DB_VERSION = 1;
    private static Dao<AddNotesTable, Integer> daoNotes;
    private final Context myContext;
    private static DBHelper ormLiteHelper  = null;



    public static DBHelper getHelperInstance(Context context){

        if (ormLiteHelper == null){
            ormLiteHelper = new DBHelper(context);
        }
        return ormLiteHelper;
    }

    public DBHelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, AddNotesTable/**/.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        boolean ignoreError = true;

        try {
            TableUtils.dropTable(connectionSource, AddNotesTable.class, ignoreError);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Dao<AddNotesTable, Integer> getNotesDao(){

        if (daoNotes == null){
            try {
                daoNotes = getDao(AddNotesTable.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return daoNotes;
    }
}

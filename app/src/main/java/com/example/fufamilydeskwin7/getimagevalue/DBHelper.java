package com.example.fufamilydeskwin7.getimagevalue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;

/**
 * Created by fufamilyDeskWin7 on 2016/1/13.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DBDataNAME = "Dataname";
    public static final String DBHSV_Spoint = "HSV_Spoint_value";
    public static final String DBG7_C80100point = "G7_C80100point_value";
    public static final String DBG11_C80100point = "G11_C80100point_value";
    public static final String DBcompleteset ="DBcompleteset";
    public static final String DBfloor ="DBfloor";
    public static final String DBpeople ="DBpeople";
    public static final String DBpillar ="DBpillar";

    public static final String DBTABLE_NAME = "VALUETABLE";  //表格名稱

    public static final String DATABASE_NAME = "mydata.db";    // 資料庫名稱
    public static final int VERSION = 1;    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
//    private static SQLiteDatabase database;// 資料庫物件，固定的欄位變數
    private static final String TAG = "OCVSample::Activity";


    public DBHelper(Context context) {
        //, String name, SQLiteDatabase.CursorFactory factory, int version
        //    super(context, name, factory, version);
        super(context, DATABASE_NAME, null, VERSION);

    }

// 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
//public static SQLiteDatabase getDatabase(Context context) {
//    if (database == null || !database.isOpen()) {
//        database = new DBHelper(context, DATABASE_NAME,
//                null, VERSION).getWritableDatabase();
//    }
//    return database;
//}

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "dbhelper star");
        final String creatTable = "CREATE TABLE IF NOT EXISTS " + DBTABLE_NAME + "( " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBDataNAME + " INTEGER(200), " +
                DBHSV_Spoint + " CHAR(32), " +
                DBG7_C80100point + " CHAR(32), " +
                DBG11_C80100point + " CHAR(32), " +
                DBcompleteset + " BINARY, " +
                DBfloor + " BINARY, " +
                DBpeople + " BINARY, " +
                DBpillar + " BINARY " +
               " );";
        Log.i(TAG, "table set");
        db.execSQL(creatTable);
//        final String creatTable = "CREATE TABLE IF NOT EXISTS " + DBTABLE_NAME + "( " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                NAME + " CHAR(32), " + TIME1H + " CHAR(3)"//, " + TIME1M + " INTEGER(1), " + TIME2H + " INTEGER(1)," + TIME2M + " INTEGER(1)"
//                +");";
//        db.execSQL(creatTable);
        Log.i(TAG, "table set OK");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String drop = "DROP TABLE IF EXISTS " + DBTABLE_NAME;
        db.execSQL(drop);
        onCreate(db);
    }
}

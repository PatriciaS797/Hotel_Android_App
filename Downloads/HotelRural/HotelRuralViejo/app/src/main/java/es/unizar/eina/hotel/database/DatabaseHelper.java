package es.unizar.eina.hotel.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 6;

    private static final String TAG = "DatabaseHelper";

    /**
     * Database creation sql statement for rooms
     */
    private static final String DATABASE_ROOMS_CREATE =
            "create table rooms ( _id integer primary key, maxOcup integer, description text, " +
                    "price1Per real, additionalCharge real);";

    /**
     * Database creation sql statement for reservations
     */
    private static final String DATABASE_RES_CREATE = "create table reservations (_id integer primary key " +
            "autoincrement, nombreCliente text, movilCliente text,entrada text, salida text, price real check (price > 0));";

    /**
     * Database creation sql statement for association between reservations and rooms
     */
    private static final String DATABASE_ASO_CREATE ="create table association (habID integer, _id integer, " +
            "numOcup integer, primary key(habID,_id), constraint FK_Hab foreign key (habID)" +
            "references rooms(_id), constraint FK_Res foreign key (_id) " +
            "references reservations(_id));";

    private static final String[] statements = new String[]{DATABASE_ROOMS_CREATE,
            DATABASE_RES_CREATE, DATABASE_ASO_CREATE};


    public DatabaseHelper(Context context/*, String databaseTable*/) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : statements) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS data");
        db.execSQL("DROP TABLE IF EXISTS reservation");
        db.execSQL("DROP TABLE IF EXISTS rooms");
        db.execSQL("DROP TABLE IF EXISTS association");
        onCreate(db);

    }
}
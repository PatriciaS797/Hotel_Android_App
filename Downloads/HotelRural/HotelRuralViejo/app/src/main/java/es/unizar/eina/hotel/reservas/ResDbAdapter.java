
package es.unizar.eina.hotel.reservas;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.text.TextUtils;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

        import es.unizar.eina.hotel.database.DatabaseHelper;
        import es.unizar.eina.hotel.habitaciones.HabDbAdapter;

/**
 * Simple reservations database access helper class. Defines the basic CRUD operations
 * for our hotel reservation managing app, and gives the ability to list all reservations as well as
 * retrieve or modify a specific reservation.
 */
public class ResDbAdapter {

    public static final String KEY_ID = "_id";
    public static final String KEY_NOMBRE_CLIENTE = "nombreCliente";
    public static final String KEY_MOVIL_CLIENTE = "movilCliente";
    public static final String KEY_FECHAENT = "entrada";
    public static final String KEY_FECHASAL = "salida";
    public static final String KEY_PRECIO= "price";


    public static final String KEY_ID_HABS= "habID";

    private DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;

    private static final String DATABASE_TABLE = "reservations";
    private static final String DATABASE_TABLE2 = "association";
    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ResDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the reservations database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ResDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new reservation using the phone , name , price , check in and check out dates provided.
     * If the reservation is
     * successfully created return the new resId for that reservation, otherwise return
     * a -1 to indicate failure.
     *
     *
     */
    public long createRes(long id, String movil, String nombre, double price,String entrada,String salida) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int comparison=0;
        try {
            Date date1 = sdf.parse(entrada);
            Date date2 = sdf.parse(salida);
            comparison = date2.compareTo(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if((id <= 0) || (nombre=="") || (movil=="") || (price<= 0.0) || (entrada =="") || (salida=="")
              || (comparison < 0)  ) {
            return -1;
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOMBRE_CLIENTE, nombre);
        initialValues.put(KEY_MOVIL_CLIENTE, movil);
        initialValues.put(KEY_PRECIO, price);
        initialValues.put(KEY_FECHAENT, entrada);
        initialValues.put(KEY_FECHASAL, salida);



        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given resId
     *
     * @param resId id of reservation to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteRes(long resId) {

        return mDb.delete(DATABASE_TABLE, KEY_ID + "=" + resId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all reservations in the database
     *
     * @return Cursor over all reservations
     */
    public Cursor fetchAllRes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_NOMBRE_CLIENTE,
                        KEY_MOVIL_CLIENTE, KEY_PRECIO, KEY_FECHAENT,KEY_FECHASAL}, null, null,
                null, null, null);
    }

    /**
     * Return a Cursor positioned at the reservation that matches the given resId
     *
     *
     * @return Cursor positioned to matching reservation, if found
     * @throws SQLException if reservation could not be found/retrieved
     */
    public Cursor fetchAllResbyId(Long resId) throws SQLException {

        Cursor resCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                                KEY_NOMBRE_CLIENTE, KEY_MOVIL_CLIENTE, KEY_PRECIO, KEY_FECHAENT,KEY_FECHASAL},
                        KEY_ID + "=" + resId, null,
                        null, null, null, null);
        if (resCursor != null) {
            resCursor.moveToFirst();
        }
        return resCursor;
    }


    /**
     * Return a Cursor positioned at the reservation that matches the given resId
     *
     *
     * @return Cursor positioned to matching reservation, if found
     * @throws SQLException if room could not be found/retrieved
     */
    public Cursor fetchAllResByName() throws SQLException {

        Cursor resCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                                KEY_NOMBRE_CLIENTE, KEY_MOVIL_CLIENTE, KEY_PRECIO, KEY_FECHAENT,KEY_FECHASAL},
                        null, null,
                        null, null, KEY_NOMBRE_CLIENTE, null);
        if (resCursor != null) {
            resCursor.moveToFirst();
        }
        return resCursor;
    }


    /**
     * Return a Cursor positioned at the reservation that matches the given p
     *
     *
     * @return Cursor positioned to matching room, if found
     * @throws SQLException if room could not be found/retrieved
     */
    public Cursor fetchAllResByPhone() throws SQLException {

        Cursor resCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                                KEY_NOMBRE_CLIENTE, KEY_MOVIL_CLIENTE, KEY_PRECIO, KEY_FECHAENT,KEY_FECHASAL},
                        null, null,
                        null, null, KEY_MOVIL_CLIENTE, null);
        if (resCursor != null) {
            resCursor.moveToFirst();
        }
        return resCursor;
    }

    /**
     * Update the reservation using the details provided. The reservation to be updated is
     * specified using the resId, and it is altered to use the name,phone,price ,check in
     * and checkout values passed in
     * @return true if the reservation was successfully updated, false otherwise
     */
    public boolean updateRes(long resId, String movil, String nombre, double price,String entrada,String salida) {
        ContentValues args = new ContentValues();
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_NOMBRE_CLIENTE, nombre);
        initialValues.put(KEY_MOVIL_CLIENTE, movil);
        initialValues.put(KEY_PRECIO, price);
        initialValues.put(KEY_FECHAENT, entrada);
        initialValues.put(KEY_FECHAENT, salida);


        return mDb.update(DATABASE_TABLE, args, KEY_ID + "=" + resId, null) > 0;
    }


    /**
     * Calculate the price of the reservation matching the id pased in
     * @return price
     */
    public static double Precio(String id){
        String total= "SELECT rooms._id,maxOcup,price1Per , additionalCharge" +
                " FROM rooms" +
                " JOIN association on association.habID = rooms._id" +
                " WHERE association._id = " +id+
                " AND association is not null";
        Cursor cursor= mDb.rawQuery(total,null);
        double precio=0;
        while (cursor.moveToNext()){
            double precioNoche = cursor.getDouble(0);
            double recargo = cursor.getDouble(1);
            double ocupantes = cursor.getDouble(2);
            precio+= precioNoche + recargo*(ocupantes-1);
        }
        return precio;
    }

    /**
     * Check if the rooms the user has introduced exist .If all of them exist , it updates the
     * association table with the rooms id and resId .
     * @return association id if the rooms are correct , -1 if any is wrong.
     */

    public static long checkHab(String habitaciones, String resId) {
        String[] tokens = habitaciones.split(",");
        int i=0;
        Cursor cursor;
        String total = "SELECT rooms._id " +
                " FROM rooms " +
                " WHERE rooms._id = " +  tokens[i];
        cursor= mDb.rawQuery(total,null);

        while ( i < tokens.length && cursor != null) {


            total = "SELECT rooms.additionalCharge " +
                    "FROM rooms " +
                    "WHERE rooms._id = " +  tokens[i];
            cursor= mDb.rawQuery(total,null);
            i++;
        }
        if (i== tokens.length ){
            ContentValues args = new ContentValues();
            ContentValues initialValues = new ContentValues();
            long newRowId=0;
            for (int j=0 ; j < tokens.length ; j++){
                initialValues.put(KEY_ID_HABS, tokens[j]);
                initialValues.put(KEY_ID, resId);
                newRowId = mDb.insert(DATABASE_TABLE2, null, initialValues);
            }


            return newRowId;}
        else{ return -1;}
    }


}


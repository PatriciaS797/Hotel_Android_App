package es.unizar.eina.hotel.pruebas;

import es.unizar.eina.hotel.habitaciones.HabDbAdapter;
import es.unizar.eina.hotel.reservas.ResDbAdapter;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

/**
 * Public class designed to test our software
 */

public class Test extends AppCompatActivity {
    private HabDbAdapter HabmDbHelper;
    private ResDbAdapter ResmDbHelper;
    private static final String CREATE = "Creation test";
    private static final String DELETE = "Deletion test";
    private static final String UPGRADE = "Update test";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        HabmDbHelper = new HabDbAdapter(this);
        HabmDbHelper.open();
        ResmDbHelper = new ResDbAdapter(this);
        ResmDbHelper.open();

        this.computeTests();
        this.TestSobrecarga();
    }

    /**
     * Creates a new room with the id, price, occupation, description, and recharge percentage
     * specified. If the room has been created, it return a number above 0. If not, it returns -1.
     * @param id
     *         room id
     *         id > 0
     * @param price
     *           price per night
     *           price > 0
     * @param addCharge
     *              recharge percentage to be paid for every other guest staying in the room
     *              addCharge > 0
     * @param desc
     *           room description
     * @param maxOcup
     *              maximum amount of guests that can stay in the room
     *              maxOcup > 0
     * @return an integer above 0 if the room has been created, -1 if not
     */
    public long createRoom(long id, int maxOcup, String desc, double price, double addCharge) {
        try {
            long result = HabmDbHelper.createRoom(id, maxOcup, desc, price, addCharge);
            if(result >= 0) {
                Log.d(CREATE, "ROOM CREATED");
                return result;
            }
            else {
                Log.d(CREATE, "ROOM NOT CREATED");
                return result;
            }
        } catch(Exception error) {
            Log.d(CREATE, "EXCEPTION = " + error);
        };
        return -1;
    }


    /**
     * Creates a new reservation with the name , phone , price ,check in and check out dates specified.
     * . If the room has been created, it return a number above 0. If not, it returns -1.
     * @param id
     *         reservation id
     *         id > 0
     * @param price
     *           price of the reservation ( price = sum (price1Per + additionalRecharge*(occupation-1);
     *           price > 0
     * @param phone
     *              client's phone number
     * @param name
     *           client's name
     * @param checkIn
     *              date of client's check in
     *@param checkOut
     *              date of client's check out
     * @return an integer above 0 if the reservation has been created, -1 if not
     */
    public long createRes(long id, String phone, String name, double price,String checkIn,String checkOut) {
        try {
            long result = ResmDbHelper.createRes(id, phone,name, price, checkIn, checkOut);
            if(result >= 0) {
                Log.d(CREATE, "RESERVATION CREATED");
                return result;
            }
            else {
                Log.d(CREATE, "RESERVATION NOT CREATED");
                return result;
            }
        } catch(Exception error) {
            Log.d(CREATE, "EXCEPTION = " + error);
        };
        return -1;
    }

    /**
     * Deletes the room whose id has been passed as the argument
     * @param roomId
     *            id of the room to be deleted
     *            id > 0
     * @return true if the room has been deleted, false if not
     */
    public boolean deleteRoom(long roomId) {
        try {
            boolean result = HabmDbHelper.deleteRoom(roomId);
            if(result) {
                Log.d(DELETE, "ROOM DELETED");
            }
            else {
                Log.d(DELETE, "ROOM NOT DELETED");
            }
            return result;
        } catch (Exception error) {
            Log.d(DELETE, "EXCEPTION = " + error);
        };
        return false;
    }


    /**
     * Deletes the room whose id has been passed as the argument
     * @param resId
     *            id of the reservation to be deleted
     *            id > 0
     * @return true if the reservation has been deleted, false if not
     */
    public boolean deleteRes(long resId) {
        try {
            boolean result = ResmDbHelper.deleteRes(resId);
            if(result) {
                Log.d(DELETE, "RESERVATION DELETED");
            }
            else {
                Log.d(DELETE, "RESERVATION NOT DELETED");
            }
            return result;
        } catch (Exception error) {
            Log.d(DELETE, "EXCEPTION = " + error);
        };
        return false;
    }

    /**
     * Updates the room using the details provided. The room to be updated is
     * specified using the roomId, and it is altered to use the occupation, description,
     * price per one person and additional recharge values passed in
     * @param roomId
     *              id of note to update
     *              id > 0
     * @param maxOcup
     *              maximum amount of guests that can stay in the room
     *              maxOcup > 0
     * @param desc
     *              room description
     * @param price
     *              price per night
     *              price > 0
     * @param recharge
     *               recharge percentage to be paid for every other guest staying in the room
     *               recharge > 0
     *
     * @return true if the room was successfully updated, false otherwise
     */
    public boolean updateRoom(long roomId, int maxOcup, String desc, double price,
                              double recharge) {
        try {
            boolean result = HabmDbHelper.updateRoom(roomId, maxOcup, desc, price, recharge);
            if(result) {
                Log.d(UPGRADE, "ROOM UPDATED");
            }
            else {
                Log.d(UPGRADE, "ROOM NOT UPDATED");
            }
            return result;
        } catch (Exception error) {
            Log.d(UPGRADE, "EXCEPTION = " + error);
        };
        return false;
    }

    /**
     * Updates the reservation using the details provided. The reservation to be updated is
     * specified using the resId, and it is altered to use the name , phone , price , check in and check out date values passed in
     *  @param id
     *      id > 0
     * @param price
     *     price > 0
     * @param phone
     *     client's phone number
     *  @param name
     *       client's name
     * @param checkIn
     *      date of client's check in
     * @param checkOut
     *        date of client's check out
     *
     *
     * @return true if the reservation was successfully updated, false otherwise
     */
    public boolean updateRes(long id, String phone, String name, double price,String checkIn,String checkOut) {
        try {
            boolean result = ResmDbHelper.updateRes( id,phone, name, price,checkIn, checkOut);
            if(result) {
                Log.d(UPGRADE, "RESERVATION UPDATED");
            }
            else {
                Log.d(UPGRADE, "RESERVATION NOT UPDATED");
            }
            return result;
        } catch (Exception error) {
            Log.d(UPGRADE, "EXCEPTION = " + error);
        };
        return false;
    }

    /**
     * Computes a series of tests to verify that our software works as desired and generates a Log
     * with the events generated by the execution of the said tests
     */
    public void computeTests() {
        HabmDbHelper = new HabDbAdapter(this);
        HabmDbHelper.open();
        // creamos una habitación correcta
        createRoom(1, 3, "hola", 34, 21);
        // modificamos correctamente la habitación creada en el paso anterior
        updateRoom(1, 4, "adios", 43, 22);
        // eliminamos una habitación existente
        deleteRoom(1);
        // intentamos eliminar una habitación cuyo id no es adecuado
        deleteRoom(-14);
        // intentamos eliminar una habitación inexistente
        deleteRoom(3);
        // intentamos crear una habitación con identificador menor que 1
        createRoom(-2, 3, "hola", 34, 21);
        // intentamos crear una habitación con número de ocupantes nulo
        createRoom(1, 0, "hola", 23, 21);
        // intentamos crear una habitación con precio nulo
        createRoom(1, 3, "hola", 0, 21);
        // intentamos crear una habitación con recargo nulo
        createRoom(1, 3, "hola", 23, 0);
        // creamos una habitación correctamente
        createRoom(1, 3, "hola", 34, 21);
        // intentamos modificar la habitación correcta del paso anterior con un número de ocupantes
        // no adecuado
        updateRoom(1, 0, "hola", 34, 21);
        // intentamos modificar la habitación correcta del paso anterior con un precio no adecuado
        updateRoom(1, 3, "hola", 0, 21);
        // intentamos modificar la habitación correcta del paso anterior con un recargo no adecuado
        updateRoom(1, 3, "hola", 34, -2);



        // RESERVATION TESTS

        ResmDbHelper = new ResDbAdapter(this);
        ResmDbHelper.open();
        // creamos una reserva correcta
        createRes(1, "93224", "pepe", 34, "1/1/23","3/1/23");
        // modificamos correctamente la reserva creada en el paso anterior
        updateRes(1, "63225", "pepe", 34, "1/1/23","4/1/23");
        // eliminamos una reserva existente
        deleteRes(1);
        // intentamos eliminar una reserva cuyo id no es adecuado
        deleteRes(-14);
        // intentamos eliminar una reserva inexistente
        deleteRes(3);
        // intentamos crear una reserva con identificador menor que 1
        createRes(-4, "93224", "pepe", 34, "1/1/23","3/1/23");
        // intentamos crear una reserva con precio nulo
        createRes(-4, "93224", "pepe", 0, "1/1/23","3/1/23");
        // creamos una reserva correctamente
        createRes(1, "93224", "pepe", 34, "1/1/23","3/1/23");
        // intentamos modificar la reserva correcta del paso anterior con un precio no adecuado
        updateRes(1, "63225", "pepe", 0, "1/1/23","4/1/23");



    }

    public  void  TestSobrecarga() {
        int valor=0;
        for(int idHab = 0; idHab < 400;idHab++){
            valor=idHab*8;
            HabmDbHelper.createRoom(5+idHab,idHab,Integer.toString(idHab*8),
                       idHab*2,idHab);
            Log.w("TestSobrecarga", String.valueOf(valor));
        }


    }



}
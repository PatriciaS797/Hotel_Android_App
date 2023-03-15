package es.unizar.eina.hotel.gestion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import es.unizar.eina.hotel.R;
import es.unizar.eina.hotel.habitaciones.Hab;
import es.unizar.eina.hotel.habitaciones.RoomEdit;

import es.unizar.eina.hotel.reservas.Res;
import es.unizar.eina.hotel.reservas.ReservationEdit;
import es.unizar.eina.hotel.pruebas.Test;

/**
 * Public class to be used as the main menu on the Hotel Management app. It offers the necessary
 * methods to create, edit and delete both rooms and reservations.
 */

public class Menu extends AppCompatActivity {

    private static final int ACTIVITY_CREATE_ROOM = 0;
    private static final int ACTIVITY_CREATE_RES = 1;
    private static final int ACTIVITY_LIST_ROOMS = 2;
    private static final int ACTIVITY_LIST_RES = 3;
    private static final int ACTIVITY_TEST = 4;
    private static final int TESTS = android.view.Menu.FIRST;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button sRoom = (Button) findViewById(R.id.showRoom);
        Button sRes = (Button) findViewById(R.id.showRes);
        Button cRoom = (Button) findViewById(R.id.createRoom);
        Button cRes = (Button) findViewById(R.id.createRes);

        sRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listRooms();
            }
        });


         sRes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        listReservations();
        }
        });


        cRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoom();
            }
        });


        cRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            createRes();
        }
    });

    }

    /**
     * private method to list all rooms saved in our database
     */
    private void listRooms() {
        Intent i = new Intent(this, Hab.class);
        startActivityForResult(i, ACTIVITY_LIST_ROOMS);
    }


    /**
     * private method to list all reservations saved in our database
     */
     private void listReservations() {
     Intent i = new Intent(this, Res.class);
     startActivityForResult(i, ACTIVITY_LIST_RES);
     }

    /**
     * private method to create a room. When called, the app will automatically redirect the user
     * to a menu that has been designed specially for this task
     */
    private void createRoom() {
        Intent i = new Intent(this, RoomEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE_ROOM);
    }

    /**
     * private method to create a reservation. When called, the app will automatically redirect the user
     * to a menu that has been designed specially for this task
     */
     private void createRes() {
     Intent i = new Intent(this, ReservationEdit.class);
     startActivityForResult(i, ACTIVITY_CREATE_RES);
     }

    /**
     * starts new activity in order to obtain a result
     * @param requestCode activity to be started
     * @param resultCode variable in which the result will be stored
     * @param intent an Intent, which can return result data to the caller
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * Adds options to menu
     * @param menu
     * @return true if the menu has been correctly created, false if not
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(android.view.Menu.NONE, TESTS, android.view.Menu.NONE, R.string.compute_tests);
        return result;
    }

    /**
     * computes the option selected from the menu by the user
     * @param item option selected by the user
     * @return true if execution was correct, false if not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case TESTS:
                executeTest();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private void executeTest() {
        Intent i = new Intent(this, Test.class);
        startActivityForResult(i, ACTIVITY_TEST);
    }
}
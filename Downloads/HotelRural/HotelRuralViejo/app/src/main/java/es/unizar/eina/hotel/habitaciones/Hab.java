package es.unizar.eina.hotel.habitaciones;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.hotel.R;

/**
 * Public class to be used as the room display menu on the Hotel Management app. It offers the
 * necessary methods to create, edit, delete and order rooms in a specific order.
 */

public class Hab extends AppCompatActivity {
    private static final int ACTIVITY_EDIT=0;

    private static final int DELETE_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int ORDER_BY_ID = Menu.FIRST + 2;
    private static final int ORDER_BY_OC = Menu.FIRST + 3;
    private static final int ORDER_BY_PRICE = Menu.FIRST + 4;


    private HabDbAdapter mDbHelper;
    private ListView mList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_rooms);

        mDbHelper = new HabDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        fillData();

        registerForContextMenu(mList);
    }

    /**
     * Retrieves all of the rooms from the database and displays them as specified in our rooms_row
     * layout
     */
    private void fillData() {
        // Get all of the rooms from the database and create the item list
        Cursor roomsCursor = mDbHelper.fetchAllRooms();

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] {HabDbAdapter.KEY_ID, HabDbAdapter.KEY_MAX_OCCUPANTS,
                HabDbAdapter.KEY_PRICE1PER, HabDbAdapter.KEY_RECHARGE,
                HabDbAdapter.KEY_DESCRIPTION};

        // and an array of the fields we want to bind those fields to
        int[] to = new int[] {R.id.roomId, R.id.roomOccupation, R.id.roomPrice, R.id.roomRecharge,
                R.id.roomDesc};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.rooms_row, roomsCursor, from, to);
        mList.setAdapter(notes);
    }

    /**
     * Adds options to menu
     * @param menu
     * @return true if the menu has been correctly created, false if not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, ORDER_BY_ID, Menu.NONE, R.string.order_by_room_id);
        menu.add(Menu.NONE, ORDER_BY_OC, Menu.NONE, R.string.order_by_maxOc);
        menu.add(Menu.NONE, ORDER_BY_PRICE, Menu.NONE, R.string.order_by_price);
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
            case ORDER_BY_ID:
                order_by_id();
                return true;
            case ORDER_BY_OC:
                order_by_oc();
                return true;
            case ORDER_BY_PRICE:
                order_by_price();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds different options for the user to select when selecting a room by clicking on it
     * for a short period of time
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.delete_room);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.edit_room);
    }

    /**
     * Computes the option selected by the user from a room's context menu, which is accessed
     * by clicking on the room for a short period of time
     * @param item action to be computed
     * @return true if action was correctly computed, false if not
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                        item.getMenuInfo();
                mDbHelper.deleteRoom(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editRoom(info.position, info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * private method to edit a room. When called, the app will automatically redirect the user
     * to a menu that has been designed specially for this task
     * @param position
     * @param id
     */
    private void editRoom(int position, long id) {
        Intent i = new Intent(this, RoomEdit.class);
        i.putExtra(HabDbAdapter.KEY_ID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * Retrieves all of the rooms from the database and displays them as specified in our rooms_row
     * layout, ordering them by their id
     */
    private void order_by_id() {
        Cursor roomsCursor = mDbHelper.fetchAllRoomsByID();

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] {HabDbAdapter.KEY_ID, HabDbAdapter.KEY_MAX_OCCUPANTS,
                HabDbAdapter.KEY_PRICE1PER, HabDbAdapter.KEY_RECHARGE,
                HabDbAdapter.KEY_DESCRIPTION};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.roomId, R.id.roomOccupation, R.id.roomPrice, R.id.roomRecharge,
                R.id.roomDesc};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.rooms_row, roomsCursor, from, to);
        mList.setAdapter(notes);

    }

    /**
     * Retrieves all of the rooms from the database and displays them as specified in our rooms_row
     * layout, ordering them by their price
     */
    private void order_by_price() {
        Cursor roomsCursor = mDbHelper.fetchAllRoomsByPrice();

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] {HabDbAdapter.KEY_ID, HabDbAdapter.KEY_MAX_OCCUPANTS,
                HabDbAdapter.KEY_PRICE1PER, HabDbAdapter.KEY_RECHARGE,
                HabDbAdapter.KEY_DESCRIPTION};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.roomId, R.id.roomOccupation, R.id.roomPrice, R.id.roomRecharge,
                R.id.roomDesc};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.rooms_row, roomsCursor, from, to);
        mList.setAdapter(notes);
    }

    /**
     * Retrieves all of the rooms from the database and displays them as specified in our rooms_row
     * layout, ordering them by their maximum occupation
     */
    private void order_by_oc() {
        Cursor roomsCursor = mDbHelper.fetchAllRoomsByOc();

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] {HabDbAdapter.KEY_ID, HabDbAdapter.KEY_MAX_OCCUPANTS,
                HabDbAdapter.KEY_PRICE1PER, HabDbAdapter.KEY_RECHARGE,
                HabDbAdapter.KEY_DESCRIPTION};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[] { R.id.roomId, R.id.roomOccupation, R.id.roomPrice, R.id.roomRecharge,
                R.id.roomDesc};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.rooms_row, roomsCursor, from, to);
        mList.setAdapter(notes);
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
        fillData();
    }

}
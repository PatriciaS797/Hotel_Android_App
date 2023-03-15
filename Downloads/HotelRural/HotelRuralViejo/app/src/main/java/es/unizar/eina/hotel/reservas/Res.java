package es.unizar.eina.hotel.reservas;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
 * Public class to be used as the reservation display menu on the Hotel Management app. It offers the
 * necessary methods to create, edit, delete and order reservations in a specific order.
 */
public class Res extends AppCompatActivity {
    private static final int ACTIVITY_EDIT=0;

    private static final int DELETE_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int ORDER_BY_ID = Menu.FIRST + 2;
    private static final int ORDER_BY_NAME = Menu.FIRST + 3;
    private static final int ORDER_BY_PHONE = Menu.FIRST + 4;


    private ResDbAdapter mDbHelper;
    private ListView mList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_res);

        mDbHelper = new ResDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        fillData();

        registerForContextMenu(mList);
    }


    /**
     * Retrieves all of the reservations from the database and displays them as specified in our res_row
     * layout
     */
    private void fillData() {
        // Get all of the reservations from the database and create the item list
        try {
            Cursor resCursor = mDbHelper.fetchAllRes();

            // Create an array to specify the fields we want to display in the list
            String[] from = new String[]{ResDbAdapter.KEY_ID, ResDbAdapter.KEY_NOMBRE_CLIENTE,
                    ResDbAdapter.KEY_MOVIL_CLIENTE, ResDbAdapter.KEY_FECHAENT,
                    ResDbAdapter.KEY_FECHASAL,ResDbAdapter.KEY_PRECIO};

            // and an array of the fields we want to bind those fields to
            int[] to = new int[]{R.id.resId, R.id.customerName, R.id.customerPhone, R.id.entryDate,
                    R.id.outDate};

            // Now create an array adapter and set it to display using our row
            SimpleCursorAdapter res =
                    new SimpleCursorAdapter(this, R.layout.res_row, resCursor, from, to);
            mList.setAdapter(res);
        }catch(Exception e ){
            Log.w("reservas", e.toString());
        }
    }



    /**
     * Adds options to menu
     * @param menu
     * @return true if the menu has been correctly created, false if not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, ORDER_BY_ID, Menu.NONE, R.string.order_by_res_id);
        menu.add(Menu.NONE, ORDER_BY_NAME, Menu.NONE, R.string.order_by_name);
        menu.add(Menu.NONE, ORDER_BY_PHONE, Menu.NONE, R.string.order_by_phone);
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
            case ORDER_BY_NAME:
                order_by_name();
                return true;
            case ORDER_BY_PHONE:
                order_by_phone();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds different options for the user to select when selecting a reservation by clicking on it
     * for a short period of time
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
       menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.delete_res);
       menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.edit_res);
    }


    /**
     * Computes the option selected by the user from a reservation's context menu, which is accessed
     * by clicking on the reservation for a short period of time
     * @param item action to be computed
     * @return true if action was correctly computed, false if not
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                        item.getMenuInfo();
                mDbHelper.deleteRes(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editRes(info.position, info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }


    /**
     * private method to edit a reservation. When called, the app will automatically redirect the user
     * to a menu that has been designed specially for this task
     * @param position
     * @param id
     */
    private void editRes(int position, long id) {
        Intent i = new Intent(this, ReservationEdit.class);
        i.putExtra(ResDbAdapter.KEY_ID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    /**
     * Retrieves all of the reservations from the database and displays them as specified in our res_row
     * layout, ordering them by their id
     */
    private void order_by_id() {
        Cursor resCursor = mDbHelper.fetchAllRes();

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] {ResDbAdapter.KEY_ID, ResDbAdapter. KEY_NOMBRE_CLIENTE ,
                ResDbAdapter. KEY_MOVIL_CLIENTE , ResDbAdapter. KEY_FECHAENT,
                ResDbAdapter. KEY_FECHASAL, ResDbAdapter.KEY_PRECIO};

        // and an array of the fields we want to bind those fields to
        int[] to = new int[] { R.id.resId, R.id.customerName, R.id.customerPhone, R.id.entryDate,
                R.id.outDate};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter res =
                new SimpleCursorAdapter(this, R.layout.res_row, resCursor, from, to);
        mList.setAdapter(res);

    }


    /**
     * Retrieves all of the reservations from the database and displays them as specified in our res_row
     * layout, ordering them by their phone
     */
    private void order_by_phone() {
        Cursor resCursor = mDbHelper.fetchAllResByPhone();

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] {ResDbAdapter.KEY_ID, ResDbAdapter. KEY_NOMBRE_CLIENTE ,
                ResDbAdapter. KEY_MOVIL_CLIENTE , ResDbAdapter. KEY_FECHAENT,
                ResDbAdapter. KEY_FECHASAL};

        // and an array of the fields we want to bind those fields to
        int[] to = new int[] { R.id.resId, R.id.customerName, R.id.customerPhone, R.id.entryDate,
                R.id.outDate};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter res =
                new SimpleCursorAdapter(this, R.layout.res_row, resCursor, from, to);
        mList.setAdapter(res);
    }

    /**
     * Retrieves all of the reservations from the database and displays them as specified in our res_row
     * layout, ordering them by their name
     */
    private void order_by_name() {
        Cursor resCursor = mDbHelper.fetchAllResByName();

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] {ResDbAdapter.KEY_ID, ResDbAdapter. KEY_NOMBRE_CLIENTE ,
                ResDbAdapter. KEY_MOVIL_CLIENTE , ResDbAdapter. KEY_FECHAENT,
                ResDbAdapter. KEY_FECHASAL};

        // and an array of the fields we want to bind those fields to
        int[] to = new int[] { R.id.resId, R.id.customerName, R.id.customerPhone, R.id.entryDate,
                R.id.outDate};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter res =
                new SimpleCursorAdapter(this, R.layout.res_row, resCursor, from, to);
        mList.setAdapter(res);
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
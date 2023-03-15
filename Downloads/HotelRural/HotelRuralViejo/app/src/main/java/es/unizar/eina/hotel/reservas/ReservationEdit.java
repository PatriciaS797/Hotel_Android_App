package es.unizar.eina.hotel.reservas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.pm.PackageInfo ;
import android.content.pm.PackageManager ;

import es.unizar.eina.hotel.habitaciones.Hab;
import es.unizar.eina.hotel.habitaciones.HabDbAdapter;
import es.unizar.eina.hotel.send.SendAbstractionImpl;
import es.unizar.eina.hotel.send.WhatsAppImplementor;

import es.unizar.eina.hotel.send.SendImplementor;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import es.unizar.eina.hotel.R;
import es.unizar.eina.hotel.reservas.ResDbAdapter;



/**
 * Public class to be used as the reservation edit menu on the Hotel Management app. It offers the
 * necessary methods to create, edit and delete reservations.
 */
public class ReservationEdit extends AppCompatActivity {

    private EditText mCustomerName;
    private EditText mCustomerPhone;
    private EditText mPrecio;
    private EditText mFechaSal;
    private EditText mFechaEnt;
    private EditText mResId;
    private EditText mHabs;
    private Long _ID;
    private static final int WH_ID =  1;
    private static final int SMS_ID = 2;
    private ResDbAdapter mDbHelper;
    private Long aux;


    /**
     * Method called when an activity is interrupted and can be deleted before being resumed
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ResDbAdapter.KEY_ID, _ID);
    }
    /**
     * Method called when an activity finishes. Used to save the current state of the system in
     * our database
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    /**
     * Method called when a paused activity is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }


    /**
     * Method to save data in our database
     */
    private void saveState() {
        Long aux = (_ID != null) ? _ID : null;
        if(aux == null) {
            if(!mResId.getText().toString().isEmpty()) {
                aux = Long.parseLong(mResId.getText().toString());
            }

        }
        String nombre = mCustomerName.getText().toString();
        String movil= mCustomerPhone.getText().toString();

        String entrada  =  mFechaEnt.getText().toString();
        String salida  =  mFechaSal.getText().toString();

        String habitaciones  =  mHabs.getText().toString();
        long idAsoc = ResDbAdapter.checkHab(habitaciones,mResId.getText().toString());
        if ( idAsoc != -1) {


            if (_ID == null && aux != null) {
               long a= mDbHelper.createRes(aux, nombre, movil, 1, entrada, salida);
              // Double precio = ResDbAdapter.Precio(String.valueOf(a));
                Log.w("reservas", String.valueOf(a));
            } else if (_ID != null && aux != null) {

                mDbHelper.updateRes(_ID, nombre, movil, 1, entrada, salida);
            }
        }

    }


    /**
     * Method to send a confirmation message to the client
     * @param type  SMS or WhatsApp
     */
private void sendConfirmation(int type){
    SendAbstractionImpl a;
    if (type==SMS_ID) {
        a = new SendAbstractionImpl(this, "SMS");

        a.send(String.valueOf(mCustomerPhone), "Su reserva ha sido confirmada");
    }
    else if(type==WH_ID){
        a = new SendAbstractionImpl(this, "WhatsApp");

        a.send(String.valueOf(mCustomerPhone), "Su reserva ha sido confirmada");

    }

}

    /**
     * Called when the activity is first created
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new ResDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.create_reservation);

        mResId = (EditText) findViewById(R.id.resId);
        mCustomerName = (EditText) findViewById(R.id.customerName);
        mCustomerPhone = (EditText) findViewById(R.id.customerPhone);
        mFechaSal = (EditText) findViewById(R.id.checkOut);
        mFechaEnt = (EditText) findViewById(R.id.entryDate);
        mPrecio = (EditText) findViewById(R.id.resId);
        mHabs = (EditText) findViewById(R.id.Rooms_in_res);
        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        _ID = (savedInstanceState == null) ? null:
                (Long)savedInstanceState.getSerializable(ResDbAdapter.KEY_ID);
        if(_ID == null) {
            Bundle extras = getIntent().getExtras();
            _ID = (extras != null) ? extras.getLong(ResDbAdapter.KEY_ID) : null;
        }
        populateFields();
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent mIntent = new Intent();

                setResult(RESULT_OK, mIntent);
                finish();

            }

        });

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                sendConfirmation(SMS_ID);
            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }

        });
    }


    /**
     * Retrieves all available information on the reservation with id = _ID from our database
     */
    private void populateFields() {
        if(_ID != null) {
            Cursor res = mDbHelper.fetchAllResbyId(_ID);

            mResId.setText(res.getString(res.getColumnIndexOrThrow(ResDbAdapter.KEY_ID)));

            mCustomerName.setText(res.getString(res.
                    getColumnIndexOrThrow(ResDbAdapter.KEY_NOMBRE_CLIENTE)));
            mCustomerPhone.setText(res.getString(res.getColumnIndexOrThrow
                    (ResDbAdapter.KEY_MOVIL_CLIENTE)));
            mPrecio.setText(res.getString(res.getColumnIndexOrThrow
                    (ResDbAdapter.KEY_PRECIO)));
            mFechaEnt.setText(res.getString(res.getColumnIndexOrThrow
                    (ResDbAdapter.KEY_FECHAENT)));
            mFechaSal.setText(res.getString(res.getColumnIndexOrThrow
                    (ResDbAdapter.KEY_FECHASAL)));
           /* mHabs.setText(res.getString(res.getColumnIndexOrThrow
                    (ResDbAdapter.KEY_ID_HABS))); */


        }
    }
}

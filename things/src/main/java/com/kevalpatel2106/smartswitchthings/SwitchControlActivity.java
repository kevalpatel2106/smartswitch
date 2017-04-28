package com.kevalpatel2106.smartswitchthings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class SwitchControlActivity extends AppCompatActivity {
    public static final String TAG = SwitchControlActivity.class.getSimpleName();

    private DatabaseReference mDatabaseRef;
    private ArrayList<Switch> mSwitches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();

        //Check for the available GPIOs
        if (peripheralManagerService.getGpioList().isEmpty()) {
            Log.e(TAG, "No GPIO port available on this device.");
            finish();
            return;
        }

        //Firebase mFirebaseDatabase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constant.REF_NAME);

        //Set the BCM6 port as the GPIO for all pins.
        try {
            mSwitches = Switch.getSwitches(peripheralManagerService);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, BoardDefaults.getGPIOForLED() + " GPIO port is not available on this device.");
            finish();
            return;
        }

        //Set the pins in real-time database
//        for (Switch switches : mSwitches) {
//            DatabaseReference switchRef = mDatabaseRef.child(switches.getName()).getRef();
//            switchRef.setValue(switches);
//        }

        //Set the change listener.
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Switch s : mSwitches) {
                    Switch dataSwitch = dataSnapshot.child(s.getName()).getValue(Switch.class);

                    Log.d("FB Switch", dataSnapshot.toString());

                    //Check changed pin by the pin name
                    if (s.getName().equals(dataSwitch.getName())) {
                        try {
                            //Change the status
                            s.setStatus(dataSwitch.getStatus());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Release all the GPIO pins
        for (Switch s : mSwitches) {
            if (s.getGpio() != null) {
                try {
                    s.getGpio().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

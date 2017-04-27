package com.kevalpatel2106.smartswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mDatabaseRef;
    private ArrayList<Switch> mSwitches = new ArrayList<>();
    private SwitchesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase mFirebaseDatabase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constant.REF_NAME);

        //Set the BCM6 port as the GPIO for all pins.
        mSwitches = Switch.getSwitches();

        //Set the change listener.
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Switch s : mSwitches) {
                    Switch dataSwitch = dataSnapshot.child(s.getName()).getValue(Switch.class);

                    //Check changed pin by the pin name
                    if (s.getName().equals(dataSwitch.getName())) {
                        s.setStatus(dataSwitch.getStatus());
                    }
                }

                //Refresh the list
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Set recycler view
        mAdapter = new SwitchesAdapter(this, mSwitches, mDatabaseRef);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.switches_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }
}

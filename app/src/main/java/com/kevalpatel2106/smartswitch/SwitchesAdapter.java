package com.kevalpatel2106.smartswitch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Keval on 27-Apr-17.
 */

public class SwitchesAdapter extends RecyclerView.Adapter<SwitchesAdapter.SwitchViewHolder> {

    private final Context mContext;
    private final ArrayList<Switch> mSwitches;
    private final DatabaseReference mDatabaseReference;

    public SwitchesAdapter(Context context, ArrayList<Switch> switches,DatabaseReference reference) {
        mContext = context;
        mSwitches = switches;
        mDatabaseReference = reference;
    }

    @Override
    public SwitchesAdapter.SwitchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SwitchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_switch, parent, false));
    }

    @Override
    public void onBindViewHolder(SwitchesAdapter.SwitchViewHolder holder, int position) {
        final Switch switch1 = mSwitches.get(position);

        holder.mSwitchTextView.setText(switch1.getName().toUpperCase().replace("PIN",""));

        holder.mSwitchToggel.setBackgroundDrawable(switch1.getIcon());
        holder.mSwitchToggel.setOnCheckedChangeListener(null);
        holder.mSwitchToggel.setChecked(switch1.getStatus());
        holder.mSwitchToggel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch1.setStatus(isChecked);

                DatabaseReference switchRef = mDatabaseReference.child(switch1.getName()).getRef();
                switchRef.setValue(switch1);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSwitches.size();
    }

    public class SwitchViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView mSwitchTextView;
        private ToggleButton mSwitchToggel;

        public SwitchViewHolder(View itemView) {
            super(itemView);

            mSwitchTextView = (AppCompatTextView) itemView.findViewById(R.id.switch_name_tv);
            mSwitchToggel = (ToggleButton) itemView.findViewById(R.id.switch_status_toggel);

            ViewGroup.LayoutParams params = mSwitchToggel.getLayoutParams();
            int square = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
            params.width = square;
            params.height = square;
            mSwitchToggel.setLayoutParams(params);

        }
    }
}

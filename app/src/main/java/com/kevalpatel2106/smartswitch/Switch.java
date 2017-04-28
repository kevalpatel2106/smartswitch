package com.kevalpatel2106.smartswitch;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Keval on 27-Apr-17.
 */

@IgnoreExtraProperties
public class Switch {

    /**
     * Name of the switch.
     *
     * @see Constant for switch names.
     */
    private String name;

    /**
     * The status of the pin. True indicates "ON" state and false indicates "OFF".
     */
    private boolean status;

    /**
     * This is the icon to indicate the item.
     */
    @Exclude
    private Drawable icon;

    /**
     * Public constructor.
     *
     * @param switchName Name of the switch.
     * @param status     Status of the switch. True indicates "ON" state and false indicates "OFF".
     * @throws IOException
     */
    public Switch(@NonNull String switchName,
                  boolean status,
                  Drawable icon) {
        this.name = switchName;
        this.status = status;
        this.icon = icon;
    }

    public Switch() {
        //Required for real-time database
    }

    /**
     * Get the name of the switch.
     *
     * @return name of the switch.
     * @see Constant
     */
    public String getName() {
        return name;
    }

    /**
     * Get current status of the GPIO pin.
     *
     * @return current status.
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Set the status of pin.
     *
     * @param status Status of the switch. True indicates "ON" state and false indicates "OFF".
     * @throws IOException If GPIO status change fails.
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Get the icon for the switch.
     *
     * @return drawable of the icon.
     */
    @Exclude
    public Drawable getIcon() {
        return icon;
    }

    /**
     * Get the list of switches.
     *
     * @return list of the {@link Switch}
     * @throws IOException If GPIO initialization fails.
     */
    public static ArrayList<Switch> getSwitches(Context context) {
        ArrayList<Switch> switches = new ArrayList<>();
        switches.add(new Switch(Constant.LED_PIN, false, ContextCompat.getDrawable(context,R.drawable.led_selector)));
        switches.add(new Switch(Constant.FAN_PIN, false,ContextCompat.getDrawable(context,R.drawable.fan_selector)));
        switches.add(new Switch(Constant.LIGHT_BULB_PIN, false,ContextCompat.getDrawable(context,R.drawable.bulb_selector)));
        return switches;
    }

    @Override
    public String toString() {
        return name + " " + status;
    }
}

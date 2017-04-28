package com.kevalpatel2106.smartswitchthings;

import android.support.annotation.NonNull;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
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
     * GPIO pin for the switch. This pin is excluded from the firebase database.
     */
    @Exclude
    private Gpio gpio;

    /**
     * Public constructor.
     *
     * @param switchName Name of the switch.
     * @param status     Status of the switch. True indicates "ON" state and false indicates "OFF".
     * @param gpio       GPIO for the switch
     * @throws IOException
     */
    public Switch(@NonNull String switchName,
                  boolean status,
                  @NonNull Gpio gpio) throws IOException {
        this.name = switchName;
        this.status = status;
        this.gpio = gpio;

        //Set gpio
        this.gpio.setEdgeTriggerType(Gpio.EDGE_NONE);
        this.gpio.setActiveType(Gpio.ACTIVE_HIGH);
        this.gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

        //Set the state of GPIO based on status
        this.gpio.setValue(this.status);
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
    public void setStatus(boolean status) throws IOException {
        this.status = status;

        //Change the output of the GPIO pin.
        if (gpio != null) gpio.setValue(status);
    }

    @Exclude
    public Gpio getGpio() {
        return gpio;
    }

    /**
     * Get the list of switches.
     *
     * @param peripheralManagerService {@link PeripheralManagerService} instance.
     * @return list of the {@link Switch}
     * @throws IOException If GPIO initialization fails.
     */
    public static ArrayList<Switch> getSwitches(PeripheralManagerService peripheralManagerService) throws IOException {
        ArrayList<Switch> switches = new ArrayList<>();
        switches.add(new Switch(Constant.LED_PIN, false, peripheralManagerService.openGpio(BoardDefaults.getGPIOForLED())));
        switches.add(new Switch(Constant.FAN_PIN, false, peripheralManagerService.openGpio(BoardDefaults.getGPIOForFan())));
        switches.add(new Switch(Constant.LIGHT_BULB_PIN, false, peripheralManagerService.openGpio(BoardDefaults.getGPIOForLightBulb())));
        return switches;
    }

    @Override
    public String toString() {
        return name + " " + status;
    }
}

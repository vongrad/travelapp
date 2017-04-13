package dk.itu.vongrad.travelapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dk.itu.vongrad.travelapp.utils.AlarmHelper;

/**
 * Created by Adam Vongrej on 4/13/17.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    // Every minute
    public static final int MILLIS_INTERVAL = 60000;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, AutoCheckoutService.class);

        if (!AlarmHelper.isServiceAlarmUp(context, serviceIntent)) {
            AlarmHelper.scheduleRepeatingService(context, serviceIntent, MILLIS_INTERVAL);
        }
    }
}

package dk.itu.vongrad.travelapp.services;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

/**
 * Broadcast receiver used to turn on/off the Estimote beacon monitoring
 */
public class BeaconBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_TURNING_OFF:
                    context.stopService(new Intent(context, BeaconService.class));
                    break;
                case BluetoothAdapter.STATE_ON:
                    context.startService(new Intent(context, BeaconService.class));
                    break;
            }
        }
    }
}

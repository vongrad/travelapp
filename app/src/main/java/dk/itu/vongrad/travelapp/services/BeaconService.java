package dk.itu.vongrad.travelapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import dk.itu.vongrad.travelapp.realm.model.Location;
import dk.itu.vongrad.travelapp.repository.TripsRepository;
import dk.itu.vongrad.travelapp.utils.NotificationHelper;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class BeaconService extends Service {

    private static final String TAG = "BeaconService";
    private static final String BEACON_UUID = "e3b54450-ab73-4d79-85d6-519eaf0f45d9";

    private BeaconManager beaconManager;
    private Region region;

    private IBinder binder;

    private BeaconManager.MonitoringListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();

        binder = new BeaconBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (beaconManager == null) {
            beaconManager = new BeaconManager(getApplicationContext());
            region = new Region("ITU", UUID.fromString(BEACON_UUID), null, null);

            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startMonitoring(region);
                }
            });

            beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                @Override
                public void onEnteredRegion(Region region, List<Beacon> list) {

                    Beacon beacon = list.get(0);

                    NotificationHelper.showNotification(getApplicationContext(), "Entered region: " + beacon.getMajor(), "Bla bla");
                    Log.d(TAG, "Entered region: major: " + beacon.getMajor() + " minor: " + beacon.getMinor() + " power: " + beacon.getMeasuredPower());

                    if (TripsRepository.isActive()) {
                        Location location = new Location(beacon);

                        try {
                            TripsRepository.addLocation(getApplicationContext(), location);
                        } catch (Exception e) {
                            // This should never happen
                            e.printStackTrace();
                            NotificationHelper.showNotification(getApplicationContext(), "Fatal error", "Something is terribly wrong (probably concurrency issue), contact system developer :/");
                        }
                    }

                    if (mListener != null) {
                        mListener.onEnteredRegion(region, list);
                    }
                }

                @Override
                public void onExitedRegion(Region region) {
                    if (mListener != null) {
                        mListener.onExitedRegion(region);
                    }
                    Log.d(TAG, "Exited region: " + region.getMajor());
                }
            });
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.stopMonitoring(region);
        beaconManager.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mListener = null;
        return false;
    }

    public class BeaconBinder extends Binder {

        /**
         * Sets listener for beacon changes
         * @param listener
         */
        public void setListener(BeaconManager.MonitoringListener listener) {
            mListener = listener;
        }
    }
}

package dk.itu.vongrad.travelapp;

import android.app.Application;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import dk.itu.vongrad.travelapp.utils.NotificationHelper;
import io.realm.Realm;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;

/**
 * Created by Adam Vongrej on 3/22/17.
 */

public class GlobalApplication extends Application {

    private BeaconManager beaconManager;
    private static final String TAG = "GlobalApplication";
    private BeaconManager.MonitoringListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();

        // Default Realm config
        Realm.init(this);
        RealmLog.setLevel(LogLevel.TRACE);

//        beaconManager = new BeaconManager(getApplicationContext());
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                beaconManager.startMonitoring(new Region(
//                        "ITU",
//                        UUID.fromString("e3b54450-ab73-4d79-85d6-519eaf0f45d9"),
//                        null, null
//                ));
//            }
//        });
//
//        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
//            @Override
//            public void onEnteredRegion(Region region, List<Beacon> list) {
//                Beacon beacon = list.get(0);
//
//                if (mListener != null) {
//                    mListener.onEnteredRegion(region, list);
//                }
//
//                NotificationHelper.showNotification(getApplicationContext(), "Entered region: " + beacon.getMajor(), "Bla bla");
//                Log.d(TAG, "Entered region: major: " + beacon.getMajor() + " minor: " + beacon.getMinor() + " power: " + beacon.getMeasuredPower());
//            }
//
//            @Override
//            public void onExitedRegion(Region region) {
//                mListener.onExitedRegion(region);
//                Log.d(TAG, "Exited region: " + region.getMajor());
//            }
//        });
    }
}

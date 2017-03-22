package dk.itu.vongrad.travelapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Adam Vongrej on 3/22/17.
 */

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Default Realm config
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("travelapp.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}

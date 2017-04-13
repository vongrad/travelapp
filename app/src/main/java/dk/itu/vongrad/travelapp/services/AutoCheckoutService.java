package dk.itu.vongrad.travelapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Date;

import dk.itu.vongrad.travelapp.exceptions.NoActiveTripException;
import dk.itu.vongrad.travelapp.realm.model.Trip;
import dk.itu.vongrad.travelapp.repository.TripsRepository;

/**
 * Created by Adam Vongrej on 4/13/17.
 */

/**
 * Service used for automatic checkouts of the trip
 */
public class AutoCheckoutService extends Service {

    /**
     * Amount of time after which the trip will be auto-checked out
     */
    private static final int timeoutMinutes = 30;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Trip trip = TripsRepository.getActive();

        if(trip != null) {
            Date createdAt = trip.getLocations().last().getCreatedAt();

            long minutes30 = 60 * timeoutMinutes * 1000;

            if (!new Date().before(new Date(createdAt.getTime() + minutes30))) {
                try {
                    TripsRepository.finishTrip(getApplicationContext());
                } catch (NoActiveTripException e) {
                    // This should never happen
                    e.printStackTrace();
                }
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

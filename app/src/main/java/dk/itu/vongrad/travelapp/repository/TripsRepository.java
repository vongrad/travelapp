package dk.itu.vongrad.travelapp.repository;

import android.content.Context;

import java.util.Date;

import dk.itu.vongrad.travelapp.R;
import dk.itu.vongrad.travelapp.exceptions.ActiveTripException;
import dk.itu.vongrad.travelapp.exceptions.NoActiveTripException;
import dk.itu.vongrad.travelapp.realm.model.Location;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.Trip;
import dk.itu.vongrad.travelapp.realm.model.User;
import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import dk.itu.vongrad.travelapp.utils.TripsHelper;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Adam Vongrej on 4/10/17.
 */

public class TripsRepository {

    /**
     * Get all trips for the current user
     * @return RealmList<Trip> list of all trips
     */
    public static RealmList<Trip> getAll() {
        return UserRepository.getCurrentUser().getTrips();
    }

    /**
     * Get last trip
     * @return Trip
     */
    public static Trip getLast() {
        return getAll().last(null);
    }

    /**
     * Get active trip
     * The active trip does not have the 'ended_at' field set
     * @return trip
     */
    public static Trip getActive() {
        return getAll().where().isNull(RealmTable.Trip.ENDED_AT).findFirst();
    }

    /**
     * Check if there is an active trip
     * @return
     */
    public static boolean isActive() {
        return getActive() != null;
    }

    /**
     * Start a new trip
     * @param location - starting location of the trip
     * @return Trip
     * @throws ActiveTripException - if other trip is already active, throw exception
     */
    public static Trip startTrip(Context context, Location location) throws Exception {

        if (isActive()) {
            throw new ActiveTripException(context.getString(R.string.exc_already_active));
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Date startDate = new Date();

        User user = UserRepository.getCurrentUser();

        Trip trip = realm.createObject(Trip.class);
        trip.setCreatedAt(startDate);

        trip.getLocations().add(location);

        user.getTrips().add(trip);

        realm.commitTransaction();

        return trip;
    }

    /**
     * Add a new location to the trip
     * @param context
     * @param location - the next location user visited
     * @throws Exception - if no active trip found, throw exception
     */
    public static void addLocation(final Context context, final Location location) throws Exception {

        final Trip trip = getActive();
        if(trip == null) {
            throw new NoActiveTripException(context.getString(R.string.exc_no_active));
        }

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                trip.getLocations().add(location);
            }
        });
    }

    /**
     * Finish active trip
     * @throws NoActiveTripException - if no active trip found, throw exception
     */
    public static void finishTrip(Context context) throws Exception {

        final Trip trip = getActive();
        if (trip == null) {
            throw  new NoActiveTripException(context.getString(R.string.exc_no_active));
        }

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Transaction transaction = realm.createObject(Transaction.class);
                transaction.setCreatedAt(new Date());

                double amount = TripsHelper.calculateCost(trip);
                transaction.setAmount(-amount);

                User user = UserRepository.getCurrentUser();
                user.getAccount().getTransactions().add(transaction);

                user.getAccount().setBalance(user.getAccount().getBalance() + transaction.getAmount());

                trip.setEndedAt(new Date());
                trip.setTransaction(transaction);
            }
        });
    }
}

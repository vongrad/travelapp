package dk.itu.vongrad.travelapp.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import dk.itu.vongrad.travelapp.realm.model.Location;
import dk.itu.vongrad.travelapp.realm.model.Trip;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class TripsHelper {

    /**
     * Calculate cost of the trip
     * Charning 5 DKK per visited location, disregarding if the user passed in multiple times
     * @param trip Trip
     * @return double - cost of the trip
     */
    public static double calculateCost(Trip trip) {

        Set<Integer> uniqueHashes = new LinkedHashSet<>();

        for(Location location : trip.getLocations()) {
            uniqueHashes.add(location.hashCode());
        }

        // Visiting each location costs 5 DKK
        return uniqueHashes.size() * 5;
    }
}

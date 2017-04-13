package dk.itu.vongrad.travelapp.utils;

import dk.itu.vongrad.travelapp.realm.model.Location;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class LocationHelper {

    public static String formatText(Location location) {
        StringBuilder sb = new StringBuilder();
        sb.append("F: " + location.getFloor());
        sb.append(" R: " + location.getRoom());

        return sb.toString();
    }
}

package dk.itu.vongrad.travelapp.utils;

import dk.itu.vongrad.travelapp.realm.model.Location;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class LocationHelper {

    public static String formatText(Location location) {
        StringBuilder sb = new StringBuilder();
        sb.append("Floor: " + location.getFloor());
        sb.append(" Room: " + location.getRoom());

        return sb.toString();
    }
}

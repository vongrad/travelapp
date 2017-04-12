package dk.itu.vongrad.travelapp.realm.utils;

/**
 * Created by Adam Vongrej on 4/10/17.
 */

public class RealmUtils {

    public static String buildNested(String parent, String child) {
        return parent + "." + child;
    }
}

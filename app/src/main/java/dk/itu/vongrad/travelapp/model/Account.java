package dk.itu.vongrad.travelapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Adam Vongrej on 3/22/17.
 */

public class Account extends RealmObject {

    @PrimaryKey
    private long id;
    private double balance;


}

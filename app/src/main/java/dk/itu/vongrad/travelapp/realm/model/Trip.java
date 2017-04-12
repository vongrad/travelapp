package dk.itu.vongrad.travelapp.realm.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class Trip extends RealmObject {

    public Trip() {}

    public Trip(Date createdAt) {
        this.createdAt = createdAt;
    }

    private Date createdAt;
    private Date endedAt;

    private Transaction transaction;
    private RealmList<Location> locations;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public RealmList<Location> getLocations() {
        return locations;
    }

    public void setLocations(RealmList<Location> locations) {
        this.locations = locations;
    }
}

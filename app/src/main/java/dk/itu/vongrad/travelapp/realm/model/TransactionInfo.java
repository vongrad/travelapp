package dk.itu.vongrad.travelapp.realm.model;

import java.util.Date;

import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import dk.itu.vongrad.travelapp.realm.utils.AutoIncementable;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Adam Vongrej on 3/22/17.
 */

public class TransactionInfo extends RealmObject implements AutoIncementable {

    @PrimaryKey
    private long id;

    private String fromLocation;
    private Date fromTime;

    private String toLocation;
    private Date toTime;

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
        this.toTime = toTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setPrimaryKey(int id) {
        this.id = id;
    }

    @Override
    public int getNextPrimaryKey(Realm realm) {
        Number maxId = realm.where(User.class).max(RealmTable.ID);

        int nextId;

        if(maxId == null) {
            nextId = 1;
        } else {
            nextId = maxId.intValue() + 1;
        }
        return nextId;
    }
}

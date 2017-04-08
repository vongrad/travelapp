package dk.itu.vongrad.travelapp.realm.utils;

import io.realm.Realm;

/**
 * Created by vongrad on 3/29/17.
 */

public interface AutoIncementable {
    public void setPrimaryKey(int primaryKey);
    public int getNextPrimaryKey(Realm realm);
}

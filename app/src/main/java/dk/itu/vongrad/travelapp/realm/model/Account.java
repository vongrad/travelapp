package dk.itu.vongrad.travelapp.realm.model;

import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import dk.itu.vongrad.travelapp.realm.utils.AutoIncementable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Adam Vongrej on 3/22/17.
 */

public class Account extends RealmObject implements AutoIncementable {

    @PrimaryKey
    private long id;
    private double balance;

    private RealmList<Transaction> transactions;

    public Account() {}

    public Account(double balance) {
        this.balance = balance;
        setPrimaryKey(getNextPrimaryKey(Realm.getDefaultInstance()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public RealmList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(RealmList<Transaction> transactions) {
        this.transactions = transactions;
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

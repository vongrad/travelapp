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

public class Transaction extends RealmObject implements AutoIncementable {

    @PrimaryKey
    private long id;

    private Date createdAt;
    private double amount;

    private TransactionInfo transactionInfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
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

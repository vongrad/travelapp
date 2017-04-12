package dk.itu.vongrad.travelapp.repository;

import java.util.Date;

import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.User;
import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Adam Vongrej on 4/9/17.
 */

public class AccountRepository {

    /**
     * Add account transaction
     * @param amount
     */
    public static void addTransaction(final int amount) {

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = UserRepository.getCurrentUser();

                Transaction transaction = realm.createObject(Transaction.class);
                transaction.setAmount(amount);
                transaction.setCreatedAt(new Date());

                user.getAccount().getTransactions().add(transaction);

                // This is executed within Realm transaction so we should not have problems with concurrency
                user.getAccount().setBalance(user.getAccount().getBalance() + amount);
            }
        });
    }

    /**
     * Add acount transaction
     * @param transaction
     */
    public static void addTransaction(final Transaction transaction) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

            }
        });
    }

    /**
     * Get all transactions
     */
    public static RealmResults<Transaction> getTransactions() {
        return UserRepository.getCurrentUser().getAccount().getTransactions().where().findAllSorted(RealmTable.Transaction.CREATED_AT, Sort.DESCENDING);
    }
}

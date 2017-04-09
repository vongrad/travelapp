package dk.itu.vongrad.travelapp.repository;

import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.User;
import io.realm.Realm;
import io.realm.RealmList;

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

                user.getAccount().getTransactions().add(transaction);

                // This is executed within Realm transaction so we should not have problems with concurrency
                user.getAccount().setBalance(user.getAccount().getBalance() + amount);
            }
        });
    }

    /**
     * Get all transactions
     */
    public static RealmList<Transaction> getTransactions() {
        return UserRepository.getCurrentUser().getAccount().getTransactions();
    }
}

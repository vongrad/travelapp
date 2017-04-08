package dk.itu.vongrad.travelapp.realm.table;

/**
 * Created by vongrad on 3/22/17.
 */

public interface RealmTable {

    String ID = "id";

    interface User {

        String FIRSTNAME = "firstName";
        String LASTNAME = "lastName";
        String EMAIL = "email";
        String CARD_ID = "cardId";
        String USERNAME = "username";
        String PASSWORD = "password";
        String ACCOUNT = "account";
    }

    interface Account {
        String BALANCE = "balance";
        String TRANSACTIONS = "transactions";
    }

    interface Transaction {
        String CREATED_AT = "createdAt";
        String AMOUNT = "amount";
        String TRANSACTION_INFO = "transactionInfo";
    }

    interface TransactionInfo {
        String FROM_LOCATION = "fromLocation";
        String FROM_TIME = "fromTime";
        String TO_LOCATION = "toLocation";
        String TO_TIME = "toTime";
    }
}

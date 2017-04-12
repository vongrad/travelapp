package dk.itu.vongrad.travelapp.realm.table;

/**
 * Created by vongrad on 3/22/17.
 */

public interface RealmTable {

    String ID = "id";
    String TABLE_ACCOUNT = "account";
    String TABLE_USER = "user";
    String TABLE_TRANSACTION = "transaction";
    String TABLE_TRIP = "trip";

    interface User {

        String FIRSTNAME = "firstName";
        String LASTNAME = "lastName";
        String EMAIL = "email";
        String CARD_ID = "cardId";
        String USERNAME = "username";
        String PASSWORD = "password";
        String ACCOUNT = "account";
    }

    interface Trip {
        String CREATED_AT = "createdAt";
        String ENDED_AT = "endedAt";
        String TRANSACTION = TABLE_TRANSACTION;
        String LOCATIONS = "locations";
    }

    interface Location {
        String FLOOR = "floor";
        String AREA = "area";
        String ROOM = "room";
        String NUMBER = "number";
        String CREATED_AT = "createdAt";
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

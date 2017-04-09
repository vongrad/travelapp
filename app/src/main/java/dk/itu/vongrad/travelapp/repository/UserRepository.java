package dk.itu.vongrad.travelapp.repository;

import dk.itu.vongrad.travelapp.realm.model.User;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Adam Vongrej on 3/22/17.
 */

public class UserRepository {

    /**
     * Create a new user in the realm
     * @param user
     */
    public static void add(final User user){

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(user);
            }
        });
    }

    /**
     * Get current user
     * @return
     */
    public static RealmResults<User> getCurrent() {
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<User> query = realm.where(User.class);
        RealmResults<User> result = query.findAll();

        return result;
    }

    public static User getCurrentUser() {
        return getCurrent().first();
    }
}

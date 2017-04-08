package dk.itu.vongrad.travelapp.realm.utils;

import dk.itu.vongrad.travelapp.realm.model.User;
import dk.itu.vongrad.travelapp.repository.RealmAPI;
import dk.itu.vongrad.travelapp.repository.UserRepository;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by vongrad on 4/8/17.
 */

public class AuthManager {

    /**
     * Create a new account in the Realm object server and subsequently create a new user in this realm
     * @param user
     */
    public static void register(final User user, final SyncUser.Callback callback) {
        handleAuthentication(user, callback, true);
    }

    /**
     * Login to the Realm object server
     * @param username
     * @param password
     */
    public static void login(String username, String password, final SyncUser.Callback callback) {
        handleAuthentication(new User(username, password), callback, false);
    }

    /**
     * Handle user authentication
     * @param user
     * @param callback
     * @param register - whether to create the user or not
     */
    public static void handleAuthentication(final User user, final SyncUser.Callback callback, final boolean register) {

        final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(user.getUsername(), user.getPassword(), register);

        SyncUser.loginAsync(syncCredentials, RealmAPI.AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser syncUser) {

                setActiveUser(syncUser);

                if(register) {
                   UserRepository.add(user);
                }

                callback.onSuccess(syncUser);
            }

            @Override
            public void onError(ObjectServerError error) {
                System.out.println(error);
            }
        });
    }

    /**
     * Logout the current user
     */
    public static void logout() {
        if(SyncUser.currentUser() != null) {
            Realm.getDefaultInstance().close();
            SyncUser.currentUser().logout();
        }
    }

    /**
     * Check whether the user is already authenticated
     * @return
     */
    public static boolean isAuthenticated() {
        return SyncUser.currentUser() != null;
    }

    /**
     * Sets the default realm to the currently authenticated user
     * @param user
     */
    public static void setActiveUser(SyncUser user) {
        SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, RealmAPI.REALM_URL).build();
        //syncConfiguration.shouldDeleteRealmIfMigrationNeeded();
        Realm.setDefaultConfiguration(syncConfiguration);
    }
}

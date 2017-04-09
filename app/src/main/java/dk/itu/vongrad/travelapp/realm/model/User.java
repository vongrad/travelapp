package dk.itu.vongrad.travelapp.realm.model;

import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import dk.itu.vongrad.travelapp.realm.utils.AutoIncementable;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Adam Vongrej on 3/22/17.
 */

public class User extends RealmObject {

    public User() {}

    public User(String firstName, String lastName, String email, long cardId, String username, String password, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cardId = cardId;
        this.username = username;
        this.password = password;
        this.account = account;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String firstName;
    private String lastName;
    private String email;

    private long cardId;

    @Ignore
    private String username;
    @Ignore
    private String password;

    private Account account;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

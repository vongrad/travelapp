package dk.itu.vongrad.travelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.Trip;
import dk.itu.vongrad.travelapp.realm.model.User;
import dk.itu.vongrad.travelapp.realm.utils.AuthManager;
import dk.itu.vongrad.travelapp.repository.UserRepository;
import dk.itu.vongrad.travelapp.services.AutoCheckoutService;
import dk.itu.vongrad.travelapp.services.BootBroadcastReceiver;
import dk.itu.vongrad.travelapp.utils.AlarmHelper;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        AccountFragment.OnFragmentInteractionListener,
        TripsFragment.OnListFragmentInteractionListener {

    private TextView txt_name;
    private TextView txt_email;
    private RealmResults<User> user;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, AutoCheckoutService.class);

        if (!AlarmHelper.isServiceAlarmUp(this, serviceIntent)) {
            AlarmHelper.scheduleRepeatingService(this, serviceIntent, BootBroadcastReceiver.MILLIS_INTERVAL);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(HomeFragment.newInstance());
        navigationView.getMenu().getItem(0).setChecked(true);

        View headerLayout = navigationView.getHeaderView(0);

        txt_name = (TextView) headerLayout.findViewById(R.id.txt_name);
        txt_email = (TextView) headerLayout.findViewById(R.id.txt_mail);

        user = UserRepository.getCurrent();

        user.addChangeListener(new RealmChangeListener<RealmResults<User>>() {
            @Override
            public void onChange(RealmResults<User> user) {
                updateUserViews(user.first(null));
            }
        });

        updateUserViews(user.first(null));

    }

    /**
     * Update all views related to the user model
     */
    private void updateUserViews(User user) {
        if(user != null) {
            txt_email.setText(user.getEmail());
            txt_name.setText(user.getFullName());
        }
    }

    @Override
    protected void onStop() {
        user.removeAllChangeListeners();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            AuthManager.logout();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // Clear the history stack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = HomeFragment.newInstance();
        }
        else if (id == R.id.nav_profile) {
            fragment = ProfileFragment.newInstance();
        } else if (id == R.id.nav_account) {
            fragment = AccountFragment.newInstance();

        } else if (id == R.id.nav_trips) {
            fragment = TripsFragment.newInstance();

        } else if (id == R.id.nav_logout) {
            AuthManager.logout();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // Clear the history stack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        if(fragment != null ) {
            replaceFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fragment)
                .commit();
    }


    @Override
    public void onFinish() {
        Fragment fragment = HomeFragment.newInstance();
        replaceFragment(fragment);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemClicked(Trip transaction) {

    }
}

package dk.itu.vongrad.travelapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.MacAddress;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.rojoxpress.slidebutton.SlideButton;

import java.util.List;
import java.util.UUID;

import dk.itu.vongrad.travelapp.adapters.LocationsAdapter;
import dk.itu.vongrad.travelapp.exceptions.NoActiveTripException;
import dk.itu.vongrad.travelapp.realm.model.Location;
import dk.itu.vongrad.travelapp.realm.model.Trip;
import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import dk.itu.vongrad.travelapp.repository.TripsRepository;
import dk.itu.vongrad.travelapp.services.BeaconService;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements BeaconManager.MonitoringListener {

    private Beacon lastBeacon;

    private RealmResults<Trip> activeTrips;

    private SlideButton sb_start;
    private RecyclerView rv_locations;

    private RecyclerView.Adapter adapter;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(getString(R.string.app_name));

        SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());

        // Start the service
        getContext().startService(new Intent(getContext(), BeaconService.class));

        // Bind to the service
        getContext().bindService(new Intent(getContext(), BeaconService.class), serviceConnection, Context.BIND_AUTO_CREATE);

        activeTrips = TripsRepository.getActiveTrips();
        activeTrips.addChangeListener(new RealmChangeListener<RealmResults<Trip>>() {
            @Override
            public void onChange(RealmResults<Trip> trips) {
                updateUI(trips.first(null));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().unbindService(serviceConnection);

        activeTrips.removeAllChangeListeners();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rv_locations = (RecyclerView) view.findViewById(R.id.rv_locations);
        rv_locations.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration divider = new DividerItemDecoration(rv_locations.getContext(), DividerItemDecoration.VERTICAL);
        rv_locations.addItemDecoration(divider);

        if (adapter != null) {
            rv_locations.setAdapter(adapter);
        }

        sb_start = (SlideButton) view.findViewById(R.id.sb_start);

        sb_start.setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void onSlide() {
                handleTrip();
            }
        });

        toggleEnabled(sb_start, false);
        sb_start.setText(getString(R.string.not_ready));

        return view;
    }

    /**
     * Toggle enabled state of ViewGroup by recursively traversing the ViewGroup and disabling all children
     * Used for SlideButton as it is an immature 3rd-party view that is implemented in FrameLayout and does not respect 'enabled' property
     * @param view
     * @param enabled
     */
    private void toggleEnabled(ViewGroup view, boolean enabled) {

        int childCount = view.getChildCount();

        for (int i = 0; i < childCount; i++) {

            View child = view.getChildAt(i);
            child.setEnabled(enabled);

            if (child instanceof ViewGroup) {
                toggleEnabled((ViewGroup) child, enabled);
            }
        }
    }

    /**
     * Helper function for starting/ending trips
     */
    private void handleTrip() {
        // Start trip
        if(activeTrips.isLoaded() && activeTrips.isEmpty()) {
            startTrip();
        }
        // End trip
        else if (activeTrips.isLoaded() && !activeTrips.isEmpty()){
            endTrip();
        }
        // Not ready - for some reason, it seems impossible to disable the slide button (3rd party)
        else {
            Toast.makeText(getContext(), getString(R.string.exc_query_not_complete), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start the user trip
     * The trip will be started only if other trip is not already active
     */
    private void startTrip() {
        // TODO: remove this - only for testing purposes
        lastBeacon = new Beacon(UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b"), MacAddress.fromString("AA:BB:CC:DD:EE:FF"), 4, 1234, 56, 10);

        if (lastBeacon == null) {
            Toast.makeText(getContext(), getString(R.string.exc_location_unknown), Toast.LENGTH_LONG).show();
        }
        else {
            Location location = new Location(lastBeacon);

            try {
                TripsRepository.startTrip(getContext(), location);

                Toast.makeText(getContext(), getString(R.string.trip_started), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getContext(), getString(R.string.exc_already_active), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * End the user trip
     * The trip will end only if there is an active trip
     */
    private void endTrip() {
        try {
            TripsRepository.finishTrip(getContext());

            Toast.makeText(getContext(), getString(R.string.trip_ended), Toast.LENGTH_LONG).show();
        } catch (NoActiveTripException e) {
            Toast.makeText(getContext(), getString(R.string.exc_no_active), Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI(Trip trip) {
        updateSlideButton(trip);
        updateRecyclerView(trip);
    }

    /**
     * Update the slide button
     * @param trip
     */
    private void updateSlideButton(Trip trip) {
        toggleEnabled(sb_start, true);

        if (trip != null && trip.isLoaded()) {
            sb_start.setText(getString(R.string.end_trip));
        }
        else {
            sb_start.setText(getString(R.string.start_trip));
        }
    }

    /**
     * Update the RecyclerView list
     * @param trip
     */
    private void updateRecyclerView(Trip trip) {
        if (trip != null) {
            adapter = new LocationsAdapter(getContext(), trip.getLocations().sort(RealmTable.Location.CREATED_AT, Sort.DESCENDING));
        }
        else {
            adapter = null;
        }
        rv_locations.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        lastBeacon = list.get(0);
        sb_start.setEnabled(true);
    }

    @Override
    public void onExitedRegion(Region region) {
        sb_start.setEnabled(false);
        lastBeacon = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BeaconService.BeaconBinder binder = (BeaconService.BeaconBinder) service;
            binder.setListener(HomeFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}

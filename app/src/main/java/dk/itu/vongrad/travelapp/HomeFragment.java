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
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.rojoxpress.slidebutton.SlideButton;

import java.util.List;

import dk.itu.vongrad.travelapp.adapters.LocationsAdapter;
import dk.itu.vongrad.travelapp.exceptions.ActiveTripException;
import dk.itu.vongrad.travelapp.exceptions.NoActiveTripException;
import dk.itu.vongrad.travelapp.realm.model.Location;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.Trip;
import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import dk.itu.vongrad.travelapp.repository.TripsRepository;
import dk.itu.vongrad.travelapp.services.BeaconService;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
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

    private Trip activeTrip;

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

        SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());

        // Start the service
        getContext().startService(new Intent(getContext(), BeaconService.class));

        // Bind to the service
        getContext().bindService(new Intent(getContext(), BeaconService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().unbindService(serviceConnection);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activeTrip = TripsRepository.getActive();

        if (activeTrip != null) {
            adapter = new LocationsAdapter(getContext(), activeTrip.getLocations().sort(RealmTable.Location.CREATED_AT, Sort.DESCENDING));
        }

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rv_locations = (RecyclerView) view.findViewById(R.id.rv_locations);
        rv_locations.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration divider = new DividerItemDecoration(rv_locations.getContext(), DividerItemDecoration.VERTICAL);
        rv_locations.addItemDecoration(divider);

        if (adapter != null) {
            rv_locations.setAdapter(adapter);
        }

        sb_start = (SlideButton) view.findViewById(R.id.sb_start);

        updateSlideButton();

        sb_start.setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void onSlide() {
                updateTripState();
            }
        });

        sb_start.setEnabled(lastBeacon != null);

        return view;
    }

    private void updateTripState() {
        try {
            // Start trip
            if(activeTrip == null) {
                startTrip();
            }
            // End trip
            else {
                endTrip();
            }
        }
        catch (Exception e) {
            // Not trip was found
            startTrip();
        }

        updateSlideButton();
    }

    private void startTrip() {

        if (lastBeacon == null) {
            Toast.makeText(getContext(), getString(R.string.exc_location_unknown), Toast.LENGTH_LONG).show();
        }
        else {
            Location location = new Location(lastBeacon);

            try {
                activeTrip = TripsRepository.startTrip(getContext(), location);
                adapter = new LocationsAdapter(getContext(), activeTrip.getLocations());
                rv_locations.setAdapter(adapter);

                Toast.makeText(getContext(), getString(R.string.trip_started), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getContext(), getString(R.string.exc_already_active), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void endTrip() {
        try {
            TripsRepository.finishTrip(getContext());

            activeTrip = null;
            adapter = null;
            rv_locations.setAdapter(null);

            Toast.makeText(getContext(), getString(R.string.trip_ended), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), getString(R.string.exc_no_active), Toast.LENGTH_LONG).show();
        }
    }

    private void updateSlideButton() {
        if (activeTrip == null) {
            sb_start.setText(getString(R.string.start_trip));
        }
        else {
            sb_start.setText(getString(R.string.end_trip));
        }
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

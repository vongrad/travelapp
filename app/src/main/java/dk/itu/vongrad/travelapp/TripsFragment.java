package dk.itu.vongrad.travelapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.itu.vongrad.travelapp.adapters.TripsAdapter;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.Trip;
import dk.itu.vongrad.travelapp.realm.table.RealmTable;
import dk.itu.vongrad.travelapp.repository.AccountRepository;
import dk.itu.vongrad.travelapp.repository.TripsRepository;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TripsFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    public TripsFragment() {}

    public static TripsFragment newInstance() {
        TripsFragment fragment = new TripsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(getString(R.string.drawer_trips));

        adapter = new TripsAdapter(getContext(), TripsRepository.getAllActive(), mListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trips_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(divider);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        public void onItemClicked(Trip trip);
    }
}

package dk.itu.vongrad.travelapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.itu.vongrad.travelapp.R;
import dk.itu.vongrad.travelapp.TripsFragment.OnListFragmentInteractionListener;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.Trip;
import dk.itu.vongrad.travelapp.utils.LocationHelper;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TripsAdapter extends RealmRecyclerAdapter<Trip, TripsAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    public TripsAdapter(Context context, RealmList<Trip> data, OnListFragmentInteractionListener mListener) {
        super(context, data);
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trip_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Trip trip = getItem(position);
        holder.txt_from.setText(LocationHelper.formatText(trip.getLocations().first()));
        holder.txt_to.setText(LocationHelper.formatText(trip.getLocations().last()));
        holder.txt_price.setText(String.valueOf(trip.getTransaction().getAmount()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClicked(trip);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView txt_from;
        public final TextView txt_to;
        public final TextView txt_price;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            txt_from = (TextView) view.findViewById(R.id.txt_from);
            txt_to = (TextView) view.findViewById(R.id.txt_to);
            txt_price = (TextView) view.findViewById(R.id.txt_price);
        }
    }
}

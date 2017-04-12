package dk.itu.vongrad.travelapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.itu.vongrad.travelapp.R;
import dk.itu.vongrad.travelapp.realm.model.Location;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import io.realm.OrderedRealmCollection;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class LocationsAdapter extends RealmRecyclerAdapter<Location, LocationsAdapter.ViewHolder> {

    public LocationsAdapter(Context context, OrderedRealmCollection<Location> data) {
        super(context, data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_location_simple, parent, false);
        return new LocationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Location location = getItem(position);
        holder.txt_location.setText(location.toString());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_location;

        public ViewHolder(View view) {
            super(view);
            txt_location = (TextView) view.findViewById(R.id.txt_location);
        }
    }
}

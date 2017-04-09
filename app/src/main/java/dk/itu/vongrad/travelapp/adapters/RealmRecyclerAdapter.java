package dk.itu.vongrad.travelapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Adam Vongrej on 4/9/17.
 */

public abstract class RealmRecyclerAdapter<T extends RealmObject, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected LayoutInflater inflater;
    protected RealmList<T> adapterData;
    protected Context context;
    private final RealmChangeListener<RealmList<T>> listener;

    public RealmRecyclerAdapter(Context context, RealmList<T> data) {

        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        this.adapterData = data;
        this.inflater = LayoutInflater.from(context);

        this.listener = new RealmChangeListener<RealmList<T>>() {
            @Override
            public void onChange(RealmList<T> element) {
                notifyDataSetChanged();
            }
        };

        if (data != null) {
            addListener(data);
        }
    }

    private void addListener(RealmList<T> data) {
        data.addChangeListener(listener);
    }

    private void removeListener(RealmList<T> data) {
        data.removeChangeListener(listener);
    }

    /**
     * Returns how many items are in the data set.
     *
     * @return the number of items.
     */
    @Override
    public int getItemCount() {
        return adapterData == null ? 0 : adapterData.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    public T getItem(int position) {
        if (adapterData == null) {
            return null;
        }
        return adapterData.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list. Note that item IDs are not stable so you
     * cannot rely on the item ID being the same after {@link #notifyDataSetChanged()} or
     * {@link #updateData(RealmList)} has been called.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Updates the data associated with the Adapter.
     *
     * Note that RealmResults and RealmLists are "live" views, so they will automatically be updated to reflect the
     * latest changes. This will also trigger {@code notifyDataSetChanged()} to be called on the adapter.
     *
     * This method is therefore only useful if you want to display data based on a new query without replacing the
     * adapter.
     *
     * @param data the new {@link RealmList} to display.
     */
    public void updateData(RealmList<T> data) {
        if (listener != null) {
            if (adapterData != null) {
                removeListener(adapterData);
            }
            if (data != null) {
                addListener(data);
            }
        }

        this.adapterData = data;
        notifyDataSetChanged();
    }
}

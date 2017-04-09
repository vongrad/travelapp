package dk.itu.vongrad.travelapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.itu.vongrad.travelapp.R;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import io.realm.RealmList;

/**
 * Created by Adam Vongrej on 4/9/17.
 */

public class TransactionAdapter extends RealmRecyclerAdapter<Transaction, TransactionAdapter.ViewHolder> {

    public TransactionAdapter(Context context, RealmList<Transaction> data) {
        super(context, data);
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_transaction_simple, parent, false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Transaction transaction = getItem(position);

        holder.txt_transaction.setText(getSign(transaction.getAmount()) + transaction.getAmount());

        if(transaction.getAmount() < 0) {
            holder.txt_transaction.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }
        else {
            holder.txt_transaction.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
    }

    private String getSign(double amount) {
        return amount < 0 ? "- " : "+ ";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_transaction;

        public ViewHolder(View view) {
            super(view);

            txt_transaction = (TextView) view.findViewById(R.id.txt_amount);
        }
    }
}
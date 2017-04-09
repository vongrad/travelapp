package dk.itu.vongrad.travelapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import dk.itu.vongrad.travelapp.R;
import dk.itu.vongrad.travelapp.realm.model.Account;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.repository.AccountRepository;
import dk.itu.vongrad.travelapp.repository.UserRepository;
import io.realm.Realm;

/**
 * Created by Adam Vongrej on 4/9/17.
 */

public class DepositDialog extends DialogFragment {

    private EditText edt_amount;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_deposit, null);

        edt_amount = (EditText) view.findViewById(R.id.edt_amount);

        builder.setView(view)
                .setPositiveButton(R.string.deposit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(!TextUtils.isEmpty(edt_amount.getText())) {
                            AccountRepository.addTransaction(Integer.parseInt(edt_amount.getText().toString()));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DepositDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}

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
import android.widget.Button;
import android.widget.EditText;

import dk.itu.vongrad.travelapp.adapters.TransactionAdapter;
import dk.itu.vongrad.travelapp.dialog.DepositDialog;
import dk.itu.vongrad.travelapp.realm.model.Account;
import dk.itu.vongrad.travelapp.realm.model.Transaction;
import dk.itu.vongrad.travelapp.realm.model.User;
import dk.itu.vongrad.travelapp.repository.AccountRepository;
import dk.itu.vongrad.travelapp.repository.UserRepository;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private User user;

    private Button btn_deposit;
    private EditText edt_balance;

    private RecyclerView rv_transactions;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = UserRepository.getCurrentUser();

        RealmResults<Transaction> transactions = AccountRepository.getTransactions();
        adapter = new TransactionAdapter(getContext(), transactions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initializeViews(view);

        return view;
    }

    /**
     * Initialize views
     * @param view - root view
     */
    public void initializeViews(View view) {
        btn_deposit = (Button) view.findViewById(R.id.btn_deposit);
        edt_balance = (EditText) view.findViewById(R.id.edt_balance);

        updateBalance(user.getAccount());

        user.getAccount().addChangeListener(new RealmChangeListener<Account>() {
            @Override
            public void onChange(Account account) {
                updateBalance(account);
            }
        });

        rv_transactions = (RecyclerView) view.findViewById(R.id.rv_transactions);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_transactions.getContext(), DividerItemDecoration.VERTICAL);
        rv_transactions.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(getContext());
        rv_transactions.setLayoutManager(layoutManager);

        rv_transactions.setAdapter(adapter);

        btn_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepositDialog dialog = new DepositDialog();
                dialog.show(getFragmentManager(), "DepositDialog");
            }
        });
    }

    public void updateBalance(Account account) {
        System.out.println("Updating balance");
        edt_balance.setText(String.valueOf(account.getBalance()));
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
    }
}

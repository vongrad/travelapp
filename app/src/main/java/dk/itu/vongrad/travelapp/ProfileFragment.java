package dk.itu.vongrad.travelapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import dk.itu.vongrad.travelapp.realm.model.User;
import dk.itu.vongrad.travelapp.repository.UserRepository;
import io.realm.Realm;
import io.realm.RealmResults;


public class ProfileFragment extends Fragment {

    private EditText edt_firstName;
    private EditText edt_lastName;
    private EditText edt_email;
    private EditText edt_cardId;

    private Button btn_update;

    private static final String ARG_USER = "user";

    // TODO: Rename and change types of parameters
    private User user;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = UserRepository.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(getString(R.string.drawer_profile));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeViews(view);

        return view;
    }

    /**
     * Initialize views
     * @param view - root view
     */
    public void initializeViews(View view) {

        edt_firstName = (EditText) view.findViewById(R.id.edt_firstname);
        edt_lastName = (EditText) view.findViewById(R.id.edt_lastname);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_cardId = (EditText) view.findViewById(R.id.edt_cardid);

        edt_firstName.setText(user.getFirstName());
        edt_lastName.setText(user.getLastName());
        edt_email.setText(user.getEmail());
        edt_cardId.setText(String.valueOf(user.getCardId()));

        btn_update = (Button) view.findViewById(R.id.btn_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        User user = UserRepository.getCurrentUser();
                        user.setFirstName(edt_firstName.getText().toString());
                        user.setLastName(edt_lastName.getText().toString());
                        user.setEmail(edt_email.getText().toString());
                        user.setCardId(Integer.parseInt(edt_cardId.getText().toString()));

                        realm.insertOrUpdate(user);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        mListener.onFinish();
                    }
                });
            }
        });
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

    public interface OnFragmentInteractionListener {
        void onFinish();
    }
}

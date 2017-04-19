package dk.itu.vongrad.travelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dk.itu.vongrad.travelapp.realm.utils.AuthManager;
import io.realm.ObjectServerError;
import io.realm.SyncUser;

public class LoginActivity extends AppCompatActivity implements SyncUser.Callback {

    private EditText edt_username;
    private EditText edt_password;

    private Button btn_login;

    private TextView txt_register;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AuthManager.isAuthenticated()) {
            AuthManager.setActiveUser(SyncUser.currentUser());

            completeLogin();
        }

        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

        btn_login = (Button) findViewById(R.id.btn_login);

        txt_register = (TextView) findViewById(R.id.txt_register);


        txt_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edt_username.getText()) && !TextUtils.isEmpty(edt_password.getText())) {
                    progress = ProgressDialog.show(LoginActivity.this, getString(R.string.progress_login), getString(R.string.progress_wait));
                    AuthManager.login(edt_username.getText().toString(), edt_password.getText().toString(), LoginActivity.this);
                }
            }
        });
    }

    @Override
    public void onSuccess(SyncUser user) {
        progress.dismiss();
        completeLogin();
    }

    /**
     * Complete the login
     */
    private void completeLogin() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onError(ObjectServerError error) {
        progress.dismiss();
        //TODO: show error message
    }
}

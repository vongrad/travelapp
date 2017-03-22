package dk.itu.vongrad.travelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_firstName;
    private EditText edt_lastName;
    private EditText edt_email;
    private EditText edt_cardId;
    private EditText edt_username;
    private EditText edt_password;

    private Button btn_register;

    private TextView txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_firstName = (EditText) findViewById(R.id.edt_firstname);
        edt_lastName = (EditText) findViewById(R.id.edt_lastname);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_cardId = (EditText) findViewById(R.id.edt_cardid);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

        btn_register = (Button) findViewById(R.id.btn_register);

        txt_login = (TextView) findViewById(R.id.txt_login);

        txt_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }
}

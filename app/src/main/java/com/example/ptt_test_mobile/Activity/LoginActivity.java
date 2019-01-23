package com.example.ptt_test_mobile.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ptt_test_mobile.Database.DatabaseHelper;
import com.example.ptt_test_mobile.R;
import com.example.ptt_test_mobile.Validation;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    TextInputEditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvRegister;
    private Validation validation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInstance();

        validation = new Validation(LoginActivity.this);
        databaseHelper = new DatabaseHelper(LoginActivity.this);
    }

    private void initInstance() {
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQLite();
            }
        });

        tvRegister.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    private void verifyFromSQLite(){
        if (!validation.validateFilled(edtEmail,textInputLayoutEmail,"Enter Valid Email")){
            return;
        }
        if (!validation.validateEmail(edtEmail,textInputLayoutEmail,"Enter Valid Email")){
            return;
        }
        if (!validation.validateFilled(edtPassword, textInputLayoutPassword, "Enter Password")){
            return;
        }
        Log.d("TEST pass this line","--------------------------------------");

        if (databaseHelper.checkUser(edtEmail.getText().toString(), edtPassword.getText().toString())){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            int userId = databaseHelper.getUserId(edtEmail.getText().toString());
            Log.d("TAG--------","User_id is ........ "+userId);
            intent.putExtra("USER_ID",userId);
            emptyInput();
            startActivity(intent);
        }
    }

    private void emptyInput(){
        edtEmail.setText(null);
        edtPassword.setText(null);
    }
}

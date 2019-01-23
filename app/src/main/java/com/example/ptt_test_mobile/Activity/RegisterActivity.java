package com.example.ptt_test_mobile.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ptt_test_mobile.Database.DatabaseHelper;
import com.example.ptt_test_mobile.Model.User;
import com.example.ptt_test_mobile.R;
import com.example.ptt_test_mobile.Validation;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutEmail, textInputLayoutPassword, textInputLayoutRePassword;
    TextInputEditText edtEmail, edtPassword, edtRePassword;
    Button btnRegister;
    TextView tvLogin;
    private Validation validation;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initInstance();

        validation = new Validation(RegisterActivity.this);
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        user = new User();
    }

    private void initInstance() {
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutRePassword = (TextInputLayout) findViewById(R.id.textInputLayoutRePassword);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        edtRePassword = (TextInputEditText) findViewById(R.id.edtRePassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserToDatabase();
            }
        });

        tvLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    private void addUserToDatabase() {
        if (!validation.validateFilled(edtEmail,textInputLayoutEmail,"Enter Valid Email")){
            return;
        }
        if (!validation.validateEmail(edtEmail,textInputLayoutEmail,"Enter Valid Email")){
            return;
        }
        if (!validation.validateFilled(edtPassword, textInputLayoutPassword, "Enter Password")){
            return;
        }
        if (!validation.validateMatch(edtPassword, edtRePassword, textInputLayoutRePassword, "Password Does Not Matches")){
            return;
        }

        if (!databaseHelper.checkEmail(edtEmail.getText().toString())){
            user.setEmail(edtEmail.getText().toString());
            user.setPassword(edtPassword.getText().toString());

            databaseHelper.addUser(user);

//            Snackbar.make()
            emptyInput();
        }
    }

    private void emptyInput(){
        edtEmail.setText(null);
        edtPassword.setText(null);
        edtRePassword.setText(null);
    }
}

package com.example.controlit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    Button signup, login;
    TextInputLayout name, email, password, passwordconfirm;
    String _name, _email, _password, _confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordconfirm = findViewById(R.id.passwordConf);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        signup.setOnClickListener(v -> {
            if (!validateFullName() | !validateEmail() | !validatePassword() | !validatePasswordConfirm()) {
                return;
            }

            _name = name.getEditText().getText().toString().trim();
            _email = email.getEditText().getText().toString().trim();
            _password = password.getEditText().getText().toString().trim();
            _confirmPassword = passwordconfirm.getEditText().getText().toString().trim();

            storeNewUserData();

            Intent intent = new Intent(Register.this, Login.class);
            intent.putExtra("name", _name);
            intent.putExtra("email", _email);
            intent.putExtra("password", _password);
            startActivity(intent);
        });
    }

    private void storeNewUserData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://controlit-78cc1-default-rtdb.firebaseio.com/");
        DatabaseReference reference = rootNode.getReference("Users");

        UserHelperClass helperClass = new UserHelperClass(_name, _email, _password);

        reference.child(_name).setValue(helperClass);

    }

    private boolean validateFullName() {
        String fullNameInput = name.getEditText().getText().toString().trim();

        if (fullNameInput.isEmpty()) {
            name.setError("Field can't be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +
                //"(?=.*[a-z])" +
                //"(?=.*[A-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{8,}" +
                "$";

        if (val.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password must contain at least 8 characters");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        String val = passwordconfirm.getEditText().getText().toString().trim();
        String val2 = password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            passwordconfirm.setError("Field can't be empty");
            return false;
        } else if (!val.equals(val2)) {
            passwordconfirm.setError("Passwords do not match");
            return false;
        } else {
            passwordconfirm.setError(null);
            passwordconfirm.setErrorEnabled(false);
            return true;
        }
    }
}
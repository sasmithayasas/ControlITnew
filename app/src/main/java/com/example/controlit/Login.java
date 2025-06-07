package com.example.controlit;

import static com.example.controlit.SessionManager.SESSION_USERSESSION;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    Button login, signup;
    TextInputLayout username, password;
    TextInputEditText usernameEdit, passwordEdit;
    String _username, _password;
    RelativeLayout loading;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.rememberMe);
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);


        SessionManager sessionManager = new SessionManager(Login.this, SESSION_USERSESSION);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> userData = sessionManager.getRememberMeDetailsFromSession();
            usernameEdit.setText(userData.get(SessionManager.KEY_SESSIONUSERNAME));
            passwordEdit.setText(userData.get(SessionManager.KEY_SESSIONPASSWORD));
            rememberMe.setChecked(true);
        }

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {

            if (!isConnected(Login.this)) {
                Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::showCustomDialog, 1000);
                return;
            }
            if (!validateFields()) {
                return;
            }

            loading.setVisibility(View.VISIBLE);

            _username = usernameEdit.getText().toString().trim();
            _password = passwordEdit.getText().toString().trim();

            if (rememberMe.isChecked()) {
                sessionManager.createRememberMeSession(_username, _password);
            }

            Query checkUser = FirebaseDatabase.getInstance("https://controlit-78cc1-default-rtdb.firebaseio.com/").getReference("Users").orderByChild("name").equalTo(_username);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        username.setError(null);
                        username.setErrorEnabled(false);

                        String passwordFromDB = snapshot.child(_username).child("password").getValue(String.class);
                        String emailFromDB = snapshot.child(_username).child("email").getValue(String.class);

                        if (passwordFromDB.equals(_password)) {

                            SessionManager sessionManager = new SessionManager(Login.this, SESSION_USERSESSION);
                            sessionManager.createLoginSession(_username, _password, emailFromDB);

                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            loading.setVisibility(View.GONE);
                            password.setError("Invalid Credentials");
                            password.requestFocus();
                        }

                    } else {
                        loading.setVisibility(View.GONE);
                        username.setError("User does not exist");
                        username.requestFocus();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        });


    }

    private boolean validateFields() {
        String val = usernameEdit.getText().toString().trim();
        String val2 = passwordEdit.getText().toString().trim();
        if (val.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if (val2.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else {
            username.setError(null);
            password.setError(null);
            username.setErrorEnabled(false);
            password.setErrorEnabled(false);
            return true;
        }

    }

    private boolean isConnected(Login login) {

        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo MobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConn != null && wifiConn.isConnected()) || (MobileConn != null && MobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("No Internet Connection").setCancelable(false).setPositiveButton("Retry", (dialog, id) -> {
            startActivity(new Intent(Login.this, Login.class));
        });
    }
}
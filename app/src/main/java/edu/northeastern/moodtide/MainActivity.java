package edu.northeastern.moodtide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.object.User;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signIn, signUp;
    private EditText emailInput, passwordInput;
    private TextView reset;
    private String email, password, savedEmail, savedPassword;

    private DatabaseReference usersRef= FirebaseDatabase.getInstance().getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        SharedPreferences sharedPreferences = getSharedPreferences("memory", Context.MODE_PRIVATE);
        savedEmail = sharedPreferences.getString("email", "").trim();
        savedPassword = sharedPreferences.getString("password", "").trim();
        if(!TextUtils.isEmpty(savedEmail)){emailInput.setText(savedEmail);}
        if(!TextUtils.isEmpty(savedPassword)){passwordInput.setText(savedPassword);}


        signUp = findViewById(R.id.signUpButton);
        signIn = findViewById(R.id.signInButton);
        reset = findViewById(R.id.resetPassword);


        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailInput.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Please enter your email.",
                            Toast.LENGTH_SHORT).show();
                }else{forgotPassword(email);}
            }
        });

    }

    private void signInUser(){
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "Please enter your credentials.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            // Get SharedPreferences instance
                            SharedPreferences sharedPreferences = getSharedPreferences("memory", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uid", uid);
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            // Handle specific exceptions
                            Exception e = task.getException();
                            //Log.e("SignIn", "Sign-in failed: " + e.getMessage());
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                            } else {
                                // General error, handle accordingly
                                Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                                // You can display a generic error message or perform additional error handling
                            }
                        }
                    }
                });
    }
    private void signUpUser(){
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "Please enter your credentials.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            User addUser=new User(uid);
                            usersRef.child(uid).setValue(addUser);
                            Toast.makeText(MainActivity.this, "New account created.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // Handle specific exceptions
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                // Email already exists, handle accordingly
                                Toast.makeText(MainActivity.this, "Account exists with this email address.", Toast.LENGTH_SHORT).show();
                                // You can navigate to a login screen or display an error message to the user
                            } else {
                                // General error, handle accordingly
                                Toast.makeText(MainActivity.this, "Failed to create user.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void forgotPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Password reset email sent successfully
                        Toast.makeText(MainActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        // Password reset email failed to send
                        Toast.makeText(MainActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
//
//    private void showSnackbar(String message) {
//        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
//    }
//
//    private void showSnackbarWithAction(String message, String actionText, View.OnClickListener listener) {
//        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
//                .setAction(actionText, listener)
//                .show();
//    }
}
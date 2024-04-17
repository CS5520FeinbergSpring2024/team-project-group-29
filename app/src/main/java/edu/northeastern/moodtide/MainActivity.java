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

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.notification.NotificationHelper;
import edu.northeastern.moodtide.object.Trigger;
import edu.northeastern.moodtide.object.User;


//Log in page
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

        //set up notification channel
        NotificationHelper.createNotificationChannel(this);

        //set up log in interface
        //retrieve saved email and password if available
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

        //connect to firebase authentication
        mAuth = FirebaseAuth.getInstance();

        //handle button clicks for different methods
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {signInUser();}
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

    //method to check for credentials and sign in user
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            SharedPreferences sharedPreferences = getSharedPreferences("memory", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uid", uid);
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //method to check for credentials and sign up new user
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
                            List<Trigger> myTriggers = new ArrayList<>();
                            myTriggers.add(new Trigger("Food"));
                            myTriggers.add(new Trigger("Friend"));
                            myTriggers.add(new Trigger("Weather"));
                            myTriggers.add(new Trigger("Family"));
                            myTriggers.add(new Trigger("Work"));
                            myTriggers.add(new Trigger("School"));
                            User addUser = new User(uid, myTriggers);
                            usersRef.child(uid).setValue(addUser);
                            Toast.makeText(MainActivity.this, "New account created.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(MainActivity.this, "Account exists with this email address.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to create user.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    //method to send a password reset email to the user through firebase authentication
    private void forgotPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
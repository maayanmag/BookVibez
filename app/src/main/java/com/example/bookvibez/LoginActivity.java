package com.example.bookvibez;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookvibez.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Arrays;

import static com.facebook.appevents.UserDataStore.EMAIL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CallbackManager callbackManager;
    private LoginButton fbloginButton;
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button btnLogIn;
    private Button btnRegister;

    private static final String TAG = "EmailPassword";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "login activity create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.email_box);
        mPasswordField = findViewById(R.id.password_box);
        btnRegister = (Button) findViewById(R.id.register_button);
        btnLogIn = (Button) findViewById(R.id.login_button);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                String pass = mPasswordField.getText().toString();
                if (!mEmailField.equals("") && !pass.equals("")) {
                    mAuth.signInWithEmailAndPassword(email, pass);
                } else {
                    toastMessage("You didn't fill in all the fields");
                }
            }
        });
    }
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        } else{
//            Log.i(TAG,"sign in");
//        }};


    @Override
    public void onStart() {
        Log.i(TAG, "login activity start");

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }



    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
        if (user != null) {

//            findViewById(R.id.password_box).setVisibility(View.GONE);
//            findViewById(R.id.email_box).setVisibility(View.GONE);
            findViewById(R.id.login_button).setVisibility(View.VISIBLE);

        } else {

            findViewById(R.id.password_box).setVisibility(View.VISIBLE);
            findViewById(R.id.email_box).setVisibility(View.VISIBLE);
//            findViewById(R.id.login_button).setVisibility(View.GONE);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {


        int i = v.getId();
        if (i == R.id.register_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.login_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
//        Toast.makeText(this, "moving to main", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

//    public void addUser(String disName, String email, String pass, Uri image, String university,
//                        ArrayList<String> langs)
//    {
//        signUp(email, pass);
//        MainActivity.user = FirebaseAuth.getInstance().getCurrentUser();
//
//
//
//        /* update display name, and image*/
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(disName).setPhotoUri(image)
//                .build();
//
//
//        String userId = MainActivity.userId;
//
//        DocumentReference busRef = db.collection(USERS_DATA_STRING).document(userId);
//        User userObj = new User(disName, image, university, langs, userId);
//        busRef.set(userObj);
//
//    }

}

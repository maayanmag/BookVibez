package com.example.mybookvibez;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;
    private EditText mNameField;
    private EditText mVibeField;
    private ImageView mProfileImage;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "EmailPassword";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "register activity create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

//        if (firebaseAuth.getCurrentUser() != null){
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }

        mEmailField = (EditText) findViewById(R.id.register_email_box);
        mPasswordField = (EditText) findViewById(R.id.register_password_box);
        mConfirmPasswordField = (EditText) findViewById(R.id.register_confirm_password);
        btnRegister = (Button) findViewById(R.id.main_register_button);
        mNameField = (EditText) findViewById(R.id.register_name);
        mVibeField = (EditText) findViewById(R.id.register_vibe);
        mProfileImage = (ImageView) findViewById(R.id.profile_pic);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister){
            registerUser(this);
        }
    }

    private void registerUser(final Activity act){
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        String confirmPassword = mConfirmPasswordField.getText().toString().trim();
        final String name = mNameField.getText().toString().trim();
        final String vibe = mVibeField.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password (at least 6 digits)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Please make sure you confirm your password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        // if everything OK:
        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AuthUI", "createUserWithEmail:success");
                            MainActivity.user = firebaseAuth.getCurrentUser();
                            MainActivity.userId = MainActivity.user.getUid();
                            User user = new User (name, 0, vibe);
                            // add here the profile picture, call here "mProfileImage"
                            ServerApi.getInstance().addUser(user, MainActivity.userId);
                            Intent main = new Intent(act.getApplicationContext(),
                                    MainActivity.class);
                            act.startActivity(main);
                            act.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AuthUI", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Could not register, please try again",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }
    @Override
    protected void onStart() {
        super.onStart();

    }
}
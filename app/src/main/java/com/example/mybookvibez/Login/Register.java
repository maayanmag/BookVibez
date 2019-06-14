package com.example.mybookvibez.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mybookvibez.AddBook.NewBookFragment;
import com.example.mybookvibez.MainActivity;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.example.mybookvibez.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Callable;

import static android.app.Activity.RESULT_OK;

public class Register extends Fragment {

    private EditText mEmailField, mPasswordField, mConfirmPasswordField, mNameField, mVibeField;
    private Button btnRegister, addProfilePicture;
    private Uri image = null;
    public static ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 1;
    private final static int GALLERY_INTENT = 2;
    private FirebaseAuth.AuthStateListener mAuthListener;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register, container, false);

        progressDialog = new ProgressDialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();

//        if (firebaseAuth.getCurrentUser() != null){
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }

        getAttributes(view);
        return view;
    }


    private void getAttributes(View view){
        mEmailField = (EditText) view.findViewById(R.id.register_email_box);
        mPasswordField = (EditText) view.findViewById(R.id.register_password_box);
        mConfirmPasswordField = (EditText) view.findViewById(R.id.register_confirm_password);
        mNameField = (EditText) view.findViewById(R.id.register_name);
        mVibeField = (EditText) view.findViewById(R.id.register_vibe);

        addProfilePicture = (Button) view.findViewById(R.id.addProfilePicture);
        addProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        btnRegister = (Button) view.findViewById(R.id.main_register_button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btnRegister", "clicked");
                registerUser();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            progressDialog.setMessage("Uploading image ...");
            progressDialog.show();
            image = data.getData();
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Photo Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validation(String email, String password, String confirm, String name){
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getContext(), "Please enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(getContext(), "Please enter password (at least 6 digits)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirm)){
            Toast.makeText(getContext(), "Please make sure you confirm your password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void registerUser(){
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString();
        String confirmPassword = mConfirmPasswordField.getText().toString();
        final String name = mNameField.getText().toString();
        final String vibe = mVibeField.getText().toString();

        if(!validation(email, password, confirmPassword, name)) {
            return; //TODO
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


                            Callable<Void> func = new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                    return null;
                                }
                            };
                            ServerApi.getInstance().addUser(new User(name, vibe, "0"), firebaseAuth.getCurrentUser().getUid(), image, func);

                        } else {    // If sign in fails, display a message to the user.
                            Log.w("AuthUI", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Could not register, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
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
import android.widget.Toast;
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

    private EditText mEmailField, mPasswordField, mPhoneNumber, mNameField, mVibeField;
    private Button btnRegister, addProfilePicture;
    private Uri image = null;
    public static ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private final static int GALLERY_INTENT = 2;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register, container, false);

        progressDialog = new ProgressDialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        getAttributes(view);
        return view;
    }

    /**
     * this func get the attributes needed for this screen by their id as assigned in XML
     * @param view - View to get the objects from
     */
    private void getAttributes(View view){
        mEmailField = (EditText) view.findViewById(R.id.register_email_box);
        mPasswordField = (EditText) view.findViewById(R.id.register_password_box);
        mPhoneNumber = (EditText) view.findViewById(R.id.phone_number);
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

    /**
     * this func makes a validation check to registration arguments
     * @param email - the text inserted by the user for mail address
     * @param password - the text inserted by the user for password
     * @param phone - the text inserted by the user for phone number
     * @param name - the text inserted by the user for his name
     * @return true if all fields aren't empty
     */
    private boolean validation(String email, String password, String phone, String name){
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getContext(), "Please enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(getContext(), "Please enter password (at least 6 digits)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(getContext(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(name)){
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * this func checks the user's input and, if OK, create a new user object for it and saves it.
     */
    private void registerUser(){
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString();
        final String phoneNumber = mPhoneNumber.getText().toString();
        final String name = mNameField.getText().toString();
        final String vibe = mVibeField.getText().toString();

        if(!validation(email, password, phoneNumber, name)) {
        Toast.makeText(getContext(), "registration failed, plead check again your input", Toast.LENGTH_SHORT).show();
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


                            Callable<Void> func = new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                    return null;
                                }
                            };
                            ServerApi.getInstance().addUser(new User(name, vibe, phoneNumber), firebaseAuth.getCurrentUser().getUid(), image, func);

                        } else {    // If sign in fails, display a message to the user.
                            Log.w("AuthUI", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Could not register, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
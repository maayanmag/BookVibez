package com.vibez.mybookvibez.AddBook;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.vibez.mybookvibez.R;
import static android.app.Activity.RESULT_OK;

//@SuppressWarnings("deprecation")
public class AddBookImagePopup extends DialogFragment {

    public static ProgressDialog mProgress;
    private final static int CAMERA_REQUEST_CODE = 1;
    private final static int GALLERY_INTENT = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_book_image_popup, container, false);
        mProgress = new ProgressDialog(getContext());

        Button cameraBtn = (Button) v.findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        Button galleryBtn = (Button) v.findViewById(R.id.gallery_btn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_INTENT) && resultCode == RESULT_OK){
            mProgress.setMessage("Uploading image ...");
            mProgress.show();
            Uri uri = data.getData();
            NewBookFragment.setUri(uri);
            mProgress.dismiss();
            Toast.makeText(getContext(), "Photo Selected", Toast.LENGTH_SHORT).show();

        }
    }
}
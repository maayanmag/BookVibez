package com.example.bookvibez;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

public class MyBooksRecyclerAdapter extends RecyclerView.Adapter<MyBooksRecyclerAdapter.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference imgRef = storageRef.child("Photos/PNG_transparency_demonstration_1.png");
//    private CollectionReference bookRef = db.collection("books");
//    private BookFirestoreAdapter firestoreAdapter;
    private Context context;

    private StorageReference reference = FirebaseStorage.getInstance().getReference("Photos");


    private List<BookItem> booksList;

    private final BooksRecyclerAdapter.OnItemClickListener mListener;


    public MyBooksRecyclerAdapter(Context context, List<BookItem> books,
                                  BooksRecyclerAdapter.OnItemClickListener listener) {
        this.booksList = books;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.my_books_single_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.bind(booksList.get(i), mListener);
    }

    @Override
    public int getItemCount() {
        return this.booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.single_book_image);
        }

        public void bind(final BookItem book, final BooksRecyclerAdapter.OnItemClickListener listener) {
            try {
                String imgId = String.valueOf(book.getBookImg());
                reference = reference.child("Photos");
                reference = reference.child(imgId);
                db.collection("books")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            String temp = "bookImg";
                                            DocumentReference ref = (DocumentReference) document.get(temp);
                                            if (ref != null){
                                                reference = FirebaseStorage.getInstance().getReference("Photos");
                                                reference = reference.child(ref.get().toString());
                                                System.out.println("here: " + ref.get().toString());

                                            }

                                        } else {

                                        }
                                    }
                                } else {
                                }
                            }
                        });
//                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String imageUrl = uri.toString(); //image url
//
//                    }
//                });
            //            GlideApp.with(context)
//                    .load(url)
//                    .into(image);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View view) {
//                    listener.onItemClick(book);
//                }
//            });
            } catch (Exception e){

            }
        }
    }
}

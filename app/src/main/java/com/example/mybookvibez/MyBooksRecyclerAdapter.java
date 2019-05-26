package com.example.mybookvibez;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyBooksRecyclerAdapter extends RecyclerView.Adapter<MyBooksRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BookItem book);
    }

    private Context context;

    private List<BookItem> booksList;

    private final OnItemClickListener mListener;


    public MyBooksRecyclerAdapter(Context context, List<BookItem> booksList,
                                  OnItemClickListener listener) {
        this.booksList = booksList;
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
        private ImageView image;
        private View mView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            image = itemView.findViewById(R.id.single_book_image);
        }

        public void bind(final BookItem book, final OnItemClickListener listener) {
//            downloadBookFrontCover(image, book, book.getId());
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(book);
                }
            });

//                String imgId = String.valueOf(book.getBookImg());
//                reference = reference.child("Photos");
//                reference = reference.child(imgId);
//                db.collection("books")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        if (document.exists()) {
//                                            String temp = "bookImg";
//                                            DocumentReference ref = (DocumentReference) document.get(temp);
//                                            if (ref != null){
//                                                reference = FirebaseStorage.getInstance().getReference("Photos");
//                                                reference = reference.child(ref.get().toString());
//                                                System.out.println("here: " + ref.get().toString());
//
//                                            }
//
//                                        } else {
//
//                                        }
//                                    }
//                                } else {
//                                }
//                            }
//                        });
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
        }
    }
}

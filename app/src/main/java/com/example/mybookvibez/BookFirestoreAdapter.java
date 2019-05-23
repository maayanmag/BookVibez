//package com.example.bookvibez;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.module.AppGlideModule;
//
//
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.InputStream;
//
//public class BookFirestoreAdapter extends FirestoreRecyclerAdapter<BookItem, BookFirestoreAdapter.BookHolder> {
//
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private FirebaseStorage storage = FirebaseStorage.getInstance();
//    private StorageReference storageRef = storage.getReference();
//    private StorageReference s = storageRef.child("Photos/maayanTry.jpeg");
//    private CollectionReference bookRef = db.collection("books");
//    private BookFirestoreAdapter firestoreAdapter;
//    private Context context;
//
//
//    public BookFirestoreAdapter(@NonNull FirestoreRecyclerOptions<BookItem> options, Context context) {
//        super(options);
//        this.context = context;
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull BookItem model) {
//        GlideApp.with(context)
//                .load(s)
//                .into(holder.image);
//        holder.image.setImageResource(model.getBookImg());
//
//    }
//
//    @NonNull
//    @Override
//    public BookHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_books_single_item, viewGroup, false);
//        return new BookHolder(v);
//    }
//
//    class BookHolder extends RecyclerView.ViewHolder {
//
//        ImageView image;
//
//        public BookHolder(@NonNull View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.single_book_image);
//
//        }
//    }
//
//}

package com.example.bookvibez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookvibez.ListOfBooks.random_books;


public class ProfileFragment extends Fragment {

//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//    private CollectionReference bookRef = db.collection("books");
//    private BookFirestoreAdapter firestoreAdapter;

    private List<BookItem> myBooks = new ArrayList<>();

    private List<BookItem> booksIRead = new ArrayList<>();

    private MyBooksRecyclerAdapter myBooksRecyclerAdapter;

    private MyBooksRecyclerAdapter booksIReadAdapter;

    private RecyclerView myBooksRecyclerView;

    private RecyclerView booksIReadRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_layout, null);

        myBooks = random_books();
        myBooksRecyclerView = view.findViewById(R.id.my_books_recycler_view);
        booksIReadRecyclerView = view.findViewById(R.id.books_i_read_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        myBooksRecyclerView.setLayoutManager(mLayoutManager);
        myBooksRecyclerAdapter = new MyBooksRecyclerAdapter(this.getContext(), myBooks,
                new BooksRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookItem book) {
                return;
            }
        });
        myBooksRecyclerView.setAdapter(myBooksRecyclerAdapter);


        booksIRead = random_books();
        RecyclerView.LayoutManager booksIReadLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        booksIReadRecyclerView.setLayoutManager(booksIReadLayoutManager);
        booksIReadAdapter = new MyBooksRecyclerAdapter(this.getContext(), booksIRead,
                new BooksRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookItem book) {
                return;
            }
        });

        booksIReadRecyclerView.setAdapter(booksIReadAdapter);
//        setUpRecyclerView(view);


        return view;
    }

//    private void setUpRecyclerView(View view) {
//        Query query = bookRef.orderBy("dateAdded");
//        FirestoreRecyclerOptions<BookItem> options = new FirestoreRecyclerOptions.Builder<BookItem>()
//                .setQuery(query, BookItem.class).build();
//
//        firestoreAdapter = new BookFirestoreAdapter(options, getContext());
//        RecyclerView recyclerView = view.findViewById(R.id.my_books_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        recyclerView.setAdapter(firestoreAdapter);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        firestoreAdapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        firestoreAdapter.stopListening();
//    }
}

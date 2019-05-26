package com.example.mybookvibez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

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

        ImageView bookGenreImg = view.findViewById(R.id.book_genre_img);

//        setUpRecyclerView(view);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String user_id = currentUser.getUid();
        User[] user = new User[1];
        TextView firstName = view.findViewById(R.id.user_first_name);
        TextView lastName = view.findViewById(R.id.user_last_name);
        TextView vibez = view.findViewById(R.id.myVibe);
        //ServerApi.getInstance().getUser(user_id, user);
        return view;
    }


}

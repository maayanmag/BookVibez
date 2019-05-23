package com.example.bookvibez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookvibez.ListOfBooks.random_books;


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

        ImageView bookGenreImg = view.findViewById(R.id.book_genre_img);

//        setUpRecyclerView(view);


        return view;
    }


}

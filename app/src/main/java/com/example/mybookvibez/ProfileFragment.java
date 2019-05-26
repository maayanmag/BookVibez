package com.example.mybookvibez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    private User[] user;

    private List<BookItem> myBooks = new ArrayList<>();

    private List<BookItem> booksIRead = new ArrayList<>();

    private RecyclerView myBooksRecyclerView;

    private RecyclerView booksIReadRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_layout, null);
        user = new User[1];
        TextView firstName = view.findViewById(R.id.user_first_name);
//        TextView lastName = view.findViewById(R.id.user_last_name);
        TextView vibez = view.findViewById(R.id.vibePointsNum);
        ServerApi.getInstance().getUser(MainActivity.userId, user, firstName, vibez, null);
        myBooks = user[0].getMyBooks();
        booksIRead = user[0].getBooksIRead();

        myBooksRecyclerView = view.findViewById(R.id.my_books_recycler_view);
        setBooksRecyclerView(myBooksRecyclerView, myBooks);
        booksIReadRecyclerView = view.findViewById(R.id.books_i_read_recycler_view);

        setBooksRecyclerView(booksIReadRecyclerView, booksIRead);
        return view;
    }

    private void setBooksRecyclerView(RecyclerView recyclerView, List<BookItem> booksList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        MyBooksRecyclerAdapter adapter = new MyBooksRecyclerAdapter(this.getContext(), booksList,
                new MyBooksRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BookItem book) {
                        BookPageFragment.bookToDisplay = book;
                        loadBookPageFragment();
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    /**
     * this function replaces the layout to a book page layout in case some book was clicked in the list
     */
    private void loadBookPageFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.profile_layout, new BookPageFragment());
        transaction.commit();
    }

}

package com.example.mybookvibez;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private User[] userArr = new User[1];
    public static String userToDisplay = MainActivity.userId;
    public static boolean displayMyProfile = false;

    private ArrayList<BookItem> myBooks, booksIRead;
    private CircleImageView ownerImg;
    private TextView firstName, vibezString, vibez;
    private ArrayList<String> readId, mybooksId;
    private RecyclerView myBooksRecyclerView;
    private MyBooksRecyclerAdapter adapterMyBooks;
    private RecyclerView booksIReadRecyclerView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_layout, container, false);
        mybooksId = new ArrayList<>();
        readId = new ArrayList<>();

        getAttributes(view);

        String id;
        if(displayMyProfile) {
            id = MainActivity.userId;
        }
        else {
            id = userToDisplay;
        }

        ServerApi.getInstance().getUserForProfileFragment(id, userArr, ownerImg, firstName,
                vibezString, vibez, mybooksId, readId);



        myBooks = new ArrayList<>();
        ServerApi.getInstance().getBooksByIdsList(myBooks, mybooksId, new Callable<Void>() {
            @Override
            public Void call() {
                return setBooksRecyclerView(myBooksRecyclerView, myBooks);
            }
        });


        booksIRead = new ArrayList<>();
        ServerApi.getInstance().getBooksByIdsList(booksIRead, readId, new Callable<Void>() {
            @Override
            public Void call() {
                return setBooksRecyclerView(booksIReadRecyclerView, booksIRead);
            }
        });


        return view;
    }

    private void getAttributes(View view) {
        firstName = view.findViewById(R.id.user_first_name);
        vibez = view.findViewById(R.id.vibePointsNum);
        vibezString = view.findViewById(R.id.myVibe);
        myBooksRecyclerView = view.findViewById(R.id.my_books_recycler_view);
        booksIReadRecyclerView = view.findViewById(R.id.books_i_read_recycler_view);
        ownerImg = (CircleImageView) view.findViewById(R.id.circ_image);
    }

    private Void setBooksRecyclerView(RecyclerView recyclerView, List<BookItem> booksList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterMyBooks = new MyBooksRecyclerAdapter(this.getContext(), booksList,
                new MyBooksRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BookItem book) {
                        BookPageFragment.bookToDisplay = book;
                        loadBookPageFragment();
                    }
                });
        recyclerView.setAdapter(adapterMyBooks);
        return null;
    }

    /**
     * this function replaces the layout to a book page layout in case some book was clicked in the list
     */
    private void loadBookPageFragment() {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new BookPageFragment());
        transaction.commit();
    }

}

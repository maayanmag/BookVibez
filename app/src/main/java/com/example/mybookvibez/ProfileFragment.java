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

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private User[] userArr = new User[1];

    public static User userToDisplay = null;

    private ArrayList<BookItem> myBooks, booksIRead;
    private CircleImageView ownerImg;
    private TextView firstName, vibezString, vibez;
    private ArrayList<String> readId, mybooksId;
    private RecyclerView myBooksRecyclerView;
    private RecyclerView booksIReadRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_layout, container, false);
        mybooksId = new ArrayList<>();
        readId = new ArrayList<>();


        getAttributes(view);

        String id;
        if(userToDisplay == null) {
            id = MainActivity.userId;
        }
        else {
            id = userToDisplay.getId();
        }

        ServerApi.getInstance().getUserForProfileFragment(id, userArr, ownerImg, firstName,
                vibezString, vibez, mybooksId, readId);

        try {
            myBooks = new ArrayList<>();
            ServerApi.getInstance().getBooksByIdsList(myBooks, mybooksId);
            setBooksRecyclerView(myBooksRecyclerView, myBooks);
        } catch (IndexOutOfBoundsException e) { }
        try {
            booksIRead = new ArrayList<>();
            ServerApi.getInstance().getBooksByIdsList(booksIRead, readId);
            setBooksRecyclerView(booksIReadRecyclerView, booksIRead);
        } catch (IndexOutOfBoundsException ex){ }

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
        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new BookPageFragment());
        transaction.commit();
    }

}

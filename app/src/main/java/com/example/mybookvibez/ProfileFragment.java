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

    private ArrayList<BookItem> myBooks;
    private CircleImageView ownerImg;
    private ArrayList<BookItem> booksIRead;
    private RecyclerView myBooksRecyclerView;
    private RecyclerView booksIReadRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_layout, container, false);

        TextView firstName = view.findViewById(R.id.user_first_name);
        TextView vibez = view.findViewById(R.id.vibePointsNum);
        TextView vibezString = view.findViewById(R.id.myVibe);
        myBooksRecyclerView = view.findViewById(R.id.my_books_recycler_view);
        booksIReadRecyclerView = view.findViewById(R.id.books_i_read_recycler_view);
        ownerImg = (CircleImageView) view.findViewById(R.id.circ_image);

        ArrayList<ArrayList<BookItem>> booksLists = new ArrayList<>(3);
        String id;
        if(userToDisplay == null)
            id = MainActivity.userId;
        else
            id = userToDisplay.getId();

        ServerApi.getInstance().getUserForProfileFragment(id, userArr, ownerImg, firstName,
                vibezString, vibez, booksLists);

        try {
            myBooks = booksLists.get(0);
            booksIRead = booksLists.get(1);

            setBooksRecyclerView(myBooksRecyclerView, myBooks);
            setBooksRecyclerView(booksIReadRecyclerView, booksIRead);


        } catch (IndexOutOfBoundsException ex){
            myBooks = null;
            booksIRead = null;
        }



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
        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new BookPageFragment());
        transaction.commit();
    }

}

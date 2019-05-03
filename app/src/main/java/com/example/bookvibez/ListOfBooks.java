package com.example.bookvibez;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListOfBooks extends Fragment {

    private List<BookItem> booksList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BooksRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_layout, null);

        booksList = random_books(); //TODO - will be replaced when connecting to FireBase

        /* handling RecyclerView object in layout */
        handlingRecycleViewer(view);

        /* handling click on "addBook" button */
        handlingAddBookButton(view);

        return view;
    }


    /**
     * the function handles the RecycleView object in content_scrolling_list.
     * it defines the viewer and set a manager and an adapter to it.
     * @param view - current view (content_scrolling_list)
     */
    private void handlingRecycleViewer(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(new BooksRecyclerAdapter(booksList, new BooksRecyclerAdapter.OnItemClickListener() {
            @Override public void onItemClick(BookItem book) {
                String toShow = "Item with id " + book.getId() + " clicked";
                Toast.makeText(getContext(), toShow, Toast.LENGTH_LONG).show();
                loadBookPageFragment();
            }
        }));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * this function replaces the layout to a book page layout in case some book was clicked in the list
     */
    private void loadBookPageFragment() {
        BookPageFragment bookPage = new BookPageFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction frag_trans = getActivity().getSupportFragmentManager().beginTransaction();;
        frag_trans = manager.beginTransaction();
        frag_trans.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        frag_trans.hide(ListOfBooks.this);
        frag_trans.replace(android.R.id.content, bookPage);
        frag_trans.commit();
    }


    /**
     * the function handles the Add floating button object in content_scrolling_list.
     * it defines a listener.
     * @param view - current view (content_scrolling_list)
     */
    private void handlingAddBookButton(View view){
        FloatingActionButton fabAdd = (FloatingActionButton) view.findViewById(R.id.addBookFloatingBottom);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new AddBookPopup();
                dialogFragment.show(ft, "dialog");
            }
        });
    }


    /* temporary function to load data into booksList, will be deleted when we have a database */
    private List<BookItem> random_books() {
        List<BookItem> l = new ArrayList<>();
        BookItem b1 = new BookItem(1,"book1", "author1");
        BookItem b2 = new BookItem(2,"book2", "author2");
        BookItem b3 = new BookItem(3,"book3", "author3");
        BookItem b4 = new BookItem(4,"book4", "author4");
        BookItem b11 = new BookItem(5,"book1", "author1");
        BookItem b22 = new BookItem(6,"book2", "author2");
        BookItem b33 = new BookItem(7,"book3", "author3");
        BookItem b44 = new BookItem(8,"book4", "author4");
        l.add(b1); l.add(b2); l.add(b3); l.add(b4);
        l.add(b11); l.add(b22); l.add(b33); l.add(b44);
        return l;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }





}



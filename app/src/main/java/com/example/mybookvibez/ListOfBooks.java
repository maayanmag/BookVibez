package com.example.mybookvibez;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mybookvibez.AddBook.AddBookPopup;

import java.util.ArrayList;
import java.util.List;


public class ListOfBooks extends Fragment implements SearchView.OnQueryTextListener {

    public static ArrayList<BookItem> booksList = MapFragment.getListOfBooks();
    private RecyclerView recyclerView;
    private static BooksRecyclerAdapter adapter;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);

        handlingRecycleViewer(view);
        handlingAddBookButton(view);
        handlingSearchView(view);

        //ServerApi.getInstance().getBooksList(booksList, adapter);

        return view;
    }


    /**
     * the function handles the RecycleView object in content_scrolling_list.
     * it defines the viewer and set a manager and an adapter to it.
     * @param view - current view (content_scrolling_list)
     */
    private void handlingRecycleViewer(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new BooksRecyclerAdapter(booksList, new BooksRecyclerAdapter.OnItemClickListener() {
            @Override public void onItemClick(BookItem book) {
                BookPageFragment.bookToDisplay = book;
                loadBookPageFragment();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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


    private void handlingSearchView(View view) {
        searchView = (SearchView) view.findViewById(R.id.search_bar);
        CharSequence query = searchView.getQuery(); // get the query string currently in the text field
        searchView.setQueryHint("Search View");
        searchView.setOnQueryTextListener(this);
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


    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getContext(), "Query submitted", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String place) {
        Toast.makeText(getContext(), "Query filtering", Toast.LENGTH_SHORT).show();
        adapter.filter(place);
        return true;
    }

    public static List<BookItem> getBooksList() {
        return booksList;
    }

    public static void clearBooksList(){
        booksList.clear();
    }

}



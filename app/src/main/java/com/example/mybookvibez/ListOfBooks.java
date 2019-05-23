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

    public ArrayList<BookItem> booksList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BooksRecyclerAdapter adapter;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);

        handlingRecycleViewer(view);
        handlingAddBookButton(view);
        handlingSearchView(view);

        ServerApi.getInstance().getBooksList(booksList, adapter);

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


    /* temporary function to load data into booksList, will be deleted when we have a database
    public static List<BookItem> random_books() {
        List<BookItem> l = new ArrayList<>();
        BookItem b1 = new BookItem(0,"A Little Bit of Meditation", "Amy Leigh Mercree", R.drawable.book1);
        BookItem b2 = new BookItem(1,"Transcendental Meditation", "Jack Forem",R.drawable.book2);
        BookItem b3 = new BookItem(2,"Real Happiness", "Sharon Salzberg", R.drawable.book3);
        BookItem b4 = new BookItem(3,"No Time Like The Present", "Jack Kornfield", R.drawable.book4);
        BookItem b44 = new BookItem(4,"Yoga and Vipassana", "Amit Ray", R.drawable.book8);
        BookItem b11 = new BookItem(5,"A Path With Heart", "Jack Kornfield", R.drawable.book5);
        BookItem b22 = new BookItem(6,"Your Best Meditation", "P. M. Marrison", R.drawable.book6);
        BookItem b33 = new BookItem(7,"Peace, Love and You", "Nerissa Marie", R.drawable.book7);
        l.add(b1); l.add(b2); l.add(b3); l.add(b4);
        l.add(b11); l.add(b22); l.add(b33); l.add(b44);
        return l;
    }
*/

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

    public List<BookItem> getBooksList() {
        return booksList;
    }


}



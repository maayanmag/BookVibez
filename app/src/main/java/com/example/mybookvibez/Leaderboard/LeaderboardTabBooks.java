package com.example.mybookvibez.Leaderboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mybookvibez.BookItem;
import com.example.mybookvibez.BookPageFragment;
import com.example.mybookvibez.ListOfBooks;
import com.example.mybookvibez.MapFragment;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeaderboardTabBooks extends Fragment {

    private List<BookItem> booksList = MapFragment.getListOfBooks();
    private RecyclerView recyclerView;
    public static BooksLeaderAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboard_tab_books, container, false);

        handlingRecycleViewer(view);

        /* sort */
        Comparator<BookItem> cmp = new Comparator<BookItem>() {
            @Override
            public int compare(BookItem b1, BookItem b2) {
                return Integer.compare(b1.getPoints(), b2.getPoints());
            }
        };
        Collections.sort(booksList, cmp);

        return view;
    }


    /**
     * the function handles the RecycleView object in content_scrolling_list.
     * it defines the viewer and set a manager and an adapter to it.
     * @param view - current view (content_scrolling_list)
     */
    private void handlingRecycleViewer(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_books);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new BooksLeaderAdapter(booksList, new BooksLeaderAdapter.OnItemClickListener() {
            @Override public void onItemClick(BookItem book) {
                BookPageFragment.bookToDisplay = book;
                loadBookPageFragment();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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




package com.example.bookvibez;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListOfBooks extends Fragment implements OnItemClickListener {

    private List<BookItem> booksList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_layout, null);

        //booksList = BookItem.getBooks();
        booksList = random_books();

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new BooksAdapter(booksList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        /* handling click on "addBook" button */
        FloatingActionButton fabAdd = (FloatingActionButton) view.findViewById(R.id.addBookFloatingBottom);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return view;
    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}



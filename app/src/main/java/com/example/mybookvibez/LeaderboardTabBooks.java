package com.example.mybookvibez;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeaderboardTabBooks extends Fragment {

    private List<BookItem> booksList;
    private RecyclerView recyclerView;
    public static BooksLeaderAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboard_tab_books, container, false);

        /* sort */
        Comparator<BookItem> cmp = new Comparator<BookItem>() {
            @Override
            public int compare(BookItem o1, BookItem o2) {
                return 0;
            }
        };
        Collections.sort(booksList, cmp);

        handlingRecycleViewer(view);
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


class BooksLeaderAdapter extends RecyclerView.Adapter<BooksLeaderAdapter.BooksViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BookItem book);
    }

    private final List<BookItem> bookslist, copyList;
    private final OnItemClickListener mListener;


    public BooksLeaderAdapter(List<BookItem> list, OnItemClickListener listener) {
        this.bookslist = list;
        this.mListener = listener;
        this.copyList = new ArrayList<>();
        copyList.addAll(list);
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_leaderboard2, parent, false);

        return new BooksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BooksViewHolder holder, int position) {
        holder.bind(bookslist.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return bookslist.size();
    }


    static class BooksViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, info;
        private FrameLayout frame;
        private ImageView img;
        private View mView;

        public BooksViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.title_name);
            info = (TextView) view.findViewById(R.id.info);
            img = (ImageView) view.findViewById(R.id.image);
            frame = (FrameLayout) view.findViewById(R.id.frameLayout_books);
        }

        public void bind(final BookItem book, final OnItemClickListener listener) {
            title.setText(book.getTitle());
            info.setText(book.getAuthor());

            int newWidth = 100; // Leaderboard.screenWidth);
            if (newWidth <= 0)
                newWidth = 100;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(frame.getLayoutParams());
            params.width = newWidth;
            frame.setLayoutParams(params);

            img.setImageResource(R.mipmap.as_few_days);         //TODO
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(book);
                }
            });
        }

    }
}




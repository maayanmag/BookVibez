package com.example.mybookvibez.Leaderboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mybookvibez.BookItem;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;

import java.util.ArrayList;
import java.util.List;

public class BooksLeaderAdapter extends RecyclerView.Adapter<BooksLeaderAdapter.BooksViewHolder> {

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
                .inflate(R.layout.single_book_leaderboard, parent, false);

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
            info.setText(book.getPoints()+"");

            int newWidth = 100; // Leaderboard.screenWidth);
            if (newWidth <= 0)
                newWidth = 100;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(frame.getLayoutParams());
            params.width = newWidth;
            frame.setLayoutParams(params);

            ServerApi.getInstance().downloadBookImage(img, book.getId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(book);
                }
            });
        }

    }
}

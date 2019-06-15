package com.example.mybookvibez;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BookItem book);
    }

    private List<BookItem> bookslist;
    private OnItemClickListener mListener;


    public BooksRecyclerAdapter(List<BookItem> list, OnItemClickListener listener) {
        this.bookslist = list;
        this.mListener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_book_in_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.bind(bookslist.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return bookslist.size();
    }


    public void filter(ArrayList<BookItem> results) {
        bookslist = results;
        notifyDataSetChanged();
    }

    //--------------------------------------

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, author;
        private ImageView img;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.book_name);
            author = (TextView) view.findViewById(R.id.book_author);
            img = (ImageView) view.findViewById(R.id.book_img);
        }

        public void bind(final BookItem book, final OnItemClickListener listener) {
            title.setText(book.getTitle());
            author.setText(book.getGenre());
            ServerApi.getInstance().downloadBookImage(img, book.getId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(book);
                }
            });
        }

    }
}




package com.example.bookvibez;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;


public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BookItem book);
    }

    private final List<BookItem> bookslist;
    private final OnItemClickListener mListener;


    public BooksRecyclerAdapter(List<BookItem> list, OnItemClickListener listener) {
        this.bookslist = list;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_singleitem, parent, false);

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


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, author;
        private BookItem book;
        private View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.book_title);
            author = (TextView) view.findViewById(R.id.book_author);
        }

        public void bind(final BookItem book, final OnItemClickListener listener) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(book);
                }
            });
        }

    }
}




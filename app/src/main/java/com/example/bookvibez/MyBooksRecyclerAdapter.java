package com.example.bookvibez;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MyBooksRecyclerAdapter extends RecyclerView.Adapter<MyBooksRecyclerAdapter.ViewHolder> {

    private List<BookItem> booksList;

    private final BooksRecyclerAdapter.OnItemClickListener mListener;

    private Context context;

    public MyBooksRecyclerAdapter(Context context, List<BookItem> books,
                                  BooksRecyclerAdapter.OnItemClickListener listener) {
        this.booksList = books;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.my_books_single_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(booksList.get(i), mListener);
    }

    @Override
    public int getItemCount() {
        return this.booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.single_book_image);

        }

        public void bind(final BookItem book, final BooksRecyclerAdapter.OnItemClickListener listener) {
            image.setImageResource(book.getBookImg());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(book);
                }
            });
        }
    }
}

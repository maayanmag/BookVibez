package com.example.mybookvibez;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;

public class MyBooksRecyclerAdapter extends RecyclerView.Adapter<MyBooksRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BookItem book);
    }

    private Context context;

    private List<BookItem> booksList;

    private final OnItemClickListener mListener;


    public MyBooksRecyclerAdapter(Context context, List<BookItem> booksList,
                                  OnItemClickListener listener) {
        this.booksList = booksList;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.bind(booksList.get(i), mListener);
    }

    @Override
    public int getItemCount() {
        return this.booksList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private View mView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            image = itemView.findViewById(R.id.single_book_image);
        }


        public void bind(final BookItem book, final OnItemClickListener listener) {
            ServerApi.getInstance().downloadBookImage(image, book.getId());
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(book);
                }
            });
        }
    }
}

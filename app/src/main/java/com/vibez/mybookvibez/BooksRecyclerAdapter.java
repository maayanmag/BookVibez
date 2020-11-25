package com.vibez.mybookvibez;


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

    /** constructor **/
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

    /** updates the book's list with the results whenever there was a search in the list
     * @param results - the books to set instead the old list
     */
    public void filter(ArrayList<BookItem> results) {
        bookslist = results;
        notifyDataSetChanged();
    }


    /**
     * a local static class which uses as a ViewHolder for the books RecyclerViewer
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, location, pastOwners, points;
        private ImageView img;

        /**
         * constructor.
         * @param view - the view in where all objects are located
         */
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.book_name);
            location = (TextView) view.findViewById(R.id.book_location);
            points = (TextView) view.findViewById(R.id.vibe_points_list);
            pastOwners = (TextView) view.findViewById(R.id.owners_num_in_list);
            img = (ImageView) view.findViewById(R.id.book_img);
        }

        /**
         * this function binds the listener to each book and set it's background's length according
         * to the book's vibePoints.
         * @param book - the book to bind
         * @param listener - the listener to bind
         */
        public void bind(final BookItem book, final OnItemClickListener listener) {
            title.setText(book.getTitle());
            location.setText(book.getLocation());
            points.setText(book.getPoints()+"");
            pastOwners.setText(book.getOwnedBy()+"");
            ServerApi.getInstance().downloadBookImage(img, book.getId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(book);
                }
            });
        }

    }
}
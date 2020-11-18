package com.vibez.mybookvibez.Leaderboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vibez.mybookvibez.BookItem;
import com.vibez.mybookvibez.R;
import com.vibez.mybookvibez.ServerApi;
import java.util.List;

public class BooksLeaderAdapter extends RecyclerView.Adapter<BooksLeaderAdapter.BooksViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BookItem book);
    }

    private final List<BookItem> bookslist;
    private final OnItemClickListener mListener;

    /**
     * constructor
     * @param list - the books list to display
     * @param listener - the listener which enable to press and choose a book from the list
     */
    public BooksLeaderAdapter(List<BookItem> list, OnItemClickListener listener) {
        this.bookslist = list;
        this.mListener = listener;
    }

    @NonNull
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

    /**
     * a local static class which uses as a ViewHolder for the books RecyclerViewer
     */
    static class BooksViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, info;
        private FrameLayout frame;
        private ImageView img;

        /**
         * constructor.
         * @param view - the view in where all objects are located
         */
        public BooksViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_name);
            info = (TextView) view.findViewById(R.id.info);
            img = (ImageView) view.findViewById(R.id.image);
            frame = (FrameLayout) view.findViewById(R.id.frameLayout_books);
        }

        /**
         * this function binds the listener to each book and set it's background's length according
         * to the book's vibePoints.
         * @param book - the book to bind
         * @param listener - the listener to bind
         */
        public void bind(final BookItem book, final OnItemClickListener listener) {
            title.setText(book.getTitle());
            info.setText(book.getPoints()+"");

            int newWidth = 10*book.getPoints(); // Leaderboard.screenWidth);
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

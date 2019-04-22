package com.example.bookvibez;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {


   private  List<BookItem> bookslist;
   private SingleBookInListFragment.OnListFragmentInteractionListener mListener;


    public BooksAdapter(List<BookItem> list) {
        this.bookslist = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_singleitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.book = bookslist.get(position);
        holder.title.setText(holder.book.getTitle());
        holder.author.setText(holder.book.getAuthor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //TODO go to book page
                    mListener.onListFragmentInteraction(holder.book);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookslist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView title, author;
        public BookItem book;
        public View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.book_title);
            author = (TextView) view.findViewById(R.id.book_author);
        }


    }
}




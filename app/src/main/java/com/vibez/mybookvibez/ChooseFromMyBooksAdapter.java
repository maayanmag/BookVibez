package com.vibez.mybookvibez;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ChooseFromMyBooksAdapter extends RecyclerView.Adapter<ChooseFromMyBooksAdapter.ViewHolder> {


    private Context context;

    private List<BookItem> booksList;

    private int selectedBookIndex = -1;


    public ChooseFromMyBooksAdapter(Context context, List<BookItem> booksList) {
        this.booksList = booksList;
        this.context = context;
    }


    @NonNull
    @Override
    public ChooseFromMyBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.single_item_in_my_books, viewGroup, false);
        return new ChooseFromMyBooksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChooseFromMyBooksAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(booksList.get(i));
    }

    @Override
    public int getItemCount() {
        return this.booksList.size();
    }

    public BookItem getSelectedBook() {
        if (selectedBookIndex == -1)
            return null;
        return booksList.get(selectedBookIndex);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image, checked;


        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.single_book_image);
            checked = itemView.findViewById(R.id.checked_image);
        }


        public void bind(final BookItem book) {
            ServerApi.getInstance().downloadBookImage(image, book.getId());
            if (selectedBookIndex == -1) {
                checked.setVisibility(View.GONE);
            } else {
                if (selectedBookIndex == getAdapterPosition()) {
                    checked.setVisibility(View.VISIBLE);
                } else {
                    checked.setVisibility(View.GONE);
                }
            }

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checked.setVisibility(View.VISIBLE);
                    if (selectedBookIndex != getAdapterPosition()) {
                        notifyItemChanged(selectedBookIndex);
                        selectedBookIndex = getAdapterPosition();
                    }
                }
            });

        }
    }
}

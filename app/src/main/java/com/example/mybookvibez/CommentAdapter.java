package com.example.mybookvibez;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Comment comment);
    }

    private final List<Comment> commentList;
    private final OnItemClickListener mListener;


    public CommentAdapter(List<Comment> list, OnItemClickListener listener) {
        this.commentList = list;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.bind(commentList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        private Comment comment;
        private TextView publisher, text, date;
        private View mView;
        private CardView card;


        public MyViewHolder(View view) {
            super(view);
            mView = view;
            publisher = (TextView) view.findViewById(R.id.publisher);
            text = (TextView) view.findViewById(R.id.comment_text);
            date = (TextView) view.findViewById(R.id.date);
            card = (CardView) view.findViewById(R.id.comment_card);
        }

        public void bind(final Comment comment, final OnItemClickListener listener) {
            final User[] user = new User[1];
            ServerApi.getInstance().getUser(comment.getPublisherId(), user, publisher);
            text.setText(comment.getComment());


            CardView catCard = new CardView(getApplicationContext());
            catCard.setLayoutParams(new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
            catCard.setMinimumHeight(75);

            String[] toDate = comment.getTime().toString().split(" ");
            String dateToShow = toDate[1] + " " + toDate[2] + " " + toDate[5];
            date.setText(dateToShow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(comment);
                }
            });
        }

    }
}



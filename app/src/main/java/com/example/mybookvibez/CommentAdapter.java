package com.example.mybookvibez;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Comment comment);
    }

    private final List<Comment> commentList, copyList;
    private final OnItemClickListener mListener;


    public CommentAdapter(List<Comment> list, OnItemClickListener listener) {
        this.commentList = list;
        this.mListener = listener;
        this.copyList = new ArrayList<>();
        copyList.addAll(list);
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

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            publisher = (TextView) view.findViewById(R.id.publisher);
            text = (TextView) view.findViewById(R.id.comment_text);
            date = (TextView) view.findViewById(R.id.date);
        }

        public void bind(final Comment comment, final OnItemClickListener listener) {
            publisher.setText(comment.getPublisherName());
            text.setText(comment.getComment());
            date.setText(comment.getDate().toString());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(comment);
                }
            });
        }

    }
}



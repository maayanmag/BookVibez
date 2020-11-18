package com.example.mybookvibez.BookPage;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mybookvibez.ProfileFragment;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.example.mybookvibez.User;
import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private final List<Comment> commentList;

    /** constructor **/
    public CommentAdapter(List<Comment> list) {
        this.commentList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.bind(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * a local static class which uses as a ViewHolder for the comments RecyclerViewer
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView publisher, text, date;
        private ImageView publisherImg;

        /** constructor **/
        public MyViewHolder(View view) {
            super(view);
            publisher = (TextView) view.findViewById(R.id.publisher);
            publisherImg = (ImageView) view.findViewById(R.id.past_owner_img);
            text = (TextView) view.findViewById(R.id.comment_text);
            date = (TextView) view.findViewById(R.id.date);
        }

        /**
         * this function binds each comment and set it's content in place.
         * @param comment - the comment to bind
         */
        public void bind(final Comment comment) {
            final User[] user = new User[1];
            ServerApi.getInstance().getUser(comment.getPublisherId(), user, publisher, null);
            text.setText(comment.getComment());

            ServerApi.getInstance().downloadProfilePic(publisherImg, comment.getPublisherId());
            publisherImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFragment.userIdToDisplay = comment.getPublisherId();
                    ProfileFragment.displayMyProfile = false;
                }
            });

            date.setText(comment.getTime());
        }
    }

}



package com.example.mybookvibez.Leaderboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.example.mybookvibez.User;

import java.util.List;

public class UsersLeaderAdapter extends RecyclerView.Adapter<UsersLeaderAdapter.UserViewHolder> {
    private static ServerApi server = ServerApi.getInstance();

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    private final List<User> users;
    private final OnItemClickListener mListener;


    public UsersLeaderAdapter(List<User> list, OnItemClickListener listener) {
        this.users = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_user_leaderboard, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        holder.bind(users.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView title, info;
        private ImageView img;
        private FrameLayout frame;
        private View mView;

        public UserViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.user_name1);
            info = (TextView) view.findViewById(R.id.user_info);
            img = (ImageView) view.findViewById(R.id.user_image);
            frame = (FrameLayout) view.findViewById(R.id.frameLayout_points);
        }

        public void bind(final User user, final OnItemClickListener listener) {
            title.setText(user.getName());
            info.setText(user.getVibePoints()+"");

            int newWidth = 10*user.getVibePoints(); // Leaderboard.screenWidth);
            if (newWidth <= 0)
                newWidth = 100;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(frame.getLayoutParams());
            params.width = newWidth;
            frame.setLayoutParams(params);

            server.downloadProfilePic(img, user.getId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    listener.onItemClick(user);
                }
            });
        }

    }
}

package com.example.mybookvibez.Leaderboard;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mybookvibez.ProfileFragment;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.example.mybookvibez.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeaderboardTabUsers extends Fragment {

    private static ArrayList<User> userList = new ArrayList<>();
    public static UsersLeaderAdapter adapter;


    public static void clearUsersList() {
        userList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboard_tab_users, container, false);

        handlingRecycleViewer(view);
        ServerApi.getInstance().getUsersList(userList, adapter);

        /* sort */
        Comparator<User> cmp = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return Integer.compare(u2.getVibePoints(), u1.getVibePoints());
            }
        };
        Collections.sort(userList, cmp);

        return view;
    }


    /**
     * the function handles the RecycleView object in content_scrolling_list.
     * it defines the viewer and set a manager and an adapter to it.
     * @param view - current view (content_scrolling_list)
     */
    private void handlingRecycleViewer(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_users);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new UsersLeaderAdapter(userList, new UsersLeaderAdapter.OnItemClickListener() {
            @Override public void onItemClick(User user) {
                ProfileFragment.userToDisplay = user.getId();
                loadProfilePageFragment();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * this function replaces the layout to a book page layout in case some book was clicked in the list
     */
    private void loadProfilePageFragment() {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new ProfileFragment());
        transaction.commit();
    }



}







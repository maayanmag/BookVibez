package com.example.mybookvibez.Leaderboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybookvibez.ProfileFragment;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.example.mybookvibez.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Callable;


public class LeaderboardTabUsers extends Fragment {

    private static ArrayList<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    public static UsersLeaderAdapter adapter;


    public static void clearUsersList() {
        userList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboard_tab_users, container, false);

        handlingRecycleViewer(view);

        Callable<Void> func = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return sortUsers();
            }
        };
        ServerApi.getInstance().getUsersList(userList, adapter, func);


        return view;
    }


    private Void sortUsers(){
        Comparator<User> cmp = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return Integer.compare(u2.getVibePoints(), u1.getVibePoints());
            }
        };
        Collections.sort(userList, cmp);
        userList = (ArrayList<User>) userList.subList(0, Math.min(userList.size()-1, 12));
        return null;
    }

    /**
     * the function handles the RecycleView object in list_content_scrolling.
     * it defines the viewer and set a manager and an adapter to it.
     * @param view - current view (list_content_scrolling)
     */
    private void handlingRecycleViewer(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_users);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new UsersLeaderAdapter(userList, new UsersLeaderAdapter.OnItemClickListener() {
            @Override public void onItemClick(User user) {
                ProfileFragment.userIdToDisplay = user.getId();
                ProfileFragment.displayMyProfile = false;
                loadProfilePageFragment();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    private void loadProfilePageFragment() {
        FragmentManager manager = getParentFragment() != null ? getParentFragment().getFragmentManager() : getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("profile");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, new ProfileFragment());
        transaction.commit();
    }



}







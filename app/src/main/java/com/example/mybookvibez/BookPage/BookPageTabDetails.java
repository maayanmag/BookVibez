package com.example.mybookvibez.BookPage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybookvibez.AddBook.AddBookImagePopup;
import com.example.mybookvibez.Exchange.AddCommentAfterExchange;
import com.example.mybookvibez.Exchange.ExchangeBookPopup;
import com.example.mybookvibez.ProfileFragment;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.example.mybookvibez.User;

public class BookPageTabDetails extends Fragment {

    private ImageView ownerImg;
    private TextView name, author, genre, ownerName, curLocation;
    private Button gotBookButton;
    private ImageButton gmailButton, whatsappButtom, fbButton;
    private boolean isGotTheBookPressed = false;
    private String userPhoneNum;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_page_tab_details, container, false);

        getAttributesIds(view);
        handleButtons();

        if(BookPageFragment.bookToDisplay != null) {
            handleAttribute();

            User[] temp = new User[1];
            ServerApi.getInstance().getUser(BookPageFragment.bookToDisplay.getOwnerId(), temp, ownerName);
            ServerApi.getInstance().downloadProfilePic(ownerImg, BookPageFragment.bookToDisplay.getOwnerId());

            if (temp[0] != null) userPhoneNum = temp[0].getPhoneNumber();
        }

        return view;
    }

    private void getAttributesIds(View view) {
        ownerImg = (ImageView) view.findViewById(R.id.current_owner_profile_pic);
        name = (TextView) view.findViewById(R.id.book_name_content);
        author = (TextView) view.findViewById(R.id.book_author_content);
        genre = (TextView) view.findViewById(R.id.book_genre_content);
        curLocation = (TextView) view.findViewById(R.id.cur_location);
        ownerName = (TextView) view.findViewById(R.id.current_owner_name);
        gotBookButton = (Button) view.findViewById(R.id.got_the_book_button);
        whatsappButtom = (ImageButton) view.findViewById(R.id.whatsapp_icon);
    }

    private void handleButtons(){
        gotBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                DialogFragment exchangePopup = new ExchangeBookPopup();
                exchangePopup.show(ft, "dialog");
                }
        });

        ownerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.userIdToDisplay = BookPageFragment.bookToDisplay.getOwnerId();
                ProfileFragment.displayMyProfile = false;
                Fragment profile = new ProfileFragment();
                loadFragment(profile);
            }
        });

        whatsappButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickWhatsApp(v);

            }
        });
    }

    private void handleAttribute() {
        name.setText(BookPageFragment.bookToDisplay.getTitle());
        BookPageFragment.collapsingToolbar.setTitle(BookPageFragment.bookToDisplay.getTitle());
        author.setText(BookPageFragment.bookToDisplay.getAuthor());
        genre.setText(BookPageFragment.bookToDisplay.getGenre());
        curLocation.setText(BookPageFragment.bookToDisplay.getLocation());
        ServerApi.getInstance().downloadBookImage(BookPageFragment.bookImg, BookPageFragment.bookToDisplay.getId());
    }

    public void onClickWhatsApp(View view) {

        PackageManager pm = getActivity().getPackageManager();
        userPhoneNum = "548325053"; //todo: change this! default lior's number
        try {
            // todo change the country!
            String contactNumber = "972" + userPhoneNum; //without '+'
            Intent whatsappIntent = new Intent("android.intent.action.MAIN");
            whatsappIntent.setAction(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            String text = "Hi! I want to swap books";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code in catch block will be called
            whatsappIntent.putExtra("jid", contactNumber + "@s.whatsapp.net");
            whatsappIntent.setPackage("com.whatsapp");

            whatsappIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(whatsappIntent, "Connect owner by What'sapp"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getParentFragment() != null ? getParentFragment().getFragmentManager() : getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("ListView");  // enables to press "return" and go back to the list view
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }


}

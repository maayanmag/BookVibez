package com.example.mybookvibez.BookPage;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.mybookvibez.Exchange.ExchangeBookPopup;
import com.example.mybookvibez.ProfileFragment;
import com.example.mybookvibez.R;
import com.example.mybookvibez.ServerApi;
import com.example.mybookvibez.User;

public class BookPageTabDetails extends Fragment {

    private ImageView ownerImg;
    private TextView name, author, genre, ownerName;
    private Button gotBookButton;
    private ImageButton gmailButton, whatsappButtom, fbButton;
    private boolean isGotTheBookPressed = false;
    private String userPhoneNum;

    private final static int LEVEL_ONE_TRESH = 30;
    private final static int LEVEL_TWO_TRESH = 70;
    private final static int LEVEL_THREE_TRESH = 100;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_page_tab_details, container, false);

        getAttributesIds(view);
        handleButtons();

        if(BookPageFragment.bookToDisplay != null) {
            handleAttribute();
            setBookmarkImg();

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
        ownerName = (TextView) view.findViewById(R.id.current_owner_name);
        gotBookButton = (Button) view.findViewById(R.id.got_the_book_button);
        fbButton = (ImageButton) view.findViewById(R.id.fb_messenger_icon);
        gmailButton = (ImageButton) view.findViewById(R.id.gmail_icon);
        whatsappButtom = (ImageButton) view.findViewById(R.id.whatsapp_icon);

    }

    private void handleButtons(){
        gotBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGotTheBookPressed = !isGotTheBookPressed;     // change state
                String text = "I Got This Book!";
                gotBookButton.setText(text);
                if(isGotTheBookPressed) {
//                    text = "I Got This Book!";
                    gotBookButton.setBackgroundResource(R.drawable.buttonshape);
                    Fragment exchangePopup = new ExchangeBookPopup();
                    loadFragment(exchangePopup);
                    //TODO - change current owner to this user
                }
//                else {
//                    text = "I Got This Book!";
//                    gotBookButton.setBackgroundResource(R.drawable.button_filled_shape);
                }
//            }
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


    private void setBookmarkImg() {
        // adjust bookmark image to the amount of points
        int points = BookPageFragment.bookToDisplay.getPoints();
        if (points < LEVEL_ONE_TRESH) {
            BookPageFragment.bookmarkImg.setVisibility(View.INVISIBLE);
        } else if (BookPageFragment.bookToDisplay.getPoints() >= LEVEL_ONE_TRESH &&
                points < LEVEL_TWO_TRESH) {
            BookPageFragment.bookmarkImg.setImageResource(R.drawable.star_bookmark);
        } else if (points >= LEVEL_TWO_TRESH &&
                points < LEVEL_THREE_TRESH) {
            BookPageFragment.bookmarkImg.setImageResource(R.drawable.diamond_bookmark);
        } else if (points >= LEVEL_THREE_TRESH) {
            BookPageFragment.bookmarkImg.setImageResource(R.drawable.crown_bookmark);
        }
    }
}

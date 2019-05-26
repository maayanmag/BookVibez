package com.example.mybookvibez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private HashMap<Marker,BookItem> markerMap = MapFragment.getMarkerMap();


    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window_try, null);

    }

    private void renderWindowText(Marker marker, View view) {
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (title != null){
            tvTitle.setText(title);
        }

        BookItem book = markerMap.get(marker);
        String genre = book.getGenre();
        TextView tvGenre = (TextView) view.findViewById(R.id.book_category);
        if (tvGenre != null){
            tvGenre.setText(genre);
        }
        String ownedBy = book.getOwnedBy() + " people read this book";
        TextView tvOwnedBy = (TextView) view.findViewById(R.id.people_read);
        if (tvOwnedBy != null){
            tvOwnedBy.setText(ownedBy);
        }

        String vibePoints =  book.getPoints() + " VibePoints";
        TextView tvVibePoints = (TextView) view.findViewById(R.id.book_vibes);
        if (tvVibePoints != null){
            tvVibePoints.setText(vibePoints);
        }
//        String snippet = marker.getSnippet();
//        TextView tvSnippet = (TextView) view.findViewById(R.id.custom_snippet);
//        if (snippet!= null){
//            tvSnippet.setText(snippet);
//        }
        ImageView img = (ImageView) view.findViewById(R.id.book_Image);
        if (marker != null){
            //img.setImageResource(MapFragment.bookItemList.get(id).getBookImg());        //TODO
            img.setImageResource(R.mipmap.as_few_days);
        }

    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}

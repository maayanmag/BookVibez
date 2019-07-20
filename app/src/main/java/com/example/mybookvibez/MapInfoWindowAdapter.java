package com.example.mybookvibez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private HashMap<Marker,BookItem> markerMap = MapFragment.getMarkerMap();

    /**
     * constructor
     * @param context - activity context
     */
    public MapInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.map_info_window, null);

    }

    /**
     * this func sets the text and details in the small window which open when a marker is pressed in the map
     * @param marker - the marker which was pressed
     * @param view - the view of the details
     */
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

        ImageView img = (ImageView) view.findViewById(R.id.book_Image);
        if (marker != null){
            ServerApi.getInstance().downloadBookImage(img, book.getId());
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

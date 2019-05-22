package com.example.bookvibez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view) {
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.custom_title);

        if (title != null){
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView) view.findViewById(R.id.custom_snippet);
        if (snippet!= null){
            tvSnippet.setText(snippet);
        }
        ImageView img = (ImageView) view.findViewById(R.id.book_img_in_marker);
        if (marker != null){
            int id = (int) marker.getTag();
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

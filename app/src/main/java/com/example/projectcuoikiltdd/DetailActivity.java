package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class DetailActivity extends AppCompatActivity {
    static final String EXTRA_PLACE_NAME = "place_name";
    static final String EXTRA_PLACE_ADDRESS = "place_address";
    static final String EXTRA_PLACE_DESCRIPTION = "place_description";
    static final String EXTRA_PLACE_LATITUDE = "place_latitude";
    static final String EXTRA_PLACE_LONGITUDE = "place_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnBackTop).setOnClickListener(v -> finish());

        // Tab "Đánh giá" -> mở màn Đánh giá
        findViewById(R.id.tabReview).setOnClickListener(v ->
                startActivity(new Intent(DetailActivity.this, ReviewActivity.class)));

        Intent place = getIntent();
        String name = place.getStringExtra(EXTRA_PLACE_NAME);
        String address = place.getStringExtra(EXTRA_PLACE_ADDRESS);
        ((TextView) findViewById(R.id.placeName)).setText(name);
        ((TextView) findViewById(R.id.placeAddress)).setText(address);
        ((TextView) findViewById(R.id.placeDescription)).setText(place.getStringExtra(EXTRA_PLACE_DESCRIPTION));

        double latitude = place.getDoubleExtra(EXTRA_PLACE_LATITUDE, Double.NaN);
        double longitude = place.getDoubleExtra(EXTRA_PLACE_LONGITUDE, Double.NaN);
        LatLng location = null;
        if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
            location = new LatLng(latitude, longitude);
        }
        PlaceMap.bind(this, location, name, address);
    }
}

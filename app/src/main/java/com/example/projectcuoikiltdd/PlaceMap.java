package com.example.projectcuoikiltdd;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

final class PlaceMap {
    // Overview frame for the mainland and the offshore archipelagos.
    private static final LatLngBounds VIETNAM_BOUNDS = new LatLngBounds(
            new LatLng(6.5, 102.0), new LatLng(23.5, 117.5));

    private PlaceMap() {}

    static void bind(FragmentActivity activity, @Nullable LatLng location, String name, String address) {
        View openButton = activity.findViewById(R.id.placeOpenMap);
        View preview = activity.findViewById(R.id.placeMapPreview);
        FragmentManager fragments = activity.getSupportFragmentManager();

        Runnable revealMap = () -> {
            openButton.setVisibility(View.GONE);
            preview.setVisibility(View.VISIBLE);

            SupportMapFragment map = (SupportMapFragment) fragments.findFragmentById(R.id.placeMapPreview);
            if (map == null) {
                LatLng center = location == null ? VIETNAM_BOUNDS.getCenter() : location;
                float zoom = location == null ? 4f : 16f;
                GoogleMapOptions options = new GoogleMapOptions()
                        .liteMode(location != null)
                        .mapToolbarEnabled(false)
                        .camera(CameraPosition.fromLatLngZoom(center, zoom));
                map = SupportMapFragment.newInstance(options);
                fragments.beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.placeMapPreview, map)
                        .commit();
            }

            map.getMapAsync(googleMap -> {
                if (location == null) {
                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                    fitVietnam(googleMap, preview);
                    return;
                }

                // Override the default lite map tap that opens Google Maps.
                googleMap.setOnMapClickListener(point -> {});
                googleMap.addMarker(new MarkerOptions().position(location).title(name).snippet(address));
            });
        };

        if (fragments.findFragmentById(R.id.placeMapPreview) != null) {
            revealMap.run();
        } else {
            openButton.setOnClickListener(v -> {
                revealMap.run();
                OneShotPreDrawListener.add(preview, () -> preview.requestRectangleOnScreen(
                        new Rect(0, 0, preview.getWidth(), preview.getHeight())));
            });
        }
    }

    private static void fitVietnam(GoogleMap map, View preview) {
        Runnable updateCamera = () -> {
            int width = preview.getWidth() - preview.getPaddingLeft() - preview.getPaddingRight();
            int height = preview.getHeight() - preview.getPaddingTop() - preview.getPaddingBottom();
            if (width <= 0 || height <= 0) {
                return;
            }
            int margin = Math.round(12 * preview.getResources().getDisplayMetrics().density);
            int padding = Math.min(margin, Math.min(width, height) / 4);
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(VIETNAM_BOUNDS, width, height, padding));
        };

        if (preview.isLaidOut() && !preview.isLayoutRequested()) {
            updateCamera.run();
        } else {
            OneShotPreDrawListener.add(preview, updateCamera);
        }
    }
}

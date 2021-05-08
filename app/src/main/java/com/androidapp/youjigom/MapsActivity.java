package com.androidapp.youjigom;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        String[][] locations = {

                {"40.0424870042855", "116.38425286915037", "베이징"},
                {"35.74306540022329", "139.77245318272045", "도쿄"},
                {"37.557667", "126.926546", "서울시"}

        };
        for (int i = 0; i < 3; i++) {
            // 위치 설정
            double lat = Double.parseDouble(locations[i][0]);
            double lon = Double.parseDouble(locations[i][1]);
            LatLng latLng = new LatLng(lat, lon);
            //LatLng latLng = new LatLng(37.557667, 126.926546);

            // 카메라를 설정 위치로 옮긴다
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // 카메라 줌 정도를 설정한다
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            //구글 맵에 표시할 마커에 대한 옵션 설정
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(locations[i][2]);
            // 마커 생성
            googleMap.addMarker(markerOptions);
        }

    }
}
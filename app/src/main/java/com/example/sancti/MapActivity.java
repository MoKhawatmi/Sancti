package com.example.sancti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.HuaweiMapOptions;
import com.huawei.hms.maps.MapFragment;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;
import com.huawei.hms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, HuaweiMap.OnMyLocationClickListener {

    private HuaweiMap hMap;
    private MapView mMapView;
    double lat;
    double lng;



    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.i("TAG", "onCreate");
          Intent i=getIntent();
          lat=i.getExtras().getDouble("lat");
          lng=i.getExtras().getDouble("lng");


        mMapView = findViewById(R.id.mapView);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        MapsInitializer.setApiKey("CgB6e3x9sRVyS0oZJ74m/s8RFTewbGU7JWGO7J49f+UL8yKI3WvAjr2B+ze4LAmOKWSrLqZmgljI8ybqo8SA5pbk");
        mMapView.onCreate(mapViewBundle);
        //get map instance
        mMapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(HuaweiMap map) {

            //get map instance in a callback method
            Log.d("TAG", "onMapReady: ");
            hMap = map;

            hMap.getUiSettings().setZoomControlsEnabled(true);
            hMap.getUiSettings().setZoomGesturesEnabled(true);
            hMap.setMyLocationEnabled(true);
            hMap.getUiSettings().setMyLocationButtonEnabled(true);
            hMap.setOnMyLocationClickListener(this);

        hMap.setOnMyLocationButtonClickListener(new HuaweiMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(getApplicationContext(), "MyLocation button clicked", Toast.LENGTH_LONG).show();
                return false;
            }
        });

            Toast.makeText(this, lat+" "+lng, Toast.LENGTH_LONG).show();

            LatLng location = new LatLng(lat, lng);
            hMap.addMarker(new MarkerOptions().position(location));
            CameraPosition cameraPosition = new CameraPosition(location,8,2.2f,31.5f);
            hMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


  /*  @Override
    public boolean onMyLocationButtonClick() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            Toast.makeText(this, location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_LONG).show();
            CameraPosition cameraPosition = new CameraPosition(latLng,8,2.2f,31.5f);
            hMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }catch (Exception e){
            Log.i("error user location",e.getMessage());
        }

        return true;
    }*/




    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}




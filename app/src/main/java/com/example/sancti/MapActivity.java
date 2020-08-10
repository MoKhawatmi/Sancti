package com.example.sancti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private HuaweiMap hMap;

    private MapView mMapView;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Log.i("TAG", "onCreate");

          Intent i=getIntent();
          double lat=i.getExtras().getDouble("lat");
          double lng=i.getExtras().getDouble("lng");
          Log.i("duh2",lat+"");
          Log.i("duh2",lng+"");



        //get mapview instance
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
        mMapView.onPause();
        super.onPause();
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




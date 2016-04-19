package com.nscc.jared.landgrab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nscc.jared.biz.CellSupport;
import com.nscc.jared.data.AsyncResponse;
import com.nscc.jared.data.ObjectManager;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // TODO - Do some bloody refactoring

    // activity result codes
    private static int CELL_ACTIVITY = 1;

    // for background thread to keep markers loaded
    private Handler h;
    private int FRAME_RATE = 1000;

    // Shared prefs are so useful
    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences sharedpreferences;

    // Where the last photo taken is
    private String mCurrentPhotoPath;

    // map stuff
    private GoogleMap map;
    public LocationManager locationManager;
    public LocationUpdateListener listener;
    public Location userLocation;
    public Location lastRefreshLocation;

    // Widgets
    private TextView tvloading;
    private TextView tvLoadingPhotos;
    private TextView tvUsername;
    private LinearLayout lyInfoView;
    private Button btnOkay;
    private Button btnAbout;
    private Button btnZoom1;
    private Button btnZoom2;
    private TextView tvSupporters;
    private Button btnEdit;
    private Button btnFlag;

    public String username = "";
    private ObjectManager objects = new ObjectManager();

    private ArrayList<Marker> cellMarkers = new ArrayList<>();
    private ArrayList<Polygon> cellShapes = new ArrayList<>();

    private float currentZoom = 17.0f;

//    private WebService remote = new WebService();
//    private PhotoUtil photoUtil = new PhotoUtil();
    private boolean loading = true;
    private boolean grey_out = true;

    // Stores the thumbnail captured by the user
    // holding onto it while waiting for confirmation actvivity
    //private Bitmap photo;
    //private Bitmap photo;

    @Override
    public void onPause()
    {
//        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
//            // TODO - request permissions
//        }
        if (locationManager != null)
            locationManager.removeUpdates(listener);

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if (mapAnimation != null)
        //mapAnimation.start();



//        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
//            Toast.makeText(getApplicationContext(), "Tell Jared: Coarse location. On resume", Toast.LENGTH_LONG).show();
//            // TODO - request permissions
//        }
        if (locationManager != null)
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }

        if (userLocation != null)
        {
            RefreshAllData(userLocation.getLatitude(), userLocation.getLongitude());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "onResume() - cannot rerefrsh data. no location.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //loginUser();

        // Setup Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // handler for keeping markers loaded on map
        h = new Handler();

        // XML widgets
        tvloading = (TextView)findViewById(R.id.tvLoading);
        tvLoadingPhotos = (TextView) findViewById(R.id.tvLoadingPhotos);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        lyInfoView = (LinearLayout) findViewById(R.id.lyInfoView);
        btnOkay = (Button) findViewById(R.id.btnOkay);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnZoom1 = (Button) findViewById(R.id.btnZoom1);
        btnZoom2 = (Button) findViewById(R.id.btnZoom2);
        tvSupporters = (TextView) findViewById(R.id.tvSupporters);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnFlag = (Button) findViewById(R.id.btnFlag);

        tvUsername.setText(sharedpreferences.getString("username", "error. restart app"));

        // Check if welcome screen is needed
        boolean viewed = sharedpreferences.getBoolean("viewed", false);
        if (!viewed)
            lyInfoView.setVisibility(View.VISIBLE);

        tvloading.setVisibility(View.VISIBLE);
        grey_out = true;

        btnOkay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("viewed", true);
                editor.apply();
                lyInfoView.setVisibility(View.GONE);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lyInfoView.setVisibility(View.VISIBLE);
            }
        });

        btnFlag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent("FlagActivity"));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent("SetupActivity"));
            }
        });

        btnZoom1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (map != null && userLocation != null) {
                    currentZoom = 15.0f;
                    LatLng currentPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom);
                    map.moveCamera(yourLocation);

                    btnZoom1.setBackgroundColor(Color.parseColor("#3399ff"));
                    btnZoom1.setTextColor(Color.parseColor("#000000"));

                    btnZoom2.setBackgroundColor(Color.parseColor("#000099"));
                    btnZoom2.setTextColor(Color.parseColor("#ffffff"));

                    for (int c = 0; c < cellMarkers.size(); c++) {
                        cellMarkers.get(c).setVisible(false);
                    }
                }
            }
        });

        btnZoom2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (map != null && userLocation != null) {
                    currentZoom = 17.0f;
                    LatLng currentPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom);
                    map.moveCamera(yourLocation);

                    btnZoom2.setBackgroundColor(Color.parseColor("#3399ff"));
                    btnZoom2.setTextColor(Color.parseColor("#000000"));

                    btnZoom1.setBackgroundColor(Color.parseColor("#000099"));
                    btnZoom1.setTextColor(Color.parseColor("#ffffff"));

                    for (int c = 0; c < cellMarkers.size(); c++) {
                        cellMarkers.get(c).setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CELL_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                Bundle res = data.getExtras();
                int newSupport = res.getInt("newSupport", 0);
                Toast.makeText(getApplicationContext(), newSupport + " new supporters", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // disable control gentures to lock the map in place
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationUpdateListener();

        //mapAnimation = new MapAnimation(map);
        //mapAnimation.start();

//        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
//            Toast.makeText(getApplicationContext(), "Tell Jared: Coarse location denied. onMapReady.", Toast.LENGTH_LONG).show();
//            // TODO - request permissions
//        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {

                if (!grey_out)
                {
                    String[] data = arg0.getSnippet().split(":");

                    DecimalFormat df = new DecimalFormat("#.###");
                    double latId = Double.parseDouble(df.format(Double.parseDouble(data[0])));
                    double lngId = Double.parseDouble(df.format(Double.parseDouble(data[1])));

                    ArrayList<CellSupport> support = new ArrayList<>();
                    for (int i = 0;i < objects.cellSupporters.size();i++)
                    {
                        if (objects.cellSupporters.get(i).lat == latId && objects.cellSupporters.get(i).lng == lngId)
                        {
                            support.add(objects.cellSupporters.get(i));
                        }
                    }

                    Bundle extras = new Bundle();
                    extras.putDouble("lat", latId);
                    extras.putDouble("lng", lngId);
                    extras.putDouble("markerLat", arg0.getPosition().latitude);
                    extras.putDouble("markerLng", arg0.getPosition().longitude);
                    extras.putDouble("userLat", userLocation.getLatitude());
                    extras.putDouble("userLng", userLocation.getLongitude());

                    // cell has supporters
                    if (support.size() > 0) {

                        ArrayList<String> usernames = new ArrayList<>();
                        ArrayList<Integer> supporters = new ArrayList<>();

                        // find owner
                        int ownerIndex = 0;
                        for (int i = 0; i < support.size(); i++) {
                            if (support.get(i).getSupporters() > support.get(ownerIndex).getSupporters()) {
                                ownerIndex = i;
                            }
                            usernames.add(support.get(i).getUsername());
                            supporters.add(support.get(i).getSupporters());
                        }

                        extras.putString("username", support.get(ownerIndex).getUsername());
                        extras.putString("name", support.get(ownerIndex).name);
                        extras.putStringArrayList("usernames", usernames);
                        extras.putIntegerArrayList("supporters", supporters);
                    }
                    else
                    {
                        // cell has no supporters
                        extras.putString("username", "Not claimed");
                        extras.putString("name", "No man's land");
                    }

                    Intent i = new Intent("CellActivity");//create intent object
                    i.putExtras(extras);
                    startActivityForResult(i, CELL_ACTIVITY);
                }



                return true;
            }

        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickCoords) {

                Toast.makeText(getApplicationContext(), clickCoords.latitude + ", " + clickCoords.longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void RefreshAllData(double lat, double lng)
    {
        lastRefreshLocation = userLocation;
        tvSupporters.setText(sharedpreferences.getInt("supporters", 0) + " supporters");

        DecimalFormat df = new DecimalFormat("#.###");
        objects.loadCells(Double.parseDouble(df.format(lat)), Double.parseDouble(df.format(lng)));

        h.postAtTime(checkForLoad, SystemClock.uptimeMillis() + 400);
    }

    private Runnable checkForLoad = new Runnable() {
        @Override
        public void run() {
            if (objects.waitingForCellsToLoad)
            {
                if (objects.loadComplete())
                    drawCells();
                h.postDelayed(checkForLoad, FRAME_RATE);
            }
            else
            {
                h.removeCallbacks(checkForLoad);
            }
        }
    };

    public void drawCells()
    {
        // clear current cells and markers
        for (int c = 0;c < cellShapes.size();c++)
        {
            cellShapes.get(c).remove();
        }
        cellShapes.clear();
        for (int c = 0;c < cellMarkers.size();c++)
        {
            cellMarkers.get(c).remove();
        }
        cellMarkers.clear();

        if (userLocation != null)
        {


            DecimalFormat df = new DecimalFormat("#.###");
            double lat = Double.parseDouble(df.format(userLocation.getLatitude()));
            double lng = Double.parseDouble(df.format(userLocation.getLongitude()));

            lat += 0.01;
            lng -= 0.01;

            // loop through cells within 0.01 degrees of user
            for (int x = 0;x < 20;x++)
            {
                for (int y = 0;y < 20;y++)
                {

                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.gray_marker);

                    // southeast corner of cell
                    double southWestLat = Double.parseDouble(df.format(lat - (x*0.001)));
                    double southWestLng = Double.parseDouble(df.format(lng + (y*0.001)));

                    // does the cell have any supporters?
                    ArrayList<CellSupport> support = new ArrayList<>();
                    for (int i = 0;i < objects.cellSupporters.size();i++)
                    {
                        if (objects.cellSupporters.get(i).lat == southWestLat && objects.cellSupporters.get(i).lng == southWestLng)
                        {
                            support.add(objects.cellSupporters.get(i));
                        }
                    }

                    // the cell has supporters
                    if (support.size() > 0)
                    {
                        // find owner
                        int ownerIndex = 0;
                        for (int i = 0;i< support.size();i++)
                        {
                            if (support.get(i).getSupporters() > support.get(ownerIndex).getSupporters())
                            {
                                ownerIndex = i;
                            }
                        }

                        icon = support.get(ownerIndex).icon;

                        // draw cell
                        PolygonOptions rectOptions = new PolygonOptions()
                                .add(new LatLng(southWestLat, southWestLng),
                                        new LatLng(southWestLat + 0.001, southWestLng),
                                        new LatLng(southWestLat + 0.001, southWestLng - 0.001),
                                        new LatLng(southWestLat, southWestLng - 0.001),
                                        new LatLng(southWestLat, southWestLng))
                                .strokeWidth(2)
                                .fillColor(Color.argb(50, support.get(ownerIndex).R, support.get(ownerIndex).G, support.get(ownerIndex).B));

                        // Get back the mutable Polygon
                        Polygon polygon = map.addPolygon(rectOptions);
                        cellShapes.add(polygon);
                    }




                    // TODO only create markers for nearby cells
                    // would mean faster data refresh when moving?

                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(lat - (x*0.001) + 0.0005, lng + (y*0.001) - 0.0005))
                            .icon(icon)
                            .snippet((lat - (x * 0.001)) + ":" + (lng + (y * 0.001))));
                    cellMarkers.add(marker);
                }
            }

        }

//        for (int i = 0;i < objects.cells.size();i++)
//        {
//            // TODO - these demensions only work in this quarter of the earth
//            PolygonOptions rectOptions = new PolygonOptions()
//                    .add(new LatLng(objects.cells.get(i).lat, objects.cells.get(i).lng),
//                            new LatLng(objects.cells.get(i).lat + 0.001, objects.cells.get(i).lng),
//                            new LatLng(objects.cells.get(i).lat + 0.001, objects.cells.get(i).lng - 0.001),
//                            new LatLng(objects.cells.get(i).lat, objects.cells.get(i).lng - 0.001),
//                            new LatLng(objects.cells.get(i).lat, objects.cells.get(i).lng))
//                    .strokeWidth(0)
//                    .fillColor(Color.argb(30, 50, 0, 255));
//
//            // Get back the mutable Polygon
//            Polygon polygon = map.addPolygon(rectOptions);
//        }

    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

//        if (marker.equals(pictureMarker))
//        {
//            Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
//        }
        return false;
    }

    // create file to write new image to
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // public gallery folder
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // private to the app storage
        File storageDir = getExternalFilesDir(null);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    class LocationUpdateListener implements LocationListener
    {
        private ArrayList<Polyline> gridLines = new ArrayList<>();

        @Override
        public void onLocationChanged(Location location) {

            userLocation = location;

            // TODO detect sudden jumps and display grey out

            // check location accuracy
            float acc = location.getAccuracy();
            if (acc > 50f)
            {
                tvloading.setVisibility(View.VISIBLE);
                grey_out = true;
            }
            else
            {
                tvloading.setVisibility(View.GONE);
                grey_out = false;
            }

            boolean animate = true;
            if (loading)
        {
                animate = false;
                loading = false;
                RefreshAllData(location.getLatitude(), location.getLongitude());
            }

            // determine if the user has move too far away from last update location
            if (lastRefreshLocation != null)
            {
                double latDiff = Math.abs(lastRefreshLocation.getLatitude() - userLocation.getLatitude());
                double lngDiff = Math.abs(lastRefreshLocation.getLongitude() - userLocation.getLongitude());
                double threshold = 0.05;
                if (latDiff > threshold || lngDiff > threshold)
                {
                    RefreshAllData(location.getLatitude(), location.getLongitude());
                }
            }

            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, currentZoom);
            //CameraUpdate yourLocation = CameraUpdateFactory.newLatLng(currentPosition);
            if (animate)
                map.animateCamera(yourLocation);
            else
                map.moveCamera(yourLocation);

            drawGridLines();

        }



        public void drawGridLines()
        {
            for (int c = 0;c < gridLines.size();c++)
            {
                gridLines.get(c).remove();
            }
            gridLines.clear();

            double cellSize = 0.001;
            LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

            // Horizontal Lines

            double _numHLines = (bounds.southwest.latitude - bounds.northeast.latitude) / cellSize;
            int numHLines = Math.abs((int) _numHLines);

            double topLat = bounds.southwest.latitude;
            DecimalFormat df = new DecimalFormat("#.###");
            double startLat = Double.parseDouble(df.format(topLat));

            for (int i = 0;i < numHLines;i++)
            {
                // Instantiates a new Polyline object and adds points to define a rectangle
                PolylineOptions linePath = new PolylineOptions()
                        .add(new LatLng(startLat + (cellSize * i), bounds.southwest.longitude))
                        .add(new LatLng(startLat + (cellSize * i), bounds.northeast.longitude))
                        .width(2)
                        .color(Color.GRAY)
                        .geodesic(true);

                // Get back the mutable Polyline
                Polyline polyline = map.addPolyline(linePath);
                gridLines.add(polyline);
            }

            // Vertical Lines

            double _numVLines = (bounds.southwest.longitude - bounds.northeast.longitude) / cellSize;
            int numVLines = Math.abs((int) _numVLines);

            double leftLng = bounds.southwest.longitude;
            df = new DecimalFormat("#.###");
            double startLng = Double.parseDouble(df.format(leftLng));

            for (int i = 0;i < numVLines;i++)
            {
                PolylineOptions linePath = new PolylineOptions()
                        .add(new LatLng(bounds.southwest.latitude, startLng + (cellSize * i)))
                        .add(new LatLng(bounds.northeast.latitude, startLng + (cellSize * i)))
                        .width(2)
                        .color(Color.GRAY)
                        .geodesic(true);

                // Get back the mutable Polyline
                Polyline polyline = map.addPolyline(linePath);
                gridLines.add(polyline);
            }

        }



        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }

}

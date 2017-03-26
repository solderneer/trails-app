package ism.trails;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;

    private Location currentStart;
    private Location currentDestination;
    private HashMap<Location, String> trailMarkers;
    private ArrayList<Pair<Location, LocationInfo>> route;
    private int routeIndex;
    private HashMap<Location, ArrayList<Pair<Location, LocationInfo>>> preRoute;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.getMapAsync(this);

        showExplore();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private static HashMap<String, Integer> exploreId;
    static
    {
        exploreId = new HashMap<String, Integer>();
        exploreId.put("goButton", 1);
        exploreId.put("infoView", 2);
        exploreId.put("nameView", 3);
        exploreId.put("descriptionView", 4);
        exploreId.put("imageView", 5);
        exploreId.put("otherData", 6);
    }


	private enum State{explore, search, make};
	private State state;
	
    private void showExplore() {
        highlightTab(R.id.explore);
        LinearLayout root = (LinearLayout) findViewById(R.id.view_ui);
		root.setVisibility(INVISIBLE);
        root.removeAllViews();

        Button goButton = new Button(this);
        goButton.setId(exploreId.get("goButton"));
        goButton.setBackgroundColor(getColor(R.color.colorPrimary));
        goButton.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        goButton.setText("Go");
        goButton.setTextSize(getResources().getDimension(R.dimen.buttonSize);
		
		root.addChildren(goButton);

        LinearLayout infoView = new LinearLayout(this);
		infoView.setId(exploreId.get("infoView"));
		infoView.setOrientation(LinearLayout.VERTICAL);
		infoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		TextView otherData = new TextView(this);
		otherData.setId(exploreId.get("otherData"));
		infoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		LinearLayout hBox = new LinearLayout(this);
		hBox.setOrientation(LinearLayout.HORIZONTAL);
		hBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		LinearLayout vBox = new LinearLayout(this);
		vBox.setOrientation(LinearLayout.VERTICAL);
		vBoxView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		TextView nameView = new TextView(this);
		nameView.setId(exploreId.get("nameView"));
		nameView.setTextSize(getResources().getDimension(R.dimen.titleSize));
		
		vBox.addChildren(nameView);
		
		TextView descriptionView = new TextView(this);
		descriptionView.setId(exploreId.get("descriptionView"));
		descriptionView.setTextSize(getResources().getDimension(R.dimen.textSize));
		
		vBox.addChildren(descriptionView);
		
		hBox.addChildren(vBox);
		
		ImageView image = new ImageView(this);
		imageView.setId(exploreId.get("imageView");
		
		hBox.addChildren(image);
		
		infoView.addChildren(hBox);
		
		TextView otherView = new TextView(this);
		otherView.setId(exploreId.get("otherView"));
		otherView.setTextColor(getColor(R.id.fadeColor));
		
		infoView.addChildren(otherView);
		
		root.addChildren(infoView);
		
		state = explore;
    }

    private void highlightTab(int tabId) {
        int tabs[] = {R.id.explore, R.id.search, R.id.make};
        for (int tab : tabs) {
            LinearLayout tabView = (LinearLayout) findViewById(tab);
            tabView.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        }
        LinearLayout tabView = (LinearLayout) findViewById(tabId);
        tabView.setBackgroundColor(getColor(R.color.colorPrimaryDark));
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

        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            enableMyLocation();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentDestination != null && location.distanceTo(currentDestination) < 10) {
            if (checkEnd()) {
                showEnd();
                return;
            }
            currentStart = route.get(routeIndex).first;
            routeIndex++;
            currentDestination = route.get(routeIndex).first;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void startRoute() {
        currentDestination = currentStart;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.google.com";

        // Request a string response from the provided URL.

        JsonObjectRequest stringRequest = new JsonObjectRequest(url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private boolean checkEnd() {
        return (routeIndex == route.size() - 1);
    }

    private void showEnd() {
        routeIndex = -2;
        currentStart = null;
        currentDestination = null;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
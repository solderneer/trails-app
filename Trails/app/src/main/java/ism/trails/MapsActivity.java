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

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapReadyCallback, GoogleMap.OnCameraMoveListener, LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    private Location currentStart;
    private Location currentDestination;
    private HashMap<String, LatLng> trailMarkers;
	private ArrayList<LatLng> roughRoute;
    private ArrayList<LocationInfo> route;
    private int routeIndex;
    private HashMap<LocationInfo, ArrayList<Pair<LatLng, LocationInfo>>> preRoute;
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


	private enum State{explore, enRoute, search, make};
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
		
		state = State.explore;
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
		mMap.

        enableMyLocation();
    }
	
	double prevMaxLat = 0, prevMinLat = 0, prevMaxLong = 0, prevMinLong = 0;
	@Override
	public void onCameraMove() {
		if (state == State.explore && prevMaxLat != 0) {
			RequestQueue queue = Volley.newRequestQueue(this);
			VisibleRegion vr = mMap.getProjection().getVisibleRegion();
			LatLng farLeft = vr.farLeft;
			LatLng nearRight = vr.nearRight;
			double maxLat = max(farLeft.latitude, nearRight.latitude);
			double minLat = min(farLeft.latitude, nearRight.latitude);
			double maxLong = max(farLeft.longitude, nearRight.longitude);
			double minLong = min(farLeft.longitude, nearRight.longitude);
			
			if (maxLat < prevMaxLat || maxLong < prevMaxLong || minLat > prevMinLat || minLong > prevMinLong) {
				return;
			}
			
			double diffLat = (maxLat - minLat) * 5;
			double diffLong = (maxLong - minLong) * 5;
			maxLat = maxLat + diffLat;
			maxLong = maxLong + diffLong;
			minLat = minLat + diffLat;
			minLong = minLong + diffLong;
			
			
			String url = "https://trails.sudharshan.makerforce.io/trails/restricted?" +
				"longbot=" + minLong  + "&" +
				"longtop=" + maxLong + "&" +
				"latbot=" + minLat + "&" +
				"lattop=" + maxLat;

			// Request a string response from the provided URL.

			JsonArrayRequest stringRequest = new JsonArrayRequest(url, new JSONArray(),
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						//reponse is a list of trailObjects
						
						for (int i = 0; i < response.length(); i++) {
							JSONObject trail = reponse.get(i);
							if (trailMarkers.getOrDefault(trail.getString("id"), null) == null) {
								trailMakers.put(trail.getString("id"), new LatLng(trail.getDouble("latitude"), trail.getDouble("longitude")));
								
								Marker marker = map.addMarker(new MarkerOptions()
									.position(new LatLng(trail.getDouble("latitude"), trail.getDouble("longitude")))
								)
								marker.setTag(trail.getString("id"));
								
							}
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

					}
				}
			);
			// Add the request to the RequestQueue.
			queue.add(stringRequest);
			
			prevMaxLat = maxLat;
			prevMaxLong = maxLong;
			prevMinLat = minLat;
			prevMinLong = minLong;
		}
	}
	
	@Override
	public boolean onMarkerClick(final Marker marker) {
		String id = (String) marker.getTag();
		
		String url = "https://trails.sudharshan.makerforce.io/trails/" + id;

		// Request a string response from the provided URL.

		JsonObjectRequest stringRequest = new JsonObjectRequest(url, new JSONObject(),
			new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					//reponse is a trailObjects
					
					LinearLayout root = (LinearLayout) findViewById(R.id.view_ui);
		
					TextView nameView = (TextView) root.findViewById(exploreId.get("nameView"));
					nameView.setText(response.getString("name"));
					
					TextView descriptionView = (TextView) root.findViewById(exploreId.get("descriptionView"));
					descriptionView.setText(response.getString("description"));
					
					TextView otherView = (TextView) root.findViewById(exploreId.get("otherView"));
					long rawTime = response.getDouble("time");
					String time = "";
					if (rawTime / 3600 != 0) {
						time += (rawTime/3600) + "h ";
						if (rawTime / 60 != 0) {
							time += (rawTime/60) + "m " ;
						}
					}
					else {
						if (rawTime / 60 != 0) {
							time += (rawTime/60) + "m " ;
						}
						time += (rawTime % 60) + "s";
					}
					otherView.setText("Distance: " + response.getString("distance") + " Time Taken: " + time + " Markers: " + response.getString("markers"));
					
					root.setVisibility(VISIBLE);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {

				}
			}
		);
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
		
		
		return true;
	}
	
	@Override
	public void onMapClick(LatLng point) {
		LinearLayout root = (LinearLayout) findViewById(R.id.view_ui);
		root.setVisibility(INVISIBLE);
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
		if (state == State.enRoute) {
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

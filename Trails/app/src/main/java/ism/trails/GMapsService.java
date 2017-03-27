package ism.trails;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class GMapsService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GMapsService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();

    public GMapsService() {
    }

    public class LocalBinder extends Binder {
        public GMapsService getService() {
            return GMapsService.this;
        }
    }

    //Client CMethods - start
    public ArrayList<Pair<Location, LocationInfo>> getRoute(Location start, Location end) {
        return null;
    }

    //Google Map - start
    private GoogleApiClient googleApiClient;
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //Google Map - stop

    //Service - start
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Current time " + System.currentTimeMillis());
        Log.d(TAG, "Service is running");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        googleApiClient.disconnect();
        super.onDestroy();
    }
}

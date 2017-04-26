package myideasforworld.toolbarteste;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.media.audiofx.BassBoost;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static android.R.attr.fragment;


public class MapaActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private static final int REQUEST_ERRO_PLAY_SERVICES = 1;
    private static final int REQUEST_CHECAR_GPS = 2;
    private static final String EXTRA_DIALOG = "dialog";


    private Context context;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Activity activity;




    Handler handler;
    boolean exDialog;
    int tentativas;
    GoogleMap mMap;
    GoogleApiClient googleApiClient;
    LatLng mOrigem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        context = getApplicationContext();
        activity = this;
        super.onCreate(savedInstanceState);

        //checkPermission();
        //requestPermission();


        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        handler = new Handler();
        exDialog = savedInstanceState == null;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa1);

        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_DIALOG, exDialog);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        exDialog = savedInstanceState.getBoolean(EXTRA_DIALOG, true);
    }



    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ERRO_PLAY_SERVICES && resultCode == RESULT_OK) {
            googleApiClient.connect();
        }
    }

    @Override

    public void onConnected(@Nullable Bundle bundle) {
        //ultimaLocalizacao();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_ERRO_PLAY_SERVICES);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            msgErro(this, connectionResult.getErrorCode());
        }
    }

    @SuppressWarnings("MissingPermission")
    private void ultimaLocalizacao() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            mOrigem = new LatLng(location.getLatitude(), location.getLongitude());
            atualizarMapa();
        }

    }

    private void atualizarMapa() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 17.0f));
        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(mOrigem).title("Local atual"));
    }


    private void msgErro(FragmentActivity activity, final int codigoDoErro) {
        final String TAG = "DIALOG_ERRO_PLAY_SERVICES";
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            DialogFragment errorFragment = new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle bundle) {
                    return GooglePlayServicesUtil.getErrorDialog(
                            codigoDoErro, getActivity(), REQUEST_ERRO_PLAY_SERVICES);
                }
            };
            errorFragment.show(activity.getSupportFragmentManager(), TAG);
        }
    }

    //converte o texto digitado no edit text para latlng e cria um marker
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.edtLocal);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.isEmpty()) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Local"));

            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            return;
        }


        mMap.setMyLocationEnabled(true);
        if (mMap.getMyLocation() ==null){
            Toast.makeText(context,"Buscando local atual ...",Toast.LENGTH_LONG).show();
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mMap.setTrafficEnabled(true );
        mMap.getUiSettings().setMapToolbarEnabled(false);


        // Add a marker in Sydney and move the camera
        //VerificarStatusGPS();
    }

    //Faz requisição de permissões ao usuário
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

            return false;
        }

    }


    @Override
    public void onBackPressed(){
        finish();
    }
}

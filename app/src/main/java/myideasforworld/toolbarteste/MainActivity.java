package myideasforworld.toolbarteste;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.ShareActionProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,View.OnClickListener {





    private static final int PERMISSION_REQUEST_CODE = 1;
    Activity activity;
    FloatingActionButton fab;
    LinearLayout searchBar;
    Handler handler;
    boolean exDialog;
    Marker marker;
    GoogleMap mMap;
    String local,titulo;

    TextView opcoes,irAte,levar,pesquisar;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab1,fab2,fab3;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward,fab_scale_down,fab_scale_up;

    ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializador do Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("OETPNBfo0Gt3MLzqZzHH9x7Vfpm1qJtXpGYvli7f")
                .clientKey("4NgbCa6Fkb6TXYhL83O9n6FFzyhQfYnVrBQQkRH0")
                .server("https://parseapi.back4app.com/").build());



        //esse código verifica se o GPS está ativo
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider == null || provider.length() == 0) {
            Toast.makeText(this, "Favor habilitar o GPS antes de continuar", Toast.LENGTH_LONG).show();

              //essa intent abre a configuração para ativar o GPS antes de continuar
              Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
              startActivityForResult(intent, 1);
              finish(); //para a aplicação, evitando sua continuação antes de ativar o GPS
            }

           //solicita o login do usuário
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            //Instancia e cria uma toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        //---------------------------AutoComplete place---------------------------------

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Digite o Local desejado");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                local = place.getAddress().toString();
                titulo = place.getName().toString();
                Log.i("PLACE", "Place: " + place.getAddress());
                addMarker(place.getLatLng(),place.getName().toString());
            }

            @Override
            public void onError(Status status) {

                Log.i("PLACE", "Ocorreu um erro: " + status);
            }
        });
        fab_scale_down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_scale_down);
        fab_scale_up = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_scale_up);
        //---------------------------AutoComplete place---------------------------------

            searchBar = (LinearLayout)findViewById(R.id.searchBar);


//---------------------------------------FAB-----------------------------------------------
            //instancia e cria um botão flutuante

        opcoes = (TextView)findViewById(R.id.txtMenu);
        levar = (TextView)findViewById(R.id.txtMenu1);
        irAte = (TextView)findViewById(R.id.txtMenu2);
        pesquisar = (TextView)findViewById(R.id.txtMenu3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
//--------------------------------------FAB------------------------------------------------

            //código para criação de um menu lateral em um viem drawer layoult
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);




        //instancia mapa do mapfragment


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        handler = new Handler();
        exDialog = savedInstanceState == null;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);

        mapFragment.getMapAsync(this);




    }


        int cont = 1;
        @Override
        public void onBackPressed () {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                //super.onBackPressed();
            }

            if (!drawer.isDrawerOpen(GravityCompat.START) && cont == 1) {
                Toast.makeText(getApplicationContext(), "Toque novamente para sair", Toast.LENGTH_LONG).show();
                --cont;
            } else {
                finish();
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);

            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_map) {
                //Intent mapa = new Intent(getApplicationContext(), MapaActivity.class);
                //startActivity(mapa);
            } else if (id == R.id.nav_ref) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {
                MenuItem shareItem = item;
                shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
                String texto = getString(R.string.texto_compartilhar);
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("text/plain");
                it.putExtra(Intent.EXTRA_TEXT, texto);
                shareActionProvider.setShareIntent(it);

            } else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;


        }


    //--------------------------------------Mapa-----------------------------------------------------

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
           // Toast.makeText(getApplicationContext(),"Buscando local atual ...",Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(R.id.mapa), "Procurando sua localização", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener(this);
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }



    public void addMarker(LatLng latLng, String name) {
                fab.setVisibility(View.VISIBLE);
                mMap.clear();
                searchBar.setVisibility(GONE);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                marker.setTag(0);
                marker.showInfoWindow();


    }

    @Override
    public boolean onMarkerClick(final Marker marker) {


        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            if(marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }


        }

        return true;
       // return false;

    }





//=============================FAB=================================================
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                animateFAB();
                break;

            case R.id.fab1:
                if (marker == null){
                    Toast.makeText(getApplicationContext(),"Nenhum lugar foi selecionado, faça uma procura antes !",Toast.LENGTH_LONG).show();
                }else {
                    Intent it = new Intent(this, AddPonto.class);
                    it.putExtra("local", local);
                    it.putExtra("titulo", titulo);
                    startActivity(it);
                    animateFAB();
                }

                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab2:

                Log.d("Raj", "Fab 2");
                break;
            case R.id.fab3:
                if (searchBar.getVisibility() == View.VISIBLE){
                    searchBar.startAnimation(fab_close);
                    searchBar.setVisibility(View.GONE);
                }else {
                    searchBar.startAnimation(fab_open);
                    searchBar.setVisibility(View.VISIBLE);
                }
                Log.d("Raj", "Fab 2");
                animateFAB();
                break;
        }

    }


    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            opcoes.startAnimation(fab_close);
            opcoes.setVisibility(GONE);
            levar.startAnimation(fab_close);
            levar.setVisibility(GONE);
            irAte.startAnimation(fab_close);
            irAte.setVisibility(GONE);
            pesquisar.startAnimation(fab_close);
            pesquisar.setVisibility(GONE);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            opcoes.startAnimation(fab_open);
            opcoes.setVisibility(View.VISIBLE);
            levar.startAnimation(fab_open);
            levar.setVisibility(View.VISIBLE);
            irAte.startAnimation(fab_open);
            irAte.setVisibility(View.VISIBLE);
            pesquisar.startAnimation(fab_open);
            pesquisar.setVisibility(View.VISIBLE);

            isFabOpen = true;
            Log.d("Raj","open");

        }
    }
}
//=============================FAB=================================================


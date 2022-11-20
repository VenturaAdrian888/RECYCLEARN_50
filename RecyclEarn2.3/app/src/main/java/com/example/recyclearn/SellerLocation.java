package com.example.recyclearn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerLocation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public TextView navHeadername;

    public CircleImageView nav_profseller;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private FirebaseAuth fAuth;
    private StorageReference storageReference;

    private CircleImageView Seller_Profile;
    private ImageView btn_drawer;

    //Dro-off Points
    private ImageView SM;
    private ImageView airport;
    private ImageView caniezo;
    private ImageView uc;

    //Search view
    private GoogleMap map;
    private SearchView searchView;
    private SupportMapFragment mapFragment;

    //Adding GEO-Fencing
    private GeofencingClient geofencingClient;
    private float GEOFENCERADIUS = 200;

    ConstraintLayout contentView;
    //Set animation when drawer is being dragged
    static final float END_SCALE = 0.8f;


    private int ACCESS_LOCATION_REQUEST_CODE = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_location);
//drawer
        drawerLayout = findViewById(R.id.seller_drawer_layout2);
        navigationView = findViewById(R.id.nav_view2);
        btn_drawer = findViewById(R.id.btn_drawer);
        navigationView.setItemIconTintList(null);

        contentView = findViewById(R.id.locationContent);
        navigationSellerDrawer();

// display onclick on sellerprofile
        Seller_Profile = (CircleImageView) findViewById(R.id.Seller_Profile1);
        Seller_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensellerprofile();
            }
        });

//Display user info in drawer
        navHeadername = navigationView.getHeaderView(0).findViewById(R.id.nav_usernameseller);
        reference = FirebaseDatabase.getInstance().getReference().child("Seller_Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                navHeadername.setText(snapshot.child("username").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SellerLocation.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });
// display profile picture in navigation drawer
        nav_profseller = navigationView.getHeaderView(0).findViewById(R.id.nav_sellerprofile2);
        storageReference = FirebaseStorage.getInstance().getReference().child("user_seller/"+ fAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(nav_profseller);
            }
        });
//display profile picture

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        Seller_Profile =(CircleImageView) findViewById(R.id.Seller_Profile1);

        StorageReference profileRef1 = storageReference.child("user_seller/"+ fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Seller_Profile);
            }
        });

        SM = findViewById(R.id.SMBaguio);
        airport = findViewById(R.id.LoakanAirport);
        caniezo = findViewById(R.id.CaniezoJunkshop);
        uc = findViewById(R.id.UC);

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressesList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(SellerLocation.this);
                    try {
                        addressesList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressesList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
       
    }

    private void navigationSellerDrawer() {
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
// navigation menu items declaration
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_sellerlocation);
        btn_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    //Code para dun sa animation ng Drawer
    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                //Scale the view on current slide offset
                final float diffScaleoffset =  slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaleoffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //Translate the View, accounting for the scaled wdith
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaleoffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    // back button on drawer navigation
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    //drawer link to other activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

        switch (menuitem.getItemId()) {
            case R.id.nav_sellerhome:
                Intent intent = new Intent(SellerLocation.this, SellerDashboard.class);
                startActivity(intent);
                break;
            case  R.id.nav_sellerprofile:
                Intent intent1 = new Intent(SellerLocation.this, SellerProfile.class);
                startActivity(intent1);
                break;
            case R.id.nav_sellerlocation:
                break;
            case R.id.nav_sellerinfo:
                Intent intent2 = new Intent(SellerLocation.this, SellerAboutUs.class);
                startActivity(intent2);
                break;
            case R.id.nav_sellerlogout:
                // YUNG CODE PARA DI NA BUMALIK ULIT SA ACTIVITY AFTER NAG LOG OUT YUNG USER-------------
                Intent intent3 = new Intent(getBaseContext(),MainActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // YUNG CODE PARA DI NA BUMALIK ULIT SA ACTIVITY AFTER NAG LOG OUT YUNG USER-------------
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // Open seller profile
    public void opensellerprofile(){
        Intent intent = new Intent(SellerLocation.this, SellerProfile.class);
        startActivity(intent);
    }


    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        addMarker(latLng);
        addCircle(latLng, GEOFENCERADIUS);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLongClickListener(this);
        enableuserlocation();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == getPackageManager().PERMISSION_GRANTED) {
        }

        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }

        LatLng SMBaguio = new LatLng(16.4089, 120.5992);
        map.moveCamera(CameraUpdateFactory.newLatLng(SMBaguio));
        MarkerOptions mark1 = new MarkerOptions().position(SMBaguio).title("SM Baguio");
        mark1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(mark1);

        LatLng LoakanAirport = new LatLng(16.3755, 120.6130);
        map.moveCamera(CameraUpdateFactory.newLatLng(LoakanAirport));
        MarkerOptions mark2 = new MarkerOptions().position(LoakanAirport).title("Loakan Airport");
        mark2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(mark2);

        LatLng Junkshop = new LatLng(16.409295875685704, 120.58747924628034);
        map.moveCamera(CameraUpdateFactory.newLatLng(Junkshop));
        MarkerOptions mark3 = new MarkerOptions().position(Junkshop).title("Caniezo JunkShop");
        mark3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(mark3);

        LatLng Cordilleras = new LatLng(16.4087, 120.5978);
        map.addMarker(new MarkerOptions().position(Cordilleras).title("University of the Cordilleras"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Cordilleras));
        MarkerOptions mark4 = new MarkerOptions().position(Cordilleras).title("University of the Cordilleras");
        mark4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(mark4);

        LatLng Philippines = new LatLng(12.8797, 121.7740);
        map.addMarker(new MarkerOptions().position(Philippines).title("Philippines"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Philippines));

        LatLng Baguio = new LatLng(16.4023, 120.5960);
        map.moveCamera(CameraUpdateFactory.newLatLng(Baguio));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Baguio, 15));
        MarkerOptions baguio = new MarkerOptions().position(Baguio).title("Baguio City");
        baguio.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        map.addMarker(baguio);



        //DROP-OFF POINTS
        SM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                LatLng SMBaguio = new LatLng(16.4089, 120.5992);
                map.moveCamera(CameraUpdateFactory.newLatLng(SMBaguio));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(SMBaguio, 17));
                MarkerOptions bag = new MarkerOptions().position(SMBaguio).title("SM Baguio");
                bag.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                map.addMarker(bag);
            }
        });

        airport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                LatLng LoakanAirport = new LatLng(16.3755, 120.6130);
                map.moveCamera(CameraUpdateFactory.newLatLng(LoakanAirport));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(LoakanAirport, 16));
                MarkerOptions mark2 = new MarkerOptions().position(LoakanAirport).title("Loakan Airport");
                mark2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                map.addMarker(mark2);
            }
        });

        caniezo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                LatLng Junkshop = new LatLng(16.409295875685704, 120.58747924628034);
                map.moveCamera(CameraUpdateFactory.newLatLng(Junkshop));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(Junkshop, 30));
                MarkerOptions mark3 = new MarkerOptions().position(Junkshop).title("Caniezo JunkShop");
                mark3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                map.addMarker(mark3);
            }
        });

        uc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent class will help to go to next activity using
                // it's object named intent.
                // SecondActivty is the name of new created EmptyActivity.
                LatLng Cordilleras = new LatLng(16.4087, 120.5978);
                map.moveCamera(CameraUpdateFactory.newLatLng(Cordilleras));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(Cordilleras, 19));
                MarkerOptions mark4 = new MarkerOptions().position(Cordilleras).title("University of the Cordilleras");
                mark4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                map.addMarker(mark4);
            }
        });

    }

    private void enableuserlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
                enableuserlocation();
            }
            else {

            }
        }

    }
    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        map.addMarker(markerOptions);
    }
    private void addCircle(LatLng latLng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255,0,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(4);
        map.addCircle(circleOptions);
    }

    }

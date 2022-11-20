package com.example.recyclearn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    private Button sellerRedeem;

    private ImageView dash_seller_location;

    private ImageView dash_seller_trade1;

    private ImageView dash_seller_leaderboards;

    private ImageView dash_seller_faqs;

    private ImageView btn_drawer;


    ConstraintLayout contentView;

    //Set animation when drawer is being dragged
    static final float END_SCALE = 0.8f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);
//drawer navigation
        drawerLayout = findViewById(R.id.seller_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        btn_drawer = findViewById(R.id.btn_drawer);
        navigationView.setItemIconTintList(null);

        contentView = findViewById(R.id.dashboardContent);
        navigationSellerDrawer();


//display username in welcome back,
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Seller_Users");
        userID = user.getUid();

        final TextView usernameTextView = (TextView) findViewById(R.id.usernameseller);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Seller userprofile = snapshot.getValue(User_Seller.class);

                if (userprofile != null) {
                    String username = userprofile.username;
                    usernameTextView.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SellerDashboard.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
//display onClcik to redeem seller
        sellerRedeem = (Button) findViewById(R.id.btn_seller_redeem);
        sellerRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensellerRedeem();
            }
        });

//display onlick to seller profile
        Seller_Profile = (CircleImageView) findViewById(R.id.Seller_Profile1);
        Seller_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensellerprofile();
            }
        });
//display onclick on seller location
        dash_seller_location = (ImageView) findViewById(R.id.dash_seller_location);
        dash_seller_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensellerlocation();
            }
        });

//Display onclick on seller trade
        dash_seller_trade1 = (ImageView) findViewById(R.id.dash_seller_trade1);
        dash_seller_trade1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensellertrade();
            }
        });

//Display onClick on seller leaderboards
        dash_seller_leaderboards = (ImageView) findViewById(R.id.dash_seller_leaderboards);
        dash_seller_leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensellerLeaderboards();
            }
        });


//Display onclick seller faqs
        dash_seller_faqs = (ImageView) findViewById(R.id.dash_seller_faqs);
        dash_seller_faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensellerFaqs();
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
                Toast.makeText(SellerDashboard.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });
// dispaly profile picture in navigation drawer
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


    }

    private void navigationSellerDrawer() {
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
// navigation menu items declaration
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_sellerhome);
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


    //drawer backbutton
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent alis = new Intent(getBaseContext(),MainActivity.class);
            alis.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // YUNG CODE PARA DI NA BUMALIK ULIT SA ACTIVITY AFTER NAG LOG OUT YUNG USER-------------
            startActivity(alis);
        }

    }
//drawer link to other activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

        switch (menuitem.getItemId()) {
            case R.id.nav_sellerhome:
                break;
            case  R.id.nav_sellerprofile:
                Intent intent = new Intent(SellerDashboard.this, SellerProfile.class);
                startActivity(intent);
                break;
            case R.id.nav_sellerlocation:
                Intent intent2 = new Intent(SellerDashboard.this, SellerLocation.class);
                startActivity(intent2);
                break;
            case R.id.nav_sellerinfo:
                Intent intent1 = new Intent(SellerDashboard.this, SellerAboutUs.class);
                startActivity(intent1);
                break;
            case R.id.nav_sellerlogout:
                // YUNG CODE PARA DI NA BUMALIK ULIT SA ACTIVITY AFTER NAG LOG OUT YUNG USER-------------
                Intent intent3 = new Intent(getBaseContext(),MainActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // YUNG CODE PARA DI NA BUMALIK ULIT SA ACTIVITY AFTER NAG LOG OUT YUNG USER-------------
                startActivity(intent3);
                break;

            // case R.id.: "dito ilalagay yung pag na click tas ma direct to other activity"
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;



    }
    //Open seller Redeem page
    public void opensellerRedeem(){
        Intent intent = new Intent(SellerDashboard.this, SellerRedeem.class);
        startActivity(intent);
    }
    // Open seller profile
     public void opensellerprofile(){
         Intent intent0 = new Intent(SellerDashboard.this, SellerProfile.class);
         startActivity(intent0);
     }
     //Open seller location
    public void opensellerlocation(){
        Intent intentr = new Intent(SellerDashboard.this, SellerLocation.class);
        startActivity(intentr);
    }
    //Open seller trade a bottle
    public void opensellertrade(){
        Intent intent3 = new Intent(SellerDashboard.this, SellerTrade.class);
        startActivity(intent3);
    }
    //Open seller Leaderboards
    public void opensellerLeaderboards(){
        Intent intentz = new Intent(SellerDashboard.this, SellerLeaderboards.class);
        startActivity(intentz);
    }
    //Open seller FAQS
    public void opensellerFaqs(){
        Intent intent1 = new Intent(SellerDashboard.this, SellerFaqs.class);
        startActivity(intent1);
    }
}
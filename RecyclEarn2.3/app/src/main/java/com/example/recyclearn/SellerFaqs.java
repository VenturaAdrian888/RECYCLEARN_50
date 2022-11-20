package com.example.recyclearn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerFaqs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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

    ConstraintLayout contentView;
    //Set animation when drawer is being dragged
    static final float END_SCALE = 0.8f;

    public ViewPager2 viewPager2;
    public ArrayList<seller_viewpager_item> sellerViewpagerItemArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_faqs);

         viewPager2 = findViewById(R.id.viewPager2);
         int[]images = {R.drawable.slide1,R.drawable.crash_bottle,R.drawable.dirty,R.drawable.bag,R.drawable.wrapper,R.drawable.foam};
         String[] heading = {"Put the water bottle cap back on after consuming.","Crush your bottle.","Do not rinse thoroughly.","Don’t Bag ’Em","Recycle the outer packaging","Know your plastics"};
         String[] description = {"Separated caps can reek havoc on equipment sending them to the landfill and causing production to come to a halt. To prevent this from happening, please replace the cap once you have finished.",
                 "Bottles must be crushed and all of the air removed.This causes more space to be saved at the processing facility, reducing the need for expansion.",
         "You don’t need to spend extra time rinsing out bottles that might have some liquid inside of them. Just make sure they’re empty and that you place them in a recycling bin.",
         "Bagged items prevent the process from being completed and adds an extra step to the recycle process. Please place the water bottles in a recycling bin or bin free of any confinements.",
         "Please separate this from the plastic bottles being recycles. The plastic film can be dropped off at participating grocery stores such as Fry’s and Safeway to be processed along.",
         "A lot of plastic just isn’t recyclable curbside. As noted earlier, you can’t recycle plastic bags or films. Additionally, you can’t recycle anything that can tear like paper. That means no cracker bags, chip bags or cereal bags."};

         sellerViewpagerItemArrayList = new ArrayList<>();

         for (int i = 0; i<images.length; i++){
             seller_viewpager_item sellerViewpagerItem = new seller_viewpager_item(images[i],heading[i],description[i]);
             sellerViewpagerItemArrayList.add(sellerViewpagerItem);
         }

         VPAdapter vpAdapter = new VPAdapter(sellerViewpagerItemArrayList);
         viewPager2.setAdapter(vpAdapter);
         viewPager2.setClipToPadding(false);
         viewPager2.setClipChildren(false);
         viewPager2.setOffscreenPageLimit(2);
         viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

//drawer
        drawerLayout = findViewById(R.id.seller_drawer_layout2);
        navigationView = findViewById(R.id.nav_view2);
        btn_drawer = findViewById(R.id.btn_drawer);
        navigationView.setItemIconTintList(null);

        contentView = findViewById(R.id.learnContent);
        navigationSekkerDrawer();

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
                Toast.makeText(SellerFaqs.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
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

    }



    private void navigationSekkerDrawer() {
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
// navigation menu items declaration
        navigationView.setNavigationItemSelectedListener(this);
        
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
                Intent intent = new Intent(SellerFaqs.this, SellerDashboard.class);
                startActivity(intent);
                break;
            case  R.id.nav_sellerprofile:
                Intent intent1 = new Intent(SellerFaqs.this, SellerProfile.class);
                startActivity(intent1);
                break;
            case R.id.nav_sellerlocation:
                Intent intent2 = new Intent(SellerFaqs.this, SellerLocation.class);
                startActivity(intent2);
                break;
            case R.id.nav_sellerinfo:
                Intent intent4 = new Intent(SellerFaqs.this, SellerAboutUs.class);
                startActivity(intent4);
                break;
            case R.id.nav_sellerlogout:
                Intent intent3 = new Intent(SellerFaqs.this, MainActivity.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // Open seller profile
    public void opensellerprofile(){
        Intent intent = new Intent(SellerFaqs.this, SellerProfile.class);
        startActivity(intent);

    }
}
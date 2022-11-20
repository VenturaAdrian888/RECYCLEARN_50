package com.example.recyclearn;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Buyer_Headernav extends AppCompatActivity {

    private FirebaseUser buser;
    private DatabaseReference breference;
    private String buserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_headernav);

        buser = FirebaseAuth.getInstance().getCurrentUser();
        breference = FirebaseDatabase.getInstance().getReference("Buyer_Users");
        buserID = buser.getUid();

        final TextView usernameTextView = (TextView) findViewById(R.id.nav_usernamebuyer);
        breference.child(buserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Buyer userprofile = snapshot.getValue(User_Buyer.class);

                if (userprofile != null) {
                    String username = userprofile.username;
                    usernameTextView.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Buyer_Headernav.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


    }}
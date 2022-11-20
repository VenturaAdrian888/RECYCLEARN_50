package com.example.recyclearn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BuyerUpdateProfile extends AppCompatActivity {

    EditText etusername, etfullname;
    Button button;

    private FirebaseUser user;
    String userID;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_update_profile);

        etusername = findViewById(R.id.editUsername);
        etfullname = findViewById(R.id.editFullname);
        button = findViewById(R.id.save);


        //displayname
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Buyer_Users");
        userID = user.getUid();

        final TextView usernameTextView = (EditText) findViewById(R.id.editUsername);
        final TextView fullnameTextView = (EditText) findViewById(R.id.editFullname);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_Buyer userprofile = snapshot.getValue(User_Buyer.class);

                if (userprofile != null) {
                    String username = userprofile.username;
                    String fullname = userprofile.fullName;
                    usernameTextView.setText(username);
                    fullnameTextView.setText(fullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyerUpdateProfile.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Buyer_Users");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = etusername.getText().toString();
                String fname = etfullname.getText().toString();


                HashMap hashMap = new HashMap();
                hashMap.put("username",uname);
                hashMap.put("fullName",fname);


                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(BuyerUpdateProfile.this,"Updated",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
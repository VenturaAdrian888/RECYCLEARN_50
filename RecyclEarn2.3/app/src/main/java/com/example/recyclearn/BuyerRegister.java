package com.example.recyclearn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BuyerRegister extends AppCompatActivity implements View.OnClickListener {
    private TextView back_signin;
    private Button btn_Create;
    private EditText Buyer_Fullname, Buyer_Username, Buyer_Email, Buyer_Phonenumber, Buyer_Location, Buyer_Password;
    private ProgressBar progresBar;
    private FirebaseAuth mAuth;
    private CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_register);

        mAuth = FirebaseAuth.getInstance();

        back_signin = (TextView) findViewById(R.id.back_signin);
        back_signin.setOnClickListener(this);

        btn_Create = (Button) findViewById(R.id.btn_Create);
        btn_Create.setOnClickListener(this);

        Buyer_Fullname = (EditText) findViewById(R.id.Buyer_Fullname);
        Buyer_Username = (EditText) findViewById(R.id.Buyer_Username);
        Buyer_Email = (EditText) findViewById(R.id.Buyer_Email);
        Buyer_Phonenumber = (EditText) findViewById(R.id.Buyer_Phonenumber);
        Buyer_Location = (EditText) findViewById(R.id.Buyer_Location);
        Buyer_Password = (EditText) findViewById(R.id.Buyer_Password);

        progresBar = (ProgressBar) findViewById(R.id.progresBar);
        agree = (CheckBox) findViewById(R.id.check_agreement);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_signin:
                startActivity(new Intent(this, BuyerLogIn.class));
                break;

            case R.id.btn_Create:
                registerBuyerUser();
                break;
        }
        }

    private void registerBuyerUser() {
        String email = Buyer_Email.getText().toString().trim();
        String password = Buyer_Password.getText().toString().trim();
        String fullName = Buyer_Fullname.getText().toString().trim();
        String username = Buyer_Username.getText().toString().trim();
        String phonenumber = Buyer_Phonenumber.getText().toString().trim();
        String location = Buyer_Location.getText().toString().trim();

        if (fullName.isEmpty()) {
            Buyer_Fullname.setError("Full name is required!");
            Buyer_Fullname.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            Buyer_Username.setError("Username is required!");
            Buyer_Username.requestFocus();
            return;
        }
        if (phonenumber.isEmpty()) {
            Buyer_Phonenumber.setError("Phone Number is required!");
            Buyer_Phonenumber.requestFocus();
            return;
        }
        if (phonenumber.length() < 11) {
            Buyer_Phonenumber.setError(("Phone Number must be 11 digits"));
            Buyer_Phonenumber.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            Buyer_Location.setError("Location is required!");
            Buyer_Location.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Buyer_Email.setError("Email is required!");
            Buyer_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Buyer_Email.setError("Please provide valid email");
            Buyer_Email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Buyer_Password.setError("Password in required!");
            Buyer_Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            Buyer_Password.setError("Min password length should be 6 characters!");
            Buyer_Password.requestFocus();
            return;
        }
        if (!agree.isChecked()) {
            Toast.makeText(this, "Please select term and condition", Toast.LENGTH_LONG).show();
            agree.requestFocus();
            return;
        }
        progresBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User_Buyer user1 = new User_Buyer(fullName, username, phonenumber, location, email, password );

                            FirebaseDatabase.getInstance().getReference("Buyer_Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(BuyerRegister.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                progresBar.setVisibility(View.GONE);
                                            }
                                            //redirect to login layout
                                            else {
                                                Toast.makeText(BuyerRegister.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                progresBar.setVisibility(View.GONE);
                                            }

                                        }
                                    });

                        } else {
                            Toast.makeText(BuyerRegister.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progresBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
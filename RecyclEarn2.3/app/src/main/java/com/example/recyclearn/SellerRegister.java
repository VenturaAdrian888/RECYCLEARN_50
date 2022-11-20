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


public class SellerRegister extends AppCompatActivity implements View.OnClickListener {

    private TextView back_signin;
    private Button btn_Create;
    private EditText Seller_Fullname, Seller_Username, Seller_Email, Seller_Phonenumber, Seller_Location, Seller_Password;
    private ProgressBar progresBar;
    private FirebaseAuth mAuth;
    private CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        mAuth = FirebaseAuth.getInstance();

        back_signin = (TextView) findViewById(R.id.back_signin);
        back_signin.setOnClickListener(this);

        btn_Create = (Button) findViewById(R.id.btn_Create);
        btn_Create.setOnClickListener(this);

        Seller_Fullname = (EditText) findViewById(R.id.Seller_Fullname);
        Seller_Username = (EditText) findViewById(R.id.Seller_Username);
        Seller_Email = (EditText) findViewById(R.id.Seller_Email);
        Seller_Phonenumber = (EditText) findViewById(R.id.Seller_Phonenumber);
        Seller_Location = (EditText) findViewById(R.id.Seller_Location);
        Seller_Password = (EditText) findViewById(R.id.Seller_Password);

        progresBar = (ProgressBar) findViewById(R.id.progresBar);
        agree = (CheckBox) findViewById(R.id.check_agreement);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_signin:
                startActivity(new Intent(this, SellerLogIn.class));
                break;

            case R.id.btn_Create:
                registerSellerUser();
                break;

        }
    }

    private void registerSellerUser() {
        String email = Seller_Email.getText().toString().trim();
        String password = Seller_Password.getText().toString().trim();
        String fullName = Seller_Fullname.getText().toString().trim();
        String username = Seller_Username.getText().toString().trim();
        String phonenumber = Seller_Phonenumber.getText().toString().trim();
        String location = Seller_Location.getText().toString().trim();


        if (fullName.isEmpty()) {
            Seller_Fullname.setError("Full name is required!");
            Seller_Fullname.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            Seller_Username.setError("Username is required!");
            Seller_Username.requestFocus();
            return;
        }
        if (phonenumber.isEmpty()) {
            Seller_Phonenumber.setError("Phone Number is required!");
            Seller_Phonenumber.requestFocus();
            return;
        }
        if (phonenumber.length() < 11) {
            Seller_Phonenumber.setError(("Phone Number must be 11 digits"));
            Seller_Phonenumber.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            Seller_Location.setError("Location is required!");
            Seller_Location.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Seller_Email.setError("Email is required!");
            Seller_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Seller_Email.setError("Please provide valid email");
            Seller_Email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Seller_Password.setError("Password in required!");
            Seller_Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            Seller_Password.setError("Min password length should be 6 characters!");
            Seller_Password.requestFocus();
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
                            User_Seller user = new User_Seller(fullName, username, phonenumber, location, email, password );

                            FirebaseDatabase.getInstance().getReference("Seller_Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(SellerRegister.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                progresBar.setVisibility(View.GONE);
                                            }
                                            //redirect to login layout
                                            else {
                                                Toast.makeText(SellerRegister.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                progresBar.setVisibility(View.GONE);
                                            }

                                        }
                                    });

                        } else {
                            Toast.makeText(SellerRegister.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progresBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
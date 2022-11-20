package com.example.recyclearn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;


public class SellerLogIn extends AppCompatActivity implements View.OnClickListener {

    private Button btn_signup;
    private Button btn_login;
    private EditText Seller_InputEmail, Seller_InputPassword;
    private TextView btn_notseller, btn_forgotps;


    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_log_in);

        mAuth = FirebaseAuth.getInstance();

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_notseller = (TextView) findViewById(R.id.btn_notseller);
        btn_notseller.setOnClickListener(this);

        btn_forgotps = (TextView) findViewById(R.id.btn_forgotps);
        btn_forgotps.setOnClickListener(this);


        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);

        Seller_InputEmail = (EditText) findViewById(R.id.Seller_InputEmail);
        Seller_InputPassword = (EditText) findViewById(R.id.Seller_InputPassword);
        progressBar = (ProgressBar) findViewById(R.id.progresBar);


    }

    @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_signup:
                    startActivity(new Intent(this, SellerRegister.class));
                    break;

                case R.id.btn_notseller:
                    startActivity(new Intent(this, MainActivity.class));
                    break;
                case R.id.btn_forgotps:
                    startActivity(new Intent(this, SellerForgotPassword.class));
                    break;

                case R.id.btn_login:
                    userLogin();
                    break;
        }
    }

        private void userLogin(){
            String email = Seller_InputEmail.getText().toString().trim();
            String password = Seller_InputPassword.getText().toString().trim();

            if(email.isEmpty()){
                Seller_InputEmail.setError("Email is required!");
                Seller_InputEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Seller_InputEmail.setError("Please enter a valid email!");
                Seller_InputEmail.requestFocus();
                return;
            }
            if(password.isEmpty()){
                Seller_InputPassword.setError("Password is required!");
                Seller_InputPassword.requestFocus();
                return;
            }
            if(password.length()<6){
                Seller_InputPassword.setError("Min password length is 6 characters!");
                Seller_InputPassword.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        FirebaseUser User_Seller = FirebaseAuth.getInstance().getCurrentUser();


                        if (User_Seller.isEmailVerified()) {
                            Intent intent = new Intent(SellerLogIn.this, SellerDashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            User_Seller.sendEmailVerification();
                            Toast.makeText(SellerLogIn.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }else {
                        Toast.makeText(SellerLogIn.this, "Failed to login! PLease check your credentials", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }


                }
            });
        }

}



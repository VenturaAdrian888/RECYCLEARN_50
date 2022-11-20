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

public class BuyerLogIn extends AppCompatActivity implements View.OnClickListener {

    private Button signup;
    private Button login;
    private EditText Buyer_InputEmail, Buyer_InputPassword;
    private TextView btn_notbuyer, btn_forgotps;


    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_log_in);

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.btn_loginb);
        login.setOnClickListener(this);

        btn_notbuyer = (TextView) findViewById(R.id.btn_notabuyer);
        btn_notbuyer.setOnClickListener(this);

        btn_forgotps = (TextView) findViewById(R.id.btn_forgotpsb);
        btn_forgotps.setOnClickListener(this);


        signup = (Button) findViewById(R.id.btn_signupb);
        signup.setOnClickListener(this);

        Buyer_InputEmail = (EditText) findViewById(R.id.Buyer_InputEmail);
        Buyer_InputPassword = (EditText) findViewById(R.id.Buyer_InputPassword);
        progressBar = (ProgressBar) findViewById(R.id.progresBar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signupb:
                startActivity(new Intent(this, BuyerRegister.class));
                break;

            case R.id.btn_notabuyer:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_forgotpsb:
                startActivity(new Intent(this, BuyerForgotPassword.class));

            case R.id.btn_loginb:
                userBuyerLogin();
                break;
        }
    }

    private void userBuyerLogin() {
        String email = Buyer_InputEmail.getText().toString().trim();
        String password = Buyer_InputPassword.getText().toString().trim();

        if(email.isEmpty()){
            Buyer_InputEmail.setError("Email is required!");
            Buyer_InputEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Buyer_InputEmail.setError("Please enter a valid email!");
            Buyer_InputEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            Buyer_InputPassword.setError("Password is required!");
            Buyer_InputPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            Buyer_InputPassword.setError("Min password length is 6 characters!");
            Buyer_InputPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    FirebaseUser User_Buyer = FirebaseAuth.getInstance().getCurrentUser();


                    if (User_Buyer.isEmailVerified()) {
                        Intent intent = new Intent(BuyerLogIn.this, BuyerDashboard.class);
                        startActivity(intent);
                        
                    } else {
                        User_Buyer.sendEmailVerification();
                        Toast.makeText(BuyerLogIn.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(BuyerLogIn.this, "Failed to login! PLease check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }


            }
        });
    }

}


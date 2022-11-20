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
import com.google.firebase.auth.FirebaseAuth;

public class BuyerForgotPassword extends AppCompatActivity {

    private TextView back_signin;
    private Button btn_resetFP;
    private ProgressBar progresBar;
    private EditText Edit_emaileditfp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_forgot_password);

        back_signin = (TextView) findViewById(R.id.back_signin);
        Edit_emaileditfp = (EditText) findViewById(R.id.Edit_emaileditfp);
        btn_resetFP = (Button) findViewById(R.id.btn_resetFP);
        progresBar = (ProgressBar) findViewById(R.id.progresBar);
        auth = FirebaseAuth.getInstance();

        btn_resetFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetbuyerfp();
            }
        });
    }
    private  void resetbuyerfp(){
        String email = Edit_emaileditfp.getText().toString().trim();
        if (email.isEmpty()){
            Edit_emaileditfp.setError("Email is required!");
            Edit_emaileditfp.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Edit_emaileditfp.setError("Please Provide valid email!");
            Edit_emaileditfp.requestFocus();
            return;
        }
        progresBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(BuyerForgotPassword.this, SellerForgotPassword2.class));
                    Toast.makeText(BuyerForgotPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(BuyerForgotPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
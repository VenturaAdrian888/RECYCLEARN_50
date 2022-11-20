package com.example.recyclearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SellerForgotPassword2 extends AppCompatActivity {


    private Button btn_fpreceived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_forgot_password2);


        btn_fpreceived = (Button) findViewById(R.id.btn_fprecieved);
        btn_fpreceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openreenter();
            }
        });

    }

    public void openreenter(){
        Intent intent = new Intent(this, SellerLogIn.class);
        startActivity(intent);
    }

}

package com.example.recyclearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    private Button btn_seller, btn_buyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btn_seller = (Button) findViewById(R.id.btn_seller);
        btn_buyer = (Button) findViewById(R.id.btn_buyer) ;
        btn_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openbuyer();
            }
        });

        btn_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openseller();
            }
        });
    }
        public void openseller(){
            Intent intent = new Intent(this, SellerLogIn.class);
            startActivity(intent);
    }
    public void openbuyer(){
        Intent intent = new Intent(this, BuyerLogIn.class);
        startActivity(intent);
    }

}
package com.shadigipay.shaprimevendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RedeemedActivity extends AppCompatActivity {
    Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeemed);

        doneBtn = findViewById(R.id.doneHBtnId);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(home);

            }
        });
    }
}
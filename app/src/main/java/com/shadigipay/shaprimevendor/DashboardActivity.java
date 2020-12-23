package com.shadigipay.shaprimevendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    Button redeemBtn, reportBtn, logoutBtn;
    String vendorId;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sp = getSharedPreferences("login", MODE_PRIVATE);
        vendorId = sp.getString("userId", null);

        redeemBtn = findViewById(R.id.redeemBtnId);
        reportBtn = findViewById(R.id.reportBtnId);
        logoutBtn = findViewById(R.id.logoutBtnId);


        redeemBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.redeemBtnId:
                Intent redeem = new Intent(getApplicationContext(), RedeemActivity.class);
                redeem.putExtra("Vendor Id", vendorId);
                startActivity(redeem);
                break;

            case R.id.reportBtnId:
                Intent report = new Intent(getApplicationContext(), ReportActivity.class);
                report.putExtra("Vendor Id", vendorId);
                startActivity(report);
                break;

            case R.id.logoutBtnId:
                Intent home = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(home);
                editor = sp.edit();
                editor.clear();
                editor.apply();
                vendorId = null;
                finish();


        }


    }
}
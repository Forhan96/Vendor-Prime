package com.shadigipay.shaprimevendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class RedeemActivity extends AppCompatActivity {

    public static final String OrderURL = "https://www.shaprime.com/api-order-data";

    Button submitBtn, doneBtn;
    EditText voucherNoET;
    String vendorIdE;
    ArrayList<Order> ordersList = new ArrayList<>();
    private String orderId, vendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        jsonOrder();

        voucherNoET = findViewById(R.id.voucherNoETId);
        submitBtn = findViewById(R.id.submitBtnId);
        doneBtn = findViewById(R.id.doneBtnId);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(home);

            }
        });

        Intent redeemIn = getIntent();
        vendorIdE = redeemIn.getStringExtra("Vendor Id");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findVoucher();

            }
        });
    }

    private int findVoucher() {
        String voucherNo = voucherNoET.getText().toString();

        int temp = 0;
        for (Order orderM : ordersList) {
            if (orderM.getVendorId().equals(vendorIdE)) {

                if (orderM.getOrderId().equals(voucherNo)) {
                    temp = 1;
                    break;
                }
            }

        }

        if (temp == 1) {
            Intent home = new Intent(getApplicationContext(), RedeemedActivity.class);
            startActivity(home);
        } else {
            Toast.makeText(getApplicationContext(), "Voucher Incorrect", Toast.LENGTH_SHORT).show();
        }


        return temp;
    }

    private void Submit()
    {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OrderURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONArray postArray = new JSONArray();

                for(int i = 0; i < 3; i++) {
                    // 1st object
                    JSONObject list1 = new JSONObject();
                    list1.put("val1",i+1);
                    list1.put("val2",i+2);
                    list1.put("val3",i+3);
                    postArray.add(list1);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);
    }

    private void jsonOrder() {
        RequestQueue queueOrder = Volley.newRequestQueue(this);
        StringRequest stringRequestOrder = new StringRequest(Request.Method.GET, OrderURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONParser parserOrder = new JSONParser();
                            JSONArray jsonArrayOrder = (JSONArray) parserOrder.parse(response);
                            System.out.println(jsonArrayOrder);

                            for (int i = 0; i < jsonArrayOrder.size(); i++) {
                                JSONObject order = (JSONObject) jsonArrayOrder.get(i);
                                orderId = order.get("order_id").toString();
                                vendorId = order.get("vendor_Id").toString();

                                ordersList.add(new Order(orderId, vendorId));
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Todo
            }
        });
        queueOrder.add(stringRequestOrder);
    }
}
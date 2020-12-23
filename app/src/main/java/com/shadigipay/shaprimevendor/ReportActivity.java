package com.shadigipay.shaprimevendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    public static final String OrderURL = "https://www.shaprime.com/api-order-data";
    TextView orderNoTV, totalAmountTv, commissionTV;
    Button doneBtn;

    int totalAmount = 0, totalOrder = 0, totalCommission = 0;
    ArrayList<OrderData> ordersData = new ArrayList<>();
    private String vendorIdE, vendorId, orderId, price, commission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        orderNoTV = findViewById(R.id.orderNumberTVId);
        totalAmountTv = findViewById(R.id.totalAmountTVId);
        commissionTV = findViewById(R.id.commissionTVId);
        doneBtn = findViewById(R.id.rDoneBtnId);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(home);

            }
        });

        Intent reportIn = getIntent();
        vendorIdE = reportIn.getStringExtra("Vendor Id");

        jsonOrderData();
    }


    private void jsonOrderData() {
        RequestQueue queueOrderData = Volley.newRequestQueue(this);
        StringRequest stringRequestOrderData = new StringRequest(Request.Method.GET, OrderURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONParser parserOrderData = new JSONParser();
                            JSONArray jsonArrayOrderData = (JSONArray) parserOrderData.parse(response);
                            System.out.println(jsonArrayOrderData);

                            for (int i = 0; i < jsonArrayOrderData.size(); i++) {
                                JSONObject orderData = (JSONObject) jsonArrayOrderData.get(i);
                                vendorId = orderData.get("vendor_Id").toString();
                                orderId = orderData.get("order_id").toString();
                                price = orderData.get("price").toString();
                                commission = orderData.get("company_comission").toString();

                                ordersData.add(new OrderData(vendorId, orderId, price, commission));


                            }


                            int totalOrderL = 0;
                            int totalAmountL = 0;
                            int totalCommissionL = 0;

                            for (OrderData orderR : ordersData) {

                                if (orderR.getVendorId().equals(vendorIdE)) {

                                    totalOrderL = totalOrderL + 1;

                                    int priceInt = Integer.parseInt(orderR.getPrice());
                                    totalAmountL = totalAmountL + priceInt;


                                    int commissionInt = Integer.parseInt(orderR.getCommission());
                                    totalCommissionL = totalCommissionL + commissionInt;
                                }

                                totalOrder = totalOrderL;
                                totalAmount = totalAmountL;
                                totalCommission = totalCommissionL;

                                orderNoTV.setText(String.valueOf(totalOrder));
                                totalAmountTv.setText(String.valueOf(totalAmount));
                                commissionTV.setText(String.valueOf(totalCommission));

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
        queueOrderData.add(stringRequestOrderData);
    }
}
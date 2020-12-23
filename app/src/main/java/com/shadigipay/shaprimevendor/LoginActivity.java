package com.shadigipay.shaprimevendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    public static final String URL = "https://www.shaprime.com/api-vendor-register";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    List<User> listOfUsers = new ArrayList<>();
    private EditText userNameET, passwordET;
    private Button signInBtn;
    private String username;
    private String password;
    private String userId;

    public static final String getmd5ofstring(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString().toUpperCase(Locale.US); // return md5

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameET = (EditText) findViewById(R.id.userNameETId);
        passwordET = (EditText) findViewById(R.id.passwordETId);
        signInBtn = (Button) findViewById(R.id.signInBtnId);

        jsonParse();
        sp = getSharedPreferences("login", MODE_PRIVATE);

        if (sp.getBoolean("logged", false)) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String username = userNameET.getText().toString();
        String pass = passwordET.getText().toString();
        String password = getmd5ofstring(pass).toUpperCase(Locale.US); // get md5

        if (username.isEmpty()) {
            userNameET.setError("Username Required");
            userNameET.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            passwordET.setError("Password Required");
            passwordET.requestFocus();
            return;
        }


        for (User user : listOfUsers) {
            if (user.getUsername().equals(username)) {

                if (user.getPassword().equals(password)) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    editor = sp.edit();
                    editor.putBoolean("logged", true).apply();
                    editor.putString("userId", user.getUserId()).apply();

                    break;

                } else {
                    Toast.makeText(LoginActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void jsonParse() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONParser parser = new JSONParser();
                            JSONArray jsonArray = (JSONArray) parser.parse(response);
                            System.out.println(jsonArray);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject first = (JSONObject) jsonArray.get(i);
                                username = first.get("username").toString();
                                String passwordP = first.get("password").toString();
                                password = (passwordP).toUpperCase(Locale.US);
                                userId = first.get("user_id").toString();
                                Log.i("Users", username);

                                listOfUsers.add(new User(username, password, userId));
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
        queue.add(stringRequest);
    }


}


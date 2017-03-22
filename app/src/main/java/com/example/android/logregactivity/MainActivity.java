package com.example.android.logregactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button btnLinkToRegister;
    private Button btnLogin;
    private EditText inputUsername;
    private EditText inputPassword;
    AlertDialog.Builder builder;
    String reg_url = "http://localhost/login.php";//URL for interacting with server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        builder = new AlertDialog.Builder(MainActivity.this);
        inputPassword = (EditText) findViewById(R.id.password);
        inputUsername = (EditText) findViewById(R.id.username);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = inputUsername.getText().toString();
                final String password = inputPassword.getText().toString();
                if (password.equals("") || username.equals("")) {
                    builder.setTitle("Something Went Wrong....");
                    displayAlert("Enter Valid Username And Password....");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        if(code.equals("Login_failed")){
                                            builder.setTitle("Login Error...");
                                            displayAlert(jsonObject.getString("message"));
                                        }
                                        else{
                                            Intent intent = new Intent(MainActivity.this,activity_login_success.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name",jsonObject.getString("name"));
                                            bundle.putString("email",jsonObject.getString("email"));
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_name", username);
                            params.put("password", password);
                            return params;
                        }
                    };
                    MySingleton.getInstance(MainActivity.this).addToRequestque(stringRequest);
                }
            }
    });
}

    public void displayAlert(final String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    inputPassword.setText("");
                    inputUsername.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

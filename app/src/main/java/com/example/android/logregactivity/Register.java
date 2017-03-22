package com.example.android.logregactivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private EditText inputUsername;
    AlertDialog.Builder builder;
    String reg_url = "http://192.168.2.3/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConfirmPassword = (EditText) findViewById(R.id.re_password);
        inputUsername = (EditText) findViewById(R.id.username);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        builder = new AlertDialog.Builder(Register.this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = inputFullName.getText().toString();
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String username = inputUsername.getText().toString();
                final String pass = inputConfirmPassword.getText().toString();
                if (name.equals("") || email.equals("") || password.equals("") || username.equals("") || pass.equals("")) {
                    builder.setTitle("Something Went Wrong....");
                    builder.setMessage("Please fill all the details.....");
                    displayAlert("input_error");
                } else if (!password.equals(pass)) {
                    builder.setTitle("Something Went Wrong....");
                    builder.setMessage("Your passwords are not matching...");
                    displayAlert("input_error");

                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");
                                        Log.v("EditText",message);
                                        builder.setTitle("Server Response....");
                                        builder.setMessage(message);
                                        displayAlert(code);
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
                            params.put("name", name);
                            params.put("email", email);
                            params.put("user_name", username);
                            params.put("password", password);
                            Gson gson = new Gson();
                            String jsonFromParam = gson.toJson(params);
                            Log.v("EditText",jsonFromParam);
                            return params;
                        }
                    };
                    MySingleton.getInstance(Register.this).addToRequestque(stringRequest);
                }
            }
        });
    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    inputPassword.setText("");
                    inputConfirmPassword.setText("");
                } else if (code.equals("reg_success")) {
                    finish();
                } else if (code.equals("reg_failed")) {
                    inputFullName.setText("");
                    inputUsername.setText("");
                    inputPassword.setText("");
                    inputConfirmPassword.setText("");
                    inputEmail.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

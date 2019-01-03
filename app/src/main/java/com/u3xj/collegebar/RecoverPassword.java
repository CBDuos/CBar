package com.u3xj.collegebar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecoverPassword extends AppCompatActivity {

    Button btnChange;
    EditText password,confirmPassword;

    private ProgressDialog progressDialog;

    String strUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, UserScreen.class));
            return;
        }

        progressDialog = new ProgressDialog(this);

        btnChange = (Button) findViewById(R.id.btnChange);
        password = (EditText) findViewById(R.id.editTextPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);


        Bundle bundle = getIntent().getExtras();

        strUsername = bundle.getString("username"," ");


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationPassword())
                {
                    registerUser();
                }
            }
        });
    }

    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z 0-9]*$";
        return s.matches(pattern);
    }

    public boolean validationPassword()
    {
        String pass = password.getText().toString();
        String passConfirm = confirmPassword.getText().toString();

        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_LONG).show();
            return false;
        }

        if (pass.length() < 6){
            Toast.makeText(getApplicationContext(),"Password must be greater than 6 digit",Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isAlphaNumeric(pass))
        {
            Toast.makeText(getApplicationContext(),"Password can only be Alphabets or Number without any space",Toast.LENGTH_LONG).show();
            return false;
        }

        if (passConfirm.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please enter your Confirm Password",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!pass.equals(passConfirm))
        {
            Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    } private void registerUser()
    {

        progressDialog.setMessage("Processing...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_CHANGE_PASSWORD,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",strUsername);
                params.put("password",password.getText().toString());
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }



}

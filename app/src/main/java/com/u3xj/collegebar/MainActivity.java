package com.u3xj.collegebar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private EditText editTextPhoneNumber, editTextPassword;
    private Button login, signup;
    FloatingActionButton btnFB,btnShare;
    private ProgressDialog progressDialog;

    private TextView textViewForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, UserScreen.class));
            return;
        }


        editTextPhoneNumber = (EditText) findViewById(R.id.phone);
        editTextPassword = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);
        btnFB = (FloatingActionButton) findViewById(R.id.buttonFB);
        btnShare = (FloatingActionButton) findViewById(R.id.buttonShare);

        textViewForget = (TextView) findViewById(R.id.textViewForget);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity2();
            }
        });

        login.setOnClickListener(this);

        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = openFacebook(MainActivity.this);
                startActivity(facebookIntent);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "bit.ly/dsbcollegebarv1");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));*/
            }
        });

        textViewForget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),VerifyNumber.class);
                startActivity(intent);
            }
        });

    }

    public static Intent openFacebook(Context context)
    {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana",0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/862155723979862"));
        }catch (Exception e){
            return  new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/thecollegebar"));
        }
    }


    public void openActivity2()
    {
        Intent intent = new Intent(MainActivity.this,Registration.class);
        startActivity(intent);
    }

    private void userLogin()
    {
        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error"))
                            {
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(obj.getInt("user_id"),obj.getString("username"),obj.getString("nick_name")
                                        ,obj.getString("collegename"),obj.getString("gender"),obj.getString("dob"),obj.getString("profile_name"));
                                startActivity(new Intent(getApplicationContext(),UserScreen.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",phoneNumber);
                params.put("password",password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == login)
            userLogin();
    }
}

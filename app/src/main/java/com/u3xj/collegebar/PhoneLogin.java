package com.u3xj.collegebar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PhoneLogin extends AppCompatActivity {

    private static final String TAG = "PhoneLogin";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    TextView t2,reSend;
    EditText e2;
    Button b2;

    String name, phone, password, gender, collegeName, date;

    private ProgressDialog progressDialog,progressDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, UserScreen.class));
            return;
        }

        t2 = (TextView)findViewById(R.id.textViewVerified);
        e2 = (EditText) findViewById(R.id.OTPeditText);
        b2 = (Button)findViewById(R.id.OTPVERIFY);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user...");
        progressDialog.setCancelable(false);

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Re-Sending OTP...");
        progressDialog2.setCancelable(false);

        reSend = (TextView)findViewById(R.id.reSend);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                progressDialog.show();
                Toast.makeText(PhoneLogin.this,"Verification Complete",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Log.w(TAG, "onVerificationFailed", e);
                t2.setText("Click on Re-Send");
                progressDialog2.dismiss();
                Toast.makeText(PhoneLogin.this,"Verification Failed, please click on Re-Send",Toast.LENGTH_LONG).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(PhoneLogin.this,"InValid Phone Number",Toast.LENGTH_LONG).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                progressDialog2.dismiss();
                Toast.makeText(PhoneLogin.this,"Verification code has been send on your number",Toast.LENGTH_SHORT).show();
                t2.setText("OTP has been sent to you on your mobile phone\nPlease enter it below");
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        Bundle bundle = getIntent().getExtras();

        name = bundle.getString("name"," ");
        phone = bundle.getString("phone"," ");
        password = bundle.getString("password"," ");
        gender = bundle.getString("gender"," ");
        collegeName = bundle.getString("collegeName","");
        date = bundle.getString("date","");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                PhoneLogin.this,
                mCallbacks);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e2.getText().length() == 6)
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, e2.getText().toString());
                    // [END verify_with_code]
                    signInWithPhoneAuthCredential(credential);
                    progressDialog.show();
                }
            }
        });

        reSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog2.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+phone,
                        60,
                        java.util.concurrent.TimeUnit.SECONDS,
                        PhoneLogin.this,
                        mCallbacks);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerUser();
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progressDialog.dismiss();
                                Toast.makeText(PhoneLogin.this,"Invalid Verification",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private void registerUser()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_REGISTER,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(obj.getInt("user_id"),obj.getString("username"),obj.getString("nick_name")
                                    ,obj.getString("collegename"),obj.getString("gender"),obj.getString("dob"),"default");
                            Intent intent = new Intent(getApplicationContext(),UserScreen.class);
                            startActivity(intent);
                            progressDialog.dismiss();
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
                params.put("nick_name",name);
                params.put("username",phone);
                params.put("collegename",collegeName);
                params.put("gender",gender);
                params.put("dob",date);
                params.put("password",password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

}
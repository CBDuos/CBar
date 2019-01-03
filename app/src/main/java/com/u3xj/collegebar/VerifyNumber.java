package com.u3xj.collegebar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class VerifyNumber extends AppCompatActivity {

    private static final String TAG = "PhoneLogin";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    TextView t1,t2;
    ImageView i1;
    EditText e1,e2;
    Button b1,b2;

    TextView textView91;
    ProgressDialog progressDialog,progressDialog2;
    String flagUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);
        e1 = (EditText) findViewById(R.id.Phonenoedittext);
        b1 = (Button) findViewById(R.id.PhoneVerify);
        t1 = (TextView)findViewById(R.id.textView2Phone);
        i1 = (ImageView)findViewById(R.id.imageView2Phone);
        e2 = (EditText) findViewById(R.id.OTPeditText);
        b2 = (Button)findViewById(R.id.OTPVERIFY);
        t2 = (TextView)findViewById(R.id.textViewVerified);

        textView91 = (TextView) findViewById(R.id.textView91);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying...");
        progressDialog.setCancelable(false);

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("OTP Verifying");
        progressDialog2.setCancelable(false);

        if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, UserScreen.class));
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                Toast.makeText(VerifyNumber.this,"Verification Complete",Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Log.w(TAG, "onVerificationFailed", e);
                progressDialog.dismiss();
                Toast.makeText(VerifyNumber.this,"Verification Failed. Click Again",Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(VerifyNumber.this,"InValid Phone Number",Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                progressDialog.dismiss();
                Toast.makeText(VerifyNumber.this,"Verification code has been send on your number",Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                e1.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                i1.setVisibility(View.GONE);
                textView91.setVisibility(View.GONE);
                t2.setVisibility(View.VISIBLE);
                e2.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                // ...
            }
        };

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberExist();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog2.show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, e2.getText().toString());
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "signInWithCredential:success");
                            progressDialog2.dismiss();
                            Intent intent = new Intent(getApplicationContext(),RecoverPassword.class);
                            intent.putExtra("username",e1.getText().toString());
                            startActivity(intent);
                            finishAffinity();
                            Toast.makeText(VerifyNumber.this,"Verification Done",Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progressDialog2.dismiss();
                                Toast.makeText(VerifyNumber.this,"Invalid Verification",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void numberExist()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL_USERNAME_EXIST+"?username="+e1.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject studentObject = new JSONObject(response);
                            flagUsername = studentObject.getString("error");

                            if (flagUsername == "true")
                            {
                                Toast.makeText(getApplicationContext(),"This number is not belongs to any account",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                progressDialog.show();
                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        "+91"+e1.getText().toString(),
                                        60,
                                        java.util.concurrent.TimeUnit.SECONDS,
                                        VerifyNumber.this,
                                        mCallbacks);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}

package com.u3xj.collegebar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DiscretePathEffect;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccountSetting extends AppCompatActivity {

    TextView textViewName, textViewPhone, textViewCollege, textViewGender, textViewDOB, textViewPass;
    Dialog mDialog;

    EditText editTextPassword, editTextPasswordConfirm;

    String strUsername;

    private FloatingActionButton buttonProfile;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap, reBitmap;
    private ImageView imageViewProfile;
    private ProgressDialog progressDialog, progressDialog1;

    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        mp = MediaPlayer.create(getApplication(), R.raw.clicksound);

        progressDialog1 = new ProgressDialog(this);

        buttonProfile = (FloatingActionButton) findViewById(R.id.buttonProfile);
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        if (SharedPrefManager.getInstance(getApplication()).getProfile().equals("default"))
        {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.defaultprofile);
            imageViewProfile.setImageBitmap(bm);
        }
        else
        {
            byte[] decodedString = Base64.decode(SharedPrefManager.getInstance(getApplication()).getProfile(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewProfile.setImageBitmap(decodedByte);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDialog = new Dialog(this);

        textViewName = (TextView) findViewById(R.id.name);
        textViewPhone = (TextView) findViewById(R.id.phone);
        textViewCollege = (TextView) findViewById(R.id.college);
        textViewGender = (TextView) findViewById(R.id.gender);
        textViewDOB = (TextView) findViewById(R.id.dob);
        textViewPass = (TextView) findViewById(R.id.pass);

        textViewName.setText(SharedPrefManager.getInstance(getApplication()).getNickName());
        textViewPhone.setText("+91 "+SharedPrefManager.getInstance(getApplication()).getUserName());
        textViewCollege.setText(SharedPrefManager.getInstance(getApplication()).getCollegeName());
        textViewGender.setText(SharedPrefManager.getInstance(getApplication()).getGender());
        textViewDOB.setText(SharedPrefManager.getInstance(getApplication()).getDOB());

        strUsername = SharedPrefManager.getInstance(getApplication()).getUserName();

        textViewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });


        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            pickImage();

            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("photo", SharedPrefManager.getInstance(getApplication()).getProfile());
                Intent intent = new Intent(getApplicationContext(),ShowPhoto.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    private void croprequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true).setAspectRatio(4,4)
                .start(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            croprequest(imageUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    reBitmap = Bitmap.createScaledBitmap(
                            bitmap, 720, 720, false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                progressDialog.show();
                uploadImage(data);
            }
        }
    }

    private void uploadImage(final Intent data)
    {
        final String user_id = Integer.toString(SharedPrefManager.getInstance(getApplication()).getUserID());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.URL_UPLOAD_IMAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            SharedPrefManager.getInstance(getApplication()).storeProfile(imageToString(reBitmap));
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        imageViewProfile.setImageBitmap(reBitmap);
                        imageViewProfile.setVisibility(View.VISIBLE);
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
                params.put("user_id",user_id);
                params.put("image",imageToString(reBitmap));
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }

    public void showPopUp()
    {
        if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
            mp.start();

        TextView txtclose;
        Button btnChange;
        mDialog.setContentView(R.layout.change_password_dialog);

        txtclose =(TextView) mDialog.findViewById(R.id.txtclose);
        btnChange = (Button) mDialog.findViewById(R.id.btnChange);

        editTextPassword = (EditText) mDialog.findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = (EditText) mDialog.findViewById(R.id.editTextConfirmPassword);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                    mp.start();
                mDialog.dismiss();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                    mp.start();

                String pass = editTextPassword.getText().toString();
                String passConfirm = editTextPasswordConfirm.getText().toString();
                if (validationPassword(pass,passConfirm))
                {
                    registerUser();
                }
            }
        });

        mDialog.setCancelable(false);
        mDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed()
    {
        this.startActivity(new Intent(getApplication(),UserScreen.class));
        this.finishAffinity();
        return;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(),UserScreen.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }


    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z 0-9]*$";
        return s.matches(pattern);
    }

    public boolean validationPassword(String pass, String passConfirm)
    {
        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.length() < 6){
            Toast.makeText(getApplicationContext(),"Password must be greater than 6 digit",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isAlphaNumeric(pass))
        {
            Toast.makeText(getApplicationContext(),"Password can only be Alphabets or Number without any space",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (passConfirm.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please enter your Confirm Password",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pass.equals(passConfirm))
        {
            Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser()
    {
        progressDialog1.setMessage("Processing...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_CHANGE_PASSWORD,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog1.hide();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",strUsername);
                params.put("password",editTextPassword.getText().toString());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}

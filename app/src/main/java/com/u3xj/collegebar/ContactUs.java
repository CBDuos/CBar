package com.u3xj.collegebar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {


    Button btnSend;
    FloatingActionButton btnFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSend = (Button)findViewById(R.id.button) ;
        btnFB = (FloatingActionButton) findViewById(R.id.buttonFB);


        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = openFacebook(ContactUs.this);
                startActivity(facebookIntent);
            }
        });

        btnSend.setOnClickListener(this);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed()
    {

        this.startActivity(new Intent(getApplication(),UserScreen.class));
        this.finishAffinity();
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(),UserScreen.class);
        finishAffinity();
        startActivityForResult(myIntent, 0);

        return true;

    }

    @Override
    public void onClick(View view)
    {
    }
}

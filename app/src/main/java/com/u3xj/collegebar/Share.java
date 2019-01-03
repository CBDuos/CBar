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

public class Share extends AppCompatActivity {

    FloatingActionButton btnFB,btnShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnFB = (FloatingActionButton) findViewById(R.id.buttonFB);
        btnShare = (FloatingActionButton) findViewById(R.id.buttonShare);


        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = openFacebook(Share.this);
                startActivity(facebookIntent);
            }
        });


        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "bit.ly/dsbcollegebarv1");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
}

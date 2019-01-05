package com.u3xj.collegebar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
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

public class UserScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btn;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mToggle;

    SwitchCompat switcher, switcherSound;

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    StudentsFragment studentsFragment;
    MessagesFragment messagesFragment;
    HelpFragment helpFragment;

    MediaPlayer mp;

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        userId =  Integer.toString(SharedPrefManager.getInstance(getApplication()).getUserID());


        mp = MediaPlayer.create(getApplication(), R.raw.clicksound);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        frameLayout = (FrameLayout) findViewById(R.id.mainFrame);

        studentsFragment = new StudentsFragment();
        messagesFragment = new MessagesFragment();
        helpFragment = new HelpFragment();

        setFragment(studentsFragment);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView imageViewProfile = (ImageView) headerView.findViewById(R.id.imageViewProfile);
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
        TextView textViewName = (TextView) headerView.findViewById(R.id.textViewName);
        textViewName.setText(SharedPrefManager.getInstance(getApplication()).getNickName());
        TextView textViewPhone = (TextView) headerView.findViewById(R.id.textViewPhone);
        textViewPhone.setText(SharedPrefManager.getInstance(getApplication()).getUserName());

        if (navigationView != null)
        {
            navigationView.setNavigationItemSelectedListener(this);
        }

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nightMode);
        View actionView = MenuItemCompat.getActionView(menuItem);

        switcher = (SwitchCompat) actionView.findViewById(R.id.nightModeSwitcher);
        switcher.setChecked(false);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcher.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });

        MenuItem menuItem1 = menu.findItem(R.id.sound);
        View actionView1 = MenuItemCompat.getActionView(menuItem1);
        switcherSound = (SwitchCompat) actionView1.findViewById(R.id.soundSwitcher);
        switcherSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcherSound.isChecked()) {
                    Toast.makeText(getApplication(),"Sound Off",Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplication()).storeSound("1");
                }
                else {
                    Toast.makeText(getApplication(),"Sound On",Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplication()).storeSound("0");
                }
            }
        });

        if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("1"))
            switcherSound.setChecked(true);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            switcher.setChecked(true);
        }

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("photo", SharedPrefManager.getInstance(getApplication()).getProfile());
                Intent intent = new Intent(getApplicationContext(),ShowPhoto.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.nav_students :
                        setFragment(studentsFragment);
                        if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                            mp.start();
                        return true;
                    case R.id.nav_messages :
                        setFragment(messagesFragment);
                        if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                            mp.start();;
                        return true;
                    case R.id.nav_help :
                        setFragment(helpFragment);
                        if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                            mp.start();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void restartApp(){
        Intent intent = new Intent(getApplicationContext(),UserScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.accoutSetting) {
            if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                mp.start();
            Intent intent = new Intent(getApplicationContext(),AccountSetting.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.logout){
            if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                mp.start();
            showAlertDialog();
        }
        return false;
    }

    private void setFragment(Fragment fragment)
    {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();
    }

    public void showAlertDialog()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Log Out");
        alert.setMessage("Do you want to logout?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                    mp.start();
                logOut();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (SharedPrefManager.getInstance(getApplication()).getStoreSound().equals("0"))
                    mp.start();
                alert.create().dismiss();
            }
        });


        alert.create().show();
    }

    public void logOut()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_OFFLINE_STATUS,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(String response) {
                            SharedPrefManager.getInstance(getApplication()).logout();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplication(), "Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("user_id",userId);
                    return params;
                }
            };

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}

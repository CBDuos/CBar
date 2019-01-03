package com.u3xj.collegebar;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText username, name, password, confirmpassword;
    private TextView errorUsername, errorName, errorPassword, errorConfirmPassword,textViewPolicy;
    private Button btnCreate;

    private  String strUserName, strName, strPassword, strConfirmPassword, strGender;

    private CheckBox checkBox;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, UserScreen.class));
            return;
        }

        username = (EditText) findViewById(R.id.phone);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        textViewPolicy = (TextView) findViewById(R.id.textViewPolicy);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        btnCreate = (Button) findViewById(R.id.create);

        errorUsername = (TextView) findViewById(R.id.errorPhone);
        errorName = (TextView) findViewById(R.id.errorName);
        errorPassword = (TextView) findViewById(R.id.errorPassword);
        errorConfirmPassword = (TextView) findViewById(R.id.errorConfirmPassword);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        btnCreate.setOnClickListener(this);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus)
            {
                checkUsername();
            }
        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus)
            {
                checkName();
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus)
            {
                checkPassword();
            }
        });
        confirmpassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus)
            {
                checkConfirmPassword();
            }
        });

        textViewPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Policy.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onClick(View view)
    {
        if (view == btnCreate)
        {
            if (validationUser())
            {
                strName = name.getText().toString().trim();
                strUserName = username.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                intent.putExtra("name",strName);
                intent.putExtra("phone",strUserName);
                intent.putExtra("password",strPassword);
                intent.putExtra("gender",radioButton.getText());
                startActivity(intent);
            }
        }
    }

    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z 0-9]*$";
        return s.matches(pattern);
    }


    public boolean validationUser()
    {
        strUserName = username.getText().toString().trim();
        strName = name.getText().toString().trim();
        strPassword = password.getText().toString().trim();
        strConfirmPassword = confirmpassword.getText().toString().trim();

        if (strName.isEmpty()) {
            errorName.setText("Please enter your Name");
            return false;
        }

        if (!isAlphaNumeric(strName)){
            errorName.setText("Name can only be contain Aplhabets");
            return false;
        }

        if (strName.length() < 2){
            errorName.setText("Name must be equal to or greater than 2 letters");
            return false;
        }

        if (strUserName.isEmpty()) {
            errorUsername.setText("Please enter your Phone Number");
            return false;
        }

        if (strUserName.length() != 10 )
        {
            errorUsername.setText("Phone Number must be equal to 10 digit");
            return false;
        }



        if (strPassword.isEmpty()) {
            errorPassword.setText("Please enter your Password");
            return false;
        }

        if (strPassword.length() < 6){
            errorPassword.setText("Password must be greater than 6 digit");
            return false;
        }

        if (!isAlphaNumeric(strPassword))
        {
            errorPassword.setText("Password can only be Alphabets or Number without any space");
            return false;
        }

        if (strConfirmPassword.isEmpty())
        {
            errorConfirmPassword.setText("Please enter your Confirm Password");
            return false;
        }
        if (!strPassword.equals(strConfirmPassword))
        {
            errorConfirmPassword.setText("Password doesn't match");
            return false;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplication(),"Please select Gender",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkBox.isChecked())
        {
            return false;
        }
        return true;
    }

    public void checkUsername()
    {
        if(!username.getText().toString().isEmpty())
            errorUsername.setText("");
    }

    public void checkName()
    {
        if (!name.getText().toString().isEmpty())
            errorName.setText("");
    }

    public void checkPassword()
    {
        if (!password.getText().toString().isEmpty())
            errorPassword.setText("");
    }

    public void checkConfirmPassword()
    {
        if (!confirmpassword.getText().toString().isEmpty())
            errorConfirmPassword.setText("");
    }

}

package com.u3xj.collegebar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.Calendar;

public class EditProfile extends AppCompatActivity {

    String name, phone, password, gender, collegeName="null", date="null";

    ListView listView = null;

    TextView textViewCollegeName;

    AlertDialog dialog;
    AlertDialog.Builder builder;

    private TextView mDisplayDate;


    Button btnSignUp;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        listView = new ListView(this);
        textViewCollegeName = (TextView) findViewById(R.id.textViewCollegName);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        final String[] college = {"DSB Campus, Nainital","MBPG College, Haldwani", "PNG Govt. PG College, Ramnagar", "SSJ Campus, Almoda",
                            "Govt. PG College, Pithoragarh", "DMS Campus, Bhimtal", "Govt. PG College, Bageshwar", "Govt. PG College College, Lohaghat",
                            "RHG Govt. PG College, Kashipur", "Govt. PG College, Rudrapur", "Govt. PG College, Khatima",
                            "Govt PG College, Bazpur"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item_college_name, R.id.txtitem,college);
        listView.setAdapter(adapter);

        builder = new  AlertDialog.Builder(EditProfile.this);
        builder.setCancelable(true);
        builder.setPositiveButton("OK",null);
        builder.setView(listView);
        dialog = builder.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg=(ViewGroup)view;
                TextView txt=(TextView)vg.findViewById(R.id.txtitem);
                textViewCollegeName.setText(txt.getText().toString());
                collegeName = txt.getText().toString();
                dialog.dismiss();
                }});

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationUser())
                {
                    Intent intent = new Intent(getApplicationContext(),PhoneLogin.class);
                    intent.putExtra("name",name);
                    intent.putExtra("phone",phone);
                    intent.putExtra("password",password);
                    intent.putExtra("gender",gender);
                    intent.putExtra("collegeName",collegeName);
                    intent.putExtra("date",date);
                    startActivity(intent);
                }
            }
        });

        mDisplayDate = (TextView) findViewById(R.id.tvDate);


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = day + "/" + month  + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name"," ");
        phone = bundle.getString("phone"," ");
        password = bundle.getString("password"," ");
        gender = bundle.getString("gender","");
    }

    public void showDialogListView(View view){
        dialog.show();
    }

    public void showDateOfBirth(View view)
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                EditProfile.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    public boolean validationUser()
    {
        if (collegeName.equals("null"))
        {
            Toast.makeText(getApplication(),"Please select your college",Toast.LENGTH_LONG).show();
            return false;
        }
        if (date.equals("null"))
        {
            Toast.makeText(getApplication(),"Please select your DOB",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

package com.dwakenya.techsavanna.newdwa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.dwakenya.techsavanna.newdwa.app.AppConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CandidateActivity extends AppCompatActivity implements View.OnClickListener {



    EditText _cityText;
    EditText _stateText;
    EditText _bestTimeToCallText;
    EditText _currentEmployerText;
    EditText _currentPayText;
    EditText _desiredPayText;
    EditText _lengthText;
    EditText _reasonText;
    EditText _refereeText;
    Button _btnProceed;
    private CheckBox chkRelocate;


    private EditText fromDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String can_relocate = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        _cityText = findViewById(R.id.input_city);
        _stateText = findViewById(R.id.input_state);
        _bestTimeToCallText = findViewById(R.id.input_best_time_to_call);
        _currentEmployerText = findViewById(R.id.input_current_employer);
        _currentPayText = findViewById(R.id.input_current_pay);
        _desiredPayText = findViewById(R.id.input_desired_pay);
        _lengthText = findViewById(R.id.input_length);
        _reasonText = findViewById(R.id.input_reason);
        _refereeText = findViewById(R.id.input_referee);
        _btnProceed = findViewById(R.id.btn_proceedTo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration Step 3/4");


        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        _btnProceed.setOnClickListener(this);

        chkRelocate = (CheckBox) findViewById(R.id.chkRelocate);

        chkRelocate.setOnClickListener(this);

        findViewsById();
        setDateTimeField();
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);


    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.registration_menu, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:

                Intent i= new Intent(this, RegisterStep2Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

                break;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        return true;


    }

    @Override
    public void onClick(View view) {

        if (view == fromDateEtxt) {
            fromDatePickerDialog.show();
        }


        if (view.equals(_btnProceed)) {

            //        //is chkRelocate checked?
            if (chkRelocate.isChecked()) {
                can_relocate = "1";
            } else if (!chkRelocate.isChecked()) {
                can_relocate = "0";
            }


            if (_cityText.getText().toString().equals("") || _stateText.getText().toString().equals("") || fromDateEtxt.getText().toString().equals("") ||
                    _refereeText.getText().toString().equals("")) {


                Toast.makeText(getApplicationContext(), "Please Fill out Required Fields", Toast.LENGTH_LONG).show();

            } else {

                AppConfig.preferences = getSharedPreferences(AppConfig.prefString, MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConfig.preferences.edit();


                editor.putString(AppConfig.CITY, _cityText.getText().toString());
                editor.putString(AppConfig.STATE, _stateText.getText().toString());
                editor.putString(AppConfig.TIME_TO_CALL, _bestTimeToCallText.getText().toString());
                editor.putString(AppConfig.AVAILABLE_FROM, fromDateEtxt.getText().toString());
                editor.putString(AppConfig.CAN_RELOCATE, can_relocate);
                editor.putString(AppConfig.CURRENT_EMPLOYER, _currentEmployerText.getText().toString());
                editor.putString(AppConfig.CURRENT_PAY, _currentPayText.getText().toString());
                editor.putString(AppConfig.DESIRED_PAY, _desiredPayText.getText().toString());

                editor.putString(AppConfig.LENGTH, _lengthText.getText().toString());
                editor.putString(AppConfig.REASON, _reasonText.getText().toString());
                editor.putString(AppConfig.REFEREE, _refereeText.getText().toString());
                editor.apply();

                Intent i = new Intent(CandidateActivity.this, ImageCompressedActivity.class);
                startActivity(i);


            }

        }

    }
}

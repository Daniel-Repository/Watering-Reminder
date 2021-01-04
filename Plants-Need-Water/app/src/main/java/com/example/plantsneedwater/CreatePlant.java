package com.example.plantsneedwater;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.graphics.Bitmap;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreatePlant extends AppCompatActivity {

    DatePickerDialog pickerDate;
    ImageView ivPhoto;
    Calendar cldr = Calendar.getInstance();
    TextInputEditText etPlantName;
    EditText etPeriod;
    EditText etLastWateredDate;
    Bitmap imageBitmap;
    Spinner spinPeriod;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plant);

        etPlantName = findViewById(R.id.etPlantName);
        etPeriod = findViewById(R.id.etPeriod);
        etLastWateredDate = findViewById(R.id.etLastWateredDate);

        //USER SELECTS CAMERA AREA TO ADD NEW PHOTO
        ivPhoto = findViewById(R.id.ivPhoto);
        LinearLayout llCameraField = findViewById(R.id.llCameraField);

        llCameraField.setOnClickListener(v -> {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResultLauncher.launch(takePhotoIntent);
        });

        //Populate spinPeriod with values from our string array 'period_array'
        spinPeriod = findViewById(R.id.spinPeriod);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.period_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPeriod.setAdapter(adapter);

        //Open date picker dialog (Calendar) when select this edit text
        etLastWateredDate.setOnClickListener(v -> {
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            //Date picker dialog
            pickerDate = new DatePickerDialog(CreatePlant.this,
                    (view, year1, monthOfYear, dayOfMonth) -> etLastWateredDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            pickerDate.getDatePicker().setMaxDate(System.currentTimeMillis());
            pickerDate.show();

            pickerDate.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    cldr.set(year, month, dayOfMonth);
                    etLastWateredDate.setText(dayOfMonth + "/" + (month +1) + "/" + year);
                }
            });
        });

        //When the SAVE button (in header) is select
        ImageButton ibSave = findViewById(R.id.ibSave);
        ibSave.setOnClickListener(v -> {
            savePlant();
        });

        //When the CANCEL button (in header) is selected
        ImageButton ibCancel = findViewById(R.id.ibCancel);
        ibCancel.setOnClickListener(v -> {
            Intent intentMainAct = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentMainAct);
        });
    }

    //CAMERA -> Launches and gets result (photo)
    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ivPhoto.setImageBitmap(imageBitmap);
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void savePlant() {
        boolean canSave = true;

        //VALIDATIONS
        if (etPlantName.getText().toString().trim().length() == 0) {
            etPlantName.setError("Please enter a name for your plant!");
            canSave = false;
        } else {
            canSave = true;
        }

        if (etPeriod.getText().toString().trim().length() == 0) {
            etPeriod.setError("Please enter how often your plant should be watered!");
            canSave = false;
        } else {
            canSave = true;
        }

        if (etLastWateredDate.getText().toString().trim().length() == 0) {
            etLastWateredDate.setError("Please enter when your plant was last watered!");
            canSave = false;
        } else {
            canSave = true;
        }

        if (canSave) {
            String pName = etPlantName.getText().toString();
            Date calendarDate = cldr.getTime();
            LocalDate pLastWatered  = calendarDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Plant newPlant = new Plant(pName, imageBitmap, pLastWatered, getPeriodIncrement());
        }
    }

    private int getPeriodIncrement() {
        int periodIncrement = 0;
        int periodValue = Integer.parseInt(etPeriod.getText().toString());
        String spinnerValue = spinPeriod.getSelectedItem().toString();
        switch (spinnerValue) {
            case "Day(s)":
                periodIncrement = periodValue;
                break;
            case "Week(s)":
                periodIncrement = periodValue * 7;
                break;
            case "Month(s)":
                periodIncrement = periodValue * 30;
                break;
        }
        return periodIncrement;
    }

}
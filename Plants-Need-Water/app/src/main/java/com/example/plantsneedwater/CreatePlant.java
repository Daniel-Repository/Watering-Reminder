package com.example.plantsneedwater;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.graphics.Bitmap;

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

import java.util.Calendar;

public class CreatePlant extends AppCompatActivity {

    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plant);

        TextInputEditText etPlantName = (TextInputEditText) findViewById(R.id.etPlantName);
        EditText etPeriod = (EditText) findViewById(R.id.etPeriod);
        ImageButton ibSave = (ImageButton) findViewById(R.id.ibSave);

        //USER SELECTS CAMERA AREA TO ADD NEW PHOTO
        ImageView ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        LinearLayout llCameraField = (LinearLayout) findViewById(R.id.llCameraField);

        ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent data = result.getData();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivPhoto.setImageBitmap(imageBitmap);
            }
        });

        llCameraField.setOnClickListener(v -> {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResultLauncher.launch(takePhotoIntent);
        });

        //Populate spinPeriod with values from our string array 'period_array'
        Spinner spinPeriod = (Spinner) findViewById(R.id.spinPeriod);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.period_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPeriod.setAdapter(adapter);

        EditText etEnterDate = (EditText) findViewById(R.id.etEnterDate);
        etEnterDate.setInputType(InputType.TYPE_NULL);

        //Open date picker dialog (Calendar) when select this edit text
        etEnterDate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(CreatePlant.this,
                    (view, year1, monthOfYear, dayOfMonth) -> etEnterDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        //When the SAVE button (in header) is selected
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VALIDATIONS
                if(etPlantName.getText().toString().trim().length() == 0){
                    etPlantName.setError("Please enter a name for your plant!");
                }
            }
        });

    }
}
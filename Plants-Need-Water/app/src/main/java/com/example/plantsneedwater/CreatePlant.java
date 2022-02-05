package com.example.plantsneedwater;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
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
    String imageFilePath;
    Spinner spinPeriod;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
            takePlantPhoto();
        });

        //POPULATE SPINPERIOD WITH VALUES FROM OUR STRING ARRAY 'PERIOD_ARRAY'
        spinPeriod = findViewById(R.id.spinPeriod);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.period_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPeriod.setAdapter(adapter);

        //OPEN DATE PICKER DIALOG (CALENDAR) WHEN THIS EDIT TEXT IS SELECTED
        etLastWateredDate.setOnClickListener(v -> {
            openDatePicker();
        });

        //WHEN THE SAVE BUTTON (IN HEADER) IS SELECTED
        ImageButton ibSave = findViewById(R.id.ibSave);
        ibSave.setOnClickListener(v -> {
            savePlant();
        });

        //WHEN THE CANCEL BUTTON (IN HEADER) IS SELECTED
        ImageButton ibCancel = findViewById(R.id.ibCancel);
        ibCancel.setOnClickListener(v -> {
            Intent intentMainAct = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentMainAct);
        });
    }

    //Opens the date picker
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openDatePicker() {
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        //Date picker dialog
        pickerDate = new DatePickerDialog(CreatePlant.this,
                (view, year1, monthOfYear, dayOfMonth) -> etLastWateredDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
        pickerDate.getDatePicker().setMaxDate(System.currentTimeMillis());
        pickerDate.show();

        pickerDate.setOnDateSetListener((view, year12, month1, dayOfMonth) -> {
            cldr.set(year12, month1, dayOfMonth);
            etLastWateredDate.setText(dayOfMonth + "/" + (month1 +1) + "/" + year12);
        });
    }

    //Called when the learner clicks the 'tick' (Done/Save) button.
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

        //Creates a new Plant object with the inputted details.
        if (canSave) {
            String pName = etPlantName.getText().toString();
            Date calendarDate = cldr.getTime();
            LocalDate pLastWatered  = calendarDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Plant newPlant = new Plant(pName, imageBitmap, pLastWatered, getPeriodIncrement());
            PlantDataHolder.plantList.add(newPlant);

            //Save update to Shared Preferences
            SharedPreferences sharedPref = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(PlantDataHolder.plantList);
            editor.putString("plant list", json);
            editor.apply();

            Intent intentMainAct = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentMainAct);
        }
    }

    //Calculates the period to the next water in days
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

    //TAKE PHOTO
    private void takePlantPhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = null;

        try {
            photo = createImageFile();

        } catch (IOException ex) {
            Toast.makeText(this, "Error creating image file.", Toast.LENGTH_SHORT).show();
        }

        if (photo != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.plantsneedwater.provider", photo);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //Gets the result of our camera activity (image) and sets it to our ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        File file = new File(imageFilePath);

        //Result of the initial image taken --> We go straight to cropping this image
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                CropImage.activity(Uri.fromFile(file))
                        .setAspectRatio(1,1)
                        .start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Result of cropping the image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                ivPhoto.setImageBitmap(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Creates a proper image file and returns it
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PLANT_IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

}
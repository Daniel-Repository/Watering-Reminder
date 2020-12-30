package com.example.plantsneedwater;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

public class CreatePlant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plant);

        TextInputEditText etPlantName = (TextInputEditText) findViewById(R.id.etPlantName);
        EditText etPeriod = (EditText) findViewById(R.id.etPeriod);
        Spinner spinPeriod = (Spinner) findViewById(R.id.spinPeriod);

        //Populate spinPeriod with values from our string array 'period_array'
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.period_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPeriod.setAdapter(adapter);

    }
}
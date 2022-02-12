package com.example.plantsneedwater;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlantSingleView extends AppCompatActivity {

    ImageView plantImage;
    TextView plantName;
    TextView plantNextWater;
    TextView plantLastWater;
    MaterialButton btnWaterPlant;
    int arrPos;
    private List<Plant> mData;
    Plant plant;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_single_view);

        plantImage = findViewById(R.id.plantImageSingle);
        plantName = findViewById(R.id.plantSingleName);
        plantNextWater = findViewById(R.id.plantSingleNextWater);
        plantLastWater = findViewById(R.id.plantSingleLastWatered);
        btnWaterPlant = findViewById(R.id.btnWaterPlant);

        //Get array position passed from RecyclerView selection
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            arrPos = extras.getInt("plantPos");
            displayPlantDetails();
        }

        //APP BAR
        Toolbar singlePlantToolbar = findViewById(R.id.singlePlantToolbar);
        setSupportActionBar(singlePlantToolbar);
        singlePlantToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        singlePlantToolbar.showOverflowMenu();
        getSupportActionBar().setTitle("");

        singlePlantToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.backHome:
                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                    intentMain.putExtra("plantPos", arrPos);
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.anim_stay_put, R.anim.anim_center_to_right);
                    break;
            }
            return true;
        });

        //WATER PLANT BUTTON CLICK
        btnWaterPlant.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Water Plant");
            builder.setMessage("Are you sure you want to water this plant?");
            builder.setCancelable(false);

            //If the user selects "YES"
            builder.setPositiveButton("Yes", (dialog, which) -> {
                waterPlant();
            });

            //If the user selects "NO"
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            Button posButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            posButton.setBackgroundColor(Color.TRANSPARENT);

            Button negButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negButton.setBackgroundColor(Color.TRANSPARENT);

        });

    }

    //Display our plant details
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayPlantDetails(){
        mData = PlantDataHolder.plantList;
        plant = mData.get(arrPos);
        Uri test = Uri.parse(plant.getImgURI());

        if(!test.toString().equals("")) {
            Picasso.get().load(test).into(plantImage);
        }

        plantName.setText(plant.getPlantName());
        plantNextWater.setText(plant.getPlantNextWaterString());
        plantLastWater.setText("Last watered on the " + plant.getPlantLastWateredDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_plant_view_app_bar, menu);
        return true;
    }

    private void waterPlant(){
        plant.waterPlant();

        //Save update to Shared Preferences
        SharedPreferences sharedPref = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(PlantDataHolder.plantList);
        editor.putString("plant list", json);
        editor.apply();

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
package com.example.plantsneedwater;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlantSingleView extends AppCompatActivity {

    ImageView plantImage;
    TextView plantName;
    TextView plantNextWater;
    TextView plantLastWater;
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

        singlePlantToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.backHome:
                        Intent intentCreatePlant = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentCreatePlant);
                        overridePendingTransition(R.anim.anim_stay_put, R.anim.anim_center_to_right);
                        break;
                }
                return true;
            }
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
}
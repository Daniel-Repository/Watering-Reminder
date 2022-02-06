package com.example.plantsneedwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PlantSingleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_single_view);

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
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_plant_view_app_bar, menu);
        return true;
    }
}
package com.example.plantsneedwater;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerViewAdapter adapter;
    List<Plant> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantList = PlantDataHolder.plantList;
        setRecycler();

        //Add new plant FAB
        FloatingActionButton fabNewPlant = (FloatingActionButton) findViewById(R.id.fabCreatePlant);
        fabNewPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreatePlant = new Intent(getApplicationContext(), CreatePlant.class);
                startActivity(intentCreatePlant);
            }
        });
    }

    private void setRecycler(){
        RecyclerView recyclerView = findViewById(R.id.rvPlants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (plantList == null) {
            //recyclerView.setVisibility(View.GONE);
        } else {
            adapter = new RecyclerViewAdapter(this, plantList);
            //adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }

    }
}
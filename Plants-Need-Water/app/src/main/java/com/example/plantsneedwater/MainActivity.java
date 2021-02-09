package com.example.plantsneedwater;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
    int recyclerColumns = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantList = PlantDataHolder.plantList;
        setRecycler(recyclerColumns);

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

    private void setRecycler(int recyclerColumns){
        RecyclerView recyclerView = findViewById(R.id.rvPlants);
        recyclerView.setLayoutManager(new GridLayoutManager(this, recyclerColumns));

        if (plantList == null) {
            //recyclerView.setVisibility(View.GONE);
        } else {
            adapter = new RecyclerViewAdapter(this, plantList);
            //adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }

    }
}
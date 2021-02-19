package com.example.plantsneedwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainActivity extends AppCompatActivity  {

    RecyclerViewAdapter adapter;
    List<Plant> plantList;
    int recyclerColumns = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantList = PlantDataHolder.plantList;
        setRecycler(recyclerColumns);

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.showOverflowMenu();
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addPlant:
                        Intent intentCreatePlant = new Intent(getApplicationContext(), CreatePlant.class);
                        startActivity(intentCreatePlant);
                        break;
                }
                return true;
            }
        });
    }

    //Sets up our recyclerview
    private void setRecycler(int recyclerColumns){
        RecyclerView recyclerView = findViewById(R.id.rvPlants);
        recyclerView.setLayoutManager(new GridLayoutManager(this, recyclerColumns));

        if (plantList == null) {
            //Should show an empty state. NEED TO DO!
        } else {
            adapter = new RecyclerViewAdapter(this, plantList);
            //adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_app_bar, menu);
        return true;
    }

}
package com.example.plantsneedwater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    RecyclerViewAdapter adapter;

    int recyclerColumns = 2; //How many columns of plants do we want displayed?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPlants();
        setRecycler(recyclerColumns);

        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        myToolbar.showOverflowMenu();
        getSupportActionBar().setTitle("Planted ");

        myToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.addPlant:
                    Intent intentCreatePlant = new Intent(getApplicationContext(), CreatePlant.class);
                    startActivity(intentCreatePlant);
                    break;
            }
            return true;
        });
    }

    //Sets up our recyclerview
    private void setRecycler(int recyclerColumns){
        RecyclerView recyclerView = findViewById(R.id.rvPlants);
        recyclerView.setLayoutManager(new GridLayoutManager(this, recyclerColumns));

        if (PlantDataHolder.plantList == null) {
            //Should show an empty state. NEED TO DO!
        } else {
            adapter = new RecyclerViewAdapter(this, PlantDataHolder.plantList);
            //adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_app_bar, menu);
        return true;
    }


    //Load SharedPreferences
    private void loadPlants() {
        SharedPreferences sharedPref = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.commit();
        String json  = sharedPref.getString("plant list", null);
        Type type = new TypeToken<ArrayList<Plant>>() {}.getType();
        PlantDataHolder.plantList = gson.fromJson(json, type);
        if (PlantDataHolder.plantList == null){
            PlantDataHolder.plantList = new ArrayList<>();
        }
    }

}
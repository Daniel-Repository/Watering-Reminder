package com.example.plantsneedwater;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class Plant {


    private String plantName;
    //private String plantDescription;
    private Bitmap plantImage;
    private LocalDate plantLastWatered;
    private int plantPeriodIncrement;
    private Period plantNextWater;

    Calendar calendar = Calendar.getInstance();

    //Constructor
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Plant(String pName, Bitmap pImage, LocalDate pLastW, int pPeriodIncrement) {
        setPlantName(pName);
        setPlantImage(pImage);
        setPlantLastWatered(pLastW);
        setPlantPeriodIncrement(pPeriodIncrement);

        setPlantNextWater(Period.between(pLastW, pLastW.plusDays(pPeriodIncrement)));
        Log.d("Plant Details", pName + " " + pLastW.toString() + "  " + plantNextWater.toString());
    }


    //Getters
    public String getPlantName() { return plantName; }

    public Bitmap getPlantImage() { return plantImage; }

    public LocalDate getPlantLastWatered() { return plantLastWatered; }

    public int getPlantPeriodIncrement() { return plantPeriodIncrement; }

    public Period getPlantNextWater() { return plantNextWater; }



    //Setters
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public void setPlantImage(Bitmap plantImage) { this.plantImage = plantImage; }

    public void setPlantLastWatered(LocalDate plantLastWatered) { this.plantLastWatered = plantLastWatered; }

    public void setPlantPeriodIncrement(int plantPeriodIncrement) { this.plantPeriodIncrement = plantPeriodIncrement; }

    public void setPlantNextWater(Period plantNextWater) { this.plantNextWater = plantNextWater; }

}

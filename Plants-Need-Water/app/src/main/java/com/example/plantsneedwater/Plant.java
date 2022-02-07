package com.example.plantsneedwater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Plant {

    private String plantName;

    private String plantLastWatered;
    private int plantPeriodIncrement;
    private Period plantNextWater;
    private String plantNextWaterString;

    private LocalDate plantLastWateredDate;

    private String imgURI;

    LocalDate today = LocalDate.now();

    //Constructor
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Plant(String pName, String pLastW, int pPeriodIncrement, String imgURI) {
        setPlantName(pName);
        setPlantLastWatered(pLastW);
        setPlantPeriodIncrement(pPeriodIncrement);

        setImgURI(imgURI);

        setPlantLastWateredDate();
        setPlantNextWater(Period.between(today, plantLastWateredDate.plusDays(pPeriodIncrement)));
        setPlantNextWaterString(plantNextWater);
    }

    public Plant() { }

    //Getters
    public String getPlantName() { return plantName; }



    public String getPlantLastWatered() { return plantLastWatered; }

    public int getPlantPeriodIncrement() { return plantPeriodIncrement; }

    public Period getPlantNextWater() { return plantNextWater; }

    public String getPlantNextWaterString() { return plantNextWaterString; }

    public LocalDate getPlantLastWateredDate() {
        return plantLastWateredDate;
    }

    public String getImgURI() {return imgURI;}

    //Setters
    public void setPlantName(String plantName) { this.plantName = plantName; }



    public void setPlantLastWatered(String plantLastWatered) { this.plantLastWatered = plantLastWatered; }

    public void setPlantPeriodIncrement(int plantPeriodIncrement) { this.plantPeriodIncrement = plantPeriodIncrement; }

    public void setPlantNextWater(Period plantNextWater) { this.plantNextWater = plantNextWater; }

    public void setImgURI(String imgURI) {this.imgURI = imgURI;}

    public void setPlantNextWaterString(Period plantNextWater) {

        int months = plantNextWater.getMonths();
        int days = plantNextWater.getDays();

        if (months == 0 && days >= 1) {
            plantNextWaterString = days + " Days until next water";
        }
        else if (months == 1) {
            plantNextWaterString = months + " Month, " + days + " Days until next water";
        }
        else if (months == 0 && days <= 0) {
            plantNextWaterString = "Water " + getPlantName() + " today!";
        }
        else {
            plantNextWaterString = months + " Months, " + days + " Days until next water";
        }
    }

    public void setPlantLastWateredDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(getPlantLastWatered(), formatter);
        plantLastWateredDate = date;
    }

    public void waterPlant() {
        setPlantLastWatered(LocalDate.now().toString());
        setPlantLastWateredDate();
        setPlantNextWater(Period.between(LocalDate.now(), getPlantLastWateredDate().plusDays(plantPeriodIncrement)));
        setPlantNextWaterString(getPlantNextWater());
    }

}

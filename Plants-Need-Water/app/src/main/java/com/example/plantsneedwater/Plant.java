package com.example.plantsneedwater;

import android.graphics.Bitmap;
import android.media.Image;

import java.time.Period;
import java.util.Date;

public class Plant {

    private String plantName;
    //private String plantDescription;
    private Bitmap plantImage;
    private Date plantLastWatered;
    private Period plantNextWater;

    //Constructor
    public Plant(String pName, Bitmap pImage, Date pLastW, Period pNextW) {
        setPlantName(pName);
        //setPlantDescription(pDesc);
        setPlantImage(pImage);
        setPlantLastWatered(pLastW);
        setPlantNextWater(pNextW);
    }

    //Getters
    public String getPlantName() { return plantName; }

    //public String getPlantDescription() { return plantDescription; }

    public Bitmap getPlantImage() { return plantImage; }

    public Date getPlantLastWatered() { return plantLastWatered; }

    public Period getPlantNextWater() { return plantNextWater; }

    //Setters
    public void setPlantName(String plantName) { this.plantName = plantName; }

    //public void setPlantDescription(String plantDescription) { this.plantDescription = plantDescription; }

    public void setPlantImage(Bitmap plantImage) { this.plantImage = plantImage; }

    public void setPlantLastWatered(Date plantLastWatered) { this.plantLastWatered = plantLastWatered; }

    public void setPlantNextWater(Period plantNextWater) { this.plantNextWater = plantNextWater; }

}

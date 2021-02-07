package com.example.plantsneedwater;

import java.util.ArrayList;
import java.util.List;

public class PlantDataHolder {

    private PlantDataHolder instance;

    static List<Plant> plantList = new ArrayList<>();

    public PlantDataHolder(){};

    PlantDataHolder getInstance() {
        if(instance == null ) {
            instance = new PlantDataHolder();
        }
        return instance;
    }

}

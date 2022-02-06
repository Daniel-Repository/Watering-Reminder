package com.example.plantsneedwater;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    private List<Plant> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    //Data is passed into the constructor
    RecyclerViewAdapter(Context context, List<Plant> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //Inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    //Binds the data to the component in each row
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap pImg = mData.get(position).getPlantImage();
        String pName = mData.get(position).getPlantName();
        mData.get(position).setPlantLastWateredDate();
        String pLastWatered = "Last watered on the " + mData.get(position).getPlantLastWateredDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        String pNextWater = mData.get(position).getPlantNextWaterString();

        if(pImg != null) {
            holder.plantImage.setImageBitmap(pImg);
        }



        holder.plantName.setText(pName);
//        holder.plantLastWatered.setText(pLastWatered);
        holder.plantNextWater.setText(pNextWater);
    }

    //Total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView plantImage;
        TextView plantName;
        TextView plantLastWatered;
        TextView plantNextWater;


        ViewHolder(View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.plantImage);
            plantName = itemView.findViewById(R.id.plantName);
//            plantLastWatered = itemView.findViewById(R.id.plantLastWatered);
            plantNextWater = itemView.findViewById(R.id.plantNextWater);
            itemView.setOnClickListener(this);

            //Click listener for the Water Plant Button
//            itemView.findViewById(R.id.btnWaterPlant).setOnClickListener(new View.OnClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.O)
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    Plant plant = mData.get(pos);
//                    plant.waterPlant();
//                    notifyDataSetChanged();
//                }
//            });

            //When click on the RV item
            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Plant plant = mData.get(pos);
                    Intent intentCreatePlant = new Intent(itemView.getContext(), PlantSingleView.class);
                    ActivityOptions animOptions = ActivityOptions.makeCustomAnimation(itemView.getContext(), R.anim.anim_right_to_left, R.anim.anim_stay_put);
                    itemView.getContext().startActivity(intentCreatePlant, animOptions.toBundle());
                }
            });

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    //Convenience method for getting data at click position
    Plant getItem(int id) {
        return mData.get(id);
    }

    //Allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    //Parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

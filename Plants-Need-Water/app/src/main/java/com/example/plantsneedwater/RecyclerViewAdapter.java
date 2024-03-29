package com.example.plantsneedwater;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.net.URI;
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

        Uri test = Uri.parse(mData.get(position).getImgURI());

        String pName = mData.get(position).getPlantName();
        mData.get(position).setPlantLastWateredDate();

        mData.get(position).setPlantNextWaterString(mData.get(position).calculateNewPeriod());

        String pNextWater = mData.get(position).getPlantNextWaterString();

        if(!test.toString().equals("")) {
            Picasso.get().load(test).into(holder.plantImage);
        }

        holder.plantName.setText(pName);
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
        TextView plantNextWater;

        ViewHolder(View itemView) {
            super(itemView);

            plantImage = itemView.findViewById(R.id.plantImage);
            plantName = itemView.findViewById(R.id.plantName);
            plantNextWater = itemView.findViewById(R.id.plantNextWater);
            itemView.setOnClickListener(this);

            //When click on the RV item
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();

                Intent intentCreatePlant = new Intent(itemView.getContext(), PlantSingleView.class);
                intentCreatePlant.putExtra("plantPos", pos);
                ActivityOptions animOptions = ActivityOptions.makeCustomAnimation(itemView.getContext(), R.anim.anim_right_to_left, R.anim.anim_stay_put);

                itemView.getContext().startActivity(intentCreatePlant, animOptions.toBundle());
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

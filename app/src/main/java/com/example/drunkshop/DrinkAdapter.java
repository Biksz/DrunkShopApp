package com.example.drunkshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> implements Filterable {

    private ArrayList<Drink> drinks;
    private ArrayList<Drink> allDrinks;
    private Context context;
    private int lastPos = -1;

    DrinkAdapter(Context context, ArrayList<Drink> drinks){
        this.drinks = drinks;
        this.allDrinks = drinks;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(DrinkAdapter.ViewHolder holder, int position) {
        Drink current = drinks.get(position);
        holder.bindTo(current);

        if(holder.getAdapterPosition() > lastPos){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_row);
            holder.itemView.startAnimation(animation);
            lastPos = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    @Override
    public Filter getFilter() {
        return drinkFilter;
    }

    private Filter drinkFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Drink> filtered = new ArrayList<>();
            FilterResults res = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                res.count = allDrinks.size();
                res.values = allDrinks;
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for(Drink drink : allDrinks){
                    if(drink.getName().toLowerCase().contains(pattern)){
                        filtered.add(drink);
                    }
                }
                res.count = allDrinks.size();
                res.values = allDrinks;
            }
            return res;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            drinks = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView info;
        private TextView price;
        private ImageView img;
        private RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.prodTitle);
            info = itemView.findViewById(R.id.prodSubTitle);
            price = itemView.findViewById(R.id.prodPrice);
            img = itemView.findViewById(R.id.prodImg);
            rating = itemView.findViewById(R.id.killerRate);

        }

        public void bindTo(Drink current) {
            title.setText(current.getName());
            info.setText(current.getInfo());
            price.setText(current.getPrice());
            rating.setRating(current.getRating());
            Glide.with(context).load(current.getImage()).into(img);

            itemView.findViewById(R.id.addToCart).setOnClickListener(view -> {
                Log.d("activity", "Chart button clicled");
                ((DashboardActivity)context).updateAlertIcon(current);
            });
            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((DashboardActivity)context).deleteDrink(current));

            itemView.findViewById(R.id.spin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    img.startAnimation(AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.spin));
                }
            });
        }
    };
};

package com.boostcamp.miniproject2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.boostcamp.miniproject2.R;
import com.boostcamp.miniproject2.database.DatabaseHelper;
import com.boostcamp.miniproject2.model.Restaurant;

import java.util.List;

/**
 * Created by moon on 2017. 7. 13..
 */

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Restaurant> restaurantList;
    private Context context;

    public RestaurantRecyclerViewAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindViewByItem(restaurantList.get(position));
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumb;
        private TextView name;
        private TextView info;
        private CheckBox check;

        public ViewHolder(View itemView) {
            super(itemView);

            thumb = itemView.findViewById(R.id.image_view_restaurant_thumb);
            name = itemView.findViewById(R.id.text_view_restaurant_name);
            info = itemView.findViewById(R.id.text_view_restaurant_info);
            check = itemView.findViewById(R.id.checkbox_restaurant_check);
        }

        private void bindViewByItem(Restaurant restaurant) {
            thumb.setImageResource(restaurant.getImage());
            name.setText(restaurant.getName());
            info.setText(restaurant.getInfo());
            check.setChecked(restaurant.isChecked());
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper db = DatabaseHelper.getInstance(context);
                    Restaurant res = restaurantList.get(getAdapterPosition());
                    res.setChecked(((CheckBox) view).isChecked());

                    int id = db.selectIdByName(res.getName());
                    db.updateCheckValueById(id, ((CheckBox) view).isChecked());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return restaurantList != null? restaurantList.size() : 0;
    }
}

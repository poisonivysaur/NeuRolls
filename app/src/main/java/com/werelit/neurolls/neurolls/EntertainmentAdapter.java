package com.werelit.neurolls.neurolls;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class EntertainmentAdapter extends RecyclerView.Adapter<EntertainmentAdapter.MyViewHolder> {

    private List<Entertainment> entertainments;

    public EntertainmentAdapter(List<Entertainment> restaurantList) {
        this.entertainments = restaurantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_test, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Entertainment entertainment = entertainments.get(position);
        holder.name.setText(entertainment.getmEntertainmentName());
        holder.desc.setText(entertainment.getmEntertainmentAuthor());
        holder.year.setText("" + entertainment.getmEntertainmentYear());
        holder.modelIndex = position;

    }

    @Override
    public int getItemCount() {
        return entertainments.size();
    }

    public void removeItem(int position) {
        entertainments.remove(position);
        // notify the item removed by position
        // to perform recycling
        // for view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Entertainment item, int position) {
        entertainments.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc, year;
        public Button editItem;
        //public final ImageButton moreButt;
        private int modelIndex = -1;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            desc = (TextView) view.findViewById(R.id.author);
            year = (TextView) view.findViewById(R.id.year);
            editItem = (Button) view.findViewById(R.id.edit_item);
            //moreButt = (ImageButton) view.findViewById(R.id.moreButton);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            /*
            editItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) itemView.getContext();
                    Intent intent = new Intent(activity, MainActivity.class);

                    // Make a bundle containing the current restaurant details
                    Bundle bundle = new Bundle();
                    bundle.putInt(AddRestaurant.EDIT_MODEL_INDEX_KEY, modelIndex);
                    bundle.putString(AddRestaurant.RESTAURANT_NAME_KEY, name.getText().toString());
                    bundle.putString(AddRestaurant.RESTAURANT_DESC_KEY, desc.getText().toString());
                    bundle.putDouble(AddRestaurant.WEIGHT_KEY, Double.parseDouble(weight.getText().toString()));
                    // Edit the restaurant item
                    intent.putExtras(bundle);
                    activity.startActivityForResult(intent, RestaurantActivity.EDIT_RESTO_REQUEST);
                }
            });*/
        }
    }
}

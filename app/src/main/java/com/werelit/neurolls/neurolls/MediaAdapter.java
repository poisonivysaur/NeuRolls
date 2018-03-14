package com.werelit.neurolls.neurolls;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.werelit.neurolls.neurolls.model.Media;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MyViewHolder> {

    private List<Media> mediaList;

    /** Resource ID for the background tint for image of the media */
    private int category;

    /** Resource ID for the background layout of the media */
    private boolean isArchived;

    public MediaAdapter(List<Media> mediaList, int category, boolean isArchived) {
        this.mediaList = mediaList;
        this.category = category;
        this.isArchived = isArchived;
    }

    public MediaAdapter(List<Media> mediaList, int category) {
        this(mediaList, category, false);
    }

    public MediaAdapter(List<Media> mediaList) {
        this(mediaList, -1, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(!isArchived){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_unarchived_media, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_archived_media, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Media entertainment = mediaList.get(position);
        holder.name.setText(entertainment.getmMediaName());
        holder.genre.setText(entertainment.getmMediaGenre());
        holder.year.setText("" + entertainment.getmMediaYear());
        switch (category){
            case 0:
                holder.image.setBackgroundColor(holder.rootView.getContext().getResources().getColor(R.color.colorAccent));
                break;
            case 1:
                holder.image.setBackgroundColor(holder.rootView.getContext().getResources().getColor(R.color.films));
                break;
            case 2:
                holder.image.setBackgroundColor(holder.rootView.getContext().getResources().getColor(R.color.books));
                break;
            case 3:
                holder.image.setBackgroundColor(holder.rootView.getContext().getResources().getColor(R.color.games));
                break;
        }
        holder.modelIndex = position;
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void removeItem(int position) {
        mediaList.remove(position);
        // notify the item removed by position
        // to perform recycling
        // for view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Media item, int position) {
        mediaList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, genre, year;
        public Button editItem;
        public ImageView image;
        public View rootView;
        //public final ImageButton moreButt;
        private int modelIndex = -1;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            rootView = view;
            name = (TextView) view.findViewById(R.id.name);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            image = (ImageView) view.findViewById(R.id.media_image);
            //editItem = (Button) view.findViewById(R.id.edit_item);
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
                    bundle.putString(AddRestaurant.RESTAURANT_DESC_KEY, genre.getText().toString());
                    bundle.putDouble(AddRestaurant.WEIGHT_KEY, Double.parseDouble(weight.getText().toString()));
                    // Edit the restaurant item
                    intent.putExtras(bundle);
                    activity.startActivityForResult(intent, RestaurantActivity.EDIT_RESTO_REQUEST);
                }
            });*/
        }
    }
}

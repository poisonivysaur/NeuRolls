package com.werelit.neurolls.neurolls;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.werelit.neurolls.neurolls.model.Media;

import java.util.List;

public class MediaCursorAdapter extends RecyclerView.Adapter<MediaAdapter.MyViewHolder> {

    private List<Media> mediaList;
    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    CursorAdapter mCursorAdapter;
    Context mContext;


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

    // wrap around solution for cursor adapters
    public MediaAdapter(Context context, Cursor c) {
        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return null;
            }

            /**
             * This method binds the media data (in the current row pointed to by cursor) to the given
             * list item layout. For example, the name for the current media can be set on the name TextView
             * in the list item layout.
             *
             * @param view    Existing view, returned earlier by newView() method
             * @param context app context
             * @param cursor  The cursor from which to get the data. The cursor is already moved to the
             *                correct row.
             */
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Find individual views that we want to modify in the list item layout
                TextView nameTextView = (TextView) view.findViewById(R.id.name);
                TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

                // Find the columns of pet attributes that we're interested in
                int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
                int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);

                // Read the pet attributes from the Cursor for the current pet
                String petName = cursor.getString(nameColumnIndex);
                String petBreed = cursor.getString(breedColumnIndex);

                // If the pet breed is empty string or null, then use some default text
                // that says "Unknown breed", so the TextView isn't blank.
                if (TextUtils.isEmpty(petBreed)) {
                    petBreed = context.getString(R.string.unknown_breed);
                }

                // Update the TextViews with the attributes for the current pet
                nameTextView.setText(petName);
                summaryTextView.setText(petBreed);
            }
        };
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
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

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

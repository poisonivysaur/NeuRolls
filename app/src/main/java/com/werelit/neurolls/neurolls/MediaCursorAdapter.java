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

import com.werelit.neurolls.neurolls.data.MediaContract.FilmEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.BookEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.GameEntry;
import com.werelit.neurolls.neurolls.model.Media;

import java.util.List;

public class MediaCursorAdapter extends RecyclerView.Adapter<MediaCursorAdapter.MyViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    private CursorAdapter mCursorAdapter;
    private Context mContext;
    private int mPosition;
    private MyViewHolder holder;
    private Media media;
    private List<Media> mediaList;

    /** Resource ID for the background tint for image of the media */
    private int category;

    /** Resource ID for the background layout of the media */
    private boolean isArchived;

    public MediaCursorAdapter(List<Media> mediaList, int category, boolean isArchived) {
        this.mediaList = mediaList;
        this.category = category;
        this.isArchived = isArchived;
    }

    public MediaCursorAdapter(List<Media> mediaList, int category) {
        this(mediaList, category, false);
    }

    public MediaCursorAdapter(List<Media> mediaList) {
        this(mediaList, -1, false);
    }

    // wrap around solution for cursor adapters
    public MediaCursorAdapter(Context context, Cursor c, final int category, final boolean isArchived) {
        //this.mediaList = mediaList;
        this.category = category;
        this.isArchived = isArchived;
        mContext = context;
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                View itemView;
                if(!isArchived){
                    itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_unarchived_media, parent, false);
                }else {
                    itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_archived_media, parent, false);
                }

                return itemView;
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

                // TODO switch case with category to know which table to get data from

                // Find the columns of pet attributes that we're interested in
                int idColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_ID);
                int nameColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_NAME);
                int genreColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_GENRE);
                int yearColumnIndex = cursor.getColumnIndex(FilmEntry.COLUMN_FILM_YEAR_RELEASED);

                // TODO get all columns and set it to media

                // Read the pet attributes from the Cursor for the current pet
                String mediaID = cursor.getString(idColumnIndex);
                String mediaName = cursor.getString(nameColumnIndex);
                String mediaGenre = cursor.getString(genreColumnIndex);
                String mediaYear = cursor.getString(yearColumnIndex);

                media = new Media(mediaID, mediaName, mediaGenre, mediaYear);

                //Media entertainment = mediaList.get(mPosition);


                switch (category){
                    case CategoryAdapter.CATEGORY_ALL:
                        holder.image.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAccent));
//                        getFilms();
//                        getBooks();
//                        getGames();
                        break;
                    case CategoryAdapter.CATEGORY_FILMS:
                        holder.image.setBackgroundColor(view.getContext().getResources().getColor(R.color.films));
                        //getFilms(context, cursor);
                        break;
                    case CategoryAdapter.CATEGORY_BOOKS:
                        holder.image.setBackgroundColor(view.getContext().getResources().getColor(R.color.books));
                        //getBooks();
                        break;
                    case CategoryAdapter.CATEGORY_GAMES:
                        holder.image.setBackgroundColor(view.getContext().getResources().getColor(R.color.games));
                        //getGames();
                        break;
                }

                // Update the TextViews with the attributes for the current media
                holder.name.setText(media.getmMediaName());
                holder.genre.setText(media.getmMediaGenre());
                holder.year.setText(media.getmMediaYear());
                // TODO holder.image.setImage here
            }
        };
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View itemView = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mPosition = position;
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
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

//    private void getFilms(Context context, Cursor cursor){
//
//    }
//
//    private void getFilms(Context context, Cursor cursor){
//
//    }
//
//    private void getFilms(Context context, Cursor cursor){
//
//    }

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
        }
    }
}

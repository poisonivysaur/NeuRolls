package com.werelit.neurolls.neurolls;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * {@link Fragment} that displays a list of number vocabulary words.
 */
public class NumbersFragment extends Fragment{
    private List<Entertainment> entertainments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_cheese_list, container, false);
        entertainments.add(new Entertainment("Pericos", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("La Casita @ 6th Andrew", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("La Casita @ 2nd Razon", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("first resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("second resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("third resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("fourth resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("5th rest", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("6th resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("seventh resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("eighth resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("9th", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("6th resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("seventh resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("eighth resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("9th", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("6th resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("seventh resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("eighth resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("9th", "Canteen @ Razon building DLSU", 3));
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), entertainments));
    }

    private List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Entertainment> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Entertainment mBoundString;

            public final View mView;
            //public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                //mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Entertainment> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText(mValues.get(position).getmEntertainmentName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, CheeseDetailActivity.class);
//                    intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                    context.startActivity(intent);)
                }
            });

//            Glide.with(holder.mImageView.getContext())
//                    .load(Cheeses.getRandomCheeseDrawable())
//                    .fitCenter()
//                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}

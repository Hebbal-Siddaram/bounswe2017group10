package com.bounswe2017.group10.atlas.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bounswe2017.group10.atlas.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
import static java.lang.StrictMath.max;

public class ListItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FeedRow> rowList;
    private OnItemClickListener listener = null;

    /**
     * RecyclerViews don't support item click listeners natively. We need to
     * come up with our own interface.
     */
    public interface OnItemClickListener {
        void onItemClick(List<FeedRow> rowList, int position);
    }

    /**
     * ViewHolder class that implements the ViewHolder pattern for a more
     * efficient adapter.
     */
    private static class ViewHolder extends RecyclerView.ViewHolder {
        TextView etTitle;
        TextView etDescr;
        TextView etLocation;
        TextView etYear;
        TextView etFavorite;
        TextView etCreator;
        ImageView imIcon;
        LinearLayout layoutTag1;
        LinearLayout layoutTag2;
        LinearLayout layoutTag3;
        View view;

        ViewHolder(View v) {
            super(v);
            this.etTitle = v.findViewById(R.id.title_textview);
            this.etDescr = v.findViewById(R.id.description_textview);
            this.etLocation = v.findViewById(R.id.location_textview);
            this.etYear = v.findViewById(R.id.year_textview);
            this.etFavorite = v.findViewById(R.id.favorite_textview);
            this.etCreator = v.findViewById(R.id.creator_textview);
            this.imIcon = v.findViewById(R.id.icon_imageview);
            this.layoutTag1 = v.findViewById(R.id.tag1);
            this.layoutTag2 = v.findViewById(R.id.tag2);
            this.layoutTag3 = v.findViewById(R.id.tag3);
            this.view = v;
        }

        void bind(List<FeedRow> rowList, int position, Context context, OnItemClickListener listener) {
            FeedRow row = rowList.get(position);
            List<String> tagList = row.getTagList();
            LinearLayout[] tagArr = {layoutTag1, layoutTag2, layoutTag3};
            int numTags = tagList.size();
            for (int i = 0; i < numTags; ++i) {
                tagArr[i].setVisibility(View.VISIBLE);
                ((TextView)tagArr[i].findViewById(R.id.tag_textview)).setText(tagList.get(i));
            }
            for (int i = numTags ; i < tagArr.length; ++i) {
                tagArr[i].setVisibility(View.INVISIBLE);
            }
            etTitle.setText(row.getTitle());
            etDescr.setText(row.getDescription());
            etLocation.setText(row.getLocation());
            etFavorite.setText(row.getFavoriteCount());
            etCreator.setText(context.getString(R.string.created_by, row.getCreatorUsername()));

            if (row.getYear() != null) {
                int[] yearPair = FeedRow.fromYearFormat(row.getYear());
                String start = Integer.toString(yearPair[0]);
                if (yearPair[0] < 0) {
                    start = start.substring(1) + "BC";
                }
                String end = Integer.toString(yearPair[1]);
                if (yearPair[1] < 0) {
                    end = end.substring(1) + "BC";
                }
                etYear.setText(context.getString(R.string.year_string, start, end));
            }

            Glide.with(context)
                .load(row.getImageUrl())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_crop_original_black_48dp)
                        .error(R.drawable.ic_crop_original_black_48dp)
                        .fallback(R.drawable.ic_crop_original_black_48dp))
                .into(imIcon);

            if (listener != null) {
                this.view.setOnClickListener((View v) -> {
                    listener.onItemClick(rowList, position);
                });
            }
        }
    }

    /**
     * TagListAdapter constructor
     * @param context Context in which this adapter object will be used.
     * @param rowList List of FeedRow objects that this adapter object will be responsible to
     *                send to RecyclerView objects this adapter is attached to.
     * @param listener Listener object whose onItemClick method will be called
     *                 when a given item is clicked. If null, then nothing happens.
     */
    public ListItemsAdapter(Context context, List<FeedRow> rowList, OnItemClickListener listener) {
        this.context = context;
        this.rowList = rowList;
        this.listener = listener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).bind(this.rowList, position, context, listener);
    }

    @Override
    public int getItemCount() {
        return this.rowList.size();
    }
}

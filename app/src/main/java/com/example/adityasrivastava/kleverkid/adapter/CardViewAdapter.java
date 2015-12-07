package com.example.adityasrivastava.kleverkid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import com.example.adityasrivastava.kleverkid.R;
import com.example.adityasrivastava.kleverkid.modal.PostDetails;
import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;




public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

   private final List<PostDetails> mItems;
   private Context mContext;

    public CardViewAdapter(Context context, List<PostDetails> modals) {
        super();
        mItems = new ArrayList<PostDetails>(modals);
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;

        public TextView post_title;
//        public TextView post_desc;
        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            post_title = (TextView)itemView.findViewById(R.id.post_title);
//            post_desc = (TextView)itemView.findViewById(R.id.post_desc);
        }

    }


    public List<PostDetails> getmItems() {
        return mItems;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_container, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        PostDetails postDetails = mItems.get(i);

        // Load Image
        Picasso.with(mContext).load(postDetails.getThumbnail())
                .error(R.drawable.ig)
                .placeholder(R.drawable.ig)
                .into(viewHolder.imgThumbnail);


        viewHolder.post_title.setText(postDetails.getTitle());
//        viewHolder.post_desc.setText(postDetails.getExcerpt());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void animateTo(List<PostDetails> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<PostDetails> newModels) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final PostDetails model = mItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<PostDetails> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PostDetails model = newModels.get(i);
            if (!mItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<PostDetails> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PostDetails model = newModels.get(toPosition);
            final int fromPosition = mItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public PostDetails removeItem(int position) {
        final PostDetails model = mItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, PostDetails model) {
        mItems.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final PostDetails model = mItems.remove(fromPosition);
        mItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}



package com.myapplication.made.submission4.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myapplication.made.submission4.R;
import com.myapplication.made.submission4.model.TvShowModel;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private ArrayList<TvShowModel> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<TvShowModel> getmData() {
        return mData;
    }

    public void setmData(ArrayList<TvShowModel> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public TvShowAdapter.TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film_tv, viewGroup, false);
        return new TvShowAdapter.TvShowViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.TvShowViewHolder tvShowViewHolder, int position) {
        tvShowViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class TvShowViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvTitle, tvOverview;

        public TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            tvTitle = itemView.findViewById(R.id.txt_filmtv_title);
            tvOverview = itemView.findViewById(R.id.txt_filmtv_overview);
        }

        void bind(final TvShowModel tvShowModel) {
            Glide.with(itemView.getContext())
                    .load(tvShowModel.getPosterPath())
                    .apply(new RequestOptions().override(350, 550))
                    .into(imgPhoto);
            tvTitle.setText(tvShowModel.getName());
            tvOverview.setText(tvShowModel.getOverview());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(mData.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShowModel tvShowModel);
    }
}

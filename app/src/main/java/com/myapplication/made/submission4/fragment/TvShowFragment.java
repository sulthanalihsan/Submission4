package com.myapplication.made.submission4.fragment;


import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.myapplication.made.submission4.R;
import com.myapplication.made.submission4.activity.DetailTvShowActivity;
import com.myapplication.made.submission4.adapter.TvShowAdapter;
import com.myapplication.made.submission4.model.TvShowModel;
import com.myapplication.made.submission4.viewmodel.TvShowViewModel;

import java.util.ArrayList;

import static com.myapplication.made.submission4.fragment.MovieFragment.EXTRA_STATE_FOR_FAV;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    private TvShowAdapter adapter;
    private ProgressBar progressBar;
    private TvShowViewModel tvShowViewModel;
    private boolean isForFavActivity, shouldRefreshOnResume = false;

    public TvShowFragment() {

    }

    public TvShowFragment(boolean isForFavActivity) {
        this.isForFavActivity = isForFavActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);

        if (savedInstanceState != null) {
            isForFavActivity = savedInstanceState.getBoolean(EXTRA_STATE_FOR_FAV);
        }

        tvShowViewModel = ViewModelProviders.of(getActivity()).get(TvShowViewModel.class);

        if (isForFavActivity) {
            tvShowViewModel.setTvShowFav(getActivity());
        } else {
            tvShowViewModel.setTvShows();
        }
        tvShowViewModel.getListTvShows().observe(getActivity(), getTvShow);

        adapter = new TvShowAdapter();
        adapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.rv_tv_show);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShowModel tvShowModel) {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_text) + " " + tvShowModel.getName(), Toast.LENGTH_SHORT).show();
                Intent moveWithObjectIntent = new Intent(getContext(), DetailTvShowActivity.class);
                moveWithObjectIntent.putExtra(DetailTvShowActivity.EXTRA_DATA, tvShowModel);
                startActivity(moveWithObjectIntent);
            }
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_STATE_FOR_FAV, isForFavActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldRefreshOnResume) {
            if (isForFavActivity) {
                tvShowViewModel.setTvShowFav(getActivity());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    private Observer<ArrayList<TvShowModel>> getTvShow = new Observer<ArrayList<TvShowModel>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvShowModel> tvShowModels) {
            if (tvShowModels != null) {
                adapter.setmData(tvShowModels);
                Log.d("Data list tvshow", adapter.getmData().toString());
                showLoading(false);
            }
        }
    };


    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}

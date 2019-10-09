package com.myapplication.made.submission4.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.myapplication.made.submission4.db.TvShowHelper;
import com.myapplication.made.submission4.model.GenreModel;
import com.myapplication.made.submission4.model.TvShowModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TvShowViewModel extends ViewModel implements LoadTvShowFavCallback {
    private static final String API_KEY = "99e9a8a9f7993100cfe848d86fdbbe26";
    private MutableLiveData<ArrayList<TvShowModel>> listTvShows = new MutableLiveData<>();


    public void setTvShows() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShowModel> listItems = new ArrayList<>();
        final ArrayList<GenreModel> listGenre = new ArrayList<>();

        String urlGenre = "http://api.themoviedb.org/3/genre/tv/list?api_key=" + API_KEY;
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US";


        client.get(urlGenre, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray genres = responseObject.getJSONArray("genres");
                    for (int i = 0; i < genres.length(); i++) {
                        JSONObject genre = genres.getJSONObject(i);
                        GenreModel genreModel = new GenreModel(genre);
                        listGenre.add(genreModel);
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Exception", error.getMessage());
            }
        });

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject tvShow = results.getJSONObject(i);

                        JSONArray arrayGenreIds = tvShow.getJSONArray("genre_ids");
                        ArrayList<String> nameGenre = new ArrayList<>();
                        for (int j = 0; j < arrayGenreIds.length(); j++) {
                            int item = arrayGenreIds.getInt(j);

                            for (int n = 0; n < listGenre.size(); n++) {
                                if (listGenre.get(n).getId() == item) {
                                    nameGenre.add(listGenre.get(n).getName());

                                    String namaGenreItem = listGenre.get(n).getName();
                                    arrayGenreIds.put(j, namaGenreItem);
                                }
                            }
                        }

                        Log.d("nameGenre", String.valueOf(nameGenre));
                        Log.d("arrayGenreIds", String.valueOf(arrayGenreIds));

                        TvShowModel tvShowModel = new TvShowModel(tvShow);
                        listItems.add(tvShowModel);
                    }
                    listTvShows.postValue(listItems);

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });

    }

    public void setTvShowFav(Context context) {
        TvShowHelper tvShowHelper = TvShowHelper.getInstance(context);
        tvShowHelper.open();
        new LoadTvShowFavAsync(tvShowHelper, this).execute();
    }

    public LiveData<ArrayList<TvShowModel>> getListTvShows() {
        return listTvShows;
    }


    @Override
    public void preExecute() {
        Log.d("TvShowViewModel", "preExecute: ");
    }

    @Override
    public void postExecute(ArrayList<TvShowModel> tvShowModels) {
        listTvShows.postValue(tvShowModels);
        Log.d("MovieViewModel", "postExecute" + tvShowModels.toString());
    }


    private static class LoadTvShowFavAsync extends AsyncTask<Void, Void, ArrayList<TvShowModel>> {
        private static final String TAG = LoadTvShowFavAsync.class.getSimpleName();
        private final WeakReference<TvShowHelper> weakTvShowHelper;
        private final WeakReference<LoadTvShowFavCallback> weakCallback;

        private LoadTvShowFavAsync(TvShowHelper tvShowHelper, LoadTvShowFavCallback callback) {
            weakTvShowHelper = new WeakReference<>(tvShowHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<TvShowModel> doInBackground(Void... voids) {
            return weakTvShowHelper.get().getAllTvShowsFav();
        }

        @Override
        protected void onPostExecute(ArrayList<TvShowModel> tvShowModels) {
            super.onPostExecute(tvShowModels);
            weakCallback.get().postExecute(tvShowModels);
        }
    }
}

interface LoadTvShowFavCallback {
    void preExecute();

    void postExecute(ArrayList<TvShowModel> tvShowModels);
}


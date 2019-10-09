package com.myapplication.made.submission4.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.myapplication.made.submission4.db.MovieHelper;
import com.myapplication.made.submission4.model.GenreModel;
import com.myapplication.made.submission4.model.MovieModel;
import com.myapplication.made.submission4.model.TvShowModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MovieViewModel extends ViewModel implements LoadMovieFavCallback {
    private static final String API_KEY = "99e9a8a9f7993100cfe848d86fdbbe26";
    private MutableLiveData<ArrayList<MovieModel>> listMovies = new MutableLiveData<>();

    public void setMovie() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieModel> listItems = new ArrayList<>();
        final ArrayList<GenreModel> listGenre = new ArrayList<>();

        String urlGenre = "http://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";

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
                        JSONObject movie = results.getJSONObject(i);

                        JSONArray arrayGenreIds = movie.getJSONArray("genre_ids");
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

                        MovieModel movieModel = new MovieModel(movie);
                        listItems.add(movieModel);
                    }
                    listMovies.postValue(listItems);
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


    public LiveData<ArrayList<MovieModel>> getMovies() {
        return listMovies;
    }

    public void setMovieFav(Context context) {
        MovieHelper movieHelper = MovieHelper.getInstance(context);
        movieHelper.open();
        new LoadMovieFavAsync(movieHelper, this).execute();
    }

    @Override
    public void preExecute() {
        Log.d("MovieViewModel", "preExecute");
    }

    @Override
    public void postExecute(ArrayList<MovieModel> listMovieModel) {
        listMovies.postValue(listMovieModel);
        Log.d("MovieViewModel", "postExecute" + listMovieModel.toString());
    }


    private static class LoadMovieFavAsync extends AsyncTask<Void, Void, ArrayList<MovieModel>> {
        private static final String TAG = LoadMovieFavAsync.class.getSimpleName();
        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadMovieFavCallback> weakCallback;

        private LoadMovieFavAsync(MovieHelper movieHelper, LoadMovieFavCallback callback) {
            weakMovieHelper = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(Void... voids) {
            return weakMovieHelper.get().getAllMoviesFav();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            weakCallback.get().postExecute(movieModels);
        }
    }
}

interface LoadMovieFavCallback {
    void preExecute();

    void postExecute(ArrayList<MovieModel> listMovieModel);
}

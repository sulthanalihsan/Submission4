package com.myapplication.made.submission4.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieModel implements Parcelable {
    private int id;
    private String posterPath;
    private String title;
    private String releaseDate;
    private double popularity;
    private int voteCount;
    private double voteAverage;
    private String originalLanguage;
    private ArrayList<Integer> genre;
    private ArrayList<GenreModel> listGenreModel;
    private ArrayList<String> listGenreString;
    private String overview;

    public MovieModel(JSONObject object) {
        try {

            int id = object.getInt("id");
            String posterPath = "https://image.tmdb.org/t/p/w500" + object.getString("poster_path");
            String title = object.getString("title");
            String releaseDate = object.getString("release_date");
            double popularity = object.getDouble("popularity");
            int voteCount = object.getInt("vote_count");
            double voteAverage = object.getDouble("vote_average");
            String originalLanguage = object.getString("original_language");

            final ArrayList<String> listGenreString = new ArrayList<>();
            JSONArray arrayGenreIds = object.getJSONArray("genre_ids");
            for (int i = 0; i < arrayGenreIds.length(); i++) {
                String item = arrayGenreIds.getString(i);
                listGenreString.add(item);
            }

            String overview = object.getString("overview");

            this.id = id;
            this.posterPath = posterPath;
            this.title = title;
            this.releaseDate = releaseDate;
            this.popularity = popularity;
            this.voteCount = voteCount;
            this.voteAverage = voteAverage;
            this.originalLanguage = originalLanguage;
            this.listGenreString = listGenreString;
            this.overview = overview;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<GenreModel> getListGenreModel() {
        return listGenreModel;
    }

    public void setListGenreModelMethod() {
        String API_KEY = "99e9a8a9f7993100cfe848d86fdbbe26";
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<GenreModel> listItems = new ArrayList<>();
        String url = "http://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray genres = responseObject.getJSONArray("genres");
                    for (int i = 0; i < genres.length(); i++) {
                        JSONObject genre = genres.getJSONObject(i);
                        GenreModel genreModel = new GenreModel(genre);
                        listItems.add(genreModel);
                    }
                    setListGenreModel(listItems);
                    Log.d("isigenre method", String.valueOf(listItems));
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

    public void setListGenreModel(ArrayList<GenreModel> listGenreModel) {
        this.listGenreModel = listGenreModel;
//        Log.d("List GenreModel", String.valueOf(getListGenreModel()));
    }

    public ArrayList<String> getListGenreString() {
        return listGenreString;
    }

    public void setListGenreString(ArrayList<String> listGenreString) {
        this.listGenreString = listGenreString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public ArrayList getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Integer> genre) {
        this.genre = genre;
    }

//
//    public void setGenre(ArrayList genre) {
//        this.genre = genre;
//    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public MovieModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.posterPath);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.originalLanguage);
        dest.writeList(this.genre);
        dest.writeList(this.listGenreModel);
        dest.writeStringList(this.listGenreString);
        dest.writeString(this.overview);
    }

    protected MovieModel(Parcel in) {
        this.id = in.readInt();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.popularity = in.readDouble();
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
        this.originalLanguage = in.readString();
        this.genre = new ArrayList<Integer>();
        in.readList(this.genre, Integer.class.getClassLoader());
        this.listGenreModel = new ArrayList<GenreModel>();
        in.readList(this.listGenreModel, GenreModel.class.getClassLoader());
        this.listGenreString = in.createStringArrayList();
        this.overview = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}

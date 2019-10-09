package com.myapplication.made.submission4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myapplication.made.submission4.model.MovieModel;

import java.util.ArrayList;
import java.util.Arrays;

import static android.provider.BaseColumns._ID;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.GENRE;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.ID_MOVIE;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.ORIGINAL_LANGUAGE;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.OVERVIEW;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.POPULARITY;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.POSTER_PATH;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.RELEASE_DATE;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.TITLE;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.VOTE_AVERAGE;
import static com.myapplication.made.submission4.db.DatabaseContract.MovieFavColumns.VOTE_COUNT;
import static com.myapplication.made.submission4.db.DatabaseContract.TABLE_FAV_MOVIE;

public class MovieHelper {
    private static final String TAG = MovieHelper.class.getSimpleName();
    private static final String DATABASE_TABLE = TABLE_FAV_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new MovieHelper(context);
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<MovieModel> getAllMoviesFav() {
        ArrayList<MovieModel> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " asc",
                null);
        cursor.moveToFirst();
        MovieModel movieModel;
        if (cursor.getCount() > 0) {
            do {
                movieModel = new MovieModel();
                movieModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_MOVIE)));
                movieModel.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movieModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movieModel.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movieModel.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                movieModel.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                movieModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                movieModel.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));

                String genre = cursor.getString(cursor.getColumnIndexOrThrow(GENRE));
                movieModel.setListGenreString(convertStringToArraylist(genre));

                movieModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                Log.d(TAG, "getAllMoviesFav: " + movieModel.toString());
                arrayList.add(movieModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public boolean checkMovieFav(int id) {
        String selectString = "SELECT * FROM " + TABLE_FAV_MOVIE + " WHERE " + ID_MOVIE + " =?";
        Cursor cursor = database.rawQuery(selectString, new String[]{String.valueOf(id)});
        boolean checkMovieFav = false;
        if (cursor.moveToFirst()) {
            checkMovieFav = true;
            int count = 0;
            while (cursor.moveToNext())
                count++;
            Log.d(TAG, String.format("%d record found", count));
        }
        cursor.close();
        return checkMovieFav;
    }

    public long insertMovieFav(MovieModel movieModel) {
        ContentValues args = new ContentValues();
        args.put(ID_MOVIE, movieModel.getId());
        args.put(POSTER_PATH, movieModel.getPosterPath());
        args.put(TITLE, movieModel.getTitle());
        args.put(RELEASE_DATE, movieModel.getReleaseDate());
        args.put(POPULARITY, movieModel.getPopularity());
        args.put(VOTE_COUNT, movieModel.getVoteCount());
        args.put(VOTE_AVERAGE, movieModel.getVoteAverage());
        args.put(ORIGINAL_LANGUAGE, movieModel.getOriginalLanguage());

        ArrayList<String> arrayList = movieModel.getListGenreString();
        args.put(GENRE, convertArraylistToString(arrayList));

        args.put(OVERVIEW, movieModel.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovieFav(int id) {
        return database.delete(DATABASE_TABLE, ID_MOVIE + "= '" + id + "'", null);
    }

    public ArrayList<String> convertStringToArraylist(String str) {
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(str.split(",")));
        return myList;
    }

    public String convertArraylistToString(ArrayList<String> arrayList) {
        String str = arrayList.toString().replaceAll("\\[|\\]", "");
        return str;
    }

}

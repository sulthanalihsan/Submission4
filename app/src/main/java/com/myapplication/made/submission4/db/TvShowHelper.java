package com.myapplication.made.submission4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myapplication.made.submission4.model.TvShowModel;

import java.util.ArrayList;
import java.util.Arrays;

import static android.provider.BaseColumns._ID;
import static com.myapplication.made.submission4.db.DatabaseContract.TABLE_FAV_TV_SHOW;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.FIRST_AIR_DATE;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.GENRE;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.ID_TV_SHOW;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.NAME;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.ORIGINAL_LANGUAGE;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.ORIGIN_COUNTRY;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.OVERVIEW;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.POPULARITY;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.POSTER_PATH;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.VOTE_AVERAGE;
import static com.myapplication.made.submission4.db.DatabaseContract.TvShowFavColumns.VOTE_COUNT;

public class TvShowHelper {
    private static final String TAG = TvShowHelper.class.getSimpleName();
    private static final String DATABASE_TABLE = TABLE_FAV_TV_SHOW;
    private static DatabaseHelper databaseHelper;
    private static TvShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvShowHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new TvShowHelper(context);
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

    public ArrayList<TvShowModel> getAllTvShowsFav() {
        ArrayList<TvShowModel> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " asc",
                null);
        cursor.moveToFirst();
        TvShowModel tvShowModel;
        if (cursor.getCount() > 0) {
            do {
                tvShowModel = new TvShowModel();
                tvShowModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_TV_SHOW)));
                tvShowModel.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                tvShowModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
                tvShowModel.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(FIRST_AIR_DATE)));
                tvShowModel.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));

                String originCountry = cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE));
                tvShowModel.setOriginCountry(convertStringToArraylist(originCountry));

                tvShowModel.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                tvShowModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                tvShowModel.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));

                String genre = cursor.getString(cursor.getColumnIndexOrThrow(GENRE));
                tvShowModel.setListGenreString(convertStringToArraylist(genre));

                tvShowModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                arrayList.add(tvShowModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }


    public long insertTvShowFav(TvShowModel tvShowModel) {
        ContentValues args = new ContentValues();
        args.put(ID_TV_SHOW, tvShowModel.getId());
        args.put(POSTER_PATH, tvShowModel.getPosterPath());
        args.put(NAME, tvShowModel.getName());
        args.put(FIRST_AIR_DATE, tvShowModel.getFirstAirDate());
        args.put(POPULARITY, tvShowModel.getPopularity());

        ArrayList<String> listOriginCountry = tvShowModel.getListGenreString();
        args.put(ORIGIN_COUNTRY, convertArraylistToString(listOriginCountry));

        args.put(VOTE_COUNT, tvShowModel.getVoteCount());
        args.put(VOTE_AVERAGE, tvShowModel.getVoteAverage());
        args.put(ORIGINAL_LANGUAGE, tvShowModel.getOriginalLanguage());

        ArrayList<String> listGenre = tvShowModel.getListGenreString();
        args.put(GENRE, convertArraylistToString(listGenre));

        args.put(OVERVIEW, tvShowModel.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteTvShowFav(int id) {
        return database.delete(DATABASE_TABLE, ID_TV_SHOW + "= '" + id + "'", null);
    }

    public boolean checkTvShowFav(int id) {
        String selectString = "SELECT * FROM " + TABLE_FAV_TV_SHOW + " WHERE " + ID_TV_SHOW + " =?";
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


    public ArrayList<String> convertStringToArraylist(String str) {
        ArrayList<String> myList = new ArrayList<String>(Arrays.asList(str.split(",")));
        return myList;
    }

    public String convertArraylistToString(ArrayList<String> arrayList) {
        String str = arrayList.toString().replaceAll("\\[|\\]", "");
        return str;
    }

}

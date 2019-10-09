package com.myapplication.made.submission4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.myapplication.made.submission4.R;
import com.myapplication.made.submission4.db.MovieHelper;
import com.myapplication.made.submission4.model.MovieModel;

public class DetailMovieActivity extends AppCompatActivity {
    private TextView dtlMovieTitle, dtlMovieReleaseDate, dtlMoviePopularity, dtlMovieVoteCount, dtlMovieVoteAverage, dtlMovieLanguage, dtlMovieGenre, dtlMovieOverview;
    private ImageView dtlImagePhoto;
    public static final String EXTRA_DATA = "extra_data";
    private boolean isFavorite = false;
    private MovieModel movieModel;
    private MenuItem addFav, delFav;

    private MovieHelper movieHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dtlImagePhoto = findViewById(R.id.dtl_imgPhoto);
        dtlMovieTitle = findViewById(R.id.dtl_movie_title);
        dtlMovieReleaseDate = findViewById(R.id.dtl_movie_release_date);
        dtlMoviePopularity = findViewById(R.id.dtl_movie_popularity);
        dtlMovieVoteCount = findViewById(R.id.dtl_movie_vote_count);
        dtlMovieVoteAverage = findViewById(R.id.dtl_movie_vote_average);
        dtlMovieLanguage = findViewById(R.id.dtl_movie_language);
        dtlMovieGenre = findViewById(R.id.dtl_movie_genre);
        dtlMovieOverview = findViewById(R.id.dtl_movie_overview);

        movieModel = getIntent().getParcelableExtra(EXTRA_DATA);

        Glide.with(this)
                .load(movieModel.getPosterPath())
                .apply(new RequestOptions().override(350, 550))
                .into(dtlImagePhoto);
//        dtlImagePhoto.setImageResource(movieModel.getPosterPath());
        dtlMovieTitle.setText(movieModel.getTitle());
        dtlMovieReleaseDate.setText(movieModel.getReleaseDate());
        dtlMoviePopularity.setText(Double.toString(movieModel.getPopularity()));
        dtlMovieVoteCount.setText(Integer.toString(movieModel.getVoteCount()));
        dtlMovieVoteAverage.setText(Double.toString(movieModel.getVoteAverage()));
        dtlMovieLanguage.setText(movieModel.getOriginalLanguage());
        dtlMovieGenre.setText(movieModel.getListGenreString().toString().replaceAll("\\[|\\]", ""));
        dtlMovieOverview.setText(movieModel.getOverview());


        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();
        if (movieHelper.checkMovieFav(movieModel.getId()))
            isFavorite = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        movieHelper.close();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_menu, menu);
        addFav = menu.findItem(R.id.action_add_fav);
        delFav = menu.findItem(R.id.action_del_fav);

        if (isFavorite) {
            delFav.setVisible(true);
            addFav.setVisible(false);
        } else {
            delFav.setVisible(false);
            addFav.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add_fav:
                long insert = movieHelper.insertMovieFav(movieModel);
                if (insert > 0) {
                    delFav.setVisible(true);
                    addFav.setVisible(false);
                    Toast.makeText(this, "berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "gagal ditambahkan", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_del_fav:
                long delete = movieHelper.deleteMovieFav(movieModel.getId());
                if (delete > 0) {
                    delFav.setVisible(false);
                    addFav.setVisible(true);
                    Toast.makeText(this, "berhasil dihapus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "gagal dihapus", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}

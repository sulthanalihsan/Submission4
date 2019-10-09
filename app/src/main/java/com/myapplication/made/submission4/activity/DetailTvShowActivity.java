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
import com.myapplication.made.submission4.db.TvShowHelper;
import com.myapplication.made.submission4.model.TvShowModel;

public class DetailTvShowActivity extends AppCompatActivity {
    private TextView dtlTvShowName, dtlTvShowReleaseDate, dtlTvShowPopularity, dtlTvShowCountry, dtlTvShowVoteCount, dtlTvShowVoteAverage, dtlTvShowLanguage, dtlTvShowGenre, dtlTvShowOverview;
    private ImageView dtlImagePhoto;
    public static final String EXTRA_DATA = "extra_data";

    private boolean isFavorite = false;
    private TvShowModel tvShowModel;
    private MenuItem addFav, delFav;


    private TvShowHelper tvShowHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dtlImagePhoto = findViewById(R.id.dtl_imgPhoto);
        dtlTvShowName = findViewById(R.id.dtl_tv_show_title);
        dtlTvShowReleaseDate = findViewById(R.id.dtl_tv_show_release_date);
        dtlTvShowPopularity = findViewById(R.id.dtl_tv_show_popularity);
        dtlTvShowCountry = findViewById(R.id.dtl_tv_show_country);
        dtlTvShowVoteCount = findViewById(R.id.dtl_tv_show_vote_count);
        dtlTvShowVoteAverage = findViewById(R.id.dtl_tv_show_vote_average);
        dtlTvShowLanguage = findViewById(R.id.dtl_tv_show_language);
        dtlTvShowGenre = findViewById(R.id.dtl_tv_show_genre);
        dtlTvShowOverview = findViewById(R.id.dtl_tv_show_overview);

        tvShowModel = getIntent().getParcelableExtra(EXTRA_DATA);

        Glide.with(this)
                .load(tvShowModel.getPosterPath())
                .apply(new RequestOptions().override(350, 550))
                .into(dtlImagePhoto);
//        dtlImagePhoto.setImageResource(movieModel.getPosterPath());
        dtlTvShowName.setText(tvShowModel.getName());
        dtlTvShowReleaseDate.setText(tvShowModel.getFirstAirDate());
        dtlTvShowPopularity.setText(Double.toString(tvShowModel.getPopularity()));
        dtlTvShowCountry.setText(tvShowModel.getOriginCountry().toString().replaceAll("\\[|\\]", ""));
        dtlTvShowVoteCount.setText(Integer.toString(tvShowModel.getVoteCount()));
        dtlTvShowVoteAverage.setText(Double.toString(tvShowModel.getVoteAverage()));
        dtlTvShowLanguage.setText(tvShowModel.getOriginalLanguage());
        dtlTvShowGenre.setText(tvShowModel.getListGenreString().toString().replaceAll("\\[|\\]", ""));
        dtlTvShowOverview.setText(tvShowModel.getOverview());

        tvShowHelper = TvShowHelper.getInstance(getApplicationContext());
        tvShowHelper.open();
        if (tvShowHelper.checkTvShowFav(tvShowModel.getId()))
            isFavorite = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                long insert = tvShowHelper.insertTvShowFav(tvShowModel);
                if (insert > 0) {
                    delFav.setVisible(true);
                    addFav.setVisible(false);
                    Toast.makeText(this, "berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "gagal ditambahkan", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_del_fav:
                long delete = tvShowHelper.deleteTvShowFav(tvShowModel.getId());
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

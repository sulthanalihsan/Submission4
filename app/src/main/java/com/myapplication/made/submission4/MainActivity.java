package com.myapplication.made.submission4;

import android.content.Intent;
import android.provider.Settings;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.myapplication.made.submission4.activity.FavoriteActivity;
import com.myapplication.made.submission4.adapter.PagerAdapter;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private final String STATE_TAB = "state_tab";
    int tabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Submission 4");
        }

        //menerapkan tablayout dan viewpager pada activity
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.viewPager);

        // Memanggil dan memasukan value pada class pageAdapetr (FragmentManager dan JumlahTab)
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), false);

        // memasang adapter pada viewPager
        viewPager.setAdapter(pagerAdapter);

        // Menambahkan Listener yang akan dipanggil kapan pun saat halaman berubah
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if (savedInstanceState == null) {
            viewPager.setCurrentItem(0);
        } else {
            int stateTab = savedInstanceState.getInt(STATE_TAB);
            viewPager.setCurrentItem(stateTab);
        }

        //callback interface saat tab dipilih
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab dipilih
                viewPager.setCurrentItem(tab.getPosition());
                tabPosition = tab.getPosition();
            }

            @Override
            //Dipanggil ketika tab tidak dipilih
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            //Dipanggil ketika tab dipilih kembali
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_TAB, tabPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_list_favorite) {
            Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

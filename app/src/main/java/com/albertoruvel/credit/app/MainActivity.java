package com.albertoruvel.credit.app;

import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.albertoruvel.credit.app.adapter.MainViewPagerAdapter;
import com.albertoruvel.credit.app.util.PrefsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.actionButton)
    FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        //create view pager adapter
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //credit cards
                        //button starts at bottom end
                        actionButton.setVisibility(View.VISIBLE);
                        actionButton.setImageResource(R.drawable.ic_add_white_24dp);
                        break;
                    case 1:
                        //summary
                        //move button to bottom middle
                        actionButton.setVisibility(View.VISIBLE);
                        actionButton.setImageResource(R.drawable.logo);
                        break;
                    case 2:
                        //current bank period
                        //hide button
                        actionButton.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //check if user is first timer
        if (PrefsUtils.isFirstTimeUser(this)) {
            //go to settings and set up application
            Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(R.string.firstTimeUsageMessage), BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.settings), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        }
                    });
            TextView text = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            text.setMaxLines(3);
            snackbar.show();
            return;
        }

    }

    private void animateActionButtonToMiddle() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        actionButton.animate().
                setDuration(150)
                .translationX(size.x / 2)
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mainSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        return true;
    }

    @OnClick(R.id.actionButton)
    public void actionButtonClick() {
        switch (viewPager.getCurrentItem()){
            case 0:
                //credit cards
                startActivity(new Intent(this, NewCreditCardActivity.class));
                break;
            case 1:
                //summary
                break;
            case 2:
                //current period
                break;
        }
    }
}

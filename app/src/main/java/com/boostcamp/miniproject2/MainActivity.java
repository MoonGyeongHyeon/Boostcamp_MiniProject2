package com.boostcamp.miniproject2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.boostcamp.miniproject2.database.DatabaseHelper;
import com.boostcamp.miniproject2.fragment.SortedFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton switchImageButton;
    private SortedFragment fragment;
    private Button nearbyButton;
    private Button popularButton;
    private Button recentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawerLayout();
        initSwitchButton();
        initFragment();
        initConditionButton();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
    }

    private void initDrawerLayout() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view_home);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home);

        ActionBarDrawerToggle toggle
                = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_contents_open, R.string.drawer_contents_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initSwitchButton() {
        switchImageButton = (ImageButton) findViewById(R.id.image_button_home_switch_print_method);
        switchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isSelected()) {
                    fragment.changePrintMethod(SortedFragment.METHOD_STAGGERED);
                    view.setSelected(false);
                } else {
                    fragment.changePrintMethod(SortedFragment.METHOD_LINEAR);
                    view.setSelected(true);
                }
            }
        });
    }

    private void initFragment() {
        fragment = SortedFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_fragment_container, fragment)
                .commit();
    }

    private void initConditionButton() {
        nearbyButton = (Button) findViewById(R.id.button_nearby_sort);
        nearbyButton.setOnClickListener(this);
        nearbyButton.setSelected(true);

        popularButton = (Button) findViewById(R.id.button_popular_sort);
        popularButton.setOnClickListener(this);

        recentButton = (Button) findViewById(R.id.button_recent_sort);
        recentButton.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home_drawer:
                Toast.makeText(getApplicationContext(), "Home!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_setting_drawer:
                Toast.makeText(getApplicationContext(), "Setting!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onClick(View view) {
        nearbyButton.setSelected(false);
        popularButton.setSelected(false);
        recentButton.setSelected(false);

        switch (view.getId()) {
            case R.id.button_nearby_sort:
                fragment.setCondition(DatabaseHelper.ORDER_BY_NEARBY);
                fragment.initViewData();

                nearbyButton.setSelected(true);
                break;
            case R.id.button_popular_sort:
                fragment.setCondition(DatabaseHelper.ORDER_BY_POPULAR);
                fragment.initViewData();

                popularButton.setSelected(true);
                break;
            case R.id.button_recent_sort:
                fragment.setCondition(DatabaseHelper.ORDER_BY_RECENT);
                fragment.initViewData();

                recentButton.setSelected(true);
                break;
        }
    }
}

package com.creative.longlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.fragment.FragmentUserCategory;
import com.creative.longlife.model.Category;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_MY_FRAGMENT = "myFragment";
    public static final String KEY_USER_CATEGORIES = "user_category_list";
    private static final int REQUEST_KEY_ALL_CATEGORY = 100;

    private FragmentUserCategory fragmentUserCategory;

    private Menu menu;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        initToolbar();

        init();

        initDrawer();

        if (savedInstanceState == null) {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity
            fragmentUserCategory = new FragmentUserCategory();

            getSupportFragmentManager()
                    .beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_layout, fragmentUserCategory, TAG_MY_FRAGMENT)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            fragmentUserCategory = (FragmentUserCategory) getSupportFragmentManager().findFragmentByTag(TAG_MY_FRAGMENT);
        }
    }

    private void init(){
        gson = new Gson();
    }


    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tv_name = (TextView) header.findViewById(R.id.tv_name);
        tv_name.setText(MydApplication.getInstance().getPrefManger().getUserProfile().getName());
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        tv_email.setText(MydApplication.getInstance().getPrefManger().getUserProfile().getName());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        this.menu = paramMenu;
        getMenuInflater().inflate(R.menu.menu_main, paramMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {

        switch (paramMenuItem.getItemId()) {

            case R.id.action_more_categories:

                Log.d("DEBUG","its clicked");
                //if you added fragment via layout xml
                proceedToAllCategoryActivity(fragmentUserCategory.userCategories());

                break;
            case R.id.action_search:

                break;
        }

        return false;
    }


    public void proceedToAllCategoryActivity(List<Category> user_category_list){

        Intent intent = new Intent(HomeActivity.this,AllCategoryActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_USER_CATEGORIES, (ArrayList<? extends Parcelable>) user_category_list);
        intent.putExtras(bundle);

       // intent.putExtra(KEY_USER_CATEGORIES, (Parcelable) user_category_list);

        startActivityForResult(intent, REQUEST_KEY_ALL_CATEGORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_KEY_ALL_CATEGORY) {
            if(resultCode == Activity.RESULT_OK){
                List<Category> user_category_list=data.getExtras().getParcelableArrayList(KEY_USER_CATEGORIES);
                //Log.d("DEBUG",String.valueOf(user_category_list));
                //call fragment method
                //TODO
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}

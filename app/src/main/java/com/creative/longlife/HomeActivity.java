package com.creative.longlife;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.fragment.FragmentAllCategory;
import com.creative.longlife.fragment.FragmentServiceList;
import com.creative.longlife.fragment.FragmentUserCategory;
import com.creative.longlife.model.Category;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private static final String TAG_USER_CATEGORY_FRAGMENT = "user_category_fragment";
    private static final String TAG_SERVICE_LIST_FRAGMENT = "service_list_fragment";
    private static final String TAG_All_CATEGORY_FRAGMENT = "all_category";
    public static final String KEY_USER_CATEGORIES = "user_category_list";
    public static final String KEY_CATEGORY = "category";
    private static final int REQUEST_KEY_ALL_CATEGORY = 100;
    private static final int ANIMATION_TYPE_ADD_FRAGMENT = 1;
    private static final int ANIMATION_TYPE_REMOVE_FRAGMENT = 0;

    private FragmentUserCategory fragmentUserCategory;

    private FragmentServiceList fragmentServiceList;
    private FragmentAllCategory fragmentAllCategory;

    private Menu menu;

    private Gson gson;

    private DrawerLayout drawer;


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

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_layout, fragmentUserCategory, TAG_USER_CATEGORY_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            fragmentUserCategory = (FragmentUserCategory) getSupportFragmentManager().findFragmentByTag(TAG_USER_CATEGORY_FRAGMENT);
        }
    }

    private void init() {
        gson = new Gson();
    }


    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        tv_email.setText(MydApplication.getInstance().getPrefManger().getUserProfile().getEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:

                gotoHomePage();

                break;
            case R.id.nav_profile:
                //Do some thing here
                // add navigation drawer item onclick method here
                break;
            case R.id.nav_reminder:
                //Do some thing here
                // add navigation drawer item onclick method here
                break;
            case R.id.nav_notification:
                //Do some thing here
                // add navigation drawer item onclick method here
                break;
            case R.id.nav_setting:
                //Do some thing here
                // add navigation drawer item onclick method here
                break;
            case R.id.nav_logout:
                //Do some thing here
                // add navigation drawer item onclick method here
                MydApplication.getInstance().getPrefManger().setUserProfile("");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawer.closeDrawers();

        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem paramMenuItem) {

        switch (paramMenuItem.getItemId()) {

            case R.id.action_search:

                break;
        }

        return false;
    }


    public void proceedToAllCategoryFragment(List<Category> user_category_list) {

        fragmentAllCategory = new FragmentAllCategory();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(KEY_USER_CATEGORIES, (ArrayList<? extends Parcelable>) user_category_list);
        fragmentAllCategory.setArguments(arguments);

        FragmentTransaction transaction = getTransaction(ANIMATION_TYPE_ADD_FRAGMENT);

        if (fragmentAllCategory.isAdded()) { // if the fragment is already in container
            transaction.show(fragmentAllCategory);
        } else { // fragment needs to be added to frame container
            transaction.add(R.id.content_layout, fragmentAllCategory, TAG_All_CATEGORY_FRAGMENT);
        }
        // Hide fragment B
        if (fragmentUserCategory.isAdded()) {
            transaction.hide(fragmentUserCategory);
        }
        transaction.commit();
    }


    public void proceedToServiceListFragment(Category category) {

        fragmentServiceList = new FragmentServiceList();
        Bundle arguments = new Bundle();
        arguments.putParcelable(KEY_CATEGORY, category);
        fragmentServiceList.setArguments(arguments);

        FragmentTransaction transaction = getTransaction(ANIMATION_TYPE_ADD_FRAGMENT);

        if (fragmentServiceList.isAdded()) { // if the fragment is already in container
            transaction.show(fragmentServiceList);
        } else { // fragment needs to be added to frame container
            transaction.add(R.id.content_layout, fragmentServiceList, TAG_SERVICE_LIST_FRAGMENT);
        }
        if (fragmentUserCategory.isAdded()) {
            transaction.hide(fragmentUserCategory);
        }
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (fragmentServiceList != null && fragmentServiceList.isAdded()) {
            gotoHomePage();
        } else if (fragmentAllCategory != null && fragmentAllCategory.isAdded()) {
            gotoHomePage();
        } else {
            super.onBackPressed();
        }
    }


    private void gotoHomePage() {

        if (fragmentServiceList != null && fragmentServiceList.isAdded()) { // if the fragment is already in container

            FragmentTransaction transaction = getTransaction(ANIMATION_TYPE_ADD_FRAGMENT);
            transaction.remove(fragmentServiceList);
            transaction.show(fragmentUserCategory);
            transaction.commit();
        }
        if (fragmentAllCategory != null && fragmentAllCategory.isAdded()) { // if the fragment is already in container

            FragmentTransaction transaction = getTransaction(ANIMATION_TYPE_REMOVE_FRAGMENT);
            transaction.remove(fragmentAllCategory);
            transaction.show(fragmentUserCategory);
            transaction.commit();
        }
    }


    private FragmentTransaction getTransaction(int animation_type) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (animation_type == ANIMATION_TYPE_ADD_FRAGMENT) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (animation_type == ANIMATION_TYPE_REMOVE_FRAGMENT) {
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        }
        return transaction;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (fragmentUserCategory.isAdded()) {
            fragmentUserCategory.filterSearch(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (fragmentUserCategory.isAdded()) {
            fragmentUserCategory.filterSearch(newText);
        }
        return true;
    }
}

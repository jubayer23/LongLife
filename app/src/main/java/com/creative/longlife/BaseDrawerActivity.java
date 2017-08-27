package com.creative.longlife;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.longlife.appdata.MydApplication;

/**
 * Created by jubayer on 7/11/2017.
 */

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //to be only accessed through methods, do not change access modifier
    private Toolbar toolbar;

    private DrawerLayout drawer;

    private  NavigationView navigationView;

    public Toolbar getToolbar() {
        return toolbar;
    }

    // Call this on very start of every activity

    /**
     * Default toolbar with no title and app's primary color as background
     */
    public void initToolbar() {
        setupToolbar(null, null, false);
    }

    /**
     * Default toolbar with no title, app's primary color as background and param to Enable/Disable
     * Home Button
     */
    public void initToolbar(boolean homeButtonEnabled) {
        setupToolbar(null, null, homeButtonEnabled);
    }

    /**
     * Default toolbar with title and app's primary color as background
     *
     * @param title
     */
    public void initToolbar(String title) {
        setupToolbar(title, null, true);
    }

    /**
     * Default toolbar with title, app's primary color as background and param to Enable/Disable
     * Home Button
     *
     * @param title
     */
    public void initToolbar(String title, boolean homeButtonEnabled) {
        setupToolbar(title, null, homeButtonEnabled);
    }

    private void setupToolbar(String title, String subTitle, boolean homeButtonEnabled) {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeButtonEnabled);
        if (title != null && !title.isEmpty())
            getSupportActionBar().setTitle(title);

    }

    public void setToolbarIcon(Bitmap profileImage) {
        //Make code for change toolbar icon
        Toast.makeText(this, "No associated code for changing icon of toolbar", Toast.LENGTH_LONG).show();
    }


    /**
     * [IMPORTANT] use this method only after initToolbar
     * Required if in between title alterations are required
     *
     * @param title
     */
    public void setTitle(String title) {
        if (toolbar != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * [IMPORTANT] use this method only after initToolbar
     * Required if in between subTitle alterations are required
     *
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        if (toolbar != null) {
            //toolbar.setSubtitle(subTitle);
        }
    }

    /**
     * In case toolbar is not required, then use this directly without init call
     */
    public void hideToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back button inside toolbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return false;
    }


    public DrawerLayout getDrawer() {
        return drawer;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

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



    private ProgressDialog progressDialog;

    public void showProgressDialog(String message, boolean isIntermidiate, boolean isCancelable) {
       /**/
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.setIndeterminate(isIntermidiate);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog == null) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}

package com.creative.longlife;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.fragment.FragmentAllCategory;
import com.creative.longlife.fragment.FragmentServiceList;
import com.creative.longlife.fragment.FragmentUserCategory;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.Notification;
import com.creative.longlife.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            fragmentUserCategory = (FragmentUserCategory) getSupportFragmentManager().findFragmentByTag(TAG_USER_CATEGORY_FRAGMENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        User user = MydApplication.getInstance().getPrefManger().getUserProfile();
        if (user.getLogin_type() != null && !user.getLogin_type().isEmpty()) {
            showDialogForChangePassword();
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
                Intent intent2 = new Intent(HomeActivity.this, Profile.class);
                startActivity(intent2);
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
                Intent intent3 = new Intent(HomeActivity.this, NotificationActivity.class);
                intent3.putExtra(GlobalAppAccess.KEY_CALL_FROM,GlobalAppAccess.TAG_HOME_ACTIVITY);
                startActivity(intent3);
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
        if (dialog_start!= null && dialog_start.isShowing()) {
            Log.d("DEBUG","called");

            super.onBackPressed();
        } else if (fragmentServiceList != null && fragmentServiceList.isAdded()) {
            gotoHomePage();
        } else if (fragmentAllCategory != null && fragmentAllCategory.isAdded()) {
            gotoHomePage();
        } else {
            super.onBackPressed();
        }
    }

    public static List<Category> selected_categories = new ArrayList<>();

    private void gotoHomePage() {
        FragmentTransaction transaction = getTransaction(ANIMATION_TYPE_REMOVE_FRAGMENT);

        if (fragmentServiceList != null && fragmentServiceList.isAdded()) {
            // if the fragment is already in container
            transaction.remove(fragmentServiceList);
        } else if (fragmentAllCategory != null && fragmentAllCategory.isAdded()) {
            // if the fragment is already in container
            selected_categories.clear();
            selected_categories.addAll(fragmentAllCategory.getUser_categories());
            transaction.remove(fragmentAllCategory);
            fragmentUserCategory.setUserCategories();

        }
        transaction.show(fragmentUserCategory);
        transaction.commit();
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


    Dialog dialog_start;

    private void showDialogForChangePassword() {
        dialog_start = new Dialog(HomeActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog_start.setCancelable(false);
        dialog_start.setContentView(R.layout.dialog_change_password);

        final EditText ed_old_password = (EditText) dialog_start.findViewById(R.id.ed_old_password);
        final EditText ed_new_password = (EditText) dialog_start.findViewById(R.id.ed_new_password);
        final EditText ed_confirm_password = (EditText) dialog_start.findViewById(R.id.ed_confirm_password);
        Button btn_send = (Button) dialog_start.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ed_old_password.getText().toString().isEmpty()) {
                    ed_old_password.setError("Fill up");
                    return;
                }
                if (ed_new_password.getText().toString().isEmpty()) {
                    ed_new_password.setError("Fill up");
                    return;
                }
                if (ed_confirm_password.getText().toString().isEmpty()) {
                    ed_confirm_password.setError("Fill up");
                    return;
                }

                if (!ed_confirm_password.getText().toString().equals(
                        ed_new_password.getText().toString()
                )) {
                    ed_confirm_password.setError("Not matched with new password.");
                    return;
                }

                String email = MydApplication.getInstance().getPrefManger().getUserProfile().getEmail();
                sendRequestToServerToChangePassword(GlobalAppAccess.URL_CHANGE_PASSWORD, email, ed_new_password.getText().toString());
            }
        });

        dialog_start.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(HomeActivity.this,"You must change the password first!",Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        dialog_start.show();
    }

    public void sendRequestToServerToChangePassword(String url, final String email_address, String new_password) {

        // TODO Auto-generated method stub
        url = url + "?email=" + email_address + "&password=" + new_password;

        showProgressDialog("Loading", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //Log.d("DEBUG",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int result = jsonObject.getInt("success");
                            if (result == 1) {

                                User user = MydApplication.getInstance().getPrefManger().getUserProfile();
                                user.setLogin_type("");
                                MydApplication.getInstance().getPrefManger().setUserProfile(user);

                                if (dialog_start.isShowing()) dialog_start.dismiss();
                                showSuccessDialog(GlobalAppAccess.SUCCESS);
                            } else {
                                showSuccessDialog(GlobalAppAccess.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialogForAnything.showAlertDialogWhenComplte(
                                    HomeActivity.this, "Error", "Server Not Responding", false
                            );
                        }
                        dismissProgressDialog();


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(HomeActivity.this, "ERROR", "Server Not Responding! Please try again later!", false);

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    private void showSuccessDialog(int code) {
        switch (code) {
            case GlobalAppAccess.SUCCESS:
                AlertDialogForAnything.showAlertDialogWhenComplte(HomeActivity.this, "Alert",
                        "Your password has been changed successfully!", true);
                break;
            case GlobalAppAccess.ERROR:
                AlertDialogForAnything.showAlertDialogWhenComplte(HomeActivity.this, "Alert",
                        "There is an error! Please try again later!", false);
                break;
        }
    }
}

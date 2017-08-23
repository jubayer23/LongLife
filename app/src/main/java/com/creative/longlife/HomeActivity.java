package com.creative.longlife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.creative.longlife.fragment.FragmentCategory;

public class HomeActivity extends BaseActivity {

    private static final String TAG_MY_FRAGMENT = "myFragment";

    private FragmentCategory fragmentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (savedInstanceState == null) {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity
            fragmentCategory = new FragmentCategory();

            getSupportFragmentManager()
                    .beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_layout, fragmentCategory, TAG_MY_FRAGMENT)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            fragmentCategory = (FragmentCategory) getSupportFragmentManager().findFragmentByTag(TAG_MY_FRAGMENT);
        }
    }
}

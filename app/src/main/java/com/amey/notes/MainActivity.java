package com.amey.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RelativeLayout optionscontainer;
    TextView headertextview;
    SearchView searchView;
    FloatingActionButton floatingActionButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
            context = this;

        init();

        optionscontainer = (RelativeLayout)findViewById(R.id.optionscontainer);
        headertextview = (TextView)findViewById(R.id.headertextview);
        headertextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = optionscontainer.getVisibility();
                if(visibility == 8){
                    optionscontainer.setVisibility(View.VISIBLE);
                }else{
                    optionscontainer.setVisibility(View.GONE);
                }
            }
        });

        /***
         * Animation
         */

        Animation animation;

       animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        optionscontainer .setAnimation(animation);
    }

    private void init() {
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,AddNotesActivity.class);
                startActivity(intent);
            }
        });

        searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocus();
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftKeyboard();

    }
}

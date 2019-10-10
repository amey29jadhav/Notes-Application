package com.amey.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amey.notes.Database.AddNotesTable;
import com.amey.notes.Database.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RelativeLayout optionscontainer;
    TextView headertextview, viewtypetextview, gridtypetextview;
    SearchView searchView;
    FloatingActionButton floatingActionButton;
    Context context;
     RecyclerView recycler_view;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    List<AddNotesTable> lstAddnotesTable;
    private Typeface fontAwesomeFont;

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

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);

        getData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        //recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL));
        recycler_view.setAdapter(mAdapter);

        /***
         * Animation
         */

        Animation animation;

       animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        optionscontainer .setAnimation(animation);
    }

    void getData(){
        try {
            lstAddnotesTable = DBHelper.getHelperInstance(context).getNotesDao().queryForAll();
            mAdapter = new NotesAdapter(this,context,lstAddnotesTable, AppSettings.Orientation.ListView);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "FontAwesome.otf");
        viewtypetextview = (TextView) findViewById(R.id.viewtypetextview);
        viewtypetextview.setTypeface(fontAwesomeFont);
        viewtypetextview.setText(getResources().getString(R.string.fa_list_ul));

        gridtypetextview = (TextView)findViewById(R.id.gridtypetextview);
        gridtypetextview.setTypeface(fontAwesomeFont);
        gridtypetextview.setText(getResources().getString(R.string.fa_th_large));

        viewtypetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewtypetextview.setVisibility(View.INVISIBLE);
                gridtypetextview.setVisibility(View.VISIBLE);

                //mAdapter = new NotesAdapter(context,lstAddnotesTable,AppSettings.Orientation.GridView);
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recycler_view.setLayoutManager(mGridLayoutManager);
                //recycler_view.addItemDecoration(new SpacesItemDecoration(10));
                //recycler_view.setAdapter(mAdapter);
            }
        });

        gridtypetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                recycler_view.setLayoutManager(linearLayoutManager);
                viewtypetextview.setVisibility(View.VISIBLE);
                gridtypetextview.setVisibility(View.INVISIBLE);



            }
        });


        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent(context,AddNotesActivity.class);
                startActivityForResult(intent,1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if(requestCode ==1){
                if(resultCode == Activity.RESULT_OK){
                    getData();
                    recycler_view.setAdapter(mAdapter);

                }

            }
    }
}

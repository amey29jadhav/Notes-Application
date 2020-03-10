package com.amey.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amey.notes.Database.AddNotesTable;
import com.amey.notes.Database.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MainActivity extends AppCompatActivity implements NotesFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    RelativeLayout optionscontainer;
    TextView headertextview, viewtypetextview, gridtypetextview;
    SearchView searchView;
    FloatingActionButton floatingActionButton;
    Context context;
     RecyclerView recycler_view;
    private RecyclerView.LayoutManager layoutManager;
    private NotesAdapter mAdapter;
    List<AddNotesTable> lstAddnotesTable;
    private Typeface fontAwesomeFont;
    ArrayList<String> arrayList;
    Button btnStart, btnStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
            context = this;

         FragmentManager fragmentManager = getSupportFragmentManager();
         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

         NotesFragment notesFragment = new NotesFragment();
         fragmentTransaction.add(R.id.fragment_container,notesFragment, "notesFragment");
         fragmentTransaction.addToBackStack(null);
         fragmentTransaction.commit();


        init();
        createObservable();

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
        //recycler_view.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL));
        recycler_view.setAdapter(mAdapter);

        /***
         * Animation
         */

        Animation animation;

       animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bottom_to_original);
        optionscontainer .setAnimation(animation);
        //checking git checkout method
    }

    public void startService(){
        Intent intent = new Intent(this,MyService.class);
        this.startService(intent);
    }

    public void stopService(){
        Intent intent = new Intent(this,MyService.class);
        this.stopService(intent);

    }



    private void createObservable() {

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
                mAdapter = new NotesAdapter(MainActivity.this,context,lstAddnotesTable, AppSettings.Orientation.GridView);

                //recycler_view.addItemDecoration(new SpacesItemDecoration(10));
                recycler_view.setAdapter(mAdapter);
            }
        });

        gridtypetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                recycler_view.setLayoutManager(linearLayoutManager);
                viewtypetextview.setVisibility(View.VISIBLE);
                gridtypetextview.setVisibility(View.INVISIBLE);
                mAdapter = new NotesAdapter(MainActivity.this,context,lstAddnotesTable, AppSettings.Orientation.ListView);
                recycler_view.setAdapter(mAdapter);




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
        searchView.clearFocus();
        //searchView.setFocusable(false);
        //searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);

                return true;
            }
        });

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService();
            }
        });

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
                    if(data != null && data.getExtras() != null){
                       AppSettings.Orientation orientation  = (AppSettings.Orientation) data.getSerializableExtra("viewtype");
                        System.out.println(orientation);
                    }
                    getData();
                    recycler_view.setAdapter(mAdapter);

                }

            }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NotesFragment2 notesFragment = new NotesFragment2();
        fragmentTransaction.replace(R.id.fragment_container,notesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public abstract class cellularplan {
        protected double rate;
        abstract void getRate();
        public void processBill(int minutes){
            System.out.println(minutes*rate);
        }
    }

    public class A{
        public void a(){

        }
    }
    public class abcNetwork extends A{
        public void getRate(){
            this.a();
        }
    }
    public class B{
        abcNetwork _abcNetwork;
        A a;
        public B(){
            a = new abcNetwork();


        }

    }
}

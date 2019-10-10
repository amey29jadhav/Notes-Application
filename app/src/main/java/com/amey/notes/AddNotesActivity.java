package com.amey.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amey.notes.Database.AddNotesTable;
import com.amey.notes.Database.DBHelper;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class AddNotesActivity extends AppCompatActivity {

    TextView datetimetextview, charactercount, headertextview, savetextview;
    EditText message_edittext;
    String characters = "characters";
    Toolbar toolbar;
    private Typeface fontAwesomeFont;
    private TextView backtextview;
    private String id;
    AddNotesTable addNotesTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_right);
        setContentView(R.layout.activity_add_notes);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        if(getIntent() != null && getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            id = bundle.getString("id");
        }

        if(!TextUtils.isEmpty(id)){
            try {
                addNotesTable = DBHelper.getHelperInstance(this).getNotesDao().queryBuilder().where().eq("id", id).queryForFirst();
                init(addNotesTable);
            }catch (Exception e){
                System.out.println();
            }
        }else {

            init();
        }

    }

    private void init(AddNotesTable... notesTables) {
        if(notesTables.length>0) {

            AddNotesTable notesTable = notesTables[0];
        }

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "FontAwesome.otf");
        backtextview= (TextView) findViewById(R.id.backtextview);
        backtextview.setTypeface(fontAwesomeFont);
        backtextview.setText(getResources().getString(R.string.fa_angle_left));
        backtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        headertextview = (TextView) findViewById(R.id.headertextview);
        headertextview.setText("Add Notes");

        datetimetextview = (TextView) findViewById(R.id.datetimetextview);
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM h:mm a");
        String currentdate=dateFormat.format(localCalendar.getTime());
        datetimetextview.setText(currentdate);

        charactercount = (TextView) findViewById(R.id.charactercount);
        charactercount.setText("0 characters");

        message_edittext = (EditText)findViewById(R.id.message_edittext);
        message_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charactercount.setText(Integer.toString(i2) +" "+ characters);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        savetextview = (TextView)findViewById(R.id.savetextview);
        savetextview.setTypeface(fontAwesomeFont);
        savetextview.setText(getResources().getString(R.string.fa_check));
        savetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = UUID.randomUUID().toString();
                saveNote(id);

            }
        });
    }

    private void saveNote(String id){
        AddNotesTable addNotesTable = new AddNotesTable();
        addNotesTable._id = id;
        addNotesTable.monthname = "october";
        if( message_edittext.getText() != null && !TextUtils.isEmpty(message_edittext.getText().toString())) {
            addNotesTable.title = message_edittext.getText().toString();
        }else{

        }

        if((datetimetextview.getText() != null) && !TextUtils.isEmpty(datetimetextview.getText().toString())){
            addNotesTable.time =  datetimetextview.getText().toString();
        }

        try {
            DBHelper.getHelperInstance(AddNotesActivity.this).getNotesDao().createIfNotExists(addNotesTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

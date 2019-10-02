package com.amey.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddNotesActivity extends AppCompatActivity {

    TextView datetimetextview, charactercount, headertextview;
    EditText message_edittext;
    String characters = "characters";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_right);
        setContentView(R.layout.activity_add_notes);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        init();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        headertextview = (TextView) findViewById(R.id.headertextview);
        headertextview.setText("Add Notes");

        datetimetextview = (TextView) findViewById(R.id.datetimetextview);
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        localCalendar.getTime();
        String currentdate=new SimpleDateFormat("d MMMM h:mm a").format(localCalendar.getTime());
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
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

package com.amey.notes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amey.notes.Database.AddNotesTable;
import com.amey.notes.Database.DBHelper;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddNotesActivity extends AppCompatActivity {

    TextView datetimetextview, charactercount, headertextview, savetextview;
    EditText message_edittext;
    String characters = "characters";
    Toolbar toolbar;
    private Typeface fontAwesomeFont;
    private TextView backtextview;
    private String id;
    AddNotesTable notesModel;
    CompositeDisposable disposable = new CompositeDisposable();
//    Observable<Integer> serverDownloadObservable = Observable.create(
//            emitter -> {
//                //SystemClock.sleep(5000); // simulate delay
//                saveNote();
//                emitter.onNext(5);
//                emitter.onComplete();
//    });

    Observable<AddNotesTable> addNotesTableObservable = Observable.create(e -> {
        saveNote();
        e.onNext(new AddNotesTable());
        e.onComplete();
    });


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
                notesModel = DBHelper.getHelperInstance(this).getNotesDao().queryBuilder().where().eq("id", id).queryForFirst();
                init();
            }catch (Exception e){
                System.out.println();
            }
        }else {
            init();
        }

    }

    private void init() {

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

        charactercount = (TextView) findViewById(R.id.charactercount);

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
                //testObservable();
                saveNote();

            }
        });
        if(notesModel != null) {

            message_edittext.setText(notesModel.title);
            message_edittext.setSelection(notesModel.title.length());
            charactercount.setText(Integer.toString(notesModel.title.length()) + " " + "characters");
            datetimetextview.setText(notesModel.time);
            id = notesModel._id;

        }else{
            message_edittext.setText("");
            charactercount.setText("0 characters");
            datetimetextview.setText(currentdate);
            notesModel = new AddNotesTable();
            id = UUID.randomUUID().toString();



        }
    }

    private void testObservable() {
       Disposable subscribe = addNotesTableObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(integer -> {
            finish();
           Toast.makeText(this, "Success " , Toast.LENGTH_SHORT).show();
        });
       disposable.add(subscribe);
    }

    private void saveNote(){
        //AddNotesTable addNotesTable = new AddNotesTable();
        notesModel._id = id;
        notesModel.monthname = "october";
        if( message_edittext.getText() != null && !TextUtils.isEmpty(message_edittext.getText().toString()) && message_edittext.getText().toString().length() > 0) {
            notesModel.title = message_edittext.getText().toString();
        }else{
            Toast.makeText(this,"Please Enter Title", Toast.LENGTH_SHORT).show();
            return;
        }

        if((datetimetextview.getText() != null) && !TextUtils.isEmpty(datetimetextview.getText().toString())){
            notesModel.time =  datetimetextview.getText().toString();
        }

        try {
            DBHelper.getHelperInstance(AddNotesActivity.this).getNotesDao().createOrUpdate(notesModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

package com.amey.notes.Database;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

@DatabaseTable(tableName =  "addnotestable")
public class AddNotesTable {

    @DatabaseField(columnName = "id",  id = true)
    public String _id;

    @DatabaseField(columnName = "title")
    public String title;

    @DatabaseField(columnName =  "monthname")
    public String monthname;

    @DatabaseField(columnName = "time")
    public String time;

    public AddNotesTable(){}

    public AddNotesTable(JSONObject jsonObject){
        _id = jsonObject.optString("id");
        title = jsonObject.optString("title");
        monthname = jsonObject.optString("monthname");
        time = jsonObject.optString("time");

    }




}

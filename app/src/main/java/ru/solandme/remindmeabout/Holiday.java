package ru.solandme.remindmeabout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Holiday {
    private String id;
    private String name;
    private String description;
    private int day;
    private int month;
    private String imageUri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public static Holiday fromJson(JSONObject jsonObject) {
        Holiday holiday = new Holiday();
        try {
            holiday.id = jsonObject.getString("id");
            holiday.name = jsonObject.getString("name");
            holiday.description = jsonObject.getString("description");
            holiday.day = jsonObject.getInt("day");
            holiday.month = jsonObject.getInt("month");
            holiday.imageUri = jsonObject.getString("imageUri");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return holiday;
    }


    public static ArrayList<Holiday> fromJson(JSONArray jsonArray) {
        JSONObject holidayJson;
        ArrayList<Holiday> holidays = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                holidayJson = jsonArray.getJSONObject(i);

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Holiday holiday = Holiday.fromJson(holidayJson);
            if (holiday != null) {
                holidays.add(holiday);
            }
        }
        return holidays;

    }
}

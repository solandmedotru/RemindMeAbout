package ru.solandme.remindmeabout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Holiday {
    private String id;
    private String name;
    private String day;
    private String month;
    private String imageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }


    public static Holiday fromJson(JSONObject jsonObject) {
        Holiday holiday = new Holiday();
        try {
            holiday.id = jsonObject.getString("id");
            holiday.name = jsonObject.getString("name");
            holiday.day = jsonObject.getString("day");
            holiday.month = jsonObject.getString("month");
            holiday.imageId = jsonObject.getString("imageId");
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

package ru.solandme.remindmeabout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.solandme.remindmeabout.Holiday;
import ru.solandme.remindmeabout.MyJSON;
import ru.solandme.remindmeabout.R;

public class HolidayFragment extends Fragment {


    public HolidayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(MyJSON.getDataFromRawDir(getContext(), R.raw.holidays));


            JSONArray jsonArray = jsonObject.getJSONArray("holidays");
            ArrayList<Holiday> holidays = Holiday.fromJson(jsonArray);

//            Log.e("TAG", "JsonObject" + holidays.get(1).getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_holiday, container, false);
    }
}

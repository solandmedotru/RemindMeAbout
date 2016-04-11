package ru.solandme.remindmeabout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import ru.solandme.remindmeabout.DBHelper;
import ru.solandme.remindmeabout.Holiday;
import ru.solandme.remindmeabout.MyJSON;
import ru.solandme.remindmeabout.R;
import ru.solandme.remindmeabout.adapters.HolidaysAdapter;

public class BirthdayFragment extends Fragment {
    public static final int LAYOUT = R.layout.fragment_birthday;
    protected View view;
    HolidaysAdapter holidaysAdapter;

    DBHelper dbHelper;

    public BirthdayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false); //надуваем вьюху

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvBirthday);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext()); //создаем новый LinearLayoutManager
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); //задаем ориентацию вертикальную
        recyclerView.setLayoutManager(linearLayoutManager); //устанавливаем для RV менеджера

        dbHelper = new DBHelper(getContext());
        holidaysAdapter = new HolidaysAdapter(dbHelper.getHolidaysByCategory("birthdays"));
        recyclerView.setAdapter(holidaysAdapter);

        dbHelper.close();

        return view;
    }

//    private ArrayList<Holiday> createListHolidayData() {
//        JSONObject jsonObject;
//        ArrayList<Holiday> birthdays = null;
//        if (!new File(getContext().getFilesDir().getPath() + "/" + "birthdays.json").exists()) {
//            try {
//                jsonObject = new JSONObject(MyJSON.getDataFromRawDir(getContext(), R.raw.birthdays));
//                MyJSON.saveData(getContext(), jsonObject.toString(), "birthdays.json");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            jsonObject = new JSONObject(MyJSON.getData(getContext(), "birthdays.json"));
//            JSONArray jsonArray = jsonObject.getJSONArray("birthdays");
//            birthdays = Holiday.fromJson(jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return birthdays;
//    }
}


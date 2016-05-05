package ru.solandme.remindmeabout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.solandme.remindmeabout.HolidayDBHelper;
import ru.solandme.remindmeabout.R;
import ru.solandme.remindmeabout.adapters.HolidaysAdapter;

public class HolidayFragment extends Fragment {
    public static final int LAYOUT = R.layout.fragment_holiday;
    protected View view;
    public static HolidaysAdapter holidaysAdapter;
    HolidayDBHelper holidayDbHelper;
    RecyclerView recyclerView;

    public HolidayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false); //надуваем вьюху

        recyclerView = (RecyclerView) view.findViewById(R.id.rvHolidays);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext()); //создаем новый LinearLayoutManager
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); //задаем ориентацию вертикальную
        recyclerView.setLayoutManager(linearLayoutManager); //устанавливаем для RV менеджера
        holidayDbHelper = new HolidayDBHelper(getContext());
        holidaysAdapter = new HolidaysAdapter(holidayDbHelper.getHolidaysByCategory("holidays"));
        recyclerView.setAdapter(holidaysAdapter);
        holidayDbHelper.close();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        holidaysAdapter = new HolidaysAdapter(holidayDbHelper.getHolidaysByCategory("holidays"));
        recyclerView.setAdapter(holidaysAdapter);
        holidayDbHelper.close();
    }
}

package ru.solandme.remindmeabout.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.solandme.remindmeabout.Holiday;
import ru.solandme.remindmeabout.MyJSON;
import ru.solandme.remindmeabout.R;


public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.ViewHolder> {

    Context context;
    ArrayList<Holiday> holidays;


    public HolidaysAdapter(ArrayList<Holiday> holidays) {
        this.holidays = holidays;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holiday, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Holiday holiday = holidays.get(position);
        holder.holidayName.setText(holiday.getName());
        holder.textHolidayDescription.setText(holiday.getDescription());
//        holder.textDays.setText(holiday.getDaysToHoliday());

        int imgResId = context.getResources().getIdentifier(holiday.getImageUri(), "drawable", context.getPackageName());
        holder.imageHoliday.setImageResource(imgResId);


    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView holidayName;
        TextView textDays;
        TextView textHolidayDescription;
        ImageView imageHoliday;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);

            holidayName = (TextView) itemView.findViewById(R.id.textDisplayHolidayName);

            textHolidayDescription = (TextView) itemView.findViewById(R.id.textHolidayDescription);

            textDays = (TextView) itemView.findViewById(R.id.textDays);

            imageHoliday = (ImageView) itemView.findViewById(R.id.imageHoliday);
        }
    }
}

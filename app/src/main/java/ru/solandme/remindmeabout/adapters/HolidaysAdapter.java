package ru.solandme.remindmeabout.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import ru.solandme.remindmeabout.AddEditDialog;
import ru.solandme.remindmeabout.Holiday;
import ru.solandme.remindmeabout.R;


public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.ViewHolder> {

    Context context;
    ArrayList<Holiday> holidays;
    private static final String HOLIDAY = "holiday";
    private static final int HOLIDAY_REQUEST = 1;


    public HolidaysAdapter(ArrayList<Holiday> holidays) {
        this.holidays = holidays;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_holiday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Holiday holiday = holidays.get(position); //получаю экземпляр праздника по позиции из массива всех праздников
        holder.holidayName.setText(holiday.getName());
        holder.textHolidayDescription.setText(holiday.getDescription());
        holder.textDays.setText(getDaysForHolidayDate(holiday.getDate()));

        Bitmap bmp = BitmapFactory.decodeFile(context.getFilesDir().getPath() + "/images/" + holiday.getImageUri());
        holder.imageHoliday.setImageBitmap(bmp);

        holder.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Holiday holiday = holidays.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, AddEditDialog.class);
                intent.putExtra(HOLIDAY, holiday);
                intent.putExtra("isActionEdit", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView holidayName;
        TextView textDays;
        TextView textHolidayDescription;
        ImageView imageHoliday;
        ImageView actionEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            holidayName = (TextView) itemView.findViewById(R.id.textDisplayHolidayName);
            textHolidayDescription = (TextView) itemView.findViewById(R.id.textHolidayDescription);
            textDays = (TextView) itemView.findViewById(R.id.textDays);
            imageHoliday = (ImageView) itemView.findViewById(R.id.imageHoliday);
            actionEdit = (ImageView) itemView.findViewById(R.id.img_action_edit);
        }
    }

    private String getDaysForHolidayDate(Long holidaysDate) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar holidayCalendar = Calendar.getInstance();
        holidayCalendar.setTimeInMillis(holidaysDate);

        int todayYear = todayCalendar.get(Calendar.YEAR);
        int month = holidayCalendar.get(Calendar.MONTH);
        int day = holidayCalendar.get(Calendar.DAY_OF_MONTH);

        int daysInYear = todayCalendar.getActualMaximum(Calendar.DAY_OF_YEAR); //максимум дней в этом году
        holidayCalendar.set(todayYear, month, day);
        int days = (int) ((holidayCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / 1000) / 86400;
        int hours = (int) ((holidayCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / 1000) / 3600;

        if (days >= 1) {
            return context.getResources().getQuantityString(R.plurals.days, days, days);
        } else if (days < -2) {
            return context.getResources().getQuantityString(R.plurals.days, daysInYear + days, daysInYear + days);
        } else if ((days == 0) && (hours < 12)) {
            return " " + context.getString(R.string.textNow);
        } else if ((days == 0) && (hours >= 12)) {
            return context.getResources().getQuantityString(R.plurals.hours, hours, hours);
        } else {
            return " " + context.getString(R.string.textFinish);
        }
    }
}

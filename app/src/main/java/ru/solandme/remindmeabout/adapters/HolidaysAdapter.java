package ru.solandme.remindmeabout.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.solandme.remindmeabout.AddEditDialog;
import ru.solandme.remindmeabout.Holiday;
import ru.solandme.remindmeabout.R;
import ru.solandme.remindmeabout.SlidePagerActivity;


public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.ViewHolder> {

    private Context context;
    private List<Holiday> holidays;
    private static final String HOLIDAY = "holiday";
    private static final int HOLIDAY_REQUEST = 1;


    public HolidaysAdapter(List<Holiday> holidays) {
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

        final Holiday holiday = holidays.get(position); //получаю экземпляр праздника по позиции из массива всех праздников

        if(holiday.getDate() == 0){
            holder.textDays.setVisibility(View.INVISIBLE);
            holder.textData.setVisibility(View.INVISIBLE);
        }

        holder.holidayName.setText(holiday.getName());
        holder.textHolidayDescription.setText(holiday.getDescription());
        holder.textDays.setText(parseDay(holiday.getDaysLeft()));

        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
        String monthAndDayText = DateUtils.formatDateTime(context, holiday.getDate(), flags);
        holder.textData.setText(monthAndDayText);

        Bitmap bmp = BitmapFactory.decodeFile(context.getFilesDir().getPath() + "/images/" + holiday.getImageUri());
        holder.imageHoliday.setImageBitmap(bmp);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAddEditActivity(holder);
                return true;
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextCongratulation(holiday);
            }
        });

        holder.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditActivity(holder);
            }
        });
    }

    private void showAddEditActivity(ViewHolder holder) {
        Holiday holiday = holidays.get(holder.getAdapterPosition());
        Intent intent = new Intent(context, AddEditDialog.class);
        intent.putExtra(HOLIDAY, holiday);
        intent.putExtra("isActionEdit", true);
        context.startActivity(intent);
    }

    private void showTextCongratulation(Holiday holiday) {
        Intent intent = new Intent(context, SlidePagerActivity.class);
        intent.putExtra("holidayName", holiday.getName());
        intent.putExtra("code", holiday.getCode());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView holidayName;
        TextView textDays;
        TextView textHolidayDescription;
        ImageView imageHoliday;
        ImageView actionEdit;
        TextView textData;

        ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            holidayName = (TextView) itemView.findViewById(R.id.textDisplayHolidayName);
            textHolidayDescription = (TextView) itemView.findViewById(R.id.textHolidayDescription);
            textDays = (TextView) itemView.findViewById(R.id.textDays);
            imageHoliday = (ImageView) itemView.findViewById(R.id.imageHoliday);
            actionEdit = (ImageView) itemView.findViewById(R.id.img_action_edit);
            textData = (TextView) itemView.findViewById(R.id.textData);
        }
    }

    private String parseDay(int days){
        if (days >= 1) {
            return context.getResources().getQuantityString(R.plurals.days, days, days);
        } else if ((days == 0)) {
            return " " + context.getString(R.string.textNow);
        } else {
            return " " + context.getString(R.string.textFinish);
        }
    }
}

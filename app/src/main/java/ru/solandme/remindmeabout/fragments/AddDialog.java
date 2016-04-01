package ru.solandme.remindmeabout.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.solandme.remindmeabout.MyJSON;
import ru.solandme.remindmeabout.R;

public class AddDialog extends DialogFragment implements DialogInterface.OnClickListener {

    View form = null;
    JSONObject jsonObject;
    JSONObject newJsonObject;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater().inflate(R.layout.add_form, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle("Добавление нового праздника")
                .setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        EditText add_holidayName = (EditText) form.findViewById(R.id.add_holiday_name);
        EditText add_holidayDescription = (EditText) form.findViewById(R.id.add_holiday_description);

        String holidayName = add_holidayName.getText().toString();
        String holidayDescription = add_holidayDescription.getText().toString();

        try {
            jsonObject = new JSONObject(MyJSON.getData(getContext(), "holidays.json"));
            JSONArray jsonArray = jsonObject.getJSONArray("holidays");

            newJsonObject = new JSONObject();

            newJsonObject
                    .put("id", "0")
                    .put("name", holidayName)
                    .put("description", holidayDescription)
                    .put("day", "0")
                    .put("month", "0")
                    .put("imageUri", "june");

            jsonArray.put(newJsonObject);
            JSONObject newJsonObject2 = new JSONObject();
            newJsonObject2.put("holidays", jsonArray);

            String text = newJsonObject2.toString();
            MyJSON.saveData(getContext(), text, "holidays.json");

            HolidayFragment.holidaysAdapter.notifyDataSetChanged();
            BirthdayFragment.holidaysAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

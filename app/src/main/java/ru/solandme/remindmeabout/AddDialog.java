package ru.solandme.remindmeabout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.solandme.remindmeabout.MyJSON;
import ru.solandme.remindmeabout.R;
import ru.solandme.remindmeabout.fragments.HolidayFragment;

public class AddDialog extends AppCompatActivity {

    JSONObject jsonObject;
    JSONObject newJsonObject;
    String holidayName;
    String holidayDescription;
    EditText add_holidayName;
    EditText add_holidayDescription;
    public static final int RESULT_SAVE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form);
        Holiday holiday = (Holiday)getIntent().getSerializableExtra("holiday");
        setTitle(holiday.getName());
        initView();
    }

    private void initView() {
        add_holidayName = (EditText) findViewById(R.id.add_holiday_name);
        add_holidayDescription = (EditText) findViewById(R.id.add_holiday_description);
    }


    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_save:
                try {
                    holidayName = add_holidayName.getText().toString();
                    holidayDescription = add_holidayDescription.getText().toString();
                    jsonObject = new JSONObject(MyJSON.getData(getApplicationContext(), "holidays.json"));
                    JSONArray jsonArray = jsonObject.getJSONArray("holidays");

                    newJsonObject = new JSONObject();

                    newJsonObject
                            .put("id", "0")
                            .put("name", holidayName)
                            .put("description", holidayDescription)
                            .put("day", "0")
                            .put("month", "0")
                            .put("imageUri", "june")
                            .put("category", "holidays");

                    jsonArray.put(newJsonObject);
                    JSONObject newJsonObject2 = new JSONObject();
                    newJsonObject2.put("holidays", jsonArray);

                    String text = newJsonObject2.toString();
                    MyJSON.saveData(getApplicationContext(), text, "holidays.json");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_SAVE);
                finish();
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }

    }
}

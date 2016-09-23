package ru.solandme.remindmeabout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import ru.solandme.remindmeabout.AddEditCongratulateDialog;
import ru.solandme.remindmeabout.Congratulation;
import ru.solandme.remindmeabout.R;
import ru.solandme.remindmeabout.SlidePagerActivity;
import ru.solandme.remindmeabout.database.CongratulateDBHelper;

public class SlidePageFragment extends Fragment {

    public static final String ARG_POSITION = "item_position";
    public static final String ARG_COUNT = "item_count";
    public static final String CONGRATULATION = "congratulation";


    private TextView textContainer;
    private TextView textCounter;
    private CheckBox checkBoxAddFavorite;
    private View colorTeg;
    private Congratulation congratulation;


    private CongratulateDBHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slide_page, container, false);
        final Bundle args = getArguments();
        congratulation = (Congratulation) args.getSerializable(CONGRATULATION);
        textCounter = (TextView) rootView.findViewById(R.id.text_counter);
        textContainer = (TextView) rootView.findViewById(R.id.text_container);
        colorTeg = rootView.findViewById(R.id.colorTeg);

        checkBoxAddFavorite = (CheckBox) rootView.findViewById(R.id.chb_add_to_fw);

        textCounter.setText(args.getInt(ARG_POSITION) + " / " + args.getInt(ARG_COUNT));
        textContainer.setText(congratulation.getText());

        textContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getContext(), AddEditCongratulateDialog.class);
                intent.putExtra(CONGRATULATION, congratulation);
                intent.putExtra("code", congratulation.getCode());
                intent.putExtra("isActionEdit", true);
                startActivity(intent);
                return true;
            }
        });


        if (congratulation.getFavorite().equals(SlidePagerActivity.OFF)) {
            checkBoxAddFavorite.setChecked(false);
        } else checkBoxAddFavorite.setChecked(true);


        if (congratulation.getVerse().equals(SlidePagerActivity.OFF)) {
            textContainer.setGravity(Gravity.START);
        }

        if (congratulation.getCode().equals(SlidePagerActivity.FORHER)) {
            colorTeg.setBackgroundColor(getResources().getColor(R.color.colorForHer));

        } else if (congratulation.getCode().equals(SlidePagerActivity.FORHIM)) {
            colorTeg.setBackgroundColor(getResources().getColor(R.color.colorForHim));
        } else {
            colorTeg.setBackgroundColor(getResources().getColor(R.color.colorWhiteDark));
        }

        checkBoxAddFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                helper = new CongratulateDBHelper(getContext());
                if (isChecked) {
                    helper.setFavorite(congratulation.get_id());
                } else {
                    helper.clearFavorite(congratulation.get_id());
                }
            }
        });
        return rootView;
    }

}

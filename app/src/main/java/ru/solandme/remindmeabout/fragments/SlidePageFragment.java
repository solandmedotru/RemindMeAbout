package ru.solandme.remindmeabout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.solandme.remindmeabout.R;

public class SlidePageFragment extends Fragment {

    public static final String ARG_POSITION = "item_position";
    public static final String ARG_COUNT = "item_count";
    public static final String ARG_TEXT = "item_text";
    TextView text_container;
    TextView text_counter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slide_page, container, false);

        text_counter = (TextView) rootView.findViewById(R.id.text_counter);
        text_container = (TextView) rootView.findViewById(R.id.text_container);

        Bundle args = getArguments();

        text_container.setText(args.getString(ARG_TEXT));
        text_counter.setText(args.getInt(ARG_POSITION) + " / " + args.getInt(ARG_COUNT));
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, text_container.getText());
                startActivity(intent);
                break;
        }
        return true;
    }
}

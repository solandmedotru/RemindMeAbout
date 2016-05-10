package ru.solandme.remindmeabout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.solandme.remindmeabout.R;

public class SlidePageFragment extends Fragment {

    public static final String ARG_POSITION = "item_position";
    public static final String ARG_COUNT = "item_count";
    public static final String ARG_TEXT = "item_text";
    TextView text_container;
    TextView text_counter;

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

        text_counter = (TextView) rootView.findViewById(R.id.text_counter);
        text_container = (TextView) rootView.findViewById(R.id.text_container);

        Bundle args = getArguments();

        text_container.setText(args.getString(ARG_TEXT));
        text_counter.setText(args.getInt(ARG_POSITION) + " / " + args.getInt(ARG_COUNT));

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, text_container.getText());
                String chooserTitle = getString(R.string.chooser_title);
                Intent chooserIntent = Intent.createChooser(intent, chooserTitle);
                startActivity(chooserIntent);
                break;
            case R.id.about_app_menu_item:
                Toast.makeText(getContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_slide_activity_menu, menu);
    }
}

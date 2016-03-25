package ru.solandme.remindmeabout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolBarMainActivity);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
            toolbar.inflateMenu(R.menu.toolbar_main_menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_app_menu_item:
                Toast.makeText(getApplicationContext(), item.getTitle().toString(), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}

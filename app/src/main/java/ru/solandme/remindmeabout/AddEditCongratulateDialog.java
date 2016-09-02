package ru.solandme.remindmeabout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import ru.solandme.remindmeabout.database.CongratulateDBHelper;
import ru.solandme.remindmeabout.fragments.SlidePageFragment;

public class AddEditCongratulateDialog extends AppCompatActivity {
    private Toolbar toolbar;

    private CheckBox checkBoxSms;
    private CheckBox checkBoxVerse;
    private CheckBox checkBoxFavorite;

    private CheckBox checkBoxForHim;
    private CheckBox checkBoxForHer;
    private CheckBox checkBoxForAll;

    private Button btnSaveCongratulation;
    private Button btnCancel;
    private Button btnDelete;

    EditText editCongratulationText;

    private CongratulateDBHelper congratulateDBHelper;

    public static final String OFF = "0";
    public static final String ON = "1";
    public static final String FORHIM = "1";
    public static final String FORHER = "2";
    public static final String FORALL = "0";


    private String smsFlag = OFF; // on - 1, off - 0
    private String verseFlag = ON; // on - 1, off - 0
    private String favoriteFlag = OFF; // on - 1, off - 0
    private String forHimFlag = OFF;
    private String forHerFlag = OFF;
    private String forAllFlag = ON;

    private Congratulation congratulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_congratulate_dialog);

        congratulation = (Congratulation) getIntent().getSerializableExtra(SlidePageFragment.CONGRATULATION);
        congratulateDBHelper = new CongratulateDBHelper(getApplicationContext());
        initToolBar();
        initView();
    }

    private void initView() {


        checkBoxSms = (CheckBox) findViewById(R.id.chb_sms);
        checkBoxVerse = (CheckBox) findViewById(R.id.chb_verse);
        checkBoxFavorite = (CheckBox) findViewById(R.id.chb_favorite);

        checkBoxForHim = (CheckBox) findViewById(R.id.chb_for_him);
        checkBoxForHer = (CheckBox) findViewById(R.id.chb_for_her);
        checkBoxForAll = (CheckBox) findViewById(R.id.chb_for_all);


        if (congratulation.getCode().equals(Holiday.CODE_WOMANSDAY) ||
                congratulation.getCode().equals(Holiday.CODE_MANSDAY)) {
            checkBoxForHer.setEnabled(false);
            checkBoxForHim.setEnabled(false);
            checkBoxForAll.setEnabled(false);
        }

        checkBoxForHim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    congratulation.setFilter(FORHIM);
                }
            }
        });

        checkBoxForHer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    congratulation.setFilter(FORHER);
                }
            }
        });

        checkBoxForAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    congratulation.setFilter(FORALL);
                }
            }
        });
        checkBoxSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    congratulation.setSms(ON);
                } else {
                    congratulation.setSms(OFF);
                }
            }
        });
        checkBoxVerse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    congratulation.setVerse(ON);
                } else {
                    congratulation.setVerse(OFF);
                }
            }
        });
        checkBoxFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    congratulation.setFavorite(ON);
                } else {
                    congratulation.setFavorite(OFF);
                }
            }
        });


        editCongratulationText = (EditText) findViewById(R.id.edit_text_congratulate);

        btnSaveCongratulation = (Button) findViewById(R.id.btn_congratulation_save);
        btnCancel = (Button) findViewById(R.id.btn_congratulation_cancel);
        btnDelete = (Button) findViewById(R.id.btn_congratulation_delete);
        btnSaveCongratulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getBooleanExtra("isActionEdit", true)) {
                    congratulation.setText(editCongratulationText.getText().toString());
                    congratulateDBHelper.replaceCongratulationOnDB(congratulation);
                } else {
                    congratulation.setText(editCongratulationText.getText().toString());
                    congratulateDBHelper.addCongratulationToDB(congratulation);
                }

                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getBooleanExtra("isActionEdit", true)) {
                    congratulateDBHelper.deleteCongratulationFromDB(congratulation);
                }
                finish();
            }
        });


        setCheckBoxes();
    }

    private void setCheckBoxes() {
        EditText editTextCongratulate = (EditText) findViewById(R.id.edit_text_congratulate);
        if (getIntent().getBooleanExtra("isActionEdit", true)) {
            editTextCongratulate.setText(congratulation.getText());
            checkBoxSms.setChecked(congratulation.getSms().equals(ON));
            checkBoxVerse.setChecked(congratulation.getVerse().equals(ON));
            checkBoxFavorite.setChecked(congratulation.getFavorite().equals(ON));
            checkBoxForHim.setChecked(congratulation.getFilter().equals(FORHIM));
            checkBoxForHer.setChecked(congratulation.getFilter().equals(FORHER));
            checkBoxForAll.setChecked(congratulation.getFilter().equals(FORALL));
        } else {
            editTextCongratulate.setHint(R.string.add_your_text);
            checkBoxSms.setChecked(smsFlag.equals(ON));
            checkBoxVerse.setChecked(verseFlag.equals(ON));
            checkBoxFavorite.setChecked(favoriteFlag.equals(ON));
            checkBoxForHim.setChecked(forHimFlag.equals(ON));
            checkBoxForHer.setChecked(forHerFlag.equals(ON));
            checkBoxForAll.setChecked(forAllFlag.equals(ON));
        }
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolBarAddEditActivity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            if (getIntent().getBooleanExtra("isActionEdit", true)) {
                getSupportActionBar().setTitle(R.string.edit_congratulate);
            } else {
                getSupportActionBar().setTitle(R.string.add_new_congratulate);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
}

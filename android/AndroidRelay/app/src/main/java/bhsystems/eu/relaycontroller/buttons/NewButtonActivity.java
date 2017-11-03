package bhsystems.eu.relaycontroller.buttons;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import bhsystems.eu.relaycontroller.R;
import bhsystems.eu.relaycontroller.entity.RelayControllerButton;
import bhsystems.eu.relaycontroller.utils.spinner.RCSpinnerHelper;
import bhsystems.eu.relaycontroller.utils.spinner.RCLabelledSpinner;

/**
 * Created by brunohorta on 03/11/2017.
 */

public class NewButtonActivity extends AppCompatActivity {

    private ImageButton ibMinus;
    private ImageButton ibPlus;
    private EditText etPoints;
    private TextInputLayout tilLabel;
    private RCLabelledSpinner rcLabelledSpinner;

    private final Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;

    private static final long REP_DELAY = 50;

    private RelayControllerButton relayControllerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_button);
        ibMinus = findViewById(R.id.ib_minus);
        ibPlus = findViewById(R.id.ib_plus);
        etPoints = findViewById(R.id.et_points);
        tilLabel = findViewById(R.id.input_layout_label);
        rcLabelledSpinner = findViewById(R.id.sp_type);

        relayControllerButton = new RelayControllerButton("", RelayControllerButton.RelayControllerButtonType.TOGGLE, 23);

        initPin();
        initTypesSpinner();
    }

    private void initPin() {
        etPoints.setEnabled(false);
        etPoints.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        etPoints.setText(relayControllerButton.getPin() + "");

        ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrement();
            }
        });
        ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increment();
            }
        });
        ibPlus.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                mAutoIncrement = true;
                repeatUpdateHandler.post(new RptUpdater());
                return false;
            }
        });

        ibPlus.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && (mAutoIncrement || relayControllerButton.getPin() == 53)) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.performClick();
                    }
                    mAutoIncrement = false;
                }
                return false;
            }
        });
        ibMinus.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        mAutoDecrement = true;
                        repeatUpdateHandler.post(new RptUpdater());
                        return false;
                    }
                }
        );
        ibMinus.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && (mAutoDecrement || relayControllerButton.getPin() == 23)) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.performClick();
                    }
                    mAutoDecrement = false;
                }
                return false;
            }
        });
        checkAddRemoveBtnStatus();
    }

    class RptUpdater implements Runnable {

        public void run() {
            if (mAutoIncrement) {
                if (relayControllerButton.getPin() == 53) {
                    mAutoIncrement = false;
                    return;
                }
                increment();
                repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
            } else if (mAutoDecrement) {
                if (relayControllerButton.getPin() == 23) {
                    mAutoDecrement = false;
                    return;
                }
                decrement();
                repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
            }
        }
    }

    private void increment() {
        relayControllerButton.setPin(relayControllerButton.getPin() + 1);
        etPoints.setText(String.valueOf(relayControllerButton.getPin()));
        checkAddRemoveBtnStatus();
    }

    private void decrement() {
        relayControllerButton.setPin(relayControllerButton.getPin() - 1);
        etPoints.setText(String.valueOf(relayControllerButton.getPin()));
        checkAddRemoveBtnStatus();
    }

    void checkAddRemoveBtnStatus() {
        ibMinus.setEnabled(relayControllerButton.getPin() > 23);
        ibPlus.setEnabled(relayControllerButton.getPin() < 53);
    }

    private void initTypesSpinner() {
        ArrayList<RCSpinnerHelper> helper = new ArrayList<>();
        helper.add(new RCSpinnerHelper(RelayControllerButton.RelayControllerButtonType.TOGGLE, getString(getResources().getIdentifier("TOGGLE", "string", getPackageName()))));
        helper.add(new RCSpinnerHelper(RelayControllerButton.RelayControllerButtonType.TOUCH, getString(getResources().getIdentifier("TOUCH", "string", getPackageName()))));

        Collections.sort(helper, new Comparator<RCSpinnerHelper>() {
            @Override
            public int compare(RCSpinnerHelper o1, RCSpinnerHelper o2) {
                String l1 = Normalizer.normalize(o1.getLabel(), Normalizer.Form.NFD);
                String l2 = Normalizer.normalize(o2.getLabel(), Normalizer.Form.NFD);
                return l1.compareTo(l2);
            }
        });
        rcLabelledSpinner.setItemsArray(helper.toArray(new RCSpinnerHelper[helper.size()]));
        rcLabelledSpinner.setSelection(0);
    }
}
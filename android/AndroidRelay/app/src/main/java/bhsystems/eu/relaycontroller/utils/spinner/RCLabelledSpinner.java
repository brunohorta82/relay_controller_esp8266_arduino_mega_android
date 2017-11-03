package bhsystems.eu.relaycontroller.utils.spinner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * DFLabelledSpinner
 * Created by ivooliveira on 26/01/17.
 */

public class RCLabelledSpinner extends LabelledSpinner {
    public RCLabelledSpinner(Context context) {
        super(context);
    }

    public RCLabelledSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RCLabelledSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RCLabelledSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setItemsArray(RCSpinnerHelper[] itemsArray, @LayoutRes int spinnerItemRes) {
        ArrayAdapter<RCSpinnerHelper> adapter = new ArrayAdapter<>(
                getContext(),
                spinnerItemRes,
                itemsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSpinner().setAdapter(adapter);
    }

    public void setItemsArray(RCSpinnerHelper[] itemsArray) {
        setItemsArray(itemsArray, android.R.layout.simple_spinner_item);

    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        getSpinner().setOnItemSelectedListener(listener);
    }
}
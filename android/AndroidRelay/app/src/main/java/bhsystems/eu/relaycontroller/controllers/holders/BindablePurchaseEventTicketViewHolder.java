package bhsystems.eu.relaycontroller.controllers.holders;

import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bhsystems.eu.relaycontroller.R;
import bhsystems.eu.relaycontroller.controllers.adapters.ButtonsAdapter;
import bhsystems.eu.relaycontroller.model.RelayControllerButton;

import static bhsystems.eu.relaycontroller.model.RelayControllerButton.RelayControllerButtonType.TOGGLE;

/**
 * BindablePurchaseEventTicketViewHolder
 * Created by ivooliveira on 08/03/17.
 */

public class BindablePurchaseEventTicketViewHolder extends BindableViewHolder<RelayControllerButton> {

   public static final int LAYOUT_RES_ID = R.layout.button_recycler_view_item;

    private TextView tvLabel;
    private ImageView imgState;
    private TextView gpioTextView;

    private final ButtonsAdapter.ButtonSelectedListener buttonSelectedListener;

    public BindablePurchaseEventTicketViewHolder(final View itemView, ButtonsAdapter.ButtonSelectedListener buttonSelectedListener) {
        super(itemView);
        this.buttonSelectedListener = buttonSelectedListener;
        gpioTextView = itemView.findViewById(R.id.gpio_label);
        tvLabel = itemView.findViewById(R.id.tv_label);
        imgState = itemView.findViewById(R.id.img_state);
    }

    public void bind(final int position, final RelayControllerButton relayControllerButton) {
        if (relayControllerButton == null) {
            return;
        }

        itemView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), android.R.color.black, itemView.getContext().getTheme()));

        gpioTextView.setText(String.valueOf(relayControllerButton.getPin()));
        tvLabel.setText(relayControllerButton.getLabel());

        switch (relayControllerButton.getRelayControllerButtonType()) {
            case TOGGLE:
                imgState.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), relayControllerButton.isActive() ? android.R.drawable.button_onoff_indicator_on : android.R.drawable.button_onoff_indicator_off, itemView.getContext().getTheme()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonSelectedListener.onButtonClicked(relayControllerButton);

                    }
                });
                break;
            case RelayControllerButton.RelayControllerButtonType.TOUCH:
                imgState.setImageResource(R.drawable.ic_touch_app_white_24dp);
                itemView.setOnTouchListener(
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_UP:
                                    case MotionEvent.ACTION_CANCEL:
                                    case MotionEvent.ACTION_DOWN:
                                        buttonSelectedListener.onButtonClicked(relayControllerButton);
                                        return true;
                                }
                                return false;
                            }
                        }
                );
                break;
                default:
        }


    }


}
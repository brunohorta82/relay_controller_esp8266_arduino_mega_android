package bhsystems.eu.relaycontroller.buttons;

import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import bhsystems.eu.relaycontroller.R;
import bhsystems.eu.relaycontroller.entity.RelayControllerButton;
import bhsystems.eu.relaycontroller.utils.BindableViewHolder;

/**
 * BindablePurchaseEventTicketViewHolder
 * Created by ivooliveira on 08/03/17.
 */

public class BindablePurchaseEventTicketViewHolder extends BindableViewHolder<RelayControllerButton> {

    public static final int LAYOUT_RES_ID = R.layout.button_recycler_view_item;

    TextView tvLabel;

    private final ButtonsAdapter.ButtonSelectedListener buttonSelectedListener;

    public BindablePurchaseEventTicketViewHolder(final View itemView, ButtonsAdapter.ButtonSelectedListener buttonSelectedListener) {
        super(itemView);
        this.buttonSelectedListener = buttonSelectedListener;

        tvLabel = itemView.findViewById(R.id.tv_label);
    }

    public void bind(final int position, final RelayControllerButton relayControllerButton) {
        if (relayControllerButton == null) {
            return;
        }
        itemView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(), relayControllerButton.isEnabled() ? android.R.color.black : android.R.color.darker_gray, itemView.getContext().getTheme()));
        updateButton(relayControllerButton);
        switch (relayControllerButton.getRelayControllerButtonType()) {
            case RelayControllerButton.RelayControllerButtonType.TOGGLE:
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        relayControllerButton.setActive(!relayControllerButton.isActive());
                        updateButton(relayControllerButton);
                        if (buttonSelectedListener != null && relayControllerButton.isEnabled()) {
                            buttonSelectedListener.onButtonClicked(relayControllerButton);
                        }
                    }
                });
                break;
            case RelayControllerButton.RelayControllerButtonType.TOUCH:
                itemView.setOnTouchListener(
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_UP:
                                    case MotionEvent.ACTION_CANCEL:
                                        relayControllerButton.setActive(false);
                                        updateButton(relayControllerButton);
                                        if (buttonSelectedListener != null && relayControllerButton.isEnabled()) {
                                            buttonSelectedListener.onButtonClicked(relayControllerButton);
                                        }
                                        return true;
                                    case MotionEvent.ACTION_DOWN:
                                        relayControllerButton.setActive(true);
                                        updateButton(relayControllerButton);
                                        if (buttonSelectedListener != null && relayControllerButton.isEnabled()) {
                                            buttonSelectedListener.onButtonClicked(relayControllerButton);
                                        }
                                        return true;
                                }
                                return false;
                            }
                        }
                );
                break;
        }
    }

    private void updateButton(RelayControllerButton relayControllerButton) {
        String lbl = relayControllerButton.getRelayControllerButtonTypeStr() + ": " + relayControllerButton.getLabel() + " " + (relayControllerButton.isActive() ? "ON" : "OFF");
        tvLabel.setText(lbl);
    }
}
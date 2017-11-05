package bhsystems.eu.relaycontroller.buttons;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
    ImageView imgState;

    private final ButtonsAdapter.ButtonSelectedListener buttonSelectedListener;

    public BindablePurchaseEventTicketViewHolder(final View itemView, ButtonsAdapter.ButtonSelectedListener buttonSelectedListener) {
        super(itemView);
        this.buttonSelectedListener = buttonSelectedListener;
        tvLabel = itemView.findViewById(R.id.tv_label);
        imgState = itemView.findViewById(R.id.imgState);
    }

    public void bind(final int position, final RelayControllerButton relayControllerButton) {
        if (relayControllerButton == null) {
            return;
        }
        tvLabel.setText(relayControllerButton.getLabel());
        imgState.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(), relayControllerButton.isActive() ? android.R.drawable.button_onoff_indicator_on : android.R.drawable.button_onoff_indicator_off , itemView.getContext().getTheme()));
        itemView.setBackgroundColor(ResourcesCompat.getColor(itemView.getResources(),android.R.color.black, itemView.getContext().getTheme()));
        switch (relayControllerButton.getRelayControllerButtonType()) {
            case RelayControllerButton.RelayControllerButtonType.TOGGLE:
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        relayControllerButton.setActive(!relayControllerButton.isActive());
                        buttonSelectedListener.onButtonClicked(relayControllerButton);

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

                                        buttonSelectedListener.onButtonClicked(relayControllerButton);

                                        return true;
                                    case MotionEvent.ACTION_DOWN:
                                        relayControllerButton.setActive(true);

                                        buttonSelectedListener.onButtonClicked(relayControllerButton);
                                        return true;
                                }
                                return false;
                            }
                        }
                );
                break;
        }
    }


}
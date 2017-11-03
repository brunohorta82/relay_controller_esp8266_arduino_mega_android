package bhsystems.eu.relaycontroller.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static bhsystems.eu.relaycontroller.entity.RelayControllerButton.RelayControllerButtonType.TOGGLE;
import static bhsystems.eu.relaycontroller.entity.RelayControllerButton.RelayControllerButtonType.TOUCH;

/**
 * Created by ivooliveira on 03/11/17.
 */

public class RelayControllerButton implements Parcelable {

    private String label;
    private boolean active;
    private @RelayControllerButtonType
    int relayControllerButtonType;

    public RelayControllerButton(String label, @RelayControllerButtonType int relayControllerButtonType) {
        this.label = label;
        this.relayControllerButtonType = relayControllerButtonType;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public @RelayControllerButtonType
    int getRelayControllerButtonType() {
        return relayControllerButtonType;
    }

    public void setRelayControllerButtonType(@RelayControllerButtonType int relayControllerButtonType) {
        this.relayControllerButtonType = relayControllerButtonType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public String getRelayControllerButtonTypeStr() {
        switch (relayControllerButtonType) {
            case RelayControllerButtonType.TOGGLE:
                return "TOGGLE";
            case RelayControllerButtonType.TOUCH:
                return "TOUCH";
        }
        return "";
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOGGLE, TOUCH})
    public @interface RelayControllerButtonType {
        int TOGGLE = 1;
        int TOUCH = 2;
    }
}
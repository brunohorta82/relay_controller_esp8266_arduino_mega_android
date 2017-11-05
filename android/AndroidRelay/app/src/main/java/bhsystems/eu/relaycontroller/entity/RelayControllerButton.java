package bhsystems.eu.relaycontroller.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
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
@Entity
public class RelayControllerButton implements Parcelable {

    private String label;
    private boolean active;
    private @RelayControllerButtonType
    int relayControllerButtonType;
    @PrimaryKey
    private Integer pin;


    public RelayControllerButton(String label, @RelayControllerButtonType int relayControllerButtonType, int pin) {
        this.label = label;
        this.pin = pin;
        this.relayControllerButtonType = relayControllerButtonType;
    }


    protected RelayControllerButton(Parcel in) {
        label = in.readString();
        active = in.readByte() != 0;
        relayControllerButtonType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeInt(relayControllerButtonType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RelayControllerButton> CREATOR = new Creator<RelayControllerButton>() {
        @Override
        public RelayControllerButton createFromParcel(Parcel in) {
            return new RelayControllerButton(in);
        }

        @Override
        public RelayControllerButton[] newArray(int size) {
            return new RelayControllerButton[size];
        }
    };

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



    public String getRelayControllerButtonTypeStr() {
        switch (relayControllerButtonType) {
            case RelayControllerButtonType.TOGGLE:
                return "TOGGLE";
            case RelayControllerButtonType.TOUCH:
                return "TOUCH";
        }
        return "";
    }

    public Integer getPin() {
        return pin;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOGGLE, TOUCH})
    public @interface RelayControllerButtonType {
        int TOGGLE = 1;
        int TOUCH = 2;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }
}
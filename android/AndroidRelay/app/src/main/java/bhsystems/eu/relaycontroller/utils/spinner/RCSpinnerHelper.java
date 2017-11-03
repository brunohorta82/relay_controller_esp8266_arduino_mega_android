package bhsystems.eu.relaycontroller.utils.spinner;

public class RCSpinnerHelper {
    private final int ordinal;
    private final String label;

    public RCSpinnerHelper(int ordinal, String label) {
        this.ordinal = ordinal;
        this.label = label;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
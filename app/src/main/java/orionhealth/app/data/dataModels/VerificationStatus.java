package orionhealth.app.data.dataModels;

/**
 * Created by luchen on 16/07/2016.
 */
public enum VerificationStatus {
    PROVISIONAL("provisional"),
    CONFIRMED("confirmed"),
    REFUTED("refuted"),
    ENTERED_IN_ERROR("entered-in-error"),
    UNKNOWN("unknown");

    private final String name;

    VerificationStatus (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString(){
        return getName();
    }
}

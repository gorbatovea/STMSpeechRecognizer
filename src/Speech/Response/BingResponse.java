package Speech.Response;

public class BingResponse {
    private String RecognitionStatus;
    private String DisplayText;
    private String Offset;
    private String Duration;

    public String getRecognitionStatus() {
        return RecognitionStatus;
    }

    public String getDisplayText() {
        return DisplayText;
    }

    public String getOffset() {
        return Offset;
    }

    public String getDuration() {
        return Duration;
    }

    public void setRecognitionStatus(String recognitionStatus) {
        RecognitionStatus = recognitionStatus;
    }

    public void setDisplayText(String displayText) {
        DisplayText = displayText;
    }

    public void setOffset(String offset) {
        Offset = offset;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}

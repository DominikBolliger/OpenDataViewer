package modell;

public class Info {
    private String lineText;
    private String arrivalTime;
    private String departureTime;
    private String day;
    private String late;
    private String canceled;

    public Info(String lineText, String arrivalTime, String departureTime, String day, String late, String canceled) {
        this.lineText = lineText;

        if (arrivalTime != null) {
            this.arrivalTime = arrivalTime.substring(11);
        } else {
            this.arrivalTime = arrivalTime;
        }

        if (departureTime != null) {
            this.departureTime = departureTime.substring(11);
        } else {
            this.departureTime = departureTime;
        }
        this.day = day;
        if (late.equals("0")){
            this.late = "No";
        } else {
            this.late = "Yes";
        }
        if (canceled.equals("0")){
            this.canceled = "No";
        } else {
            this.canceled = "Yes";
        }

    }

    public String getLineText() {
        return lineText;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getDay() {
        return day;
    }

    public String getLate() {
        return late;
    }

    public String getCanceled() {
        return canceled;
    }
}

package grafika.cafe.grafikacafe.models;

public class Activity {
    public String activity;
    public String user;
    public String date;

    public Activity(String activity, String user, String date) {
        this.activity = activity;
        this.user = user;
        this.date = date;
    }

    public String getActivity(){
        return activity;
    }

    public String getUser(){
        return user;
    }

    public String getDate() {
        return date;
    }
}

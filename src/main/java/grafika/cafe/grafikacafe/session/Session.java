package grafika.cafe.grafikacafe.session;

import java.util.prefs.Preferences;

public class Session {

    public static void setSession(User user) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("name", user.name);
        preferences.put("role", user.role);
    }

    public static User getSession() {
        Preferences preferences = Preferences.userRoot();
        var name = preferences.get("name", "String");
        var role = preferences.get("role", "String");
        return new User(name, role);
    }

}

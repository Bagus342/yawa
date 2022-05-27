package grafika.cafe.grafikacafe.session;

import java.util.prefs.Preferences;

public class Session {

    public static void setSession(User user) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("session_name", user.name);
        preferences.put("session_role", user.role);
    }

    public static User getSession() {
        Preferences preferences = Preferences.userRoot();
        var name = preferences.get("session_name", "String");
        var role = preferences.get("session_role", "String");
        return new User(name, role);
    }

}

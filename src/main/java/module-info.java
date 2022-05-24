module grafika.cafe.grafikacafe {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires java.prefs;
    requires fontawesomefx;


    opens grafika.cafe.grafikacafe to javafx.fxml;
    exports grafika.cafe.grafikacafe;
    exports grafika.cafe.grafikacafe.controller;
    opens grafika.cafe.grafikacafe.controller to javafx.fxml;
    exports grafika.cafe.grafikacafe.services;
    opens grafika.cafe.grafikacafe.services to javafx.fxml;
    exports grafika.cafe.grafikacafe.models;
    opens grafika.cafe.grafikacafe.models to javafx.fxml;
    exports grafika.cafe.grafikacafe.controller.create;
    opens grafika.cafe.grafikacafe.controller.create to javafx.fxml;
    exports grafika.cafe.grafikacafe.controller.update;
    opens grafika.cafe.grafikacafe.controller.update to javafx.fxml;
}
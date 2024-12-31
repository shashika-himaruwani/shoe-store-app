module lk.sh.shoesstoreapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;

    opens lk.sh.shoesstoreapp to javafx.fxml;
    opens lk.sh.shoesstoreapp.tm to javafx.base;

    exports lk.sh.shoesstoreapp;
    exports lk.sh.shoesstoreapp.controller;
    opens lk.sh.shoesstoreapp.controller to javafx.fxml;
}

module com.elite.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.mail;
    requires net.sf.jasperreports.core;
    requires java.compiler;



    opens com.applimax.project.controller to javafx.fxml;
    opens com.applimax.project.dto to javafx.base;
    opens com.applimax.project.dto.tm to javafx.base;
    exports com.applimax.project;
}
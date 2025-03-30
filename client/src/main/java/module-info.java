module org {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    // requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires json.simple;
    requires java.sql;
    requires static lombok;

    opens org to javafx.fxml;
    exports org;
    exports org.admin.UI.window.authorizationWindow;
    opens org.admin.UI.window.authorizationWindow to javafx.fxml;
}
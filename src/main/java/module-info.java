module pl.woelke.vjtool {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires java.logging;



    opens pl.woelke.vjtool.ui to javafx.fxml;
    exports pl.woelke.vjtool.ui;
    exports pl.woelke.vjtool.ui.model;
    opens pl.woelke.vjtool.ui.model to javafx.fxml, javafx.graphics;

}
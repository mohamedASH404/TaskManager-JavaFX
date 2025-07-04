module fr.esgi.tpfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.esgi.tpfx to javafx.fxml;
    exports fr.esgi.tpfx;
}
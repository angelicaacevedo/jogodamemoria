module com.lp3.jogodamemoria {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.lp3.jogodamemoria to javafx.fxml;
    exports com.lp3.jogodamemoria;
}
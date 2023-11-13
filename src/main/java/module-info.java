module com.ekorn.parkautomatfx {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.ekorn.parkautomatfx to javafx.fxml;
    exports com.ekorn.parkautomatfx;
}
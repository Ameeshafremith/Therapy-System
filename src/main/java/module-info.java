module ijse.theropy_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires java.naming;

    opens ijse.theropy_system to javafx.fxml;
    opens ijse.theropy_system.controller to javafx.fxml;
    opens ijse.theropy_system.entity to org.hibernate.orm.core;
    opens ijse.theropy_system.dto to javafx.base;

    exports ijse.theropy_system;
}

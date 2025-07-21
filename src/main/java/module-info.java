module Modulo_Ejercicios {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;


    // Exporta el paquete de controladores para que FXML pueda acceder a él
    opens Modulo_Ejercicios.Controladores to javafx.fxml;
    opens Modulo_Usuario.Controladores to javafx.fxml;
    opens Gamificacion_Modulo.GUI.controllers to javafx.fxml;
    opens Gamificacion_Modulo.GUI.admin to javafx.fxml;
    opens Gamificacion_Modulo to javafx.fxml;


    exports Modulo_Ejercicios.application;
    exports Modulo_Ejercicios.otrosModulos;
    exports Modulo_Ejercicios.exercises;
    exports Modulo_Ejercicios.DataBase;
    exports Modulo_Usuario.Clases;
    exports Modulo_Usuario.Controladores;
    exports Modulo_Usuario.application;
    exports Gamificacion_Modulo;
    exports Gamificacion_Modulo.GUI.admin;
} 
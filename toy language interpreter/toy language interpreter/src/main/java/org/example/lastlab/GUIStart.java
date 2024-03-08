package org.example.lastlab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIStart extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIStart.class.getResource("ProgramStatesSelection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        ProgramStatesSelectionController selectionController = fxmlLoader.getController();

        FXMLLoader fxmlLoader1 = new FXMLLoader(Application.class.getResource("ProgramStatesExecution.fxml"));
        Scene scene2 = new Scene(fxmlLoader1.load(), 600, 800);
       /* selectionController.setExecutionController(fxmlLoader1.getController());
        Stage stage2 = new Stage();
        stage2.setTitle("Main Window");
        stage2.setScene(scene2);
        stage2.show();*/


    }

    public static void main(String[] args) {
        launch();
    }
}
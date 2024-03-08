package org.example.lastlab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{
    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException{
        FXMLLoader selectionSceneLoader = new FXMLLoader(Main.class.getResource("GUILayout.fxml"));
        Parent root = selectionSceneLoader.load();
        SelectionWindowController selectionWindowController = selectionSceneLoader.getController();
        primaryStage.setTitle("Select a program");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        
        FXMLLoader executionSceneLoader = new FXMLLoader(Main.class.getResource("ExecutableGUILayout.fxml"));
        Parent programExecutionRoot = executionSceneLoader.load();
        ExecutionWindowController executionWindowController = executionSceneLoader.getController();
        selectionWindowController.setExecutionWindowController(executionWindowController);
        Stage executionStage = new Stage();
        executionStage.setTitle("Executing Program");
        executionStage.setScene(new Scene(programExecutionRoot, 1200, 800));
        executionStage.show();
    }
}

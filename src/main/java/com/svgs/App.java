package com.svgs;

import java.io.IOException;

import com.svgs.framework.reader.ReaderInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    static Scene scene;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mainScreen"));
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> ReaderInterface.stopobdRead());
        stage.show();

        // the UI still comes up with no adapter plugged in, gauges just sit at zero.
        if (!ReaderInterface.startobdRead()) {
            System.out.println("No OBD connection on " + ReaderInterface.PORT_NAME
                    + " - gauges will stay at zero.");
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}

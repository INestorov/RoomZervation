package application.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminView extends Application {
    /**
     * Loads the admin window.
     *
     * @param stage previous stage .
     * @throws Exception nasty exception to be thrown.
     */

    public void start(Stage stage) throws Exception {
        //stage.close();
        //MainApp.stage = new Stage();
        stage
            .setScene(new Scene(FXMLLoader.load(getClass().getResource("/AdminScene.fxml"))));
        stage.show();
    }
}

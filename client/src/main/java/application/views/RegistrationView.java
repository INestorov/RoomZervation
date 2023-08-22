package application.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegistrationView extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads the Registration window.
     * @throws Exception nasty exception to be thrown.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(
            getClass().getResource("/registrationScene.fxml"))));
        primaryStage.show();
    }
}

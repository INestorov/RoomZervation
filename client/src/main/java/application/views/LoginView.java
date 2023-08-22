package application.views;

import application.MainApp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginView extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads the Login window.
     *
     * @param stage previous stage .
     * @throws Exception nasty exception to be thrown.
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.close();
        MainApp.stage = new Stage();
        MainApp.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/LoginScene"
            + ".fxml"))));
        MainApp.stage.show();
        MainApp.stage.centerOnScreen();
    }
}

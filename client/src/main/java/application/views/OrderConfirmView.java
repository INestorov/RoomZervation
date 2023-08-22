package application.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OrderConfirmView extends Application {

    /**
     * Loads the Main window.
     *
     * @param stage previous stage .
     * @throws Exception nasty exception to be thrown.
     */
    @Override
    public void start(Stage stage) throws Exception {
        //stage.close();
        //MainApp.stage = new Stage();
        stage
            .setScene(new Scene(FXMLLoader.load(
                getClass().getResource("/OrderConfirmScene.fxml"))));
        stage.show();
    }
}

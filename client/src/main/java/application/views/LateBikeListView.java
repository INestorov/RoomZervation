package application.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LateBikeListView extends Application {
    /**
     * Loads the LateBikeList window.
     *
     * @param stage previous stage .
     * @throws Exception nasty exception to be thrown.
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass()
            .getResource("/LateBikeListScene.fxml"))));
    }
}

package application.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BikeView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //stage.close();
        //MainApp.stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/BikeScene.fxml"))));
        stage.show();
    }
}

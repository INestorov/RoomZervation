package application.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ForgotPasswordView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //stage.close();
        //MainApp.stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/ForgotPasswordScene"
            + ".fxml")));
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}

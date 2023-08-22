package application.controls;

import application.MainApp;
import application.views.MainView;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//@author - Silviu Marii

public class Progress {

    final Float[] values = new Float[] {-1.0f, 1.0f};
    final Label[] labels = new Label[values.length];
    final ProgressBar[] pbs = new ProgressBar[values.length];
    final ProgressIndicator[] pins = new ProgressIndicator[values.length];
    final AnchorPane[] hbs = new AnchorPane[values.length];
    private Stage stage;
    private Scene scene;

    public Progress() {
        stage = new Stage();
    }

    public Progress(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initalizes the new stage for loading screen.
     *
     * @returns the stage for closing it later
     */
    public Stage initStage() {

        Group root = new Group();
        scene = new Scene(root, 300, 250);
        stage.setScene(scene);
        stage.setTitle("Progress Controls");
        return stage;
    }

    /**
     * In case something happened we should notify the client
     * the request did not reach the server.
     *
     * @param stage for modifying the currently opened window.
     */
    public void unsuccesful(Stage stage) {
        Button ok = new Button();
        ok.setText("OK.");
        Label error = new Label("Something went wrong. Try again later.");
        VBox vb = new VBox();
        ok.setOnMouseClicked(event -> {
            try {
                new MainView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        vb.getChildren().add(error);
        vb.getChildren().add(ok);
        stage.getScene().setRoot(vb);
    }

    /**
     * Controller for the loading screen that occurs during the data sending process.
     *
     * @param i         0 is for working on the task, 1 is for done.
     * @param tempStage the temporary stage
     */
    public void showStatus(int i, Stage tempStage) {
        initStage();
        int m;
        if (i > labels.length - 1) {
            m = labels.length - 1;
        } else {
            m = i;
        }
        final Label label = labels[m] = new Label();
        label.setText("Progress: ");

        final ProgressBar pb = pbs[m] = new ProgressBar();
        pb.setProgress(values[m]);

        final ProgressIndicator pin = pins[m] = new ProgressIndicator();
        pin.setProgress(values[m]);
        final AnchorPane hb = hbs[m] = new AnchorPane();
        hb.getChildren().addAll(label, pb, pin);
        pb.setLayoutX(90);
        pin.setLayoutX(220);
        pb.setLayoutY(50);
        label.setLayoutY(50);
        pin.setLayoutY(50);
        final VBox vb = new VBox();
        vb.setSpacing(5);
        vb.getChildren().add(hbs[m]);
        tempStage.getScene().setRoot(vb);
        tempStage.show();
        if (i == 1 || i == 2) {
            Button cf = new Button();
            cf.setText("Confirm");
            Label l;
            if (i == 1) {
                l = new Label("New reservation added.");
            } else {
                hb.getChildren().clear();
                l = new Label("You reached your maximum number" + '\n' + " of reservations.");
            }
            hb.getChildren().add(l);
            hb.getChildren().add(cf);
            l.setLayoutY(80);
            l.setLayoutX(60);
            cf.setLayoutY(113);
            cf.setLayoutX(60);
            cf.setOnMouseClicked(event -> {
                try {
                    new MainView().start(MainApp.stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void close(Stage tempStage) {
        tempStage.close();
    }
}
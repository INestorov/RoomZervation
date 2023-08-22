package application.controllers;

import application.communication.ServerCommunication;
import application.views.LoginView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController {
    /**
     * Method for going back to the Login window.
     *
     * @param event the trigger for this method
     */
    @FXML
    private Label label;
    @FXML
    private Button sendButton;
    @FXML
    private TextField textField;
    private boolean cond;
    private String username;

    /** Method for returning to the login screen.
     *
     * @param event the trigger
     */
    public void backToLogin(ActionEvent event) {
        try {
            new LoginView().start((Stage) ((Node) event.getSource()).getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Sends a request to the server to send an activation key.
     *
     * @param actionEvent the trigger.
     */
    public void sendKey(ActionEvent actionEvent) {
        if (label.getText().equals("Enter the username to send an activation code.")) {
            username =  textField.getText();
            String resp = ServerCommunication.recovPass(textField.getText());
            if (resp.equals("[]")) {
                label.setText("Something went wrong. Restart the app.");
            } else if (resp.equals("false")) {
                label.setText("Wrong email address. Please check again.");
            } else {
                label.setText("Enter the activation key we sent you on this" + '\n'
                    + " email address ");
                textField.clear();
            }
        } else {
            if (!label.getText().equals("Please enter your new password.")) {
                sendButton.setOnMouseClicked(event -> {
                    if (textField.getText() != null) {
                        checkCond();
                    }
                });
            }
        }
    }

    /** Method for changing the password after checking the activation key.
     *
     */
    private void checkCond() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String s = ServerCommunication.checkKey(textField.getText());
        System.out.println("response " + s);
        if (s.equals("[]")) {
            alert.setContentText("Something went wrong. Please contact an Admin.");
            alert.setTitle("Password change failed");
            alert.showAndWait();
        } else if (s.equals("false")) {
            alert.setTitle("Bad request");
            alert.setContentText("Wrong activation key. Please enter again");
            alert.showAndWait();
        } else {
            label.setText("Please enter your new password.");
            sendButton.setText("Change password");
            textField.clear();
            sendButton.setOnMouseClicked(event2 -> {
                if (textField.getText() != null) {
                    ServerCommunication.changePassword(username, textField.getText());
                    sendButton.setDisable(true);
                    sendButton.setVisible(false);
                }
            });
        }
    }
}

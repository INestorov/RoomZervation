package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.UserManager;
import application.views.AdminView;
import application.views.BikeView;
import application.views.LoginView;
import application.views.MyOrdersView;
import application.views.OrderFoodView;
import application.views.ProfileView;
import application.views.ReservationListView;
import application.views.ReturnBikeView;
import application.views.RoomListView;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainSceneController extends Stage implements Initializable {
    public static boolean isAdmin = false;
    public static boolean hasBike = false;

    @FXML
    protected JFXDrawer drawer;
    @FXML
    protected JFXHamburger ham;
    @FXML
    protected AnchorPane drawerPane;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    String title;
    String contentText;

    /**
     * Method to logout.
     */
    public static void logout() {
        ServerCommunication.logout();
        try {
            new LoginView().start(MainApp.stage);
        } catch (Exception exxx) {
            exxx.printStackTrace();
        }
    }

    /**
     * Method which loads the side bar
     * in the MainScene.
     *
     * @param url URL
     * @param rb  resourcreBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpMenu();
    }

    /**
     * Setup the drawer and JfxHamburger functionality.
     */
    protected void setUpMenu() {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/MainSideMenu.fxml"));
            drawer.setSidePane(box);
            drawer.getParent().toFront();
            drawer.setDisable(true);
            drawerPane.setDisable(true);
            List<Node> children = box.getChildren();
            deleteNode(children);
            for (Node node : children) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getAccessibleText()) {
                            case "showRooms":
                                showRooms();
                                break;
                            case "showReservations":
                                showReservations();
                                break;
                            case "showAdmin":
                                showAdmin();
                                break;
                            case "rentBike":
                                showBike();
                                deleteNode(children);
                                break;
                            case "myOrders":
                                try {
                                    new MyOrdersView().start(MainApp.stage);
                                } catch (Exception exx) {
                                    exx.printStackTrace();
                                }
                                break;
                            case "logout":
                                logout();
                                break;
                            case "deleteAccount":
                                deleteAccount();
                                break;
                            case "myProfile":
                                try {
                                    new ProfileView().start(MainApp.stage);
                                } catch (Exception exxxx) {
                                    exxxx.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }

                    });
                }
            }
            HamburgerBackArrowBasicTransition task = new HamburgerBackArrowBasicTransition(ham);
            task.setRate(-1);
            ham.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
                if (drawer.isOpened()) {
                    drawer.setDisable(true);
                    drawerPane.setDisable(true);
                } else {
                    drawer.setDisable(false);
                    drawerPane.setDisable(false);
                }
                drawer.toggle();
            });
            drawer.setOnDrawerOpening((event) -> {
                task.setRate(task.getRate() * -1);
                task.play();
            });
            drawer.setOnDrawerClosed((event) -> {
                drawer.toBack();
                task.setRate(task.getRate() * -1);
                task.play();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes unused node for renting a bike.
     *
     * @param children - nodes of the sidebar
     */
    public void deleteNode(List<Node> children) {
        children.removeIf(node -> {
            String id = node.getId();
            if (id != null) {
                if (id.equals("rentBike") && hasBike) {
                    return true;
                } else if (id.equals("returnBike") && !hasBike) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Switches to RoomListScene.
     */
    public void showRooms() {
        try {
            new RoomListView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to ReservationListScene.
     */
    public void showReservations() {
        try {
            new ReservationListView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to AdminScene.
     */
    public void showAdmin() {
        try {
            new AdminView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clicked on orders.
     * Shows the order food page.
     */
    public void showFood() {
        try {
            new OrderFoodView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Menu item rent bike was triggered.
     */
    public void showBike() {

        if (ServerCommunication.isBikeInUse()) {
            try {
                new ReturnBikeView().start(MainApp.stage);
                hasBike = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                new BikeView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to delete an account.
     */
    public void deleteAccount() {
        int userId = UserManager.getInstance().getUser().getId();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account");
            alert.setHeaderText("Confirm the cancellation of your account");
            Optional<ButtonType> choice = alert.showAndWait();

            if (choice.get() == ButtonType.OK) {
                new LoginView().start(MainApp.stage);
                ServerCommunication.deleteUser(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


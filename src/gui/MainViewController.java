package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentServices;
import model.services.SellerServices;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private  MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction(){
        loadView ("/gui/SellerList.fxml", (SellerListController controller) -> {
            controller.setSellerService(new SellerServices());
            controller.updateTableView();

        }); //aqui vai incluir uma função que vai inicializar o controlador


    }

    @FXML
    public void onMenuItemDepartmentAction(){
        loadView ("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentServices());
            controller.updateTableView();

        }); //aqui vai incluir uma função que vai inicializar o controlador

    }

    @FXML
    public void onMenuItemAboutAction(){
        loadView("/gui/About.fxml", x -> {}); //aqui retorna uma função vazia, pois não precisar chamar nem um metodo

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private synchronized <T> void  loadView(String absoluteName, Consumer<T> initializingAction ){ // isso foi uma parametrização
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVbox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();

            Node mainMenu = mainVbox.getChildren().get(0);
            mainVbox.getChildren().clear();
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVBox.getChildren());

            //comanda para a tivar a função
            T controller = loader.getController();
            initializingAction.accept(controller);

        }
        catch (IOException e){
            Alerts.showAlert("IO EXception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);

        }
    }
}

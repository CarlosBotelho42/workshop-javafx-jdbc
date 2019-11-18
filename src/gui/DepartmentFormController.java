package gui;
import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentServices;

public class DepartmentFormController implements Initializable {

   private Department entity;

   private DepartmentServices services;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setDepartment (Department entity){
        this.entity = entity;

    }

    public void setServices (DepartmentServices services){
        this.services = services;

    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if(entity == null) {
            throw new IllegalStateException("Nulo");
        }
        if(services == null){
            throw new IllegalStateException("Nulo");

        }
            try {
                entity = getFormData();
                services.saveOrUpdate(entity);
                Utils.currentStage(event).close();
            }
            catch (DbException e){
                Alerts.showAlert("Erro ao salvar obj", null, e.getMessage(), Alert.AlertType.ERROR);
            }

    }

    private Department getFormData() {
        Department obj = new Department();

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setName(txtName.getText());

        return obj;

    }

    @FXML
    public void onBtCancelAction() {
        System.out.println("Botao cancelarrrrrrrrrrrrrrrrr");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entidades esta nula");

        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(String.valueOf(entity.getName()));

    }
    
    



}
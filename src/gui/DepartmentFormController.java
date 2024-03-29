package gui;
import java.net.URL;
import java.util.*;

import db.DbException;
import gui.listeners.DataChangeListener;
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
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentServices;

public class DepartmentFormController implements Initializable {

   private Department entity;

   private DepartmentServices services;

   private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);

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
                notifyDataChangeListeners();
                Utils.currentStage(event).close();
            }
            catch (ValidationException e){
                setErrorMessage(e.getErrors());

            }
            catch (DbException e){
                Alerts.showAlert("Erro ao salvar obj", null, e.getMessage(), Alert.AlertType.ERROR);
            }
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department obj = new Department();

        ValidationException exception = new ValidationException("Erro da validação");

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if(txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "O campo nao pode ser vazio");
        }
        obj.setName(txtName.getText());

        if(exception.getErrors().size() > 0){
            throw exception;

        }

        return obj;

    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close(); //fechar janelaaaa
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

    private void setErrorMessage(Map<String,String> error){
        Set<String> fields = error.keySet();

        if(fields.contains("name")){
            labelErrorName.setText(error.get("name"));

        }

    }


}
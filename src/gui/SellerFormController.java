package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentServices;
import model.services.SellerServices;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

   private Seller entity;

   private SellerServices services;

   private DepartmentServices departmentServices;

   private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBaseSalary;

    @FXML
    private ComboBox<Department> comboBoxDepartment;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Department> obsList;

    public void setSeller (Seller entity){
        this.entity = entity;

    }

    public void setServices (SellerServices services, DepartmentServices departmentServices){
        this.services = services;
        this.departmentServices =  departmentServices;

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

    private Seller getFormData() {
        Seller obj = new Seller();

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
        Constraints.setTextFieldMaxLength(txtName, 50);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail,80);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

        initializeComboBoxDepartment();

    }

    public void updateFormData(){
        if(entity == null){
            throw new IllegalStateException("Entidades esta nula");

        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(String.valueOf(entity.getName()));
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
       if(entity.getBirthDate() != null) {
           dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
       }
       if(entity.getDepartment() == null){
           comboBoxDepartment.getSelectionModel().selectFirst();

       }
       else {
           comboBoxDepartment.setValue(entity.getDepartment());

       }

    }

    public void loadAssociatedObjects(){
        if(departmentServices == null){
            throw new IllegalStateException("Serviço de departamento esta nulo");
        }
        List<Department> list = departmentServices.FindAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);

    }

    private void setErrorMessage(Map<String,String> error){
        Set<String> fields = error.keySet();

        if(fields.contains("name")){
            labelErrorName.setText(error.get("name"));

        }

    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }


}
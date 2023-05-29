package com.example.demo.controller;

import com.example.demo.model.Bug;
import com.example.demo.model.BugStatus;
import com.example.demo.model.QA;
import com.example.demo.service.Service;
import com.example.demo.utils.event.BugEntityChangeEvent;
import com.example.demo.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class QAController implements Observer<BugEntityChangeEvent> {
    @FXML
    private TableView<Bug> tableViewBugs;
    @FXML
    private TableColumn<Bug, Integer> idColumnQA;
    @FXML
    private TableColumn<Bug, String> nameColumnQA;
    @FXML
    private TableColumn<Bug, String> descColumnQA;
    @FXML
    private TableColumn<Bug, BugStatus> statusColumnQA;
    @FXML
    private TextField nameTFQA;
    @FXML
    private TextField descTFQA;
    @FXML
    private Label welcomeLabelQA;
    private Service service;
    private QA loggedInQA;
    private final ObservableList<Bug> bugsModel = FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }
    public void setLoggedInQA(QA qa) {this.loggedInQA = qa;}
    public void setWelcomeLabelQA() {welcomeLabelQA.setText("Welcome, " + this.loggedInQA.getUsername());}

    @FXML
    public void initialize() {
        idColumnQA.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnQA.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumnQA.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumnQA.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableViewBugs.setItems(bugsModel);
    }

    public void initModel() {
        bugsModel.setAll(service.getAllBugs());
    }

    @FXML
    private void onClickModifyBug(ActionEvent actionEvent) throws IOException {
        Bug selectedBug = tableViewBugs.getSelectionModel().getSelectedItem();
        String bugName = nameTFQA.getText();
        String bugDescription = descTFQA.getText();
        service.modify(selectedBug.getId(), bugName, bugDescription);
    }

    @FXML
    private void onClickRecordBug(ActionEvent actionEvent) throws IOException {
        String bugName = nameTFQA.getText();
        String bugDescription = descTFQA.getText();
        service.save(bugName, bugDescription);
    }

    @Override
    public void update(BugEntityChangeEvent bugEntityChangeEvent) {
        initModel();
    }
}

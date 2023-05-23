package com.example.demo.controller;

import com.example.demo.model.Bug;
import com.example.demo.model.BugStatus;
import com.example.demo.model.Programmer;
import com.example.demo.service.Service;
import com.example.demo.utils.event.BugEntityChangeEvent;
import com.example.demo.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgrammerController implements Observer<BugEntityChangeEvent> {
    @FXML
    private TableView<Bug> tableViewFiltered;
    @FXML
    private TableColumn<Bug, Integer> idColumnFilter;
    @FXML
    private TableColumn<Bug, String> nameColumnFilter;
    @FXML
    private TableColumn<Bug, String> descColumnFilter;
    @FXML
    private TableColumn<Bug, BugStatus> statusColumnFilter;
    @FXML
    private TextField nameFilterTF;
    @FXML
    private TextField descFilterTF;
    @FXML
    private TableView<Bug> tableViewBugsProg;
    @FXML
    private TableColumn<Bug, Integer> idColumnProg;
    @FXML
    private TableColumn<Bug, String> nameColumnProg;
    @FXML
    private TableColumn<Bug, String> descColumnProg;
    @FXML
    private TableColumn<Bug, BugStatus> statusColumnProg;
    @FXML
    private TextField nameTFProg;
    @FXML
    private TextField descTFProg;
    @FXML
    private Label welcomeLabelProg;
    private Service service;
    private Programmer loggedInProgrammer;
    private final ObservableList<Bug> bugsModel = FXCollections.observableArrayList();
    private final ObservableList<Bug> bugsModelFiltered = FXCollections.observableArrayList();

    public void setLoggedInProgrammer(Programmer loggedInProgrammer) {
        this.loggedInProgrammer = loggedInProgrammer;
    }

    public void setWelcomeLabelProgrammer(Programmer prog) {
        welcomeLabelProg.setText("Welcome, " + prog.getUsername());
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    public void onClickSolveBug(ActionEvent actionEvent) {
        Bug selectedBug = tableViewBugsProg.getSelectionModel().getSelectedItem();
        if (selectedBug.getStatus() == BugStatus.SOLVED) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error!");
            errorAlert.setContentText("The bug has already been solved");
            errorAlert.showAndWait();
        } else {
            service.solveBug(selectedBug.getId());
        }
    }

    public void onClickFilterBug(ActionEvent actionEvent) {
    }

    @Override
    public void update(BugEntityChangeEvent bugEntityChangeEvent) {
        initModel();
    }

    @FXML
    public void initialize() {
        idColumnProg.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnProg.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumnProg.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumnProg.setCellValueFactory(new PropertyValueFactory<>("status"));

        idColumnFilter.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnFilter.setCellValueFactory(new PropertyValueFactory<>("name"));
        descColumnFilter.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumnFilter.setCellValueFactory(new PropertyValueFactory<>("status"));

        nameFilterTF.textProperty().addListener(f -> bugFilters());
        descFilterTF.textProperty().addListener(f -> bugFilters());

        tableViewBugsProg.setItems(bugsModel);
        tableViewFiltered.setItems(bugsModelFiltered);
    }

    public void bugFilters(){
        String name = nameFilterTF.getText();
        String desc = descFilterTF.getText();
        Predicate<Bug> namePredicate = pred -> pred.getName().contains(name);
        Predicate<Bug> descPredicate = pred -> pred.getDescription().contains(desc);
        Predicate<Bug> predicateResult = namePredicate.or(descPredicate);
        List<Bug> bugs = service.getAllBugs();
        bugsModelFiltered.setAll(bugs.stream().filter(predicateResult).collect(Collectors.toList()));
    }

    public void initModel() {
        bugsModel.setAll(service.getAllBugs());
    }
}

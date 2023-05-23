package com.example.demo.controller;

import com.example.demo.model.Programmer;
import com.example.demo.model.QA;
import com.example.demo.service.Service;
import com.example.demo.utils.LoginResponse;
import com.example.demo.utils.LoginType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField usernameTF;
    @FXML
    private PasswordField pwdTF;

    private Service service;

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    private void onClickLoginBtn(ActionEvent actionEvent) throws IOException {
        LoginResponse loginResponse = service.login(usernameTF.getText(), pwdTF.getText());
        System.out.println(loginResponse.getLoginType());
        if(loginResponse.getLoginType() == LoginType.ERROR){
            errorLabel.setText("Invalid credentials!");
        } else if(loginResponse.getLoginType() == LoginType.QA){
            QA loggedInQA = loginResponse.getQa();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/views/qa-view.fxml"));
            Parent root = loader.load();
            QAController qaController = loader.getController();
            qaController.setLoggedInQA(loggedInQA);
            qaController.setWelcomeLabelQA(loggedInQA);
            qaController.setService(service);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Hello!");
            stage.show();
            closeWindow();
        } else if(loginResponse.getLoginType() == LoginType.PROGRAMMER){
            Programmer loggedInProgrammer = loginResponse.getProg();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/views/experiment.fxml"));
            Parent root = loader.load();
            ProgrammerController programmerController = loader.getController();
            programmerController.setLoggedInProgrammer(loggedInProgrammer);
            programmerController.setWelcomeLabelProgrammer(loggedInProgrammer);
            programmerController.setService(service);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Hello!");
            stage.show();
            closeWindow();
        }
    }

    private void closeWindow(){
        Stage thisStage = (Stage) loginButton.getScene().getWindow();
        thisStage.close();
    }
}
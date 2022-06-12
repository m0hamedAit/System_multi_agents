package Containers;

import Agents.Player1Agent;
import Agents.Player2Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class Player2Container extends Application {
    static Player2Agent player2;
    ObservableList<String> received_messages = FXCollections.observableArrayList();

    public static void main(String[] args) throws ControllerException {
        Player2Container player2Container = new Player2Container();
        player2Container.startContainer();
        launch(args);
    }

    public void setAgent(Player2Agent player2Agent) {
        player2 = player2Agent;
    }

    public ObservableList<String> getMessages() {
        return received_messages;
    }

    void startContainer() throws ControllerException {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer agentContainer = (AgentContainer) runtime.createAgentContainer(profile);
        AgentController agentController = agentContainer.createNewAgent("Player2", "Agents.Player2Agent", new Object[]{this});
        agentController.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Player 2");
        BorderPane root=new BorderPane();
        // guess field
        TextField guessField = new TextField();
        guessField.setPrefColumnCount(10);
        guessField.setPromptText("enter your guess");
        // submit button
        Button submitButton = new Button("Submit");

        // list view
        HBox hBox=new HBox();
        hBox.setPadding(new Insets(15));
        hBox.setSpacing(5);
        root.setTop(hBox);
        ListView<String> listView = new ListView<String>();
        listView.setPrefWidth(200);
        listView.setPrefHeight(200);
        hBox.getChildren().addAll(guessField, submitButton);
        root.setCenter(listView);


        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String guess = guessField.getText();
                GuiEvent guiEvent = new GuiEvent(this, 0);
                guiEvent.addParameter(guess);
                guiEvent.addParameter(listView);
                player2.onGuiEvent(guiEvent);
                guessField.setText("");
            }
        });

        Scene scene=new Scene(root,500,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}

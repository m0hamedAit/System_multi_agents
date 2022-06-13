package Agents;

import Containers.Player1Container;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Player1Agent extends GuiAgent {
    private Player1Container gui;
    static ListView<String> list;
    public ObservableList<String> messages = FXCollections.observableArrayList();

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
            String message = guiEvent.getParameter(0).toString();
            ACLMessage messageAcl = new ACLMessage(ACLMessage.INFORM);
            list = (ListView<String>) guiEvent.getParameter(1);
            messageAcl.addReceiver(new AID("ServerContainer", AID.ISLOCALNAME));
            list.setItems(messages);
            messageAcl.setContent(message);
            send(messageAcl);
    }

    @Override
    protected void setup() {
        gui = (Player1Container) getArguments()[0];
        gui.setAgent(this);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage = receive();
                if (aclMessage != null) {
                    messages.add(aclMessage.getContent());
                    System.out.println(aclMessage.getContent());
                } else {
                    block();
                }
            }
        });
        System.out.println("> Player 1 successfully started");
    }

    @Override
    public void takeDown() {
        System.out.println("> Player 1 successfully stopped");
    }
}

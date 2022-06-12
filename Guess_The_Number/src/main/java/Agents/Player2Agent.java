package Agents;

import Containers.Player1Container;
import Containers.Player2Container;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Player2Agent extends GuiAgent {
    private Player2Container gui;
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
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        gui = (Player2Container) getArguments()[0];
        gui.setAgent(this);
        addBehaviour(parallelBehaviour);
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
        System.out.println("> Player 2 successfully started");
    }

    @Override
    public void takeDown() {
        System.out.println("> Player 2 successfully stopped");
    }
}

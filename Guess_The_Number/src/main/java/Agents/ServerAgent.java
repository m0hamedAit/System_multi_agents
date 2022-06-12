package Agents;

import Containers.Player1Container;
import Containers.Player2Container;
import Containers.ServerContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class ServerAgent extends Agent {
    @Override
    public void setup() {
        Player1Container player1 = new Player1Container();
        Player2Container player2 = new Player2Container();
        ServerContainer server = new ServerContainer();
        // generate a random number between 1 and 100
        int randomNumber = (int) (Math.random() * 100) + 1;
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage = receive();
                if (aclMessage != null) {
                    int guessedNumber = 0;
                    ACLMessage messageAcl = new ACLMessage(ACLMessage.INFORM);
                    try {
                        // get existing agents
                        guessedNumber = Integer.parseInt(aclMessage.getContent());
                        SearchConstraints searchConstraints = new SearchConstraints();
                        searchConstraints.setMaxResults((long) -1);
                        AMSAgentDescription[] agents = AMSService.search(ServerAgent.this, new AMSAgentDescription(), searchConstraints);

                        // if a player guessed the number, send a message to all players
                        if (guessedNumber == randomNumber) {
                            for (AMSAgentDescription agent : agents) {
                                messageAcl.addReceiver(new AID(agent.getName().getLocalName(), AID.ISLOCALNAME));
                            }
                            messageAcl.setContent("Congratulation for " + aclMessage.getSender().getLocalName() + " who guessed the number " + randomNumber);
                            send(messageAcl);
                            takeDown();
                        } else if (guessedNumber > randomNumber) {
                            messageAcl.addReceiver(new AID(aclMessage.getSender().getLocalName(), AID.ISLOCALNAME));
                            messageAcl.setContent(guessedNumber+" : Try something lower !");
                            send(messageAcl);
                        } else {
                            messageAcl.addReceiver(new AID(aclMessage.getSender().getLocalName(), AID.ISLOCALNAME));
                            messageAcl.setContent(guessedNumber+" : Try something higher !");
                            send(messageAcl);
                        }
                    } catch (NumberFormatException | FIPAException e) {
                        System.out.println("The message is not a number");
                    }
                } else {
                    block();
                }
            }
        });
        System.out.println("> server successfully started");
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println("> Server successfully stopped");
    }

}

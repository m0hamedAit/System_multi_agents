package Containers;

import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class ServerContainer {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        AgentController agentController = agentContainer.createNewAgent("ServerContainer", "Agents.ServerAgent", new Object[]{});
        try {
            agentController.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}

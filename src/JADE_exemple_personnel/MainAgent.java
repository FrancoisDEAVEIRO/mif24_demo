package JADE_exemple_personnel;

import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.behaviours.SimpleBehaviour;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javax.swing.JFrame;

public class MainAgent extends Agent {
    JFrame mainwindow;
    int nbCapteur = 0;
    
    @Override
    protected void setup() 
    { 
        System.out.println("Création de l'agent principal : " + getLocalName());
        mainwindow = new mainWindow(this);
        mainwindow.setVisible(true);
        // Enregistrement de l'agent dans le Directory Facilitator
        try{ 
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);
            System.out.println(" L'agent "+getLocalName()+" est enregistré dans DF (Directory Facilitator).");
        } catch (FIPAException e) {
                e.printStackTrace();
        }
	    	
        // Définition de son comportement
        addBehaviour(
            new SimpleBehaviour(this){

                boolean end = false;
                
                @Override
                public void action(){
                    if(nbCapteur < 3)
                        createCapteurAgent();
                    else{
                        // Attente de message
			ACLMessage message = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                        if(message != null){
                            System.out.println(message.getSender().getLocalName() + " envoie : " + message.getContent());
                        }
                    }
                }
                @Override
                public boolean done() { return end; }


            }         
          );
    } 
    
    private void createCapteurAgent(){
        // Récupération des informations du mainContainer en execution sur Jade
        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl(false);
        AgentContainer container =rt.createAgentContainer(p);
        
        // Création de l'agent 
        try{
            nbCapteur++;
            AgentController Agent=null;		
            Agent = container.createNewAgent("CapteurAgent " + nbCapteur, "JADE_exemple_personnel.CapteurAgent", null);
            Agent.start();	  
        }catch (Exception any) {
            any.printStackTrace();
        }
    }
    
}

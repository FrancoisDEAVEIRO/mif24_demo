package JADE_exemple_personnel;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;


public class capteurAgent extends Agent {
    @Override
    protected void setup() 
    { 
        System.out.println("Création d'un agent mobile : " + getLocalName());
          
        // Enregistrement de l'agent dans le Directory Facilitator
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);
            System.out.println(" L'agent " + getLocalName() + " est enregistré dans DF (Directory Facilitator).");
        } catch (FIPAException e) {
            e.printStackTrace();
        } 
        
        // Définition de son comportement
        addBehaviour(
            new CyclicBehaviour(this){

                @Override
                public void action(){
                    
                    // Création et envoie d'un message à l'agent principal
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContent("envoi d'informations");
                    msg.addReceiver(new AID("mainAgent", AID.ISLOCALNAME));
                    send(msg);
                    block(1000);
                }
            }                  
        );
    } 
}
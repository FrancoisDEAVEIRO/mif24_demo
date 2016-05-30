package JADE_exemple_personnel;

import jade.core.AID;
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
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;

public class MainAgent extends Agent {
    mainWindow mainwindow;
    int nbCapteur = 0;
    boolean casesVisibles[][];
    ArrayList<String> listeagents;
    boolean grilledecouverte;
    
    @Override
    protected void setup() 
    { 
        mainwindow = new mainWindow(this);
        mainwindow.setVisible(true);
        
        System.out.println("Création de l'agent principal : " + getLocalName());
        mainwindow.getjTextArea1().append("Création de l'agent principal : " + getLocalName() + "." + "\n");
        
        casesVisibles = new boolean[mainwindow.getGrille().length][mainwindow.getGrille()[0].length];
        for(int i=0; i<casesVisibles.length; i++)
            for(int j=0; j<casesVisibles[i].length; j++)
                casesVisibles[i][j] = false;
        listeagents = new ArrayList<>();
        
        grilledecouverte = false;
        
        // Enregistrement de l'agent dans le Directory Facilitator
        try{ 
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);
            mainwindow.getjTextArea1().append("L'agent "+getLocalName()+" est enregistré dans DF (Directory Facilitator).\n");
        } catch (FIPAException e) {
                e.printStackTrace();
        }
	    	
        // Définition de son comportement
        addBehaviour(
            new SimpleBehaviour(this){
                
                @Override
                public void action(){
                    // Mise à jour de l'affichage
                    
                    mainwindow.drawGrille();           
                    
                    // Vérification du nombre de cases non découvertes
                    int compteur = 0;
                    for(int i=0; i<casesVisibles.length; i++)
                        for(int j=0; j<casesVisibles[i].length; j++)
                            if(casesVisibles[i][j] == false)
                                compteur++;     
                    // Si toutes les cases ont été découvertes, on prévient les agents capteurs 
                    if(compteur == 0){
                        grilledecouverte = true;
                        for(String nomAgent : listeagents){
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.setContent("fin");
                            msg.addReceiver(new AID(nomAgent, AID.ISLOCALNAME));
                            send(msg);
                            mainwindow.getjTextArea1().append(getLocalName() + " envoie à " + nomAgent+ " une demande de destruction.\n");
                        }
                        ((DefaultListModel)mainwindow.getjList1().getModel()).clear();
                    }  
                    else{  
                        // Reception des messages
                        ACLMessage message = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                        if(message != null){
                            mainwindow.getjTextArea1().append(message.getSender().getLocalName() + " envoie à " + getLocalName() + " sa position : {" + message.getContent() + "}.\n");
                            // Parsage du message;
                            String[] tokens = message.getContent().split(",");
                            if(tokens.length == 2){
                                int x = Integer.parseInt(tokens[0]);
                                int y = Integer.parseInt(tokens[1]);
                                // Case considérée comme visible
                                casesVisibles[y][x] = true;
                                //mainwindow.drawCase(x, y, 50);
                            }
                        }    
                    }
                    
                }
                @Override
                public boolean done() { return grilledecouverte; }


            }         
          );
    } 
    
    public void createCapteurAgent(int x, int y){
        // Récupération des informations du mainContainer en execution sur Jade
        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl(false);
        AgentContainer container =rt.createAgentContainer(p);
        
        // Création de l'agent 
        try{
            nbCapteur++;
            AgentController Agent=null;		
            
            // Paramètres de l'agent
            Object args[] = new Object[2];
            args[0] = x;
            args[1] = y;
            String nomAgent = "Agent mobile : capteur n°" + nbCapteur;
            Agent = container.createNewAgent(nomAgent, "JADE_exemple_personnel.CapteurAgent", args);
            Agent.start();
            
            // Ajout du nom de l'agent dans la liste et ajout dans les logs
            ((DefaultListModel)mainwindow.getjList1().getModel()).addElement(nomAgent);
            mainwindow.getjTextArea1().append("Création d'un nouveal agent capteur : " + nomAgent + ".\n");
            listeagents.add(nomAgent);
            
        }catch (Exception any) {
            any.printStackTrace();
        }
    }
    
}

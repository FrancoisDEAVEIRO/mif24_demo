package JADE_exemple_personnel;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CapteurAgent extends Agent {
    
    int posX, posY;
    ArrayList<Coord> historique;
    
    @Override
    protected void setup() 
    { 
        System.out.println("Création d'un agent mobile : " + getLocalName());
          
        // Récupération des paramètres
        Object[] args = getArguments();
        posX = (int)args[0];
        posY = (int)args[1];
        
        // Enregistrement de l'agent dans le Directory Facilitator
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);
            System.out.println("L'agent " + getLocalName() + " est enregistré dans DF (Directory Facilitator).");
        } catch (FIPAException e) {
            e.printStackTrace();
        } 
        Random random = new Random();
        historique = new ArrayList<>();
        
        // Définition de son comportement
        addBehaviour(
            new CyclicBehaviour(this){

                @Override
                public void action(){
                    // Vérification des messages
                    ACLMessage message = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                    if(message != null){
                        if(message.getContent().equals("fin")){
                            // Toutes les cases ont été découvertes, l'agent peut se détruire avec son container  
                            try {
                                getContainerController().kill();
                            } catch (StaleProxyException ex) {
                                doDelete();
                            }
                        }
                    }
                    
                    // Création et envoie d'un message à l'agent principal pour lui indiquer sa position
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContent(posX + "," + posY);
                    msg.addReceiver(new AID("MainAgent", AID.ISLOCALNAME));
                    send(msg);
                    historique.add(new Coord(posX, posY));
                    block(100);
                    
                    // Modification de sa position
                    
                    // Si possible l'agent se déplace sur une case qu'il n'a jamais parcouru
                    int rand = random.nextInt(4);
                    int nbEssais = 0;
                    boolean deplacement = false;
                    while(nbEssais < 4 && !deplacement){
                        rand = (rand +1)%4;
                        switch (rand) {
                            case 0:
                                if(posX>0 && !dejaParcouru(posX-1, posY)){
                                    posX--; 
                                    deplacement = true;
                                }
                                nbEssais++;
                                break;
                            case 1:
                                if(posX<99 && !dejaParcouru(posX+1, posY)){
                                    posX++;
                                    deplacement = true;
                                }
                                nbEssais++;
                                break;    
                            case 2:
                                if(posY>0 && !dejaParcouru(posX, posY-1)){
                                    posY--;
                                    deplacement = true;
                                }
                                nbEssais++;
                                break;
                            case 3:
                                if(posY<49 && !dejaParcouru(posX, posY+1)){
                                    posY++;
                                    deplacement = true;
                                }
                                nbEssais++;
                                break;  
                        }  
                    }
                    
                    // Déplacement completement aléatoire sinon
                    if(!deplacement){
                        rand = random.nextInt(4);
                        switch (rand) {
                            case 0:
                                if(posX>0){
                                    posX--;
                                    break;
                                }
                            case 1:
                                if(posX<99){
                                    posX++;
                                    break;
                                }
                            case 2:
                                if(posY>0){
                                    posY--;
                                    break;
                                }
                            case 3:
                                if(posY<49){
                                    posY++;
                                    break;
                                }
                        }    
                    }
                }
            }                  
        );
    } 
    
    
    private boolean dejaParcouru(int x, int y){
        return historique.stream().anyMatch((coord) -> (coord.x == x && coord.y == y));
    }
    
    class Coord{
        public int x, y;
        
        public Coord(int _x, int _y){
            x = _x;
            y = _y;
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JADE_exemple_personnel;


import jade.core.Agent;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class SuperAgent extends Agent {
			private JFrame jFrame = null;  //  @jve:decl-index=0:visual-constraint="135,20"
			private JPanel jContentPane = null;
			private JPanel jPanel = null;
			private JPanel jPanel1 = null;
			private JPanel jPanel2 = null;
			private JTextArea jTextArea=null;
			private JButton jButton = null;
			private JScrollPane jScrollPane=null;
			private JLabel jLabel=null;
			
			/** Cette méthode est appelé directement apèes la création de l'agent pour permettre

    			  
     l'initialisation et l'affectation des différents comportements à cet agent 
    			  
     */


			protected void setup() {
	    	getJFrame().setVisible(true);
	    	jTextArea.append("Agent "+getLocalName()+" est lancé "+"\n");
	    	System.out.println("Agent "+getLocalName()+" est lancé ");
	    	try {
			
	    		// Création de desciprion de l'agent [Portail]
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			
			// Enregistrement de la description de l'agent dans DF (Directory Facilitator)
			DFService.register(this, dfd);
			jTextArea.append("Agent "+getLocalName()+" est enregistré dans DF (Directory Facilitator) "+"\n");
			System.out.println("Agent "+getLocalName()+" est enregistré dans DF (Directory Facilitator) ");
	    	} catch (FIPAException e) {
			e.printStackTrace();}
	    	
	    	addBehaviour(new CyclicBehaviour(this) {
	    		
			public void action() {
		
			// Attente de message (de l'agent Acheteur)
			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			if (msg != null) {				
			try {
			
				// Création de l'objet [obj] à partir de du message reçu(de l'agent Acheteur)	
			Object[] obj=(Object[]) msg.getContentObject();
			jTextArea.append("Message reçu <la liste des produits>"+"\n");
			jTextArea.append("Contenu de message {"+"\n");
			for(int i=0;i<obj.length;i++){
			jTextArea.append((String) obj[i]+"\n");					
			}
			jTextArea.append("}"+"\n");
			jButton.setText("done");
			} catch (UnreadableException e) {
			e.printStackTrace();}
			jTextArea.append("Fin de traitement"+"\n");									
			
			// Suppression de l'agent [Portail]
			doDelete();
			}
			else {
		
			//Pendant que le message n'est pas encore arrivé le comportement est bloqué
			block();
				}
			}
	    	});
	    	
			}
		
			protected void takeDown() {
					try {
					// Suppression de l'agent [Portail] depuis le DF
					DFService.deregister(this);
					jTextArea.append("Agent "+getLocalName()+" est terminé et supprimé depuis DF (Directory Facilitator) "+"\n");
					System.out.println("Agent "+getLocalName()+" est terminé et supprimé depuis DF (Directory Facilitator) ");
					} catch (FIPAException e) {
					e.printStackTrace();}
					}
						
			public JFrame getJFrame() {
				if (jFrame == null) {
					jFrame = new JFrame();
					jFrame.setSize(new java.awt.Dimension(500,350));
					Dimension tailleEcran =Toolkit.getDefaultToolkit().getScreenSize();
					int largeurEcran = tailleEcran.width;
					int hauteurEcran = tailleEcran.height;
					jFrame.setLocation((largeurEcran-500)/2,(hauteurEcran-350)/2);
					jFrame.setTitle("Exemple de manipulation des agents Jade.");
					jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);					
					jFrame.setContentPane(getJContentPane());
				}
				return jFrame;
			}

			private JPanel getJContentPane() {
				if (jContentPane == null) {
					jContentPane = new JPanel();		
					jContentPane.setLayout(new BorderLayout());
					jContentPane.add(getJPanel2(), java.awt.BorderLayout.NORTH);
					jContentPane.add(getJPanel1(), java.awt.BorderLayout.CENTER);
					jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
					}
				return jContentPane;
				}

			private JPanel getJPanel() {
				if (jPanel == null) {
					FlowLayout flowLayout = new FlowLayout();
					flowLayout.setAlignment(java.awt.FlowLayout.CENTER);
					jPanel = new JPanel();
					jPanel.setLayout(flowLayout);
					jPanel.add(getJButton(), null);
					}
				return jPanel;
				}

			private JPanel getJPanel1() {
				if (jPanel1 == null) {
					jPanel1 = new JPanel();
					jPanel1.setLayout(new BorderLayout());
					jPanel1.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
					}
				return jPanel1;
				}

			private JTextArea getJTextArea() {
				if (jTextArea == null) {
					jTextArea = new JTextArea();
					}
				return jTextArea;
				}

			private JButton getJButton() {
				if (jButton == null) {
					jButton = new JButton();
					jButton.setText("Go");
					
					/** L'objectif de ce button et de créer les deux agents Vendeur et Acheteur

    					  
     et pour cela on doit premièrement récupérer le conteneur en cours (Container) de Jade
    					  
     puis créer deux agents Vendeur et Acheteur pour lancer le processu d'échange de message
    					  
     et cette partie représente la solution la plus robuste pour l'exploitation des agents  
    					  
     ansi que leurs service dans des classes java (Application, Applets, Servelets,etc...)
    					  
    */


					jButton.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
						try {
							
						// Récupération du conteneur (Main Container) en cours d'execution de Jade					
						Runtime rt = Runtime.instance();
						
						// Création du profil par défault
						ProfileImpl p = new ProfileImpl(false);
						AgentContainer container =rt.createAgentContainer(p);
						
						// Agent controleur pour permettre la création des agents 
						AgentController Agent=null;		
						
						Agent = container.createNewAgent("Vendeur", "JADE_exemple_personnel.Vendeur", null);
						
						// Démarrage de l'agent Vendeur
						Agent.start();	
						jTextArea.append("Agent Vendeur est lancé "+"\n");
						
						/* Création de l'agent Acheteur
						   cette commande est équivalente à la suivante: 
						   java jade.Boot Acheteur:JADE_exemple_personnel.Acheteur

    						
    */


						Agent = container.createNewAgent("Acheteur", "JADE_exemple_personnel.Acheteur", null);
				
						// Démarrage de l'agent Acheteur
						Agent.start();
						jTextArea.append("Agent Acheteur est lancé "+"\n");
						} catch (Exception any) {
						any.printStackTrace();}
						}
					});
					}
				return jButton;
				}

			private JScrollPane getJScrollPane() {
				if (jScrollPane == null) {
					jScrollPane = new JScrollPane();
					jScrollPane.setViewportView(getJTextArea());
				}
				return jScrollPane;
				}

			private JPanel getJPanel2() {
				if (jPanel2 == null) {
					jLabel = new JLabel();
					jLabel.setText("Exemple des agents Jade");
					jLabel.setFont(new java.awt.Font("Perpetua", java.awt.Font.BOLD, 18));
					jPanel2 = new JPanel();
					jPanel2.setBackground(java.awt.SystemColor.info);
					jPanel2.add(jLabel, null);
				}
				return jPanel2;
				}
		/*
			public static void main(String[] args) {
				try {
					// Récupération du conteneur (Main Container) en cours d'execution de Jade					
					Runtime rt = Runtime.instance();
				
					// Création du profil par défault
					ProfileImpl p = new ProfileImpl(false);
					AgentContainer container =rt.createAgentContainer(p); // get a container controller for creating new agents
				
					// Agent controleur pour permettre la création des agents 
					AgentController Agent=null;		
				
					/* Création de l'agent Portail
					   cette commande est équivalente à la suivante: 
					   java jade.Boot Portail:JADE_exemple_personnel.Portail

    					
    


					Agent = container.createNewAgent("Portail", "JADE_exemple_personnel.Portail", null);
				
					// Démarrage de l'agent Portail
					Agent.start();											
					} catch (Exception any) {
					any.printStackTrace();}				
				}*/
}
package JADE_exemple_personnel;

import java.util.Random;

public class GenerateurGrille {

    int BORNEMAX;
    int BORNEMIN;
    int TAILLEX;
    int TAILLEY;
    int ECARTMAX; 
    
    public GenerateurGrille(int max, int min, int x, int y, int ecart){
        BORNEMAX = max;
        BORNEMIN = min;
        TAILLEX = x;
        TAILLEY = y;
        ECARTMAX = ecart;
        ECARTMAX--;
    }
    
    int[][] generer(){
       int tab[][] = new int[TAILLEY][TAILLEX];
       
       // Generation
       Random random = new Random();
       tab[0][0] = 0;
       // Première ligne
       for(int i=1; i<tab[0].length; i++){
           if(tab[0][i-1] == BORNEMIN){
                int rand = random.nextInt(2);
                switch (rand) {
                    case 0:
                        tab[0][i] = tab[0][i-1] + 1;
                        break;
                    default:
                        tab[0][i] = tab[0][i-1];
                        break;
                }     
           }
           else if(tab[0][i-1] == BORNEMAX){
                int rand = random.nextInt(3);
                switch (rand) {
                    case 0:
                        tab[0][i] = tab[0][i-1] - 1;
                        break;
                    default:
                        tab[0][i] = tab[0][i-1];
                        break;
                }    
           }
           else{
                int rand = random.nextInt(2);
                switch (rand) {
                    case 0:
                        tab[0][i] = tab[0][i-1] - 1;
                        break;
                    case 1:
                        tab[0][i] = tab[0][i-1] + 1;
                        break;
                    default:
                        tab[0][i] = tab[0][i-1];
                        break;
                }       
           }
       }
       
        // Reste de la grille
        for(int j=1; j<tab.length; j++){
            for(int i=1; i<tab[j].length; i++){     
                //Régulateur d'écarts
                if(i > 0 && tab[j][i-1] > tab[j-1][i] + ECARTMAX){
                    tab[j][i] = tab[j][i-1] - 1;
                }
                else if(i > 0 && tab[j][i-1] < tab[j-1][i] - ECARTMAX){
                    tab[j][i] = tab[j][i-1] + 1;
                }
                // Si voisins OK on continue sur du random
                else{
                    if(tab[j-1][i] == BORNEMIN){
                         int rand = random.nextInt(2);
                         switch (rand) {
                             case 0:
                                 tab[j][i] = tab[j-1][i] + 1;
                                 break;
                             default:
                                 tab[j][i] = tab[j-1][i];
                                 break;
                         }     
                    }
                    else if(tab[j-1][i] == BORNEMAX){
                         int rand = random.nextInt(3);
                         switch (rand) {
                             case 0:
                                 tab[j][i] = tab[j-1][i] - 1;
                                 break;
                             default:
                                 tab[j][i] = tab[j-1][i];
                                 break;
                         }    
                    }
                    else{
                         int rand = random.nextInt(2);
                         switch (rand) {
                             case 0:
                                 tab[j][i] = tab[j-1][i] - 1;
                                 break;
                             case 1:
                                 tab[j][i] = tab[j-1][i] + 1;
                                 break;
                             default:
                                 tab[j][i] = tab[j-1][i];
                                 break;
                         }       
                    }   
                }
            }
        }
        return tab;
    }
    
    
    public static void main(String[] args) {
        GenerateurGrille grille = new GenerateurGrille(9, -9, 100, 50, 3);
        int tab[][] = grille.generer();
        
        // Affichage
        for(int j=0; j<tab.length; j++){
            for(int i=0; i<tab[j].length; i++){
                if(tab[j][i] >=0)
                    System.out.print("+");
                System.out.print(tab[j][i] + " ");
            }
            System.out.println();
        }  

    }
    
}

package JADE_exemple_personnel;

public class Grille {
    private int tab[][];
    private int largeur;
    private int hauteur;
    
    public Grille(int l, int h){
        this.largeur = l;
        this.hauteur = h;
        this.tab = new int[largeur][hauteur];
    }
    
    public void init(){
        for(int i=0; i<largeur; i++){
            for(int j=0; j<hauteur; j++){
                this.tab[i][j] = (int) (Math.random()*100 +50);
            }
        }
    }
}

package JADE_exemple_personnel;

public class Grille {
    public int tab[][];
    public int largeur;
    public int hauteur;
    
    public Grille(int l, int h){
        this.largeur = l;
        this.hauteur = h;
        this.tab = new int[largeur][hauteur];
    }
    
    public void init(){
        for(int i=0; i<largeur; i++){
            for(int j=0; j<hauteur; j++){
                this.tab[i][j] = 1000;//(int) (Math.random()*100 +50);
            }
        }
    }
    
    public void add (int x, int y, int value){
        this.tab[x][y] = value;
    }
}

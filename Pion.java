/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProjetS4;

/**
 *
 * @author Utilisateur
 */
public class Pion {
    
    private int noir, blanc;  //pour les numéros des 2 faces
    private boolean surPlateau = false;  //vrai sur le pion est sur le plateau, faux sinon
    private int x,y; //coordonnées du pion x: colonnes et y: lignes
    private int direction;
    private String faceVisible= "blanc";
    
    
    public Pion( int face, int pile){
        this.blanc=face;
        this.noir=pile;
        
    }
    
    public int getNoir(){  //avec x la face visible
        return noir;
    }

    public int getBlanc() {
        return blanc;
    }

    public boolean getSurPlateau() {
        return surPlateau;
    }

    public void setSurPlateau(boolean surPlateau) {
        this.surPlateau = surPlateau;
    }
    
    private void changerFaceVisible(){
        if (this.faceVisible=="blanc"){
            this.faceVisible="noir";
        }
        else{
            this.faceVisible="blanc";
        }
    }
    
    //deplacement d'une case d'un pion
    private void deplacerPion(int direction){
        // "1 : haut"
        //"2 : bas"
        //"3 : gauche"
        //"4 : droite"
        if(direction==1){
            this.y=this.y-1;
        }
        if(direction==2){
            this.y=this.y+1;
        }
        if(direction==3){
            this.x=this.x-1;
        }
        if(direction==4){
            this.x=this.x+1;
        }
    }
    
    private int demanderDirection(){
        Scanner sc = new Scanner (System.in);
        int choix;

        System.out.println("Où voulez-vous aller?\n"
                    + "0: terminer mon tour\n"
                    + "1 : haut\n"
                    + "2 : bas\n"
                    + "3 : gauche\n"
                    + "4 : droite");
        
        choix=sc.nextInt();
        return choix;
    }
    
    
    private boolean verifBordPlateau(int y, int x){ //les coordonnées passées en paramètres sont les coordonnées que l'on souhaite verifier
        boolean pasAuBord = true; //vrai initialement
        
        if(y<0 || y>10 || x<0 || x>15 || (y==0 && (x==12 || x==13 || x==14 || x==15)) || (y==1 && (x==13
                || x==14 || x==15)) || (y==2 && (x==14 || x==15)) || (y==3 && x==16) || (y==7 && x==1) || 
                (y==8 && (x==1 || x==2)) || (y==9 && (x==1 || x==2 || x==3)) || (y==10 && (x==1 || x==2 || 
                x==3 || x==4))){
            pasAuBord =  false;
        }
        
        return pasAuBord;
    }
    
    private boolean verifMonstre(int y, int x, PlateauDeJeu p){
        boolean pasMonstre = true;
        
        if(p.getPlateau()[y][x].getMonstre()==true){
            pasMonstre=false;
        }
        
        return pasMonstre;
    }
    
    private boolean verifPion(int y, int x, PlateauDeJeu p){
        boolean pasPion = true;
        
        if(p.getPlateau()[y][x].getPion()==true){
            pasPion=false;
        }
        
        return pasPion;
    }
    
    private boolean verifFlaqueSang(int y, int x, PlateauDeJeu p){
        boolean pasFlaque = true;
        
        if(p.getPlateau()[y][x].getClass().getName()=="Flaque"){
            pasFlaque=false;
        }
        
        return pasFlaque;
    }
    
    private boolean verifBlocPierre(int y, int x, PlateauDeJeu p){
        boolean pasBloc = true;
        
        if(p.getPlateau()[y][x].getClass().getName()=="BlocPierre"){
            pasBloc=false;
        }
        
        return pasBloc;
    }
    
    private int nombreDeDeplacementsPossibles(){
        if(this.faceVisible=="blanc"){
            return blanc;
        }
        else{
            return noir;
        }
    }
    
    private void retournerPion(){
        if(this.faceVisible=="blanc"){
            this.faceVisible="noir";
        }
        else{
            this.faceVisible="blanc";
        }
    }
    
    private int empecherArret(){
        direction= this.demanderDirection();
        while(direction==0){
            System.out.println("Vous ne pouvez pas vous arrêter ici");
            direction= this.demanderDirection();
        }
        return direction;
    }
    
    private boolean toutesLesVerificationsBloc(int y, int x, PlateauDeJeu p){

        if(!this.verifBordPlateau(y, x)){
            return false;
        }
        else if(!this.verifMonstre(y, x, p)){
            return false;
        }
        else if(this.verifBlocPierre(y, x, p)){
            return false;
        }
        else if(this.verifPion(y, x, p)){
            return false;
        }
        else return true;
    }
    
    public void Deplacement(PlateauDeJeu p){
        int i= this.nombreDeDeplacementsPossibles();
        int direction=this.demanderDirection(); //première fois qu'on demande la direction
        while(i!=0){
            if(direction!=0){
                if(direction==1){ //l'utilisateur veut aller en haut
                    if(this.verifBordPlateau(this.y-1, this.x)){
                        if(this.verifMonstre(this.y-1, this.x, p)){
                            if(this.verifFlaqueSang(this.y-1, this.x, p)){
                                if(this.verifBlocPierre(this.y-1, this.x, p)){
                                    if(this.verifPion(this.y-1, this.x, p)){ 
                                        this.deplacerPion(direction); //s'il n'y a aucun pb de deplacement
                                        i--;
                                        direction=this.demanderDirection();
                                    }
                                    else{ //s'il y a un pion, on part du principe qu'il y a une issue possible autour du pion pour le moment
                                        if(i>1){ //le pion a encore un deplacement possible apres, il peut passer sur le pion
                                            this.deplacerPion(direction);
                                            i--;
                                            direction=this.empecherArret();
                                        }
                                        else{
                                            System.out.println("Vous ne pouvez pas vous arrêter sur un pion");
                                            direction=this.demanderDirection();
                                        }
                                    }
                                }
                                //s'il y a un bloc que faire
                                else{
                                    if(this.toutesLesVerificationsBloc(y-2, x, p) && this.verifFlaqueSang(y-2, x, p)){ //s'il n'y a rien après le bloc
                                        this.deplacerPion(direction);
                                        i--;
                                        BlocPierre b = new BlocPierre(x, y-2, true); //echange des cases Case et BlocPierre
                                        p.setPlateau(y-2, x, b);
                                        Case c= new Case(x, y-1, true);
                                        p.setPlateau(y-1, x, c);
                                        
                                    }
                                    else if (!this.verifFlaqueSang(y-2, x, p)){
                                        //s'il y a une flaque de sang après le bloc
                                        boolean finFlaque=false;
                                        int k=y-2,j=x;
                                        int nbCases=0;
                                        while(finFlaque){
                                            finFlaque=p.getPlateau()[k][j].getClass().getName()!="Flaque";
                                            k--;
                                            if(!finFlaque){ //pas fini
                                                nbCases++;
                                            }
                                            
                                        }
                                       if(this.toutesLesVerificationsBloc(y-2-nbCases, x, p)){
                                           //si après la flaque il n'y a rien, poussé derrière
                                           
                                       }
                                        
                                        //s'il y a qqch va sur la dernière case de la flaque à compléter
                                    }
                                    else{ //s'il y a un pion, un autre bloc, le monstre ou le bord du plateau après le bloc
                                        System.out.println("Vous ne pouvez pas pousser ce bloc de pierre, veuillez changer de direction");
                                        direction=this.demanderDirection();
                                    }
                                }
                            }
                            //s'il y a une flaque de sang à compléter
                        }
                        //s'il y a le monstre sur la case
                        else{
                            System.out.println("Vous ne pouvez pas passer sur le monstre");
                            direction=this.demanderDirection();
                        }
                    }
                    //si on est au bord du tableau
                    else{
                        System.out.println("Vous avez atteint le bord du plateau, changez de direction");
                        direction=this.demanderDirection();
                    }
                }
            }
            //si le joueur veut et peut s'arrêter
            i=0;
            System.out.println("Votre tour est terminé");
        }
        this.retournerPion();
    }
}

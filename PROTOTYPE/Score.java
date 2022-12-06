import extensions.CSVFile;
import extensions.File;

class Score extends Program{

    String couleur_texte = "WHITE";
    String couleur_de_fond = "BLACK";
    String couleur_erreur = "YELLOW";
    String couleur_confirmation = "RED";
    String couleur_ecriture = "CYAN";

    Joueur newJoueur(){
	Joueur j = new Joueur();
	j.pseudo = "";
	j.score = 0;
	j.difficulter = "";
	j.pourcentageDifficulter = 0.0;
	j.journeePasser = 0;
	return j;
    }
    
    void actualiserScore(Joueur j,int reponse,int resultat,int seconde){
	double multiplication;
	if (seconde <= 5){
	    multiplication = 1.00;
	} else if (seconde <= 20) {
	    multiplication = 0.75;
	} else if (seconde <= 45) {
	    multiplication = 0.50;
	} else {
	    multiplication = 0.25;
	}
	if(reponse == resultat){
	    j.score += (int)(resultat*(1.25*multiplication));
	} else {
	    j.score -= (int)(resultat*(1.15*multiplication));
	}
    }

    void enregistrerJoueur(Joueur j){
	CSVFile fichierScore = loadCSV("score.csv");
	String[][] nouveauScore = new String[1][4];
        nouveauScore[0][0] = j.pseudo;
	nouveauScore[0][1] = ""+j.score;
	nouveauScore[0][2] = j.difficulter;
	nouveauScore[0][3] = ""+j.journeePasser;
	saveCSV(nouveauScore,"score.csv");
    }
	
	
    void afficherClassementScore(){
	CSVFile fichierScore = loadCSV("score.csv");
	int nbrLignes = rowCount(fichierScore);
	Joueur[] js = new Joueur[nbrLignes];
	for (int l=0;l<nbrLignes;l+=1){
	    Joueur j = newJoueur();
	    j.pseudo = getCell(fichierScore,l,0);
	    j.score = stringToInt(getCell(fichierScore,l,1));
	    j.difficulter = getCell(fichierScore,l,2);
	    j.journeePasser = stringToInt(getCell(fichierScore,l,3));
	    js[l] = j;
	}
	int indice_min ;
	for (int i=0;i<(nbrLignes-1);i+=1){
	    indice_min = -1;
	    for (int j=i;j<nbrLignes;j+=1){
		if (js[i].score < js[j].score){
		    indice_min = j;
		}
	    }
	    echanger(js,i,indice_min);
	}
	int ligneClassement = nbrLignes;
	if (ligneClassement > 10){
	    ligneClassement = 10;
	}
	int[] lesPlusLong = plusLong(js);
	int lenPseudo = lesPlusLong[0];
	int lenJourneePasser = lesPlusLong[1];
	int lenScore = lesPlusLong[2];
	String ligneDebutFin = "       ----------"+ajouterTiret(lenPseudo)+"-----"+ajouterTiret(lenScore)+"-------------------"+ajouterTiret(lenJourneePasser)+"---";
	println(ligneDebutFin);
	for (int a=0;a<ligneClassement;a+=1){
	    if ((a+1) == 10){
		print("      |  "+(a+1)+"ème |  ");
	    } else {
		if ((a+1) == 1){
		    print("      |  "+(a+1)+"er   |  ");
		} else {
		    print("      |  "+(a+1)+"ème  |  ");
		}
	    }
	    afficherJoueur(js[a],lenScore,lenPseudo,lenJourneePasser);
	    println("  |");
	}
	for (int b=ligneClassement;b<10;b+=1){
	    if ((b+1) == 10){
		print("      |  "+(b+1)+"ème |  ");
	    } else {
	        if ((b+1) == 1){
		    print("      |  "+(b+1)+"er   |  ");
		} else {
		    print("      |  "+(b+1)+"ème  |  ");
		}
	    }
	    println("*"+ajouterEspace(lenPseudo-1)+"  |  *"+ajouterEspace(lenScore-1)+"  |  *"+ajouterEspace(8)+"  |  *"+ajouterEspace(lenJourneePasser-1)+"  |");
	}
	println(ligneDebutFin);
    }

    void echanger(Joueur[] js, int indice , int indice_min){
	if(indice_min != -1){
	    Joueur save = js[indice];
	    js[indice] = js[indice_min];
	    js[indice_min] = save;
	}
    }

    int[] plusLong(Joueur[] tab){
	int[] max = new int[]{0,0,0};
	for (int i=0;i<length(tab);i+=1){
	    if (length(tab[i].pseudo)>max[0]){
		max[0] = length(tab[i].pseudo);
	    }
	    if (length(""+(tab[i].journeePasser))>max[1]){
		max[1] = length(""+(tab[i].journeePasser));
	    }
	    if (length(""+(tab[i].score))>max[2]){
		max[2] = length(""+(tab[i].score));
	    }
	}
	return max;
    }

    void afficherJoueur(Joueur j, int lenScore , int lenPseudo , int lenJourneePasser){
	print(j.pseudo+ajouterEspace((lenPseudo-length(""+(j.pseudo))))+"  |  "+j.score+ajouterEspace((lenScore-length(""+(j.score))))+"  |  "+j.difficulter+ajouterEspace(9-length(""+(j.difficulter)))+"  |  "+j.journeePasser+ajouterEspace(lenJourneePasser-length(""+(j.journeePasser))));
    }

    String ajouterEspace(int nbr){
	String espace = "";
	for (int n=0;n<nbr;n++){
	    espace += " ";
	}
	return espace;
    }

    String ajouterTiret(int nbr){
	String tiret = "";
	for (int n=0;n<nbr;n++){
	    tiret += "-";
	}
	return tiret;
    }


    void afficherMenuMode(){
	File menuMode = newFile("MenuMode.txt");
	int indiceLigne = 0;
	text(couleur_texte);
	while (ready(menuMode)){
	    if (indiceLigne == 25 || indiceLigne == 26){
		text(couleur_confirmation);
		println(readLine(menuMode));
		text(couleur_texte);
	    } else {
		println(readLine(menuMode));
	    }
	    indiceLigne += 1;
	}
    }

    void afficherMenu(){
	File menu = newFile("Menu.txt");
	int indiceLigne = 0;
	text(couleur_texte);
	while (ready(menu)){
	    if (indiceLigne == 25 || indiceLigne == 26){
		text(couleur_confirmation);
		println(readLine(menu));
		text(couleur_ecriture);
	    } else {
		println(readLine(menu));
	    }
	    indiceLigne += 1;
	}
    }

    void afficherMenuParametres(){
	File menuParametres = newFile("MenuParametres.txt");
	text(couleur_texte);
	while (ready(menuParametres)){
		println(readLine(menuParametres));
	    }
	}

    void afficherClassement(){
	File menuParametres = newFile("Classement.txt");
	text(couleur_texte);
	while (ready(menuParametres)){
		println(readLine(menuParametres));
	    }
	}

    Joueur creerJoueur(){
	File menuJoueur = newFile("CreerJoueur.txt");
	text(couleur_texte);
	while (ready(menuJoueur)){
	    println(readLine(menuJoueur));
	}
	Joueur j = newJoueur();
	String reponse;
	boolean reponseCorrect;
	do{
	    text(couleur_texte);
	    print("\n            PSEUDO : ");
	    text(couleur_ecriture);
	    reponseCorrect = true;
	    reponse = readString();
	    if (length(reponse) < 3 || length(reponse) > 16){
		reponseCorrect = false;
		text(couleur_texte);
		while (ready(menuJoueur)){
		    println(readLine(menuJoueur));
		}
		text(couleur_erreur);
		println("            ERREUR DE SAISIE ! VEUILLEZ ENTREZ UN PSEUDO ENTRE 3 ET 16 CARACTERES");
	    }
	}while(!reponseCorrect);
	j.pseudo = reponse;
	do{
	    text(couleur_texte);
	    print("            DIFFICULTER (facile / moyen / difficile) : ");
	    text(couleur_ecriture);
	    reponseCorrect = true;
	    reponse = readString();
	    if (!equals(reponse,"facile") && !equals(reponse,"moyen") && !equals(reponse,"difficile")){
		reponseCorrect = false;
		text(couleur_texte);
		while (ready(menuJoueur)){
		    println(readLine(menuJoueur));
		}
		text(couleur_erreur);
		println("            ERREUR DE SAISIE ! VEUILLEZ ENTREZ \"facile\" ou \"moyen\" ou \"difficile\"");
	    }
	}while(!reponseCorrect);
	j.difficulter = reponse;
	if (equals(reponse,"facile")){
	    j.pourcentageDifficulter = 0.75;
	} else if (equals(reponse,"moyen")) {
	    j.pourcentageDifficulter = 1.0;
	} else {
	    j.pourcentageDifficulter = 1.25;
	}
	return j;
    }

    void debutHistoire(Joueur j){
	File histoire = newFile("Histoire.txt");
	int indiceLigne = 0;
	for (int i=1;i<4;i+=1){
	    text(couleur_texte);
	    clearScreen();
	    while (ready(histoire) && indiceLigne <= 28*i){
		println(readLine(histoire));
		indiceLigne += 1;
	    }
	    if (i == 3){
		String reponse;
		boolean reponseCorrect;
		do{
		    print("       CHOIX : ");
		    text(couleur_ecriture);
		    reponseCorrect = true;
		    reponse = readString();
		    if (!(equals(reponse,"1") || equals(reponse,"2"))){
			reponseCorrect = false;
			text(couleur_erreur);
			println("       ERREUR DE SAISIE ! VEUILLEZ ENTRER \"OUI\" ou \"NON\"");
			text(couleur_texte);
		    }
		}while(!reponseCorrect);
		if (equals(reponse,"2")){
		    do{
			text(couleur_texte);
			print("       DIFFICULTER (facile / moyen / difficile) : ");
			text(couleur_ecriture);
			reponseCorrect = true;
			reponse = readString();
			if (!equals(reponse,"facile") && !equals(reponse,"moyen") && !equals(reponse,"difficile")){
			    reponseCorrect = false;
			    text(couleur_erreur);
			    println("       ERREUR DE SAISIE ! VEUILLEZ ENTREZ \"facile\" ou \"moyen\" ou \"difficile\"");
			}
		    }while(!reponseCorrect);
		    j.difficulter = reponse;
		    if (equals(j.difficulter,"facile")){
			j.pourcentageDifficulter = 0.75;
		    } else if (equals(j.difficulter,"moyen")) {
			j.pourcentageDifficulter = 1.0;
		    } else {
			j.pourcentageDifficulter = 1.25;
		    }
		}
	    } else {
		text(couleur_confirmation);
		print("       ENTRER POUR CONTINUER ");
		text(couleur_de_fond);
		readString();
	    }
	}
    }

    void afficherAtteindreScore(int atteindreScore){
	File score = newFile("afficherScore.txt");
	int indiceLigne = 0;
	text(couleur_texte);
	clearScreen();
	while (ready(score)){
	    if (indiceLigne == 3){
		print("  |	Le score à atteindre en ce jour est de : ");
		text(couleur_confirmation);
		background(couleur_texte);
		print(" "+atteindreScore+" ");
		reset();
		text(couleur_texte);
		background(couleur_de_fond);
		println(ajouterEspace(20-(2+length((""+atteindreScore))))+"|");
	    }
	    println(readLine(score));
	    indiceLigne += 1;
	}
	text(couleur_confirmation);
	print("       ENTRER POUR JOUER ");
	text(couleur_de_fond);
	readString();
    }

    int calculerAtteindreScore(Joueur j){
	return ((int)((10000.0*j.pourcentageDifficulter)+(0.2*((double)j.journeePasser))));
    }


    void algorithm(){
	boolean finDeJeu = false;
	while (!finDeJeu){
	    background(couleur_de_fond);
	    clearScreen();
	    afficherMenu();
	    text(couleur_texte);
	    print("       CHOIX : ");
	    text(couleur_ecriture);
	    boolean choixCorrect;
	    int choix;
	    do{
		choixCorrect = true;
		choix = stringToInt(readString());
		if (choix > 5 || choix < 1){
		    choixCorrect = false;
		    clearScreen();
		    afficherMenu();
		    text(couleur_erreur);
		    println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
		    text(couleur_texte);
		    print("       CHOIX : ");
		    text(couleur_ecriture);
		}
	    }while(!choixCorrect);
	    if (choix == 1){
		clearScreen();
		text(couleur_texte);
		println();
		afficherMenuMode();
		text(couleur_texte);
		print("       CHOIX : ");
		text(couleur_ecriture);
		do{
		    choixCorrect = true;
		    choix = stringToInt(readString());
		    if (choix > 3 || choix < 1){
			choixCorrect = false;
			clearScreen();
			text(couleur_texte);
			afficherMenuMode();
			text(couleur_erreur);
			print("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)\n");
			text(couleur_texte);
			print("       CHOIX : ");
			text(couleur_ecriture);
		    }
		}while(!choixCorrect);
		clearScreen();
		text(couleur_ecriture);
		if (choix == 1){
		    int atteindreScore;
		    Joueur j = creerJoueur();
		    debutHistoire(j);
		    atteindreScore = calculerAtteindreScore(j);
		    afficherAtteindreScore(atteindreScore);
		    //LanceJeu();
		} else if (choix == 2){
		    int atteindreScore;
		    Joueur j = creerJoueur();
		    do{
		    	atteindreScore = calculerAtteindreScore(j);
		    	afficherAtteindreScore(atteindreScore);
		    	//LanceJeuInfini();
		    }while(j.score < atteindreScore);
		    enregistrerJoueur(j);
		    clearScreen();
		    afficherClassement();
		    println();
		    afficherClassementScore();
		    text(couleur_confirmation);
		    print("\n\n       ENTRER POUR REVENIR AU MENU");
		    text(couleur_de_fond);
		    readString();
		}
		
	    } else if (choix == 2){
		
	    } else if (choix == 3){
		clearScreen();
		afficherClassement();
		println();
		afficherClassementScore();
		text(couleur_confirmation);
		print("\n\n       ENTRER POUR REVENIR AU MENU");
		text(couleur_de_fond);
		readString();
	    } else if (choix == 4){
		clearScreen();
		afficherMenuParametres();
		do{
		    text(couleur_texte);
		    print("       CHOIX : ");
		    text(couleur_ecriture);
		    choixCorrect = true;
		    choix = stringToInt(readString());
		    if (choix > 4 || choix < 1){
			choixCorrect = false;
			clearScreen();
			afficherMenuParametres();
			text(couleur_erreur);
			println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
		    }
		}while(!choixCorrect);
		if (choix == 1){
		    
		} else if (choix == 2){
		    
		} else if (choix == 3){

		}
	    } else {
		finDeJeu = true;
	    }
	}
	clearScreen();
    }
}

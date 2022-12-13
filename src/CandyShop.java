import extensions.CSVFile;
import extensions.File;

class CandyShop extends Program{

    String couleur_texte;
    String couleur_de_fond;
    String couleur_erreur;
    String couleur_confirmation;
    String couleur_ecriture;

    final String MENU ="Menu.txt";
    final String MENU_MODE = "MenuMode.txt";
    final String MENU_PARAMETRE = "MenuParametres.txt";
    final String MENU_COULEUR = "Couleur.txt";
    final String REGLE = "Regles.txt";
    final String TABLES = "Table_Multiplication.txt";
    final String CONSEILS = "Conseils.txt";
    final String MENU_CONSEILS = "MenuConseils.txt";
    final String MENU_CLASSEMENT = "MenuClassement.txt";
    final String MENU_CLASSEMENT_CAMPAGNE = "MenuClassementCampagne.txt";
    final String MENU_CLASSEMENT_INFINI = "MenuClassementInfini.txt";
    final String CLASSEMENT_GENERALE = "ClassementGénérale.txt";
    final String CLASSEMENT_CAMPAGNE = "ClassementCampagne.txt";
    final String CLASSEMENT_INFINI = "ClassementInfini.txt";
    final String MENU_REPRENDRE = "MenuReprendre.txt";
    final String SAUVEGARDE = "Sauvegarde.csv";
    final String CLASSEMENT = "Classement.csv";
    final String SAVE_OR_CONTINUE = "save_or_continue.txt";
    final String SUPPRIMER_SAVE = "Supprime.txt";
	final String BONBONS = "Bonbons.txt";
	final String[] LISTEBONBONS = new String[]{"Dragibus","Schtroumpf","Langue de chat","Crocodile","Moam","Carambar","Malabar","Tête brulée","Ours en gélatine","Réglisse"};
    
    void initialisationCouleur(){
	couleur_texte = "WHITE";
	couleur_de_fond = "BLACK";
	couleur_erreur = "YELLOW";
	couleur_confirmation = "RED";
	couleur_ecriture = "CYAN";
    }
    
    Joueur newJoueur(){
	Joueur j = new Joueur();
	j.pseudo = "";
	j.score = 0;
	j.difficulte = newDifficulte();
	j.journeePasse = 1;
	j.mode = "";
	return j;
    }

    Difficulte newDifficulte(){
	Difficulte d = new Difficulte();
	d.difficulte = "";
	d.nbClientsMax = 0;
	d.multiplicateurScore = 0.0;
	d.intervalle = 0.0;
	return d;
    }
    
    /*void actualiserScore(Joueur j,int reponse,int resultat,int seconde){
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
    }*/

    void enregistrerJoueur(Joueur j){
        enregistrer(j,CLASSEMENT);
    }

    void enregistrer(Joueur j , String nomFichier){
	CSVFile fichier = loadCSV(nomFichier);
	String[][] nouveauScore = new String[1][5];
        nouveauScore[0][0] = j.pseudo;
	nouveauScore[0][1] = ""+j.score;
	nouveauScore[0][2] = j.difficulte.difficulte;
	nouveauScore[0][3] = ""+j.journeePasse;
	nouveauScore[0][4] = j.mode;
	saveCSV(nouveauScore,nomFichier);
    }

    void enregistrer(Joueur j ,int conserver1, int conserver2, String nomFichier){
	CSVFile fichier = loadCSV(nomFichier);
	String[][] nouveauScore = new String[1][5];
        nouveauScore[0][0] = j.pseudo;
	nouveauScore[0][1] = ""+j.score;
	nouveauScore[0][2] = j.difficulte.difficulte;
	nouveauScore[0][3] = ""+j.journeePasse;
	nouveauScore[0][4] = j.mode;
	saveCSV(nouveauScore,nomFichier);

	// regarder si saveCSV(); supprime les anciens enregistrement , si non alors faire en sorte de crée un nouveau fichier avec uniquement les 2 save + nouvelle
	// si oui alors reprendre les anciens enregistrements
    }


    int compteElements(CSVFile fichier , int nbrLignes , String niveau , String mode){
	if (niveau == "*"){
	    return nbrLignes;
	}
	int cpt = 0;
	for(int i=0;i<nbrLignes;i+=1){
	    if (equals(getCell(fichier,i,2),niveau) && equals(getCell(fichier,i,4),mode)){
		cpt += 1;
	    }
	}
	return cpt;
    }
	
	
    void afficherClassementScore(String niveau , String mode){
	CSVFile fichierScore = loadCSV(CLASSEMENT);
	int nbrLignes = rowCount(fichierScore);
	Joueur[] js = new Joueur[compteElements(fichierScore,nbrLignes,niveau,mode)];
	int ligneClassement = length(js);
	if (niveau == "*"){
	    for(int l=0 ; l<nbrLignes; l+=1){
		Joueur j = newJoueur();
		j.pseudo = getCell(fichierScore,l,0);
		j.score = stringToInt(getCell(fichierScore,l,1));
		j.difficulte.difficulte = getCell(fichierScore,l,2);
		j.journeePasse = stringToInt(getCell(fichierScore,l,3));
		j.mode = getCell(fichierScore,l,4);
		js[l] = j;
	    }
	} else {
	    int indice = 0 , indice_tab = 0;
	    while( indice<nbrLignes && indice_tab<ligneClassement){
		if (equals(getCell(fichierScore,indice,2),niveau) && equals(getCell(fichierScore,indice,4),mode)){
		    Joueur j = newJoueur();
		    j.pseudo = getCell(fichierScore,indice,0);
		    j.score = stringToInt(getCell(fichierScore,indice,1));
		    j.difficulte.difficulte = getCell(fichierScore,indice,2);
		    j.journeePasse = stringToInt(getCell(fichierScore,indice,3));
		    j.mode = getCell(fichierScore,indice,4);
		    js[indice_tab] = j;
		    indice_tab += 1;
		}
		indice += 1;
	    }
	}
	int indice_min ;
	for (int i=0;i<ligneClassement-1;i+=1){
	    indice_min = -1;
	    for (int j=i;j<ligneClassement;j+=1){
		if (js[i].score < js[j].score){
		    indice_min = j;
		}
	    }
	    echanger(js,i,indice_min);
	}
	if (ligneClassement > 10){
	    ligneClassement = 10;
	}
	int[] lesPlusLong = plusLong(js);
	int lenPseudo = lesPlusLong[0];
	int lenJourneePasse = lesPlusLong[1];
	int lenScore = lesPlusLong[2];
	int complete = 0;
	if (length(js) == 0){
	    complete = 3;
	}
	String ligneDebutFin = "      -----------"+ajouterCaractere(lenPseudo,'-')+"-----"+ajouterCaractere(lenScore,'-')+"-------------------"+ajouterCaractere(lenJourneePasse,'-')+"-----------------"+ajouterCaractere(complete,'-');
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
	    afficherJoueur(js[a],lenScore,lenPseudo,lenJourneePasse);
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
	    println("*"+ajouterCaractere(lenPseudo-1,' ')+"  |  *"+ajouterCaractere(lenScore-1,' ')+"  |  *"+ajouterCaractere(8,' ')+"  |  *"+ajouterCaractere(lenJourneePasse-1,' ')+"  |  *         |");
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

    void echanger(String[] tab, int indice , int indice_min){
	String save = tab[indice];
	tab[indice] = tab[indice_min];
	tab[indice_min] = save;
    }

    int[] plusLong(Joueur[] tab){
	int[] max = new int[]{0,0,0};
	for (int i=0;i<length(tab);i+=1){
	    if (length(tab[i].pseudo)>max[0]){
		max[0] = length(tab[i].pseudo);
	    }
	    if (length(""+(tab[i].journeePasse))>max[1]){
		max[1] = length(""+(tab[i].journeePasse));
	    }
	    if (length(""+(tab[i].score))>max[2]){
		max[2] = length(""+(tab[i].score));
	    }
	}
	return max;
    }

    void afficherJoueur(Joueur j, int lenScore , int lenPseudo , int lenJourneePasse){
	print(j.pseudo+ajouterCaractere((lenPseudo-length(""+(j.pseudo))),' ')+"  |  "+j.score+ajouterCaractere((lenScore-length(""+(j.score))),' ')+"  |  "+j.difficulte.difficulte+ajouterCaractere(9-length(""+(j.difficulte.difficulte)),' ')+"  |  "+j.journeePasse+ajouterCaractere(lenJourneePasse-length(""+(j.journeePasse)),' ')+"  |  "+j.mode+ajouterCaractere(8-length(j.mode),' '));
    }

    String ajouterCaractere(int nbr,char caractere){
	String ajout = "";
	for (int n=0;n<nbr;n++){
	    ajout += caractere;
	}
	return ajout;
    }


    String verifInt(String entree){
	if (equals(entree,"")){
	    return "-1";
	}
	for(int indice=0;indice<length(entree);indice+=1){
	    if (!(charAt(entree,indice) <= '9' && charAt(entree,indice) >= '0')){
		return "-1";
	    }
	}
	return entree;
    }


    void afficherFichier(String fichierTXT , int indice_noms){
	File menu = newFile(fichierTXT);
	int indiceLigne = 0;
	text(couleur_texte);
	while (ready(menu)){
	    if (indiceLigne == indice_noms || indiceLigne == indice_noms+1){
		text(couleur_confirmation);
		println(readLine(menu));
		text(couleur_ecriture);
	    } else {
		println(readLine(menu));
	    }
	    indiceLigne += 1;
	}
    }


    void afficherFichier(String fichierTXT){
	File fichier = newFile(fichierTXT);
	text(couleur_texte);
	while (ready(fichier)){
		println(readLine(fichier));
	    }
	}

    void afficherRegles(){
	clearScreen();
	File fichier = newFile(REGLE);
	int[] ligneArret = new int[]{8,19,27,36,43,50,57,65,69};
	int indiceArret = 0;
	int indiceLigne = 0;
	text(couleur_texte);
	while(ready(fichier) && indiceLigne<=ligneArret[indiceArret] && indiceArret<=8){
	    println(readLine(fichier));
	    if (indiceLigne == ligneArret[indiceArret]-1 && indiceArret != 8){
		text(couleur_confirmation);
		print("\n\n       ENTRER POUR CONTINUER ");
		readString();
		clearScreen();
		fichier = newFile(REGLE);
		indiceLigne = 0;
		indiceArret += 1;
		text(couleur_texte);
	    }
	    indiceLigne += 1;
	}
	text(couleur_confirmation);
		print("\n\n       ENTRER POUR REVENIR AU MENU");
		text(couleur_ecriture);
		readString();
    }


    Joueur creerJoueur(String mode){
	File menuJoueur = newFile("CreerJoueur.txt");
	text(couleur_texte);
	while (ready(menuJoueur)){
	    println(readLine(menuJoueur));
	}
	Joueur j = newJoueur();
	j.mode = mode;
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
        chargerDifficulte(j,reponse);
	return j;
    }

    String[] afficherListeCouleur(String couleur){
	String[] listeCouleur = new String[]{"BLACK","WHITE","BLUE","RED","CYAN","YELLOW","PURPLE","GREEN"};
	int i=0 ,y;
	boolean trouver;
	while(i<5){
	    trouver = false;
	    y = i;
	    while (!trouver && y<length(listeCouleur)){
		if(equals(couleur,listeCouleur[y])){
		    echanger(listeCouleur,y,i);
		    trouver = true;
		} else if (i != 0 && (equals(couleur_texte,listeCouleur[y]) || equals(couleur_de_fond,listeCouleur[y]) || equals(couleur_erreur,listeCouleur[y]) || equals(couleur_confirmation,listeCouleur[y]) || equals(couleur_ecriture,listeCouleur[y]))){
		    echanger(listeCouleur,i,y);
		    trouver = true;
		} else {
		    y += 1;
		}
	    }
	    i += 1;
	}
	text(couleur_texte);
	print("       ____MODIFS_____________________________________\n\n");
	for (int j=0;j<length(listeCouleur);j+=1){
	    if (j == 0){
		print("       1 - "+listeCouleur[0]+" (ACTUEL)\n\n");
	    } else if (j < 5){
		print("       "+(j+1)+" - "+listeCouleur[j]+" (ECHANGER AVEC ");
		if (equals(couleur_texte,listeCouleur[j])){
		    print("COULEUR TEXTE)\n\n");
		} else if (equals(couleur_de_fond,listeCouleur[j])){
		    print("COULEUR DE FOND)\n\n");
		} else if (equals(couleur_erreur,listeCouleur[j])){
		    print("COULEUR D'ERREUR)\n\n");
		} else if (equals(couleur_confirmation,listeCouleur[j])){
		    print("COULEUR DE CONFIRMATION)\n\n");
		} else {
		    print("COULEUR D'ECRITURE)\n\n");
		}
	    } else {
		print("       "+(j+1)+" - "+listeCouleur[j]+"\n\n");
	    }
	}
	print("       9 - RETOUR AU MENU PRECEDENT\n\n\n");
	return listeCouleur;
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
		    chargerDifficulte(j,reponse);
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
		println(ajouterCaractere(20-(2+length((""+atteindreScore))),' ')+"|");
	    }
	    println(readLine(score));
	    indiceLigne += 1;
	}
	text(couleur_confirmation);
	print("       ENTRER POUR JOUER ");
	readString();
    }

    int calculerAtteindreScore(Joueur j){
	return 300;
    }

    int saisieEntree(int mini,int maxi,String nomFichier){
	int choix;
	boolean choixCorrect;
	clearScreen();
	afficherFichier(nomFichier);
	text(couleur_texte);
	print("       CHOIX : ");
	text(couleur_ecriture);
	do{
	    choixCorrect = true;
	    choix = stringToInt(verifInt(readString()));
	    if (choix > mini || choix < maxi){
		choixCorrect = false;
		clearScreen();
		afficherFichier(nomFichier);
		text(couleur_erreur);
	        println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
		text(couleur_texte);
		print("       CHOIX : ");
		text(couleur_ecriture);
	    }
	}while(!choixCorrect);
	return choix;
    }

    int saisieEntree(int maxi,int mini,String nomFichier,int colorNom){
	int choix;
	boolean choixCorrect;
	clearScreen();
	afficherFichier(nomFichier,colorNom);
	text(couleur_texte);
	print("       CHOIX : ");
	text(couleur_ecriture);
	do{
	    choixCorrect = true;
	    choix = stringToInt(verifInt(readString()));
	    text(couleur_texte);
	    if (choix > maxi || choix < mini){
		choixCorrect = false;
		clearScreen();
		afficherFichier(nomFichier,colorNom);
		text(couleur_erreur);
	        println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
		text(couleur_texte);
		print("       CHOIX : ");
		text(couleur_ecriture);
	    }
	}while(!choixCorrect);
	return choix;
    }

    void saissieEntreeCouleur(String couleurActuelle , int choixChangementCouleur){
	int choix;
	boolean choixCorrect;
	String[] liste;
	liste = afficherListeCouleur(couleurActuelle);
	do{
	    text(couleur_texte);
	    print("       CHOIX : ");
	    text(couleur_ecriture);
	    choixCorrect = true;
	    choix = stringToInt(verifInt(readString()));
	    if (choix > 9 || choix < 1){
		choixCorrect = false;
		clearScreen();
		liste = afficherListeCouleur(couleurActuelle);
		text(couleur_erreur);
		println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
	    }
	}while(!choixCorrect);
	if (choix != 9){
	    assigneCouleur(choix,liste,choixChangementCouleur);
	}
    }

    void assigneCouleur(int choix, String[] liste , int choixChangementCouleur){
	if (choix<6){
	    if (equals(couleur_texte,liste[choix-1])){
		couleur_texte = liste[0];
	    } else if (equals(couleur_de_fond,liste[choix-1])){
		couleur_de_fond = liste[0];
	    } else if (equals(couleur_erreur,liste[choix-1])){
		couleur_erreur = liste[0];
	    } else if (equals(couleur_confirmation,liste[choix-1])){
		couleur_confirmation = liste[0];
	    } else {
		couleur_ecriture = liste[0];
	    }
	}
	if (choixChangementCouleur == 1){
	    couleur_texte = liste[choix-1];
	} else if(choixChangementCouleur == 2){
	    couleur_de_fond = liste[choix-1];
	} else if(choixChangementCouleur == 3){
	    couleur_erreur = liste[choix-1];
	} else if(choixChangementCouleur == 4){
	    couleur_confirmation = liste[choix-1];
	} else {
	    couleur_ecriture = liste[choix-1];
	}
    }

    void afficherClassement(String niveau , String nomFichier,String mode){
	clearScreen();
	afficherFichier(nomFichier);
	println();
	afficherClassementScore(niveau,mode);
	text(couleur_confirmation);
	print("\n\n       ENTRER POUR REVENIR AU MENU");
	text(couleur_de_fond);
	readString();
    }

    void afficherFichierRetour(String nomFichier){
	clearScreen();
	afficherFichier(nomFichier);
	println();
	text(couleur_confirmation);
	print("\n\n       ENTRER POUR REVENIR AU MENU");
	text(couleur_de_fond);
	readString();
    }

    boolean changerCouleur(){
	int choix;
	background(couleur_de_fond);
	choix = saisieEntree(6,1,MENU_COULEUR);
	if (choix ==6){
	    return true;
	} else {
	    clearScreen();
	    int choixChangementCouleur = choix;
	    String couleurActuelle;
	    if (choix == 1){
		couleurActuelle = couleur_texte;
	    } else if (choix == 2){
		couleurActuelle = couleur_de_fond;
	    } else if (choix == 3){
		couleurActuelle = couleur_erreur;
	    } else if (choix == 4){
		couleurActuelle = couleur_confirmation;
	    } else {
		couleurActuelle = couleur_ecriture;
	    }
	    saissieEntreeCouleur(couleurActuelle , choixChangementCouleur);
	    return false;
	}
    }

    void jouerCampagne(){
        boolean reussie , save = false;
	Joueur j = creerJoueur("CAMPAGNE");
	debutHistoire(j);
	reussie = lancerPartie(j);
	if (reussie){
	    save = sauvegarder(j);
	}
        if (j.journeePasse == 2 && !save){
	    //jour_2_Histoire();
	    reussie = lancerPartie(j);
	    if (reussie){
		save = sauvegarder(j);
	    }
	}
	if (j.journeePasse == 3 && !save){
	    //jour_3_Histoire();
	    reussie = lancerPartie(j);
	    if (reussie){
		save = sauvegarder(j);
	    }
	}
	if (j.journeePasse == 4 && !save){
	    //finHistoire();
	    reussie = lancerPartie(j);
	}
	if (!save){
	    enregistrerJoueur(j);
	    afficherClassement(j.difficulte.difficulte,CLASSEMENT_CAMPAGNE,j.mode);
	}
    }

    void jouerInfini(){
        boolean reussie , save = false;
	Joueur j = creerJoueur("INFINI");
	do{
	    reussie = lancerPartie(j);
	    if (reussie){
		save = sauvegarder(j);
	    }
	}while(reussie && !save);
	if(!save){
	    enregistrerJoueur(j);
	    afficherClassement(j.difficulte.difficulte,CLASSEMENT_INFINI,"INFINI");
	}
    }

    void menuClassement(){
	int choix;
	boolean finClassement = false;
	do{
	    choix = saisieEntree(4,1,MENU_CLASSEMENT);
	    if (choix == 1){
		afficherClassement("*",CLASSEMENT_GENERALE,"*");	
	    } else if (choix == 2){
		choix = saisieEntree(4,1,MENU_CLASSEMENT_CAMPAGNE);
		if (choix == 1){
		    afficherClassement("difficile",CLASSEMENT_CAMPAGNE,"CAMPAGNE");
		} else if (choix == 2) {
		    afficherClassement("moyen",CLASSEMENT_CAMPAGNE,"CAMPAGNE");
		} else if (choix == 3){
		    afficherClassement("facile",CLASSEMENT_CAMPAGNE,"CAMPAGNE");
		}
	    } else if (choix == 3){
		choix = saisieEntree(4,1,MENU_CLASSEMENT_INFINI);
		if (choix == 1){
		    afficherClassement("difficile",CLASSEMENT_INFINI,"INFINI");
		} else if (choix == 2) {
		    afficherClassement("moyen",CLASSEMENT_INFINI,"INFINI");
		} else if (choix == 3){
		    afficherClassement("facile",CLASSEMENT_INFINI,"INFINI");
		}
	    } else {
		finClassement = true;
	    }
	}while(!finClassement);
    }

    void menuParametres(){
	int choix;
	boolean finParametre = false;
	do{
	    choix = saisieEntree(4,1,MENU_PARAMETRE);
	    if (choix == 1){
		choix = saisieEntree(3,1,MENU_CONSEILS);
		if (choix == 1){
		    afficherFichierRetour(TABLES);
		} else if (choix == 2){
		    afficherFichierRetour(CONSEILS);
		}
	    } else if (choix == 2){
		afficherRegles();
	    } else if (choix == 3){
		boolean finCouleur = false;
		do{
		    finCouleur = changerCouleur();
		}while(!finCouleur); 
	    } else {
		finParametre = true;
	    }
	}while(!finParametre);
    }

    void menuJeu(){
	int choix = saisieEntree(3,1,MENU_MODE,25);
	if (choix == 1){
	    jouerCampagne();
	} else if (choix == 2){
	    jouerInfini();
	}
    }

    String afficherLigneDonnees(CSVFile fichier,int nbr){
	String ligne = getCell(fichier,nbr,0)+" | "+getCell(fichier,nbr,1)+" | "+getCell(fichier,nbr,2)+" | "+getCell(fichier,nbr,3)+" | "+getCell(fichier,nbr,4)+"\n";
	return ligne;
    }

    int afficherDonnees(CSVFile fichier){
	clearScreen();
	int lenFichier = rowCount(fichier);
	String donnees = "";
	for (int nbr=0;nbr<lenFichier;nbr+=1){
	    donnees += "       "+(nbr+1)+" - "+afficherLigneDonnees(fichier,nbr)+"\n";
	}
	donnees += ("       4 - RETOUR AU MENU PRECEDENT\n\n       CHOIX : ");
	print(donnees);
	int choix;
	boolean choixCorrect;
	text(couleur_ecriture);
	do{
	    choixCorrect = true;
	    choix = stringToInt(verifInt(readString()));
	    if (choix > 4 || choix < 1){
		choixCorrect = false;
		clearScreen();
		text(couleur_texte);
	        print(donnees);
		text(couleur_erreur);
		println("\n       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
		text(couleur_texte);
		print("       CHOIX : ");
		text(couleur_ecriture);
	    }
	}while(!choixCorrect);
	return choix;
	    
    }

    Joueur chargerJoueur(Joueur j , CSVFile fichier , int indice){
	j.pseudo = getCell(fichier,indice,0);
	j.score = stringToInt(getCell(fichier,indice,1));
	chargerDifficulte(j,getCell(fichier,indice,2));
	j.journeePasse = stringToInt(getCell(fichier,indice,3));
	j.mode = getCell(fichier,indice,4);
	return j;
    }

    boolean sauvegarder(Joueur j){
	if (1 == saisieEntree(2,1,SAVE_OR_CONTINUE)){
	    CSVFile fichier = loadCSV(SAUVEGARDE);
	    if (rowCount(fichier) < 3){
		enregistrer(j,SAUVEGARDE);
		return true;
	    } else {
		int choix = saisieEntree(4,1,SUPPRIMER_SAVE);
		if (choix < 4){
		    if (choix == 1){
			enregistrer(j,2,3,SAUVEGARDE);
		    } else if (choix == 2){
			enregistrer(j,1,3,SAUVEGARDE);
		    } else {
			enregistrer(j,1,2,SAUVEGARDE);
		    }
		}
	    }
	}
	return false;
    }

    void menuReprendreJeu(){
	CSVFile fichier = loadCSV(SAUVEGARDE);
	int choix = afficherDonnees(fichier)-1;
	boolean reussie , save = false;
	if (choix != 3){
	    Joueur j = newJoueur();
	    chargerJoueur(j,fichier,choix);
	    if (equals(getCell(fichier,choix,4),j.mode)){
		if (j.journeePasse == 2 && !save){
		    //jour_2_Histoire();
		    reussie = lancerPartie(j);
		    if (reussie){
			save = sauvegarder(j);
		    }
		}
		if (j.journeePasse == 3 && !save){
		    //jour_3_Histoire();
		    reussie = lancerPartie(j);
		    if (reussie){
			save = sauvegarder(j);
		    }
		}
		if (j.journeePasse == 4 && !save){
		    //finHistoire();
		    reussie = lancerPartie(j);
		}
		if (!save){
		    enregistrerJoueur(j);
		    afficherClassement(j.difficulte.difficulte,CLASSEMENT_CAMPAGNE,j.mode);
		}
	    } else {
		do{
		    reussie = lancerPartie(j);
		    if (reussie){
			save = sauvegarder(j);
		    }
		}while(reussie && !save);
		if (!save){
		    enregistrerJoueur(j);
		    afficherClassement(j.difficulte.difficulte,CLASSEMENT_INFINI,j.mode);
		}
	    }
	}
    }


    Multiplication creerMultiplication() 
	// pour créer une multiplication.
    {
        Multiplication multi = new Multiplication();
        multi.a = 1 + (int) (random() * 10);
        multi.b = 1 + (int) (random() * 10);
        return multi;
    }

    Multiplication[] creerMultiMultiplication(Difficulte d) 
	// pour créer des multiplications complexes. J'ai limité à 3 multiplications en même temps sinon trop compliqué.
    {
        int nbMulti;
        if(equals(d.difficulte,"facile"))
        {
            nbMulti = 1;
        }
        else
        {
            nbMulti = 1 + (int)(random() * 2);
        }
        Multiplication[] multi = new Multiplication[nbMulti];
        for(int i=0;i<length(multi);i++)
        {
            multi[i] = creerMultiplication();
        }
        return multi;
    }

    int resultatMulti(Multiplication multi)
	// resultat des multiplications 
    {
        return multi.a * multi.b;
    }

    int resultatMulti(Multiplication[] multi)
	// resultat des multiples multiplications 
    {
        int res = 0;
        if(length(multi) == 1)
        {
            res = resultatMulti(multi[0]);
        }
        else
        {
            for(int i=0;i<length(multi);i++)
            {
                res += resultatMulti(multi[i]);
            }
        }
        return res;
    }

    String toString(Multiplication multi)
	// affichage de la multiplication
    {
        return multi.a + "*" + multi.b;
    }

    String toString(Multiplication[] multi)
	// affichage de la multi multiplication (Ex : (5*2) + (6*7))
    {
        String res = "";
        if(length(multi) == 1)
        {
            res = toString(multi[0]);
        }
        else
        {
            for(int i=0;i<length(multi)-1;i++)
            {
                res += "(" + toString(multi[i]) + ") + ";
            }
            res += "(" + toString(multi[length(multi)-1]) + ")";
        }
        return res;
    }

    void afficherTableMulti()
	// affcihe les tables de multiplications
    {
        File file = newFile("Table_Multiplication.txt");
        while(ready(file))
        {
            println(readLine(file));
        }
    }

    void chargerDifficulte(Joueur j, String  niveau)
	// permet d'initialiser la difficulte en fonction de se que le joueur choisi
    {
        j.difficulte.difficulte = niveau;
        if(equals(niveau,"facile"))
        {
           j.difficulte.nbClientsMax = 15;
            j.difficulte.multiplicateurScore = 1.25;
            j.difficulte.intervalle = 15.25 - (0.25*j.journeePasse);
        }
        else if(equals(niveau,"moyen"))
        {
            j.difficulte.nbClientsMax = 12;
            j.difficulte.multiplicateurScore = 1.0;
            j.difficulte.intervalle = 15.25 - (0.25*j.journeePasse);
        }
        else if(equals(niveau, "difficile"))
		{
            j.difficulte.nbClientsMax = 8;
            j.difficulte.multiplicateurScore = 0.75;
            j.difficulte.intervalle = 15.25 - (0.25*j.journeePasse);
        }
    }

    int ajoutClient(double duree,int nbClient,Difficulte d)
    {
        if(nbClient > 0)
        {
            if(duree > d.intervalle)
            {
                return (int)(random()*3);
            }
            else if(nbClient - 1 > 0)
            {
                return -1;
            }
        }
        return 1;
        
    }

    void afficheQueue(int nbClient,Multiplication[] multi, Difficulte d)
    {
        File fich = newFile("personnage.txt");
        File etabli = newFile("etabli.txt");
		File bonbon = newFile(BONBONS);
        String affiche = "";
        String body = "";
		String partBonbon = "";
        String partEtabli = "";
		String phrase = afficheCommandeClient(multi, d);
		for(int i=0;i<11;i++)
		{
			if(i == 6)
			{
				affiche += ajouterCaractere(93-length(phrase),' ')+ajouterCaractere(length(phrase)+6, '_') + ajouterCaractere(16, ' ') + readLine(bonbon) + "\n";
			}
			else if (i == 7)
			{
				affiche += ajouterCaractere(92-length(phrase), ' ') + "|" + ajouterCaractere(length(phrase)+6, ' ')+"|" + ajouterCaractere(15, ' ') + readLine(bonbon) + "\n";
			}
			else if(i == 8)
			{
				affiche += ajouterCaractere(92-length(phrase), ' ') + "|   " + phrase + "   |" + ajouterCaractere(15, ' ') + readLine(bonbon) + "\n";
			}
			else if (i == 9)
			{
				affiche += ajouterCaractere(92-length(phrase), ' ') + "|" + ajouterCaractere(length(phrase)-16, '_') + "  "+ ajouterCaractere(20, '_') + "|" + ajouterCaractere(15, ' ') + readLine(bonbon) + "\n";
			}
			else if(i == 10)
			{
				affiche += ajouterCaractere(77,' ')+ "\\/" + ajouterCaractere(36, ' ') + readLine(bonbon) + "\n";
			}
			else
			{
				partBonbon = readLine(bonbon);
				affiche += ajouterCaractere(115,' ') + partBonbon +"\n";
			}
		}
        for(int j=0;j<5;j++)

	    {
            body = readLine(fich);
            partEtabli = readLine(etabli);
			partBonbon = readLine(bonbon);
            if(j == 4)
            {
                affiche += "      " + ajouterCaractere(70-(nbClient*5),'_');
            }
            else
            {
                affiche += ajouterCaractere(76-(nbClient*5),' ');
            }
            if(nbClient == 1)
            {
                if(j == 4)
                {
                    affiche += "______" + body + partEtabli + body + ajouterCaractere(22,' ') + partBonbon;
                }
                else
                {
                    affiche +=  "      " + body + partEtabli + body  + ajouterCaractere(22,' ') + partBonbon;
                }                
            }
            else
            {
                for(int i=0;i<nbClient-1;i++)
                {
                    affiche += body;
                    if(i == nbClient - 2 && j == 4)
                    {
                        affiche += "______" + body + partEtabli + body + ajouterCaractere(22,' ') + partBonbon;
                    }
                    else if(i == nbClient - 2)
                    {
                        affiche +=  "      " + body + partEtabli + body + ajouterCaractere(22,' ') + partBonbon;
                    }
                }
            }
            
            
            affiche += "\n";
        }

        
        println(affiche);
    }

    void afficherJourneeTermine()
    {
        File fich = newFile("gagnant.txt");
        File etabli = newFile("etabli.txt");
        String affiche = "";
        String body = "";
        String partEtabli = "";
        for(int j=0;j<5;j++)
        {
            body = readLine(fich);
            partEtabli = readLine(etabli);
            if(j == 4)
            {
                affiche += ajouterCaractere(76, '_') + partEtabli + body;
            }
            else
            {
                affiche +=  ajouterCaractere(76, ' ') + partEtabli + body;      
            }      
            affiche += "\n";
        }

        
        println(affiche);
		delay(2000);
    }

    String afficheMultiplication(Multiplication[] multi,Difficulte d)
    {
		String res = "";
		print("      Votre résultat : ");
		res = readString();
        
        return res;
    }

    boolean jourSuivant(int score, Joueur j)
    {
        boolean suivant = false;
        if(score == j.score || score*0.25 <= j.score)
        {
            j.journeePasse += 1;
            suivant = true;
            afficherJourneeTermine();
        }
        return suivant;
    }

    int creerNbClientJour(Difficulte d)
    {
        return (int)(d.nbClientsMax + random() * 25);
    }

	String afficheCommandeClient(Multiplication[] multi, Difficulte d)
	{
		String phrase = "Bonjour, je voudrais " + multi[0].a + " " + LISTEBONBONS[multi[0].b-1];
		if(length(multi) == 2)
		{
			phrase += " et " + multi[1].a + " " + LISTEBONBONS[multi[1].b-1];
		}
		phrase += " SVP.";
		if(equals(d.difficulte,"facile") || equals(d.difficulte,"moyen"))
		{
			phrase += "(" + toString(multi) + ")";
		}
		/*println(ajouterCaractere(93-length(phrase),' ')+ajouterCaractere(length(phrase)+6, '_'));
		println(ajouterCaractere(92-length(phrase), ' ') + "|" + ajouterCaractere(length(phrase)+6, ' ')+"|");
		println(ajouterCaractere(92-length(phrase), ' ') + "|   " + phrase + "   |");
		println(ajouterCaractere(92-length(phrase), ' ') + "|" + ajouterCaractere(length(phrase)-17, '_')+"  "+ajouterCaractere(21, '_')+"|");
		println(ajouterCaractere(76,' ')+ "\\/");*/
		return phrase;
	}

    boolean lancerPartie(Joueur j)
    {
        Multiplication[] multi = creerMultiMultiplication(j.difficulte);
        int nbClient = 2+(int)(random()*j.difficulte.nbClientsMax/3);
        int nbClientJour = creerNbClientJour(j.difficulte);
        int scoreAtteindre = j.score + nbClientJour*(int)(100*j.difficulte.multiplicateurScore);
        double debut;
        double fin;
        double duree;
        String res;
		afficherAtteindreScore(scoreAtteindre);
		text(couleur_texte);
        while(nbClient < j.difficulte.nbClientsMax && nbClientJour > 0)
        {
            clearScreen();
            println("Nombre de client aujourd'hui : " + nbClientJour);
            println("Votre score : " + j.score);
            println("Score à atteindre : "+scoreAtteindre);
			println("nombre de jour : " + j.journeePasse);
            println(nbClient);
			println("(tapez 'help' pour obtenir les tables de multiplications)");
            afficheQueue(nbClient,multi,j.difficulte);
            debut = getTime();
            res = afficheMultiplication(multi,j.difficulte);
            fin = getTime();
            duree = fin - debut;
            if(equals(res,"help"))
            {
                afficherTableMulti();
                afficheQueue(nbClient,multi,j.difficulte);
                debut = getTime();
                res = afficheMultiplication(multi,j.difficulte);
                fin = getTime();
                duree = (fin - debut) - 5000;
            }
            if(stringToInt(verifInt(res)) == resultatMulti(multi))
            {
                j.score += 100 * j.difficulte.multiplicateurScore;
				text(couleur_confirmation);
				println("bien joué\n");
				text(couleur_texte);
                delay(1000);
                nbClient += ajoutClient((duree)/1000,nbClient,j.difficulte);
            }
            else
            {
                j.score -= 50 / j.difficulte.multiplicateurScore;
				text(couleur_erreur);
				println("perdu");
				text(couleur_texte);
                delay(1000);
                nbClient += ajoutClient(j.difficulte.intervalle + 5,nbClient,j.difficulte);
            }
            nbClientJour -= 1;
            multi = creerMultiMultiplication(j.difficulte);
            
        }
	if (jourSuivant(scoreAtteindre, j)){
	    return true;
	}
            
	return false ;
    }


    void algorithm(){
	initialisationCouleur();
	background(couleur_de_fond);
	clearScreen();
	boolean finDeJeu = false;
	int choix;
	do{
	    choix = saisieEntree(5,1,MENU,25);
	    if (choix == 1){
		menuJeu();
	    } else if (choix == 2){
		menuReprendreJeu();
	    } else if (choix == 3){
		menuClassement();
	    } else if (choix == 4){
		menuParametres();
	    } else {
		finDeJeu = true;
	    }
	}while(!finDeJeu);
	clearScreen();
    }
}

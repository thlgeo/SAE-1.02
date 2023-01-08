import extensions.CSVFile;
import extensions.File;

class CandyShop extends Program{


    /// VARIABLES GLOBALES /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    String couleur_texte;
    String couleur_de_fond;
    String couleur_erreur;
    String couleur_confirmation;
    String couleur_ecriture;
	boolean saveMilieuPartie = false;

    final String MENU ="../ressources/Menu.txt";
    final String MENU_MODE = "../ressources/MenuMode.txt";
    final String MENU_PARAMETRE = "../ressources/MenuParametres.txt";
    final String MENU_COULEUR = "../ressources/Couleur.txt";
    final String REGLE = "../ressources/Regles.txt";
    final String TABLES = "../ressources/Table_Multiplication.txt";
    final String CONSEILS = "../ressources/Conseils.txt";
    final String MENU_CONSEILS = "../ressources/MenuConseils.txt";
    final String MENU_CLASSEMENT = "../ressources/MenuClassement.txt";
    final String MENU_CLASSEMENT_CAMPAGNE = "../ressources/MenuClassementCampagne.txt";
    final String MENU_CLASSEMENT_INFINI = "../ressources/MenuClassementInfini.txt";
    final String CLASSEMENT_GENERALE = "../ressources/ClassementGénérale.txt";
    final String CLASSEMENT_CAMPAGNE = "../ressources/ClassementCampagne.txt";
    final String CLASSEMENT_INFINI = "../ressources/ClassementInfini.txt";
    final String MENU_REPRENDRE = "../ressources/MenuReprendre.txt";
    final String SAUVEGARDE = "../ressources/Sauvegarde.csv";
    final String CLASSEMENT = "../ressources/Classement.csv";
    final String SAVE_OR_CONTINUE = "../ressources/save_or_continue.txt";
    final String SUPPRIMER_SAVE = "../ressources/Supprime.txt";
    final String CREE_JOUEUR = "../ressources/CreerJoueur.txt";
    final String AFFICHER_SCORE = "../ressources/afficherScore.txt";
    final String PERSONNE = "../ressources/personnage.txt";
    final String ETABLI = "../ressources/etabli.txt";
    final String GAGNANT = "../ressources/gagnant.txt";
	final String PERDANT = "../ressources/perdu.txt";
    final String BONBONS = "../ressources/Bonbons.txt";
	final String REPRENDRE = "../ressources/MenuReprendre.txt";
	final String CONFIRMATION_DIFFICULTE = "../ressources/changementDifficulté.txt";
	final String SAVE_MILIEU_PARTIE = "../ressources/SaveMilieuPartie.csv";
	final String TABLE_MULTI = "../ressources/Tables.csv";
	final String CHOIXMENU = "../ressources/choixMenu.txt";
	final String[] LISTEBONBONS = new String[]{"Dragibus","Schtroumpf","Langue de chat","Crocodile","Moam","Carambar","Malabar","Tête brulée","Ours en gélatine","Réglisse"};

    
    /// CRÉATION DES TYPES /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Pour créer un joueur
    Joueur newJoueur(){
		Joueur j = new Joueur();
		j.pseudo = "";
		j.score = 0;
		j.difficulte = newDifficulte();
		j.journeePasse = 1;
		j.mode = "";
		return j;
    }

    // Pour créer une difficulter
    Difficulte newDifficulte(){
		Difficulte d = new Difficulte();
		d.difficulte = "";
		d.nbClientsMax = 0;
		d.multiplicateurScore = 0.0;
		d.intervalle = 0.0;
		return d;
    }

    // pour créer une multiplication.
    Multiplication newMultiplication() 
    {
		CSVFile fich = loadCSV(TABLE_MULTI);
        Multiplication multi = new Multiplication();
        multi.a = 1 + (int) (random() * stringToInt(getCell(fich,0,0)));
        multi.b = 1 + (int) (random() * stringToInt(getCell(fich,0,1)));
        return multi;
    }

    
    /// INITIALISATION DES TYPES ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Créer et initialise un joueur
    Joueur creerJoueur(String mode){
		String chaine = afficherFichier(CREE_JOUEUR);
		Joueur j = newJoueur();
		j.mode = mode;
		String reponse;
		boolean reponseCorrect;
		print(chaine);
		do{
			print("\n            PSEUDO : ");
			text(couleur_ecriture);
			reponseCorrect = true;
			reponse = readString();
			if (length(reponse) < 3 || length(reponse) > 16){
			reponseCorrect = false;
			text(couleur_texte);
			print(chaine);
			text(couleur_erreur);
			println("            ERREUR DE SAISIE ! VEUILLEZ ENTREZ UN PSEUDO ENTRE 3 ET 16 CARACTERES");
			}
			text(couleur_texte);
		}while(!reponseCorrect);
		j.pseudo = reponse;
		do{
			print("            DIFFICULTER (facile / moyen / difficile) : ");
			text(couleur_ecriture);
			reponseCorrect = true;
			reponse = toLowerCase(readString());
			if (!equals(reponse,"facile") && !equals(reponse,"moyen") && !equals(reponse,"difficile")){
			reponseCorrect = false;
			text(couleur_texte);
			print(chaine);
			text(couleur_erreur);
			println("            ERREUR DE SAISIE ! VEUILLEZ ENTREZ \"facile\" ou \"moyen\" ou \"difficile\"");
			}
			text(couleur_texte);
		}while(!reponseCorrect);
		chargerDifficulte(j,reponse);
		return j;
    }
    
    // Pour créer des multiplications complexes. J'ai limité à 3 multiplications en même temps sinon trop compliqué.
    Multiplication[] creerMultiMultiplication(Difficulte d) 
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
		multi[i] = newMultiplication();
	    }
        return multi;
    }

    // Permet d'initialiser la difficulte en fonction de la difficulter choisi par le joueur
    void chargerDifficulte(Joueur j, String  niveau)
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

    // Permet d'initialiser un joueur enregistrer dans un fichier CSV
    Joueur chargerJoueur(Joueur j , CSVFile fichier , int indice){
	j.pseudo = getCell(fichier,indice,0);
	j.score = stringToInt(getCell(fichier,indice,1));
	chargerDifficulte(j,getCell(fichier,indice,2));
	j.journeePasse = stringToInt(getCell(fichier,indice,3));
	j.mode = getCell(fichier,indice,4);
	return j;
    }

	// Renvoi une chaine de caractères contenant les données d'un joueur avec un espacement particulier
    String toString(Joueur j, int lenScore , int lenPseudo , int lenJourneePasse){
		return j.pseudo+ajouterCaractere((lenPseudo-length(""+(j.pseudo))),' ')+"  |  "+j.score+ajouterCaractere((lenScore-length(""+(j.score))),' ')+"  |  "+j.difficulte.difficulte+ajouterCaractere(9-length(""+(j.difficulte.difficulte)),' ')+"  |  "+(j.journeePasse-1)+ajouterCaractere(lenJourneePasse-length(""+j.journeePasse),' ')+"  |  "+j.mode+ajouterCaractere(8-length(j.mode),' ');
		}

	// Renvoi une chaine de caractère de la multi multiplication (Ex : (5*2) + (6*7))
    String toString(Multiplication[] multi)
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

	// renvoi une chaine de caractère de la multiplication
    String toString(Multiplication multi)
    {
        return multi.a + "*" + multi.b;
    }


    /// AFFICHAGE //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Affiche un fichier .txt
    String afficherFichier(String nomFichier){
	File fichier = newFile(nomFichier);
	String chaine = "";
	while (ready(fichier)){
	    chaine += readLine(fichier)+"\n";
	}
	return chaine;
    }

	void perdu(Joueur j){
		enregistrer(j,CLASSEMENT);
		clearScreen();
		text(couleur_erreur);
		print(afficherFichier(PERDANT));
		text(couleur_texte);
		delay(3000);
		if (equals(j.mode,"CAMPAGNE")){
	    	afficherClassement(j.difficulte.difficulte,CLASSEMENT_CAMPAGNE,j.mode);
		} else {
			afficherClassement(j.difficulte.difficulte,CLASSEMENT_INFINI,j.mode);
		}
	}

    // Affiche un tableau contenent les 10 meilleurs joueur en fonction de leur score , et en fontion d'un mode de jeu et d'un niveau
    void afficherClassementTableau(String niveau , String mode){
	CSVFile fichierClassement = loadCSV(CLASSEMENT);
	Joueur[] js = new Joueur[compteElements(fichierClassement,rowCount(fichierClassement),niveau,mode)];
	if (niveau == "*"){
	    readCSVFile(js,fichierClassement);
	} else {
	    readCSVFile(js,niveau,mode,fichierClassement);
	}
	triTableau(js);
	println(stringClassement(js));
    }

    // Affiche le classement en fonction d'un niveau et d'un mode de jeu
    void afficherClassement(String niveau , String nomFichier,String mode){
	clearScreen();
	println(afficherFichier(nomFichier));
	afficherClassementTableau(niveau,mode);
	text(couleur_confirmation);
	print("\n\n       ENTRER POUR REVENIR AU MENU");
	text(couleur_de_fond);
	readString();
	text(couleur_texte);
    }

    // Affiche la page des règles
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
		text(couleur_de_fond);
		readString();
		text(couleur_texte);
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
	text(couleur_texte);
    }

    // Affiche le  le menu permettent de changer de couleur
    String[] afficherListeCouleur(String couleur){
	String[] listeCouleur = new String[]{"BLACK","WHITE","BLUE","RED","CYAN","YELLOW","PURPLE","GREEN"};
	triCouleur(listeCouleur , couleur);
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

    // Affiche le score à atteindre par le joueur pour la partie prochaine partie qu'il va jouer
    void afficherAtteindreScore(int atteindreScore){
	File score = newFile(AFFICHER_SCORE);
	int indiceLigne = 0;
	clearScreen();
	text(couleur_texte);
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
	text(couleur_de_fond);
	readString();
	text(couleur_texte);
    }

    // Affiche un fichier et attend une entrée quelconque pour continuer
    void afficherFichierRetour(String nomFichier){
	clearScreen();
	print(afficherFichier(nomFichier));
	text(couleur_confirmation);
	print("\n\n\n       ENTRER POUR REVENIR AU MENU");
	text(couleur_de_fond);
	readString();
	text(couleur_texte);
    }

    // Renvoi une chaine de caractère contenent les informations des joueur dans un fichier CSV à afficher
    String afficherLigneDonnees(CSVFile fichier,int nbr){
	String ligne = getCell(fichier,nbr,0)+" | "+getCell(fichier,nbr,1)+" | "+getCell(fichier,nbr,2)+" | "+(stringToInt(getCell(fichier,nbr,3))-1)+" | "+getCell(fichier,nbr,4)+"\n";
	return ligne;
    }

    // Affiche les donnees d'un fichier CSV
    void afficherDonnees(CSVFile fichier){
	int lenFichier = rowCount(fichier);
	String donnees = "";
	for (int nbr=0;nbr<lenFichier;nbr+=1){
	    donnees += "       "+(nbr+1)+" - "+afficherLigneDonnees(fichier,nbr)+"\n";
	}
	donnees += ("       "+(lenFichier+1)+" - RETOUR AU MENU PRECEDENT\n\n");
	print(donnees);
    }

	// Affiche l'histoire 
    void afficherHistoire(int jour){
		File histoire = newFile("../ressources/Histoire"+jour+".txt");
		int indiceLigne = 0;
		int i = 1;
		while (ready(histoire)){
			text(couleur_texte);
			clearScreen();
			while (ready(histoire) && indiceLigne < 28*i){
			println(readLine(histoire));
			indiceLigne += 1;
			}
			i += 1;
			text(couleur_confirmation);
			print("       ENTRER POUR CONTINUER ");
			text(couleur_de_fond);
			readString();
			text(couleur_texte);
		}
		}

    // Affiche la file d'attente dans la partie
    void afficheQueue(int nbClient,Multiplication[] multi, Difficulte d)
    {
        File fich = newFile(PERSONNE);
        File etabli = newFile(ETABLI);
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
			affiche += ajouterCaractere(93-length(phrase),' ')+ajouterCaractere(length(phrase)+6, '_') + ajouterCaractere(41, ' ') + readLine(bonbon) + "\n";
		    }
		else if (i == 7)
		    {
			affiche += ajouterCaractere(92-length(phrase), ' ') + "|" + ajouterCaractere(length(phrase)+6, ' ')+"|" + ajouterCaractere(40, ' ') + readLine(bonbon) + "\n";
		    }
		else if(i == 8)
		    {
			affiche += ajouterCaractere(92-length(phrase), ' ') + "|   " + phrase + "   |" + ajouterCaractere(40, ' ') + readLine(bonbon) + "\n";
		    }
		else if (i == 9)
		    {
			affiche += ajouterCaractere(92-length(phrase), ' ') + "|" + ajouterCaractere(length(phrase)-16, '_') + "  "+ ajouterCaractere(20, '_') + "|" + ajouterCaractere(40, ' ') + readLine(bonbon) + "\n";
		    }
		else if(i == 10)
		    {
			affiche += ajouterCaractere(77,' ')+ "\\/" + ajouterCaractere(61, ' ') + readLine(bonbon) + "\n";
		    }
		else
		    {
			partBonbon = readLine(bonbon);
			affiche += ajouterCaractere(140,' ') + partBonbon +"\n";
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
				affiche += "______" + body + partEtabli + body + ajouterCaractere(47,' ') + partBonbon;
			    }
			else
			    {
				affiche +=  "      " + body + partEtabli + body  + ajouterCaractere(47,' ') + partBonbon;
			    }                
		    }
		else
		    {
			for(int i=0;i<nbClient-1;i++)
			    {
				affiche += body;
				if(i == nbClient - 2 && j == 4)
				    {
					affiche += "______" + body + partEtabli + body + ajouterCaractere(47,' ') + partBonbon;
				    }
				else if(i == nbClient - 2)
				    {
					affiche +=  "      " + body + partEtabli + body + ajouterCaractere(47,' ') + partBonbon;
				    }
			    }
		    }
            
            
		affiche += "\n";
	    }

        
        println(affiche);
    }

    // Affiche le fichier de lorsque l'on gagne une partie
    void afficherJourneeTermine()
    {
        File fich = newFile(GAGNANT);
        File etabli = newFile(ETABLI);
        String affiche = "";
        String body = "";
        String partEtabli = "";
	clearScreen();
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
	delay(500);
    }

    // Affiche la commande d'un client
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
	return phrase;
    }

    // Affiche la demande de resultat
    String afficheMultiplication(Multiplication[] multi,Difficulte d)
    {
	String res = "";
	print("      Votre résultat : ");
	text(couleur_ecriture);
	res = readString();
	text(couleur_texte);
        
        return res;
    }

    // Affiche un message d'erreur de saisie
    void afficherErreur(String chaine){
	clearScreen();
	text(couleur_texte);
	print(chaine);
	text(couleur_erreur);
	print("\n       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
	text(couleur_texte);
	print("       CHOIX : ");
	text(couleur_ecriture);
    }


    /// LECTURE FICHIER ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Lis un fichier CSV et rempli un tableau de joueur
    void readCSVFile(Joueur[] js , CSVFile fichier){
	int nbrLignes = rowCount(fichier);
	for(int l=0 ; l<nbrLignes; l+=1){
	    Joueur j = newJoueur();
	    j.pseudo = getCell(fichier,l,0);
	    j.score = stringToInt(getCell(fichier,l,1));
	    j.difficulte.difficulte = getCell(fichier,l,2);
	    j.journeePasse = stringToInt(getCell(fichier,l,3));
	    j.mode = getCell(fichier,l,4);
	    js[l] = j;
	}
    }

    // Lis un fichier CSV et rempli un tableau de joueur en fonction de leur niveau et mode de jeu
    void readCSVFile(Joueur[] js, String niveau , String mode , CSVFile fichier){
	int nbrLignes = rowCount(fichier);
	int indice = 0 , indice_tab = 0;
	int ligneClassement = length(js);
	while( indice<nbrLignes && indice_tab<ligneClassement){
	    if (equals(getCell(fichier,indice,2),niveau) && equals(getCell(fichier,indice,4),mode)){
		Joueur j = newJoueur();
		j.pseudo = getCell(fichier,indice,0);
		j.score = stringToInt(getCell(fichier,indice,1));
		j.difficulte.difficulte = getCell(fichier,indice,2);
		j.journeePasse = stringToInt(getCell(fichier,indice,3));
		j.mode = getCell(fichier,indice,4);
		js[indice_tab] = j;
		indice_tab += 1;
	    }
	    indice += 1;
	}
    }


    /// TRI TABLEAU ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Tri un tableau de joueur
    void triTableau(Joueur[] js){
	int indice_min , ligneClassement = length(js);
	for (int i=0;i<ligneClassement-1;i+=1){
	    indice_min = i;
	    for (int j=i+1;j<ligneClassement;j+=1){
		if (js[indice_min].score < js[j].score){
		    indice_min = j;
		}
	    }
	    echanger(js,i,indice_min);
	}
    }

    // Echange les deux valeur d'un tableau de joueur aux indice donné
    void echanger(Joueur[] js, int indice , int indice_min){
	Joueur save = js[indice];
	js[indice] = js[indice_min];
	js[indice_min] = save;
    }

    // Echange les deux valeur d'un tableau de String aux indice donné
    void echanger(String[] tab, int indice , int indice_min){
	String save = tab[indice];
	tab[indice] = tab[indice_min];
	tab[indice_min] = save;
    }

    // Tri un tableau de couleur à partir d'une couleur donné
    void triCouleur(String[] listeCouleur , String couleur){
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
    }


    /// ENREGISTREMENT /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    // Permet l'enregistrement d'un joueur dans un fichier CSV en conservent les anciennes données
    void enregistrer(Joueur j , String nomFichier){
	CSVFile fichier = loadCSV(nomFichier);
	int nbLignes = rowCount(fichier);
	String[][] nouveauScore = new String[1+nbLignes][5];
        nouveauScore[0][0] = j.pseudo;
		nouveauScore[0][1] = ""+j.score;
		nouveauScore[0][2] = j.difficulte.difficulte;
		nouveauScore[0][3] = ""+j.journeePasse;
		nouveauScore[0][4] = j.mode;;
	int indiceLigne = 1;
	for (int lig=0;lig<nbLignes;lig++){
	    for (int col=0;col<5;col++){
		nouveauScore[indiceLigne][col] = getCell(fichier,lig,col);
	    }
	    indiceLigne += 1;
	}
	saveCSV(nouveauScore,nomFichier);
    }

	// Permet l'enregistrement d'un joueur dans un fichier CSV avec ses données de la partie en cours
	void enregistrerMilieuPartie(Joueur j , int nbClient, int nbClientJour, String nomFichier){
		CSVFile fichier = loadCSV(nomFichier);
		int nbLignes = rowCount(fichier);
		String[][] nouveauScore = new String[1+nbLignes][7];
			nouveauScore[0][0] = j.pseudo;
			nouveauScore[0][1] = ""+j.score;
			nouveauScore[0][2] = j.difficulte.difficulte;
			nouveauScore[0][3] = ""+j.journeePasse;
			nouveauScore[0][4] = j.mode;
			nouveauScore[0][5] = ""+nbClient;
			nouveauScore[0][6] = ""+nbClientJour;
		int indiceLigne = 1;
		for (int lig=0;lig<nbLignes;lig++){
			for (int col=0;col<5;col++){
			nouveauScore[indiceLigne][col] = getCell(fichier,lig,col);
			}
			indiceLigne += 1;
		}
		saveCSV(nouveauScore,nomFichier);
    }

    // Permet l'enregistrement d'un joueur dans un fichier CSV et en conservent les lignes de données conserver1 et conserver2
    void enregistrer(Joueur j ,int conserver1, int conserver2, String nomFichier){
	CSVFile fichier = loadCSV(nomFichier);
	String[][] nouveauScore = new String[3][5];
        nouveauScore[0][0] = j.pseudo;
	nouveauScore[0][1] = ""+j.score;
	nouveauScore[0][2] = j.difficulte.difficulte;
	nouveauScore[0][3] = ""+j.journeePasse;
	nouveauScore[0][4] = j.mode;
	int nbLignes = rowCount(fichier);
	int nbCol = columnCount(fichier);
	int indiceLigne = 1;
	for (int lig=0;lig<nbLignes;lig++){
	    if (lig == conserver1  || lig == conserver2){
		for (int col=0;col<nbCol;col++){
		    nouveauScore[indiceLigne][col] = getCell(fichier,lig,col);
		}
		indiceLigne += 1;
	    } else {
		enregistrer(chargerJoueur(j,fichier,lig),CLASSEMENT);
	    }
	}
	saveCSV(nouveauScore,nomFichier);
    }


    /// SAISIE /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Réalise la prise de saisie de couleur pour savoir qu'elle type de couleur changer et vérifie les entrées saisies
    void saissieEntreeCouleur(String couleurActuelle , int choixChangementCouleur){
	int choix;
	boolean choixCorrect;
	String[] liste;
	liste = afficherListeCouleur(couleurActuelle);
	do{
	    print("       CHOIX : ");
	    text(couleur_ecriture);
	    choixCorrect = true;
	    choix = stringToInt(verifInt(readString()));
	    if (choix > 9 || choix < 1){
		choixCorrect = false;
		clearScreen();
		text(couleur_texte);
		liste = afficherListeCouleur(couleurActuelle);
		text(couleur_erreur);
		println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
	    }
	}while(!choixCorrect);
	text(couleur_texte);
	if (choix != 9){
	    assigneCouleur(choix,liste,choixChangementCouleur);
	}
    }

    // Réalise la prise de saisie et vérifie la validité des saisie dans l'intervalle mini et maxi
    int saisieEntree(int mini,int maxi,String nomFichier){
	int choix;
	boolean choixCorrect;
	clearScreen();
	String chaine = afficherFichier(nomFichier);
	print(chaine);
	print("       CHOIX : ");
	text(couleur_ecriture);
	do{
	    choixCorrect = true;
	    choix = stringToInt(verifInt(readString()));
	    if (choix > mini || choix < maxi){
		choixCorrect = false;
		clearScreen();
		text(couleur_texte);
		print(chaine);
		text(couleur_erreur);
		println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
		text(couleur_texte);
		print("       CHOIX : ");
		text(couleur_ecriture);
	    }
	}while(!choixCorrect);
	text(couleur_texte);
	return choix;
    }

    // Réalise la prise de saisie pour la récuperation de donnée dans un fichier CSV
    int[] choixDonnéeRecuperer(int longueur , String nomFichierCSV, String nomFichier){
		CSVFile fichier = loadCSV(nomFichierCSV);
		CSVFile fichierPartieMilieu = loadCSV(SAVE_MILIEU_PARTIE);
		int[] choix = new int[2];
		boolean choixCorrect;
		String chaine = afficherFichier(nomFichier);
		String chaineMenu = afficherFichier(CHOIXMENU);
		int choixMenu;
		clearScreen();
		print(chaine);
		do{
			println(chaineMenu);
			print("\n"+ajouterCaractere(6,' ')+"CHOIX : ");
			text(couleur_ecriture);
			choixMenu = stringToInt(verifInt(readString()));
			choixCorrect = true;
			if (choixMenu < 1 || choixMenu > 3){
				choixCorrect = false;
				clearScreen();
				text(couleur_texte);
				print(chaine);
				text(couleur_erreur);
				println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
				text(couleur_texte);
			}
		}while(!choixCorrect);
		text(couleur_texte);
		if(choixMenu != 3)
		{
			clearScreen();
			print(chaine);
			if(choixMenu == 2)
			{
				choix[0] = choixMenu;
				afficherDonnees(fichier);
				do{
					print("\n       CHOIX : ");
					text(couleur_ecriture);
					choixCorrect = true;
					choix[1] = stringToInt(verifInt(readString()));
					if (choix[1]-1 > longueur || choix[1] < 1){
						choixCorrect = false;
						clearScreen();
						text(couleur_texte);
						print(chaine);
						afficherDonnees(fichier);
						text(couleur_erreur);
						println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
						text(couleur_texte);
					}
				}while(!choixCorrect);
			}
			else if(choixMenu == 1)
			{
				choix[0] = choixMenu;
				afficherDonnees(fichierPartieMilieu);
				do{
					print("\n       CHOIX : ");
					text(couleur_ecriture);
					choixCorrect = true;
					choix[1] = stringToInt(verifInt(readString()));
					if (choix[1]-1 > longueur || choix[1] < 1){
						choixCorrect = false;
						clearScreen();
						text(couleur_texte);
						print(chaine);
						afficherDonnees(fichierPartieMilieu);
						text(couleur_erreur);
						println("       ERREUR DE SAISSIE ! VEUILLEZ CHOISIR UN CHIFFRE PARMIS CEUX PROPOSER SUR LE MENU ;)");
						text(couleur_texte);
					}
				}while(!choixCorrect);
			}
		}
		text(couleur_texte);
		return choix;
    }
	


    /// COULEUR ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Initialise les couleur de base au lancement du programme
    void initialisationCouleur(){
	couleur_texte = "WHITE";
	couleur_de_fond = "BLACK";
	couleur_erreur = "RED";
	couleur_confirmation = "GREEN";
	couleur_ecriture = "CYAN";
	text(couleur_texte);
	background(couleur_de_fond);
    }
	
    // Réalise le changement de couleur 
    boolean changerCouleur(){
	int choix;
	text(couleur_texte);
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

    // assigne les nouvelles couleurs en fonction du choix réaliser
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


    /// FONCTIONS DE CALCULE ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Compte le nombre d'éléments ayant un certains niveau et mode dans un fichier CSV
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

	String stringClassement(Joueur[] js){
		int ligneClassement = length(js);
		if (ligneClassement > 10){
			ligneClassement = 10;
		}
		int[] lesPlusLong = plusLong(js);
		int lenPseudo = lesPlusLong[0];
		int lenJourneePasse = lesPlusLong[1];
		int lenScore = lesPlusLong[2];
		String affichage = "";
		String categorie = "      |  POSITION  |  PSEUDO"+ajouterCaractere(lenPseudo-6,' ')+"  |  SCORE"+ajouterCaractere(lenScore-5,' ')+"  |  NIVEAU"+ajouterCaractere(3,' ')+"  |  JOURNEE PASSER"+ajouterCaractere(lenJourneePasse-18,' ')+"  |  MODE"+ajouterCaractere(4,' ')+"  |\n";
		if (lenPseudo < 6){
			lenPseudo = 6;
		}
		if (lenScore < 5){
			lenScore = 5;
		}
		if (lenJourneePasse < 14){
			lenJourneePasse = 14;
		}
		String ligneDebutFin = "      ---------------"+ajouterCaractere(lenPseudo,'-')+"-----"+ajouterCaractere(lenScore,'-')+"-------------------"+ajouterCaractere(lenJourneePasse,'-')+"-----------------\n";
		affichage += ligneDebutFin+categorie+ligneDebutFin;
		for (int a=0;a<ligneClassement;a+=1){
			if ((a+1) == 1){
				affichage +="      |  "+(a+1)+"er       |  ";
			} else {
				if ((a+1) == 10){
					affichage +="      |  "+(a+1)+"ème     |  ";
				} else {
					affichage +="      |  "+(a+1)+"ème      |  ";
				}
			}
			affichage +=toString(js[a],lenScore,lenPseudo,lenJourneePasse)+"  |\n";
		}
		for (int b=ligneClassement;b<10;b+=1){
			if ((b+1) == 1){
				affichage +="      |  "+(b+1)+"er       |  ";
			} else {
				if ((b+1) == 10){
					affichage +="      |  "+(b+1)+"ème     |  ";
				} else {
					affichage +="      |  "+(b+1)+"ème      |  ";
				}
			}
			affichage +="*"+ajouterCaractere(lenPseudo-1,' ')+"  |  *"+ajouterCaractere(lenScore-1,' ')+"  |  *"+ajouterCaractere(8,' ')+"  |  *"+ajouterCaractere(lenJourneePasse-1,' ')+"  |  *         |\n";
		}
		affichage +=ligneDebutFin;
		return affichage;
	}

    // resultat des multiplications 
    int resultatMulti(Multiplication multi)
    {
	return multi.a * multi.b;
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

    // resultat des multiples multiplications 
    int resultatMulti(Multiplication[] multi)
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


    // Indique si l'on passe au jour suivant
    boolean jourSuivant(int score, Joueur j)            
    {
        if(score <= j.score)
	    {
		j.journeePasse += 1;
		afficherJourneeTermine();
		return true;
	    }
        return false;
    }

    // Créer aleatoirement un entier correspondant au nombre de clients allant venir au cours de la partie
    int creerNbClientJour(Difficulte d)
    {
        return (int)(d.nbClientsMax + (random() * (25-d.nbClientsMax)));
    }

		
    // Calcule la taille de certaines informations d'une liste de joueurs et renvoie la taille la plus longue de chaqu'un d'entre eux
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
		
    // Créer une chaine de caractère d'une certaines longueur constitué uniquement d'un donné caractère
    String ajouterCaractere(int nbr,char caractere){
	String ajout = "";
	for (int n=0;n<nbr;n++){
	    ajout += caractere;
	}
	return ajout;
    }

		
    // Vérifie que l'entree est un String convertisable en Int
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

	

    // Calcule le nouveau score du joueur en fonction de sa réponse
    int ajoutPoints(int nbClient , Joueur j , String res , Multiplication[] multi , double duree){
	if(stringToInt(verifInt(res)) == resultatMulti(multi))
	    {
		j.score += 100 * j.difficulte.multiplicateurScore;
		text(couleur_confirmation);
		println("      BONNE RÉPONSE 🎉\n");
		text(couleur_texte);
		delay(1000);
		nbClient += ajoutClient((duree)/1000,nbClient,j.difficulte);
	    }
	else
	    {
		j.score -= 50 / j.difficulte.multiplicateurScore;
		text(couleur_erreur);
		println("      MAUVAISE RÉPONSE 😭\n");
		text(couleur_texte);
		delay(1000);
		nbClient += ajoutClient(j.difficulte.intervalle + 5,nbClient,j.difficulte);
	    }
	return nbClient;
    }

    // Réalise un changement de difficulté pour un joueur ou non en fonction de sa saisie
    void changementDeDifficulte(Joueur j){
	String reponse;
	boolean reponseCorrect;
	clearScreen();
	String chaine = afficherFichier(CONFIRMATION_DIFFICULTE);
	print(chaine);
	do{
	    print("       CHOIX : ");
	    text(couleur_ecriture);
	    reponseCorrect = true;
	    reponse = readString();
	    if (!(equals(reponse,"1") || equals(reponse,"2"))){
		reponseCorrect = false;
		text(couleur_texte);
		clearScreen();
		print(chaine);
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
		reponse = toLowerCase(readString());
		if (!equals(reponse,"facile") && !equals(reponse,"moyen") && !equals(reponse,"difficile")){
		    reponseCorrect = false;
		    text(couleur_erreur);
		    println("       ERREUR DE SAISIE ! VEUILLEZ ENTREZ \"facile\" ou \"moyen\" ou \"difficile\"");
		}
	    }while(!reponseCorrect);
	    chargerDifficulte(j,reponse);
	}
    }


    /// MENUS //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Menu de Classement
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
	
    // Menu de Paramètrage
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
	
    // Menu de choix du mode de jeu
    void menuJeu(){
	int choix = saisieEntree(3,1,MENU_MODE);
	if (choix == 1){
	    Joueur j = creerJoueur("CAMPAGNE");
	    jouerCampagne(j,-1,-1);
	} else if (choix == 2){
	    Joueur j = creerJoueur("INFINI");
	    jouerInfini(j,-1,-1);
	}
    }

    // Menu pour reprndre une partie en cours
    void menuReprendreJeu(){
		CSVFile fichier = loadCSV(SAUVEGARDE);
		CSVFile fichierPartieMilieu = loadCSV(SAVE_MILIEU_PARTIE);
		int longueur = rowCount(fichier);
		int longue = rowCount(fichierPartieMilieu);
		int[] choix = choixDonnéeRecuperer(longueur+1,SAUVEGARDE,REPRENDRE);
		if(choix[0] == 2 && choix[1]-1 != longueur)
		{
			Joueur j = newJoueur();
			chargerJoueur(j,fichier,choix[1]-1);
			supprimerSauvegarde(fichier,choix[1]-1);
			if (equals(getCell(fichier,choix[1]-1,4),j.mode)){
				jouerCampagne(j,-1,-1);
			} else {
				jouerInfini(j,-1,-1);
			}
		}
		else if(choix[0] == 1 && choix[1]-1 != longue)
		{
			Joueur j = newJoueur();
			chargerJoueur(j,fichierPartieMilieu,choix[1]-1);
			supprimerSauvegardeMilieuPartie(fichierPartieMilieu,choix[1]-1);
			if (equals(getCell(fichierPartieMilieu,choix[1]-1,4),j.mode)){
				saveMilieuPartie = false;
				jouerCampagne(j,stringToInt(getCell(fichierPartieMilieu,choix[1]-1,5)),stringToInt(getCell(fichierPartieMilieu,choix[1]-1,6)));
			} else {
				saveMilieuPartie = false;
				jouerInfini(j,stringToInt(getCell(fichierPartieMilieu,choix[1]-1,5)),stringToInt(getCell(fichierPartieMilieu,choix[1]-1,6)));
			}
		}
    }


    /// LANCER PARTIE //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Lance une serie de partie limité
    void jouerCampagne(Joueur j,int nbClient, int nbClientJour){
        boolean reussie , save = false;
	if (j.journeePasse == 1){
	    afficherHistoire(j.journeePasse);
		changementDeDifficulte(j);
	    reussie = lancerPartie(j,nbClient,nbClientJour);
	    if (reussie){
			save = sauvegarder(j);
	    }
	}
	if (j.journeePasse == 2 && !save){
	    afficherHistoire(j.journeePasse);
	    reussie = lancerPartie(j,-1,-1);
	    if (reussie){
		save = sauvegarder(j);
	    }
	}
	if (j.journeePasse == 3 && !save){
	    afficherHistoire(j.journeePasse);
	    reussie = lancerPartie(j,-1,-1);
	    if (reussie){
		save = sauvegarder(j);
	    }
	}
	if (j.journeePasse == 4 && !save){
	    afficherHistoire(j.journeePasse);
	    reussie = lancerPartie(j,-1,-1);
	}
	if (j.journeePasse == 5){
	    afficherHistoire(j.journeePasse);
		enregistrer(j,CLASSEMENT);
		save = true;
	}
	if (!save && !saveMilieuPartie){
	    perdu(j);
	}
    }

    // Lance une serie de partie illimité
    void jouerInfini(Joueur j,int nbClient, int nbClientJour){
        boolean reussie , save = false;
	do{
	    reussie = lancerPartie(j,nbClient,nbClientJour);
	    if (reussie){
		save = sauvegarder(j);
	    }
	}while(reussie && !save);
	if(!save && !saveMilieuPartie){
	    perdu(j);
	}
    }


    /// SAUVEGARDE /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Enregistre un joueur en fonction d'un fichier CSV et de la saisie
    boolean sauvegarder(Joueur j){
		int lenFichier; 
		int[] choix;
		CSVFile fichier = loadCSV(SAUVEGARDE);
		while(true){
			
			if (1 == saisieEntree(2,1,SAVE_OR_CONTINUE)){
				lenFichier = rowCount(fichier);
				if (lenFichier < 10){
					enregistrer(j,SAUVEGARDE);
					return true;
				} else {
					choix = choixDonnéeRecuperer(lenFichier, SAUVEGARDE,SUPPRIMER_SAVE);
					if (choix[1] < 4){
					if (choix[1] == 1){
						enregistrer(j,1,2,SAUVEGARDE);
					} else if (choix[1] == 2){
						enregistrer(j,0,2,SAUVEGARDE);
					} else {
						enregistrer(j,0,1,SAUVEGARDE);
					}
					return true;
					}
				}
			} else {
			return false;
			}
		}
    }

	// sauvegarde la partie en cours
	boolean saveMilieuPartie(Joueur j, int nbClient, int nbClientJour)
	{
		CSVFile fichier = loadCSV(SAVE_MILIEU_PARTIE);
		int lenFichier = rowCount(fichier);
		enregistrerMilieuPartie(j,nbClient,nbClientJour,SAVE_MILIEU_PARTIE);
		return true;
	}

    // Supprime les données d'un joueur dans fichier CSV 
    void supprimerSauvegarde(CSVFile fichier , int choix){
	int nbLignes = rowCount(fichier);
	int nbCol = columnCount(fichier);
	String[][] enregistrementSauvegarde = new String[nbLignes-1][5];
	int indiceLigne = 0;
	for (int lig=0;lig<nbLignes;lig++){
	    if (lig != choix){
		for (int col=0;col<nbCol;col++){
		    enregistrementSauvegarde[indiceLigne][col] = getCell(fichier,lig,col);
		}
		indiceLigne += 1;
	    }
	}
	saveCSV(enregistrementSauvegarde,SAUVEGARDE);
    }

	// supprime une ligne dans le fichier
	void supprimerSauvegardeMilieuPartie(CSVFile fichier , int choix){
		int nbLignes = rowCount(fichier);
		int nbCol = columnCount(fichier);
		String[][] enregistrementSauvegarde = new String[nbLignes-1][7];
		int indiceLigne = 0;
		for (int lig=0;lig<nbLignes;lig++){
			if (lig != choix){
			for (int col=0;col<nbCol;col++){
				enregistrementSauvegarde[indiceLigne][col] = getCell(fichier,lig,col);
			}
			indiceLigne += 1;
			}
		}
		saveCSV(enregistrementSauvegarde,SAVE_MILIEU_PARTIE);
    }


    /// JOUER PARTIE ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Lance une partie de jeu
    boolean lancerPartie(Joueur j,int nbClient, int nbClientJour)
    {
        Multiplication[] multi = creerMultiMultiplication(j.difficulte);
		if(nbClient == -1)
		{
			nbClient = 2+(int)(random()*(j.difficulte.nbClientsMax/3));
		}
        if(nbClientJour == -1)
		{
			nbClientJour = creerNbClientJour(j.difficulte);
		}
        int scoreAtteindre = j.score + (int)(nbClientJour*(100*j.difficulte.multiplicateurScore));
        double debut , fin , duree;
        String res;
		String barreScore, espaceJour, espaceScore, jourPass;
		afficherAtteindreScore(scoreAtteindre);
		text(couleur_texte);
        while(nbClient < j.difficulte.nbClientsMax && nbClientJour > 0)
	    {
			boolean resCorrect = true;
			do{
				jourPass = "Jour " + j.journeePasse;
				barreScore = ajouterCaractere(5+length("" + j.score)+length("" + scoreAtteindre),'#');
				espaceScore = ajouterCaractere(6,' ');
				espaceJour = ajouterCaractere(168-length(jourPass)-length(barreScore),' ');
				clearScreen();
				//println("Nombre de client aujourd'hui : " + nbClientJour);
				println(espaceScore + "Score :\n" + espaceScore + barreScore + "\n" + espaceScore + "# "+j.score + "/" + scoreAtteindre + " #\n" + espaceScore + barreScore + espaceJour + jourPass);
				println();
				afficheQueue(nbClient,multi,j.difficulte);
				if (!resCorrect){
					text(couleur_erreur);
					println(ajouterCaractere(6,' ') + "Erreur, seul les entiers sont acceptés. Réessayer.");
					text(couleur_texte);
				}
				println(ajouterCaractere(6,' ') + "(tapez 'help' pour obtenir les tables de multiplications)");
				println(ajouterCaractere(6,' ') + "(tapez 'save' pour sauvegarder la partie en cours)");
				debut = getTime();
				res = afficheMultiplication(multi,j.difficulte);
				fin = getTime();
				duree = fin - debut;
				resCorrect = true;
				if(equals(toLowerCase(res),"save"))
				{
					if(saveMilieuPartie(j,nbClient,nbClientJour))
					{
						println(ajouterCaractere(6,' ')+"Partie sauvegardée !");
						delay(1500);
						saveMilieuPartie = true;
						return false;
					}
				}
				if(equals(toLowerCase(res),"help"))
				{
					print(afficherFichier(TABLES));
					afficheQueue(nbClient,multi,j.difficulte);
					debut = getTime();
					res = afficheMultiplication(multi,j.difficulte);
					fin = getTime();
					duree = (fin - debut) - 5000;
				}
				else if (equals(verifInt(res),"-1")) {
					resCorrect = false;
				}

			}while(!resCorrect);
			nbClient = ajoutPoints(nbClient,j,res,multi,duree);
			nbClientJour -= 1;
			multi = creerMultiMultiplication(j.difficulte);
            
	    }
	if (jourSuivant((int)(scoreAtteindre*0.75), j)){
	    return true;
	}
            
	return false ;
    }


    /// ALGORITHME PRINCIPALE///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    void algorithm(){
	initialisationCouleur();
	clearScreen();
	boolean finDeJeu = false;
	int choix;
	do{
	    choix = saisieEntree(5,1,MENU);
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

	/// FONCTIONS TEST //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	void testResultatMultiMultiplication()
	{
		Multiplication[] m = new Multiplication[1];
		m[0] = new Multiplication();
		m[0].a = 2;
		m[0].b = 2;
		assertEquals(4,resultatMulti(m));
	}

	void testVerifInt()
	{
		assertEquals("10",verifInt("10"));
		assertEquals("-1",verifInt("a10"));
		assertEquals("-1",verifInt("1a0"));
	}

	void testAjouterCaractere()
	{
		assertEquals("llll",ajouterCaractere(4,'l'));
		assertEquals("      ",ajouterCaractere(6,' '));
	}

	void testResultatMulti()
	{
		Multiplication m = new Multiplication();
		m.a = 4;
		m.b = 5;
		assertEquals(20,resultatMulti(m));
	}
}

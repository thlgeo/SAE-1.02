import extensions.File;

class SAE extends Program
{ 
    Multiplication creerMultiplication() // pour créer une multiplication.
    {
        Multiplication multi = new Multiplication();
        multi.a = 1 + (int) (random() * 10);
        multi.b = 1 + (int) (random() * 10);
        return multi;
    }

    Multiplication[] creerMultiMultiplication(Difficulte d) // pour créer des multiplications complexes. J'ai limité à 3 multiplications en même temps sinon trop compliqué.
    {
        int nbMulti;
        if(equals(d.difficulte,"facile"))
        {
            nbMulti = 1;
        }
        else if(equals(d.difficulte,"moyen"))
        {
            nbMulti = 1 + (int)(random() * 2);
        }
        else
        {
            nbMulti = 1 + (int)(random() * 3);
        }
        Multiplication[] multi = new Multiplication[nbMulti];
        for(int i=0;i<length(multi);i++)
        {
            multi[i] = creerMultiplication();
        }
        return multi;
    }

    Joueur creerJoueur()
    {
        Joueur j = new Joueur();
        j.pseudo = "test";
        j.score = 0;
        j.difficulte = "facile";
        j.journeePasse = 3;
        return j;
    }

    int resultatMulti(Multiplication multi)
    {
        return multi.a * multi.b;
    }

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

    String toString(Multiplication multi)
    {
        return multi.a + "*" + multi.b;
    }

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

    void afficherTableMulti()
    {
        File file = newFile("Table_Multiplication.txt");
        while(ready(file))
        {
            println(readLine(file));
        }
    }

    Difficulte creerDifficulte(Joueur j)
    {
        Difficulte d = new Difficulte();
        d.difficulte = j.difficulte;
        if(equals(d.difficulte,"facile"))
        {
            d.nbClientsMax = 15;
            d.multiplicateurScore = 1.25;
            d.intervalle = 15.25 - (0.25*j.journeePasse);
        }
        else if(equals(d.difficulte,"moyen"))
        {
            d.nbClientsMax = 12;
            d.multiplicateurScore = 1.0;
            d.intervalle = 15.25 - (0.25*j.journeePasse);
        }
        else{
            d.nbClientsMax = 8;
            d.multiplicateurScore = 0.75;
            d.intervalle = 15.25 - (0.25*j.journeePasse);
        }
        return d;
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

    void afficheQueue(int nbClient)
    {
        File fich = newFile("personnage.txt");
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
                affiche += ajouterCaractere(70-(nbClient*5),'_');
            }
            else
            {
                affiche += ajouterCaractere(70-(nbClient*5),' ');
            }
            if(nbClient == 1)
            {
                if(j == 4)
                {
                    affiche += "______" + body + partEtabli + body;
                }
                else
                {
                    affiche +=  "      " + body + partEtabli + body;
                }                
            }
            else
            {
                for(int i=0;i<nbClient-1;i++)
                {
                    affiche += body;
                    if(i == nbClient - 2 && j == 4)
                    {
                        affiche += "______" + body + partEtabli + body;
                    }
                    else if(i == nbClient - 2)
                    {
                        affiche +=  "      " + body + partEtabli + body;
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
    }

    String ajouterCaractere(int nb, char car)
    {
        String ajout = "";
        for(int i=0;i<nb;i++)
        {
            ajout += car;
        }
        return ajout;
    }

    int afficheMultiplication(Multiplication[] multi)
    {
        print(toString(multi) + " : ");
        int res = readInt();
        
        return res;
    }

    boolean jourSuivant(int score, Joueur j)
    {
        boolean suivant = false;
        if(score <= j.score || score*0.25 <= j.score)
        {
            j.journeePasse += 1;
            suivant = true;
            afficherJourneeTermine();
        }
        return suivant;
    }

    int creerNbClientJour(Difficulte d)
    {
        double max = d.nbClientsMax + (int)(random()*25-d.nbClientsMax);
        return (int)d.nbClientsMax + (int)(random() * (max));
    }

    void algorithm()
    {
        Joueur j = creerJoueur();
        Difficulte d = creerDifficulte(j);
        Multiplication[] multi = creerMultiMultiplication(d);
        int nbClient = 2+(int)(random()*d.nbClientsMax/3);
        int nbClientJour = creerNbClientJour(d);
        int scoreAtteindre = nbClientJour*(int)(100*d.multiplicateurScore);
        double debut;
        double fin;
        double duree;
        int res;
        while(nbClient < d.nbClientsMax && nbClientJour > 0)
        {
            clearScreen();
            println(nbClientJour);
            println("Votre score : " + j.score);
            println("Score à atteindre : "+scoreAtteindre);
            println(nbClient);
            afficheQueue(nbClient);
            debut = getTime();
            res = afficheMultiplication(multi);
            fin = getTime();
            duree = fin - debut;
            if(res == -1)
            {
                afficherTableMulti();
                afficheQueue(nbClient);
                debut = getTime();
                res = afficheMultiplication(multi);
                fin = getTime();
                duree = (fin - debut) - 5000;
            }
            if(res == resultatMulti(multi))
            {
                j.score += 100 * d.multiplicateurScore;
                println("bien joué\n");
                delay(1000);
                nbClient += ajoutClient((duree)/1000,nbClient,d);
            }
            else
            {
                j.score -= 50 / d.multiplicateurScore;
                println("perdu");
                delay(1000);
                nbClient += ajoutClient(d.intervalle + 5,nbClient,d);
            }
            nbClientJour -= 1;
            multi = creerMultiMultiplication(d);
            
        }
        jourSuivant(scoreAtteindre, j);
    }
}
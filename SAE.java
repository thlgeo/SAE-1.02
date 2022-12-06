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

    Difficulte creerDifficulte(String diff)
    {
        Difficulte d = new Difficulte();
        d.difficulte = diff;
        if(equals(d.difficulte,"facile"))
        {
            d.nbClientsMax = 15;
            d.multiplicateurScore = 1.25;
            d.intervalle = 17;
        }
        else if(equals(d.difficulte,"moyen"))
        {
            d.nbClientsMax = 12;
            d.multiplicateurScore = 1.0;
            d.intervalle = 12;
        }
        else{
            d.nbClientsMax = 8;
            d.multiplicateurScore = 0.75;
            d.intervalle = 8;
        }
        return d;
    }

    int ajoutClient(double duree,int nbClient,Difficulte d)
    {
        if(nbClient > 0)
        {
            if(duree > d.intervalle)
            {
                print("Vraie");
                return (int)(random()*3);
            }
            else if(nbClient - 1 > 0)
            {
                return -1;
            }
        }
        return 1;
        
    }

    void algorithm()
    {
        print("Entrez difficulté : ");
        String diff = readString();
        Difficulte d = creerDifficulte(diff);
        Multiplication[] multi = creerMultiMultiplication(d);
        int nbClient = 1+(int)(random()*d.nbClientsMax/3);
        int score = 0;
        double debut;
        double fin;
        while(nbClient < d.nbClientsMax)
        {
            debut = getTime();
            println(toString(multi) + " : ");
            int res = readInt();
            fin = getTime();
            double duree = fin - debut;
            if(res == -1)
            {
                afficherTableMulti();
                debut = getTime();
                println(toString(multi) + " : ");
                res = readInt();
                fin = getTime();
                duree = (fin - debut)-5000;
            }
            else if(res == resultatMulti(multi))
            {
                score += 100 * d.multiplicateurScore;
                println("bien joué\nVotre score : " + score);
                println("Nombre de clients : "+nbClient);
            }
            else
            {
                score -= 50 / d.multiplicateurScore;
                println("perdu\nVotre score : " + score);
                println("Nombre de clients : "+nbClient);
            }
            
            nbClient += ajoutClient((duree)/1000,nbClient,d);
            multi = creerMultiMultiplication(d);
        }
        
    }
}
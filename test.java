import extensions.File;

class test extends Program
{
    Multiplication creerMultiplication() // pour créer une multiplication.
    {
        Multiplication multi = new Multiplication();
        multi.a = 1 + (int) (random() * 10);
        multi.b = 1 + (int) (random() * 10);
        return multi;
    }

    Multiplication[] creerMultiMultiplication() // pour créer des multiplications complexes. J'ai limité à 3 multiplications en même temps sinon trop compliqué.
    {
        int nbMulti = 1 + (int)(random() * 3);
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

    void algorithm()
    {
        afficherTableMulti();
        Multiplication[] multi = creerMultiMultiplication();
        println(toString(multi));
        print("Entrez le résultat : ");
        int res = readInt();
        if (res == resultatMulti(multi))
        {
            println("Bien joué");
        }
        else
        {
            println("Erreur");
        }
    }
}
# Candy Shop Math's Adventure

Développé par Clément Franckelemon et Théo Lenglart

Contacts : clement.franckelemon.etu@univ-lille.fr , theo.lenglart.etu@univ-lille.fr

## Présentation de Candy Shop Math's Adventure

Ce jeu consiste à s'améliorer en calcule mentale en incarnent une caissière de friandise venant d'ouvrir sa boutique
, au travers d'une histoire pour les joueur souhaitent évoluer tranquilement , ou encore au travers de la fonctionnalité
du mode infini où il faudra être le plus rapide et le meilleur pour atteindre le haut du classement .

Des captures d'écran illustrant le fonctionnement du logiciel sont proposées dans le répertoire shots.

## Modification possible de Candy Shop Math's Adventure

Vous pouvez modifier l'intervalles des tables de modification de manière à n'avoir qu'une table ou alors un intervalle plus grand voir plus petit

Pour cela rendez vous dans le fichier "ressources" puis ouvrez le fichier "Tables.csv" avec un tableur ('Excel','LibreOffice',etc)

Vous pourrez y trouver 2 valeur , la première est le minimum et la seconde et le maximum de l'intervale que vous souhaitez avoir , pour appliquer votre
propre intervalle , modifier les valeurs que vous souhaitez avoir pui enregistrer le fichier (au format csv)

## Utilisation de Candy Shop Math's Adventure

ATTENTION : la fenêtre du terminal doit être en Maximiser la page (si possible en 1920x1080) et à cela le zoom doit être de taille normal
(Affichage -> taille normal) pour la meilleur experience de jeux possible .

Afin d'utiliser le projet, il suffit de taper les commandes suivantes dans un terminal :

./compile.sh
//compilation des fichiers présents dans 'src' et création des fichiers '.class' dans 'classes'

./run.sh CandyShop
//lancement du jeu

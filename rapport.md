# **Rapport Analyse des Performances en Calcul Parallèle et Distribué**

---

**Auteurs :**
- [Grondin David]
- [L'IA à était utiliser comme outils]

**Date :**
- [03/03/2025]

**Établissement :**
- [IUT de Vélizy]


---


# Introduction 
Dans le cadre de ce projet, nous avons étudié différentes méthodes d'estimation de la valeur de π à l'aide de la méthode de Monte Carlo. Cette technique repose sur l'utilisation de simulations aléatoires pour obtenir des résultats approximatifs d'une valeur difficile à calculer analytiquement.

L'objectif principal est de comparer trois implémentations différentes : une approche en mémoire partagée simple (Pi.java), une implémentation optimisée avec des structures concurrentes (Assignement102.java) et une solution en mémoire distribuée utilisant des communications inter-processus (MasterWorker.java).

À travers ces implémentations, nous analyserons les performances en fonction du parallélisme et de l'utilisation des ressources matérielles disponibles, en mettant en évidence les avantages et les limites de chaque approche.

# Architecture matérielle 

## PC personnel
- Prcesseur : AMD Ryzen 5 5600H
- Carte graphique : AMD Radeon
- Nombre de coeur physique : 6 
- Nombre de coeur logique : 12
- Mémoire RAM : 16 GO

## PC sale G26
- Prcesseur : Intel Core i7-7700
- Carte graphique : Intel HD Graphics 630
- Nombre de coeur physique : 4
- Nombre de coeur logique : 8
- Mémoire RAM : 32 GO

# Le Multithreading
Le multithreading permet à un processeur de gérer plusieurs tâches simultanément en divisant un cœur physique en plusieurs cœurs logiques (threads). Bien que cela améliore les performances en optimisant l'utilisation des ressources, un cœur logique ne double pas réellement la puissance d'un cœur physique. En effet, les threads partagent les mêmes unités de calcul, ce qui peut limiter l’efficacité sur certaines tâches intensives. Ainsi, 12 cœurs logiques (avec 6 cœurs physiques) ne sont pas aussi puissants que 12 cœurs physiques, bien que cela reste avantageux pour le multitâche et certaines applications parallèles.

# La norme ISO/IEC 25010 

**- ISO :** International Organization for Standardization

**- IEC :** International Electrotechnical Commission

ISO/IEC 25010 est une norme qui définit les modèles de qualité des logiciels et des systèmes. Elle est votée par les États membres et adoptée si elle obtient plus de 75 % d'approbation. Cette norme permet d'identifier les besoins du système, de définir les tests et les critères d'évaluation

1) **Quality in use model :** 
   - Effectiveness : Faire correctement, faire ce qu’on attend.
   - Efficiency : Faire le plus rapidement avec le moins d’outils possible. Performance de temps en utilisant les ressources de manière appropriée.
   - Satisfaction : Peut-on faire confiance au logiciel ? Utilisation agréable ? Logicielle accessible ?
   - Freedom from risk : minimisation du risque, degré de dépendance face au risque, risque sur la santé (IA, aide à la décision).
   - Context coverage : flexible, utiliser dans plusieurs domaine, utilisation ouverte/plus large

**Exemple :** 

- StarUML : 
  - Est effectif
  - Utile pour dessiner un diagramme de classe
  - On peut faire confiance au logicielle car il suit les normes
  - Pas de plaisir à utiliser si on est novice
  - Logicielle confortable et accessible

- Draw.io : 
  - Est effectif
  - Utile pour dessiner un diagramme de classe
  - On ne peut pas faire confiance au logicielle car l’utilisateur est libre de faire tout les dessin sans forcément respecter les normes
  - Pas de plaisir à utiliser si on est professionnel


2) **Product quality model:**
   - Functional suitability : Les functional suitability sont définies dans les cas d’utilisation.
   - Functional completeness : l'ensemble des fonction répondent au dépendance fonctionnel
   - Performance efficiency : la performance relative au nombre de ressource utilisé
   - Compatibilité : un composant peut utiliser/appeler un autre composant.

Un système informatique est composé d’un hardware, plusieurs software.
Le **Product quality model** est différent de **Quality in use model** ar il évalue les caractéristiques du produit, indépendamment de son usage.

3) **Critère:**
   - Time behavior : la métrique pour l’évaluer est le temps
   - Resource utilization : La métrique pour l’évaluer Sp = T1/Tp (Speedup)
   - Capacity : ressource mémoire , espace disponible

4**Quality Measurement :**
  - Effectiveness measures : Task completion, comme un test de validation
  - Efficiency measure : Time Combien de temps met une tâche à être complété selon un temps cible. ça fait référence au calcule du speedup.

# Methode monte carlo 
La méthode de Monte Carlo est une technique de calcul probabiliste utilisée pour estimer des quantités numériques complexes en s’appuyant sur des simulations aléatoires. Elle repose sur le principe que, en générant un grand nombre d’échantillons aléatoires et en analysant leur comportement, on peut obtenir une approximation fiable d’une valeur mathématique ou physique difficile à calculer analytiquement.

# Calcule de Pi

![img.png](documentation/image/generation_points.png)

Dans nos traveaux nous avons utiliser méthode de Monte Carlo pour l’estimation de π en générant aléatoirement des points dans un graphe avec un quart de cercle déssiner sur celui-ci, le but etant de calculer la proportion des point qui tombent dans le cercle, Les point sont donc gnérer aléatoirement sur une zonne carré du graphe. Les point on 75% de chance d'être generer dans le cerlce et 25% de chance d'être generer en dehors.

# L'architecture du projet 

Les trois implémentation anlysées sont: 

- Pi.java : Implémentation basée sur un modèle maître-travailleur en mémoire partagée.

- Assignement102.java : Une autre approche en mémoire partagée, avec différents paramètres d'exécution.

- MasterWorker.java : Implémentation en mémoire distribuée, avec communication inter-processus.

## Pi.java 
Le programme estime la valeur de π en générant aléatoirement des points dans un carré unité [0,1]×[0,1] et en comptant combien tombent à l’intérieur d’un quart de cercle inscrit dans ce carré. 


### Objectif du programme : 
Estimer Pi en distribuant le calcul sur plusieurs threads via un exécuteur de threads.

### Décomposition du progamme : 

- Classe Pi : Point d'entrée, initialise le calcul.
- Classe Master : Gère la création des threads et collecte les résultats.
- classe Worker : Effectue la simulation Monte Carlo sur un sous-ensemble de points.

### Shémat UML :
![img.png](documentation/uml/Uml_PiJava.png)

## Assignement 102 :

### Objectif du programme : 
Cette implémentation utilise la classe PiMonteCarlo pour générer des points de manière concurrente et calculer une approximation de Pi.

### Décomposition du progamme : 

- Classe PiMonteCarlo : Utilise AtomicInteger pour éviter les conditions de course. Exécute des tâches concurrentes avec un pool de threads WorkStealingPool. Met à jour la valeur de Pi après la fin des threads.
- Classe Assignement102 : Initialise et exécute le programme en parallèle. Mesure la durée d'exécution et stocke les résultats dans un fichier CSV.

### Shémat UML : 
![img.png](documentation/uml/Uml_Pi_Assignement102.png)

## MasterWorker 

### Objectif du programme : 
Cette implémentation utilise une architecture distribuée où un Master envoie des tâches aux Workers via des sockets. Chaque Worker effectue une simulation Monte Carlo et retourne le résultat au Master, qui agrège les valeurs pour estimer Pi.

### Décomposition du progamme : 

- Classe MasterSocket : Initialise un ensemble de connexions vers les Workers via des sockets. Envoie à chaque Worker une charge de travail équitablement répartie. Agrège les résultats reçus et calcule Pi. Stocke les résultats dans un fichier CSV pour analyse des performances.
- Classe WorkerSocket : Attente de connexions entrantes depuis le Master. Exécute l'algorithme Monte Carlo sur les données reçues. Renvoie le nombre de points dans le quart de cercle au Master.

### Shémat UML :
![img.png](documentation/uml/Uml_Master_Worker.png)

### Les Sockets : 
Les sockets untilisent les méthodes send et receive pour la communication. 
Ce sont donc des objets qui contient une aprdesse IP avec un port, un flux d'entrer (inputStream) et un flux de sortie (outPutStream)

![img.png](documentation/image/socket.png)

L'échange de données entre le Master et les Workers repose sur l'utilisation des sockets TCP/IP. Cette approche permet une exécution distribuée des calculs tout en assurant une communication efficace entre les processus. Voici les principales étapes :
1) Initialisation : 
- Le Master ouvre des connexions vers les différents Workers.
- Chaque Worker écoute sur un port spécifique en attente de tâches.
2) Distribution : 
- Le Master envoie à chaque Worker un nombre d'itérations à effectuer.
- Chaque Worker effectue le calcul et renvoie le nombre de points tombant dans le quart de cercle.
3) Agregation des résultats :  
- Le Master reçoit les résultats de chaque Worker.
- Il agrège les valeurs et calcule l'approximation finale de Pi.


## Calcule du speedup

**Speedup :** Le speedup est définie comme : S(p) = T(1)/T(p), où T(1) est le temps d'exécution en séquentiel et T(p) est le temps d'exécution avec  threads.

**Scalabilité :** La scalabilité mesure la capacité du programme à améliorer ses performances en fonction du nombre de processeurs. Deux types de scalabilité sont observables :

- **Scalabilité forte** : La charge de travail reste constant et le nombre de threads augmente. Augmentation de la performance jusqu'à une certaine limite, où le système atteint ses capacités maximales.

![img.png](documentation/image/scal_forte.png)

- **Scalabilité faible** : la charge de travail augmente proprtionelelment avec le nombre de threads. 

![img.png](documentation/image/scal_faible.png)

## Programation en mémoire partagée VS distribuée

**En mémoire partagée** : La programmation en mémoire partagée consiste à faire communiquer plusieurs processus ou threads à travers un **espace mémoire commun**. Tous les threads ou processus accèdent aux mêmes données, ce qui facilite les échanges, mais nécessite des mécanismes de synchronisation (comme les mutex ou les sémaphores) pour éviter les conflits d'accès. C'est une approche que l'on utilise quand on programme sur une seule machine par exemple.

**En mémoire distribuée** : la programation en mémoire distribuée chaque processus possède sa propre mémoire et communique avec les autres via un réseau en échangeant des messages, ce qui améliore la scalabilité mais ralentit les échanges.. Ce modèle est couramment utilisé dans les clusters et supercalculateurs. 

Le choix entre ces modèles dépend du type d’application et des ressources disponibles.

# Analyse des performances

## Scalabilité forte 

### En mémoire Partager 

#### Assignement102 
- Données :

Pour chaque lancer de teste avec un nombre de procésseur j'ai fais un totale de 5 lancer pour obtenir un résultat plus juste. mais pour que se soit plus lisible j'ai fais la moyenne de la colonne "error" et celle de la "duration" pour chaque test aillant le même nombre de processeur "Threads" et de nombre totale "Ntot". 

| Error        | Ntot      | Threads | Duration |
|--------------|-----------|---------|----------|
| 4.563299E-05 | 120000000 | 1       | 6326.80  |
| 5.899260E-05 | 120000000 | 2       | 5969.60  |
| 5.094997E-05 | 120000000 | 3       | 6128.00  |
| 4.525010E-05 | 120000000 | 4       | 6005.80  |
| 3.179999E-05 | 120000000 | 5       | 6092.80  |
| 6.394504E-05 | 120000000 | 6       | 6538.20  |
| 4.519963E-05 | 120000000 | 8       | 6278.00  |
| 3.430024E-05 | 120000000 | 10      | 6027.20  |
| 2.471229E-05 | 120000000 | 12      | 6741.00  |

- Graphe :
![img.png](documentation/graph/Assignement102_scal_forte.png)

#### Pi.java
- Données : 

| Error                  | Ntot       | Threads | Duration |
|------------------------|------------|---------|----------|
| 3.893391864507592e-05  | 120000000  | 1       | 1739.2   |
| 3.44122532619994e-05   | 120000000  | 2       | 891.0    |
| 2.9180152866478063e-05 | 120000000  | 3       | 615.8    |
| 5.100256452991623e-05  | 120000000  | 4       | 482.4    |
| 4.4450082072974674e-05 | 120000000  | 5       | 407.0    |
| 2.378964839378548e-05  | 120000000  | 6       | 340.4    |
| 2.2268584204429588e-05 | 120000000  | 8       | 267.8    |
| 2.25491539709505e-05   | 120000000  | 10      | 230.4    |
| 6.048137587382007e-05  | 120000000  | 12      | 225.2    |
| 1.0725214938760653e-05 | 1200000000 | 1       | 16987.8  |
| 2.2059952472201364e-05 | 1200000000 | 2       | 8621.4   |
| 9.168646941898716e-06  | 1200000000 | 3       | 5856.4   |
| 2.0741725130248365e-05 | 1200000000 | 4       | 4455.2   |
| 9.25452530418952e-06   | 1200000000 | 5       | 3624.6   |
| 1.177939197487478e-05  | 1200000000 | 6       | 3090.8   |
| 1.223291011286499e-05  | 1200000000 | 8       | 2460.8   |
| 1.1667526447995493e-05 | 1200000000 | 10      | 2056.8   |
| 1.168305018265772e-05  | 1200000000 | 12      | 1804.8   |

- Graphe :
![img.png](documentation/graph/Pi_scal_forte.png)

#### Master/Worker.java
- Données :

| Error              | Ntot      | Threads | Duration |
|--------------------|-----------|---------|--------|
| 2.0000615948401417 | 120000000 | 1       | 4319 |
| 0.5000093456190752 | 120000000 | 2       | 2243 |
| 0.3333491647067839 | 120000000 | 3       | 1585 |
| 0.7500120549463147 | 120000000 | 4       | 1209 |
| 0.7999709841603417 | 120000000 | 5       | 978  |
| 0.8333359286335443 | 120000000 | 6       | 906  |
| 0.8749985896321503 | 120000000 | 8       | 689  |
| 0.8000117172154437 | 120000000 | 10      | 786  |
| 0.8333143737490849 | 120000000 | 12      | 708  |

- Graphe :
  ![img.png](documentation/graph/Master_scal_forte.png)

#### Interprétation :

On s'aperçois que les resultat et la courbe d'assignement 102 sont moins bon que ceux de Pi.java et MasterWorker. Mais dans l'essemble plus on augmente le nombre de procésus plus la courbe s'éloigne du speedup idéale. 

### En mémoire distribuer

#### Master/Worker.java
- Données :

| Error        | Ntot      | Threads | Duration |
|--------------|-----------|---------|----------|
|  | 120000000 | 1       |   |
|  | 120000000 | 2       |   |
|  | 120000000 | 3       |   |
|  | 120000000 | 4       |   |
|  | 120000000 | 5       |   |
|  | 120000000 | 6       |   |
|  | 120000000 | 8       |   |
|  | 120000000 | 10      |   |
|  | 120000000 | 12      |   |

- Graphe :

#### Interprétation :

## Scalabilité faible

### En mémoire Partager

#### Assignement102 
- Données :

| Error        | Ntot      | Threads | Duration |
|--------------|-----------|---------|----------|
| 0.00011811502093290698 | 12000000 | 1       | 602.0 |
| 8.269774093013097e-05 | 24000000 | 2       | 1157.8  |
| 3.752436630491873e-05 | 36000000 | 3       | 1844.8  |
| 4.8147177951170264e-05 | 148000000 | 4       | 2343.4  |
| 9.574253713262492e-05 | 60000000 | 5       | 2996.0  |
| 5.96308845113726e-05 | 72000000 | 6       | 4013.0  |
| 3.909989685256029e-05 | 96000000 | 8       | 5136.8  |
| 5.5979266150540314e-05 | 120000000 | 10      | 6368.4  |
| 2.9817626893769117e-05 | 144000000 | 12      | 8835.4  |

- Graphe :
![img.png](documentation/graph/Assignement102_scal_faible.png)


#### Pi.java
- Données :

| Error                  | Ntot      | Threads | Duration |
|------------------------|-----------|---------|----------|
| 1.332925369518373e-05  | 120000000 | 1       | 4291.0  |
| 1.9190217438009317e-05 | 240000000 | 2       | 1770.8 |
| 1.2098273173821361e-05 | 360000000 | 3       | 1818.4  |
| 1.8921629201056837e-05 | 480000000 | 4       | 1817.8  |
| 2.682545997773335e-06  | 600000000 | 5       | 1831.6  |
| 1.640425506797887e-05  | 720000000 | 6       | 1879.4  |
| 1.1455744270449875e-05 | 960000000 | 8       | 1968.2  |
| 2.169340638298398e-05  | 1200000000 | 10      | 2070.8  |
| 1.906259968437344E-5   | 1440000000 | 12      | 2152.0  |

- Graphe :
![img.png](documentation/graph/Pi_scal_faible.png)


#### Master/Worker.java
- Données : 

| Error              | Ntot      | Threads | Duration |
|--------------------|-----------|---------|--------|
| 1.9999974235670868 | 120000000 | 1       | 4051 |
| 1.4999917184337104 | 240000000 | 2       | 4285 |
| 0.6666591460923004 | 360000000 | 3       | 4628 |
| 0.5000012797791884 | 480000000 | 4       | 4722 |
| 0.799999575178732  | 600000000 | 5       | 4832 |
| 0.6666798002776743 | 720000000 | 6       | 5034 |
| 1.3370471294957895 | 960000000 | 8       | 5523 |
| 1.155705549362375  | 1200000000 | 10      | 7130 |
| 1.1498027252724643 | 1440000000 | 12      | 8146 |

- Graphe :
![img.png](documentation/graph/Master_scal_faible.png)

#### Interprétation :

On s'aperçois que plus on augmente les nombre de threads et donc la charge de travaille la courbe se rapproche de plus en plus vers 0. On peut aussi voir que la courbe de Pi.java descent plus vite que les deux autres version.


### En mémoire distribuer

#### Master/Worker.java
- Données :

| Error        | Ntot      | Threads | Duration |
|--------------|-----------|---------|----------|
|  | 120000000 | 1       |   |
|  | 120000000 | 2       |   |
|  | 120000000 | 3       |   |
|  | 120000000 | 4       |   |
|  | 120000000 | 5       |   |
|  | 120000000 | 6       |   |
|  | 120000000 | 8       |   |
|  | 120000000 | 10      |   |
|  | 120000000 | 12      |   |

- Graphe :

#### Interprétation :


# Conclusion 

Ce projet nous a permis d'explorer différentes stratégies d'optimisation du calcul parallèle et distribué, en appliquant la méthode de Monte Carlo à l'estimation de π. Nous avons mis en évidence les différences entre le multithreading en mémoire partagée et les architectures distribuées, en évaluant leurs performances respectives.

L'étude comparative a montré que le choix de l'implémentation dépend fortement du contexte d'utilisation :

- Pour des systèmes à faible nombre de cœurs, une implémentation simple en multithreading peut suffire.

- Pour des architectures multicœurs avancées, une gestion optimisée des threads améliore considérablement les performances.

- Pour des besoins de calcul massifs, une architecture distribuée devient plus pertinente malgré une complexité accrue.

En conclusion, bien que la méthode de Monte Carlo soit une approche simple et efficace pour estimer π, l'optimisation de son exécution dépend des choix d'architecture et des ressources disponibles. Ce projet nous a ainsi offert une vision approfondie des enjeux du calcul parallèle et distribué dans les environnements informatiques modernes.


# SMTP-Prank

## Description
Ce projet est un laboratoire pour le cours de API de l'HEIG-VD. Ce laboratoire à pour but de prank des utilisateur en leur envoyant des mails forgés.
## Installation et implementation d'un serveur SMTP de test
L'installation d'un serveur SMTP en local permet de faire des tests sans envoyer les mails à d'autres personnes. Cela permet également de voir le contenu du mail généré.
Pour effectuer mes tests de fonctionnements j'ai utilisé MockMock.

Lien : https://github.com/tweakers/MockMock

Cette application fournit une interface web supplémentaire qui permet de simuler une boîte de réception, accessible sur http://localhost:8282. Il est possible de choisir les ports que l'on souhaite utilisé pour l'interface web et pour le serveur SMTP. Par défaut le serveur STMP utilise le port 25, certain OS peuvent empécher le lancement du serveur sur ce port si l'application n'est pas executé par un utilisateur administateur.

## Configuration de l'outils de génération de prank
Pour utiliser cette application, il faut commencer par clone le repository. Ensuite il est possible de modifier les 3 fichiers suivants afin de renseigner les programmes sur quelques informations nécessaire. Si les fichiers ne sont pas modifiés, l'application fonctionnera et enverra des messages sur des addresses non existantes et sur le serveur SMTP mock.
A la base du git se trouve le dossier "config". Il contient les trois différents fichiers de configuration du programme.
### Config.properties 
- smtp.serverAddress : Permet de configurer l'addresse du serveur SMTP. Par défaut localhost est utilisé. 
- smtp.serverPort : Permet de configurer le port d'écoute du serveur STMP. Par défaut il utilise le port 25.
- size.group : Permet de définir le nombre de groupe de personne que l'on veut crée. Par défaut le nombre de groupes est défini à 4.
- size.sizeGroup : Permet de définir la taille des groupes de personnes. Par défaut, la taille est définie à 5 personnes (1 sender et 4 victimes)
- files.email : Définit le fichier contenant la liste des emails du prank
- files.message : Définit le fichier contenant la liste des messages du prank
- witnessToCc : permet d'ajouter une addresse en copie aux mails envoyés.
### Message.config
Ce fichier permet de définir le contenu de différents messages qui peuvent être envoyés par l'application. Les messages contiennent un sujet, sont débutées par "M3SSAG3" sont terminés par "3ND".
### Email.config
Ce fichier contient la liste de toutes les addresses mails qui seront utilisées par l'application. Les addresses sont séparées par un retour à la ligne. Il contient actuellement une liste de 19 addresses générées sur internet. 
Une fois les configurations faites, le plus simple et d'utiliser docker.

---------------- TODO --------------------

## Implementation
------------------------ AJOUTER DIAGRAMME CLASSE -----------------------------------
L'application contient 1 package avec plusieurs fichiers gérant le prank. Il contient les fichiers suivants :

- Emails/Messages/Group : Fournis via 3 classes les méthodes permettant de "set" et "get" des attributs aux messages, aux persones et aux groupes. Ces fichiers récupèrent les données via les fichiers de configuration et il permet de représenter les 3 différentes entitées cités juste aux dessus.
- PrankConfig : Va gérer les 3 classes ci-dessus et configurer entièrement le prank pour avoir toutes les données à disposition.
- Prank : Permet de récupérer le contenu d'un message ainsi que la personne qui l'envoie et ceux qui vont le recevoir.



L'application contient également un controleur qui permet de gérer toute la connexion avec le serveur STMP. Il permet d'envoyer les emails aux victimes, un système de log permet d'afficher en console tout ce qui se passe lors des discussions avec le serveur SMTP.

### Discussion avec le serveur SMTP
-------------------------------- TO DO ----------------------------------------

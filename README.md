# Test Ceetiz

## Enoncé de l'exercice
Le ministère des finances vous demande de créer un programme devant permettre de calculer les impôts dus par les entreprises françaises.

Dans un premier temps, ce programme devra gérer 2 types d'entreprise :

A) Les auto-entreprises, qui ont les propriétés suivantes :
- N° SIRET
- Dénomination

B) Les SAS, qui ont les propriétés suivantes :
- N° SIRET
- Dénomination
- Adresse du siège social

Le programme sera étendu par la suite avec d'autres types d'entreprise (SASU, SARL ...)

Par ailleurs, le calcul des impôts devra respecter les règles de gestion suivantes :
- Pour les auto-entreprises : impôts = 25% du CA annuel de l'entreprise
- Pour les SAS : impôts = 33% du CA annuel de l'entreprise

## Choix fonctionnels
L'énoncé de l'exercice est volontairement vague. On veut pouvoir calculer l'impôt de deux types de sociétés. L'énoncé ne précise pas où et comment on récupère les données de la société (base de données, fichier ou autre). Elle ne précise pas quel type d'application on doit développer (Api REST ou application en ligne de commande). Il faut donc en premier lieu définir les choix fait pour cette exercice.

L'application est une API REST qui expose une seule resoource permettant de calculer l'impôt d'une société. Les données des sociétés sont stockées dans un cache mémoire. Dans un cas réel il faudrait bien entendu utiliser une vraie base de données.

## Choix technique
Java 11, Spring Boot 2.2, Spring 5, Architecture reactive et hexagonale

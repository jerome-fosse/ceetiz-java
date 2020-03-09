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
L'énoncé de l'exercice est volontairement vague. On veut pouvoir calculer l'impôt de deux types de sociétés. L'énoncé ne précise pas où et comment on récupère les données de la société (base de données, fichier ou autre). Elle ne précise pas quel type d'application on doit développer (Api REST ou application en ligne de commande). Il faut donc en premier lieu définir les choix fait pour cet exercice.

L'application est une API REST qui expose une seule resoource permettant de calculer l'impôt d'une société. Les données des sociétés sont stockées dans un cache mémoire. Dans un cas réel il faudrait bien entendu utiliser une vraie base de données.

## Choix technique
Java 13, Spring Boot 2.2, Spring 5, Architecture reactive et hexagonale

## Lancement de l'application
Se placer à la racine du projet et faire : 
```
mvn spring-boot:run
```

## Jeu de données
Deux entreprises sont chargées dans le systeme.
- Une entreprise individuelle :
```
{
  "siret": "12345",
  "denomination": "World Company",
  "type": "INDIVIDUELLE"
}
```
- Une entreprise SAS :
```
{
  "siret": "56789",
  "denomination": "World Company",
  "type": "SAS",
  "adresse": {
    "rue": "35 rue Victor Hugo",
    "codePostal": "75001",
    "ville": "Paris"
  }
}
```
Les deux entreprises ont un chiffre d'affaire de 150 000€ en 2018 et 100 000€ en 2019.

## Requetes
- Calcul d'impot pour une entreprise individuelle :
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/impot/calculer --data '{"siret":"12345", "annee":2019}' | jq

{
  "entreprise": {
    "siret": "12345",
    "denomination": "World Company",
    "type": "INDIVIDUELLE"
  },
  "annee": 2019,
  "montant": 25000
}
```

- Calcul d'impot pour une entreprise SAS :
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/impot/calculer --data '{"siret":"56789", "annee":2019}' | jq

{
  "entreprise": {
    "siret": "56789",
    "denomination": "World Company",
    "type": "SAS",
    "adresse": {
      "rue": "35 rue Victor Hugo",
      "codePostal": "75001",
      "ville": "Paris"
    }
  },
  "annee": 2019,
  "montant": 33000
}
```

- Calcul en erreur
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/impot/calculer --data '{"siret":"12345", "annee":2017}' | jq

{
  "timestamp": 1583698200301,
  "path": "/impot/calculer",
  "status": 400,
  "error": "Bad Request",
  "message": "le Chiffre d'affaire 2017 pour l'entreprise Entreprise(siret=12345, denomination=World Company) n'est pas disponible",
  "requestId": "8ef79035-9"
}
```

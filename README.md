# BadWallet API & Payment Service

Projet réalisé dans le cadre de l'**Examen de Design Pattern**.

Le projet simule un service de portefeuille électronique (mobile money) basé sur une architecture **microservices** :

- **`badwallet-api`** (port `8080`) — gestion des portefeuilles, des transactions (dépôt, retrait, transfert) et des paiements de factures
- **`payment-service`** (port `8081`) — service externe simulant la gestion des factures (ISM, WOYAFAL)

`badwallet-api` consomme `payment-service` via HTTP — les deux services sont indépendants, avec leur propre base de données en mémoire.

## Stack technique

- Java 17
- Spring Boot 4.0.7 (Web, Data JPA, Validation)
- Maven
- H2 (base de données en mémoire)
- Lombok

## Design patterns utilisés

| Pattern | Où | Pourquoi |
|---|---|---|
| **Builder** | `Wallet`, `Transaction`, `Facture` (via Lombok `@Builder`) | Construction lisible des entités avec de nombreux champs |
| **Strategy** | `DepositStrategy` / `CreditCardDepositStrategy` / `WalletTargetDepositStrategy` / `DepositStrategyFactory` | Appliquer dynamiquement la bonne logique de dépôt selon la méthode de paiement (`CREDIT_CARD`, `WALLET_TARGET`) |
| **Factory** | `FactureServiceFactory` (badwallet-api) | Résoudre/valider le type de service de facture (`ISM`, `WOYAFAL`) avant l'appel externe |
| **Proxy** | `PaymentServiceProxy` (implémente `FactureApiClient`) | Encapsule tous les appels HTTP vers `payment-service`, isole le reste du code de la communication réseau |

## Lancer le projet

Les deux services doivent être lancés séparément (deux terminaux) ; `payment-service` doit être démarré pour que les paiements de factures fonctionnent.

```bash
# Terminal 1
cd payment-service
./mvnw spring-boot:run
# démarre sur http://localhost:8081

# Terminal 2
cd badwallet-api
./mvnw spring-boot:run
# démarre sur http://localhost:8080
```

`payment-service` génère automatiquement des données de test au démarrage (`FactureSeeder`) : 10 portefeuilles (`WLT-0000001` à `WLT-0000010`), chacun avec 3 factures ISM et 2 factures WOYAFAL impayées.

> ⚠️ Pour qu'un paiement de facture fonctionne, le `code` du wallet créé dans `badwallet-api` doit correspondre à un `walletCode` connu de `payment-service` (ex: `WLT-0000003`).

## Endpoints — `badwallet-api` (port 8080)

### Wallets

| Méthode | URL | Description |
|---|---|---|
| `POST` | `/api/wallets` | Créer un portefeuille |
| `GET` | `/api/wallets?page=&size=` | Lister les portefeuilles (paginé) |
| `GET` | `/api/wallets/{phone}` | Consulter un portefeuille par téléphone |
| `GET` | `/api/wallets/{phone}/balance` | Consulter le solde |
| `POST` | `/api/wallets/seed?numWallets=&eventsPerWallet=` | Générer des portefeuilles et transactions factices (asynchrone) |

### Transactions

| Méthode | URL | Description |
|---|---|---|
| `POST` | `/api/wallets/transactions/{id}/deposit` | Effectuer un dépôt (`CREDIT_CARD` ou `WALLET_TARGET`) |
| `POST` | `/api/wallets/transactions/withdraw` | Effectuer un retrait (frais de 1%, plafonnés à 5000 CFA) |
| `POST` | `/api/wallets/transactions/transfer` | Transférer entre deux portefeuilles |
| `GET` | `/api/wallets/transactions/{phone}/history` | Consulter l'historique des transactions |

### Paiements de factures

| Méthode | URL | Description |
|---|---|---|
| `POST` | `/api/wallets/payments/pay` | Payer toutes les factures impayées du mois en cours pour un service (ISM/WOYAFAL) |
| `POST` | `/api/wallets/payments/pay-factures` | Payer des factures spécifiques par référence |

### Proxy vers `payment-service`

| Méthode | URL | Description |
|---|---|---|
| `GET` | `/api/external/factures/{walletCode}/current?unite=` | Factures impayées du mois en cours (filtre optionnel par unité) |
| `GET` | `/api/external/factures/{walletCode}/periode?debut=&fin=` | Factures impayées sur une période |

> ℹ️ Les routes de transactions et de paiements sont préfixées par `/transactions` et `/payments`.

## Endpoints — `payment-service` (port 8081)

| Méthode | URL | Description |
|---|---|---|
| `GET` | `/api/factures/{walletCode}/current?unite=` | Factures impayées du mois en cours |
| `GET` | `/api/factures/{walletCode}/periode?debut=&fin=` | Factures impayées sur une période |
| `POST` | `/api/factures/pay` | Marquer comme payées toutes les factures impayées d'un service pour un wallet |
| `POST` | `/api/factures/pay-factures` | Marquer comme payées des factures spécifiques par référence |

## Stratégie Git

Workflow **GitFlow simplifié** :

- `main` — code stable, livrable
- `develop` — branche d'intégration
- `feature/*` — une branche par endpoint ou groupe logique d'endpoints, créée depuis `develop`, fusionnée dans `develop` via Pull Request

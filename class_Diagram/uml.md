# Crime Reporting System — UML Diagrams

A full set of UML diagrams covering the domain model, design patterns, and layered architecture of the application.


## 1. Application navigation flow

```mermaid
flowchart TD
    A["HelloApplication\n&lt;&lt;JavaFX&gt;&gt;"] --> B[RoleSelection]
    B --> C[Login]
    B --> D[UserDashboard]
    B --> E[AdminDashboard]

    D --> F["Crime\n&lt;&lt;abstract&gt;&gt;"]
    F --> G[Theft]
    F --> H[Fraud]
    F --> I[Robbery]

    C --> J[UserService]
    J --> K[UserDAO]
    K --> L[DatabaseConnection]
    L --> M[(SQLite)]
```

---

---

## 2. Crime class hierarchy + Factory pattern

```mermaid
classDiagram
    class Crime {
        <<abstract>>
        +absMethod()
        +buildForm()
        +validateCommonFields()
    }
    class Theft
    class Robbery
    class Fraud
    class CrimeFactory {
        +createCrime(type)
    }

    Crime <|-- Theft
    Crime <|-- Robbery
    Crime <|-- Fraud
    CrimeFactory ..> Crime : creates
```

`Theft`, `Robbery`, and `Fraud` all extend the abstract `Crime` class. `CrimeFactory` encapsulates the logic for instantiating the correct concrete subclass (the **Factory Method** pattern).

---
## 3. Strategy pattern — investigator assignment

```mermaid
classDiagram
    class InvestigatorAssignmentStrategy {
        <<interface>>
        +assignInvestigator()
    }
    class LocationBasedAssignment {
        +assignInvestigator()
    }
    class SpecializationBasedAssignment {
        +assignInvestigator()
    }
    class InvestigationService {
        -strategy : InvestigatorAssignmentStrategy
        +assignInvestigator()
    }

    InvestigatorAssignmentStrategy <|.. LocationBasedAssignment
    InvestigatorAssignmentStrategy <|.. SpecializationBasedAssignment
    InvestigationService o-- InvestigatorAssignmentStrategy : uses
```

`InvestigationService` holds a reference to an `InvestigatorAssignmentStrategy` and delegates assignment logic to whichever concrete strategy (`LocationBasedAssignment` or `SpecializationBasedAssignment`) is plugged in at runtime — the **Strategy** pattern.

---
## 4. State pattern — case lifecycle

```mermaid
classDiagram
    class CaseState {
        <<interface>>
        +handleCase()
        +getStatus()
    }
    class SubmittedState
    class UnderReviewState
    class AssignedState
    class UnderInvestigationState
    class ResolvedState
    class CaseContext {
        -state : CaseState
        +setState()
        +handleCase()
    }

    CaseState <|.. SubmittedState
    CaseState <|.. UnderReviewState
    CaseState <|.. AssignedState
    CaseState <|.. UnderInvestigationState
    CaseState <|.. ResolvedState
    CaseContext o-- CaseState : current state
```
## 5. Observer pattern — case notifications

```mermaid
classDiagram
    class CaseContext {
        <<Subject>>
        -observers : List~CaseObserver~
        +attach()
        +detach()
        +notifyObservers()
    }
    class CaseObserver {
        <<Observer>>
        +update()
    }
    class AdminNotification {
        +update()
    }
    class InvestigatorNotification {
        +update()
    }

    CaseContext ..> CaseObserver : notifies
    CaseObserver <|.. AdminNotification
    CaseObserver <|.. InvestigatorNotification
```

`CaseContext` doubles as the **Subject**: it maintains a list of `CaseObserver`s and notifies them on state changes. `AdminNotification` and `InvestigatorNotification` are concrete observers that react via `update()` — the **Observer** pattern.

---

## 6. Layered architecture — Service / DAO / Database

```mermaid
flowchart TD
    UI[JavaFX UI] --> SVC

    subgraph SVC[Service layer]
        direction TB
        S1[UserService]
        S2[CrimeReportService]
        S3[InvestigationService]
    end

    SVC --> DAO

    subgraph DAO[DAO layer]
        direction TB
        D1[UserDAO]
        D2[CrimeReportDAO]
        D3[InvestigatorDAO]
        D4[InvestigationDAO]
        D5[EvidenceDAO]
    end

    DAO --> DC[DatabaseConnection]
    DC --> DB[(SQLite)]
```

---

## 7. Full application architecture overview

```mermaid
flowchart TD
    App[HelloApplication] --> UI

    subgraph UI[JavaFX UI]
        direction TB
        U1[Login]
        U2[SignUp]
        U3[UserDashboard]
        U4[AdminDashboard]
        U5[InvestigatorDashboard]
    end

    UI --> SVC

    subgraph SVC[Service layer]
        direction TB
        S1[UserService]
        S2[CrimeReportService]
        S3[InvestigationService]
    end

    SVC --> CF[CrimeFactory]
    SVC --> STR[Strategy: Investigator assignment]
    SVC --> ST[State: CaseContext + CaseState]

    CF --> CH[Crime hierarchy]
    ST --> OBS[Observer: Admin / Investigator notifications]

    SVC --> DAO

    subgraph DAO[DAO layer]
        direction TB
        D1[UserDAO]
        D2[CrimeReportDAO]
        D3[InvestigatorDAO]
        D4[InvestigationDAO]
        D5[EvidenceDAO]
    end

    DAO --> DC[DatabaseConnection]
    DC --> DB[(SQLite)]
```

---

## Design pattern summary

| Pattern | Participants | Purpose |
|---|---|---|
| **Factory Method** | `CrimeFactory`, `Crime`, `Theft`, `Fraud`, `Robbery` | Centralizes creation of the correct `Crime` subclass |
| **Strategy** | `InvestigatorAssignmentStrategy`, `LocationBasedAssignment`, `SpecializationBasedAssignment`, `InvestigationService` | Swaps investigator-assignment logic at runtime |
| **State** | `CaseState`, `SubmittedState`, `UnderReviewState`, `AssignedState`, `UnderInvestigationState`, `ResolvedState`, `CaseContext` | Encapsulates case-status behavior per lifecycle stage |
| **Observer** | `CaseContext` (Subject), `CaseObserver`, `AdminNotification`, `InvestigatorNotification` | Notifies interested parties when a case's state changes |

## Layer summary

| Layer | Components |
|---|---|
| Presentation (JavaFX UI) | `Login`, `SignUp`, `UserDashboard`, `AdminDashboard`, `InvestigatorDashboard` |
| Service | `UserService`, `CrimeReportService`, `InvestigationService` |
| Data Access (DAO) | `UserDAO`, `CrimeReportDAO`, `InvestigatorDAO`, `InvestigationDAO`, `EvidenceDAO` |
| Infrastructure | `DatabaseConnection` → `SQLite` |

## Notes

- Diagram 4 adds an inferred state-transition sequence since the original ASCII only grouped the states visually without arrows/labels between them — verify against your actual state-transition logic and adjust if needed.
- `CaseContext` appears twice in the source diagrams (once for State, once for Observer/Subject); it's shown as a single unified class in diagrams 4 and 5 since it plays both roles.

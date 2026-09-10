# 10. Technical Documentation

## 10.1 Project Title

**Crime Management and Reporting System**

**Project Type:** Desktop application

**Technologies**

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| JavaFX 17 | Graphical User Interface |
| Maven | Build / dependency management |
| SQLite | Persistent database |
| JDBC | Database connectivity |
| GitHub | Version control |
| JUnit 5 | Testing |

---

## 10.2 System Objective

The system provides a desktop-based platform for managing crime reports and investigations. Users can submit crime reports, while authorized personnel review reports, assign investigators, manage investigations, maintain evidence records, update case statuses, and monitor case progress.

---

## 10.3 Main Users

| Role | Capabilities |
|---|---|
| **General User** | Register, login, submit crime report, select crime type, enter crime information, view submitted reports, track case status |
| **Administrator** | View crime reports, review cases, manage investigators, assign investigators, monitor case status, view system information |
| **Investigator** | View assigned cases, investigate cases, add findings, manage evidence, update investigation status |

---

## 10.4 Major Functional Modules

```mermaid
flowchart TD
    CMS[Crime Management System]

    CMS --> AUTH[Authentication]
    AUTH --> AUTH1[Login]
    AUTH --> AUTH2[Registration]

    CMS --> CR[Crime Reporting]
    CR --> CR1[Select Crime Type]
    CR --> CR2[Submit Report]
    CR --> CR3[View History]

    CMS --> CM[Case Management]
    CM --> CM1[Review Case]
    CM --> CM2[Assign Investigator]
    CM --> CM3[Update Status]

    CMS --> INV[Investigation]
    INV --> INV1[Investigation Details]
    INV --> INV2[Findings]
    INV --> INV3[Evidence]

    CMS --> RPT[Reporting / Search]
    RPT --> RPT1[Search Cases]
    RPT --> RPT2[Case History]
    RPT --> RPT3[Investigation Information]
```

---

## 10.5 Design Patterns Used

| Pattern | Where Used | Problem Solved |
|---|---|---|
| Factory | Crime creation | Creates different crime types centrally |
| Strategy | Investigator assignment | Supports multiple assignment algorithms |
| State | Case status | Handles different case states |
| Observer | Case notifications | Notifies interested users of changes |
| DAO | Database access | Separates SQL from business logic |
| Service Layer | Business operations | Separates business logic from UI |

Design patterns operate mainly around the business/domain layer.

---

## 10.6 Main Workflow 1 — Crime Reporting

```mermaid
flowchart TD
    A[User] --> B[Login]
    B --> C[User Dashboard]
    C --> D[Select Crime Type]
    D --> E[CrimeFactory]
    E --> F[Create Crime Object]
    F --> G[Fill Crime Information]
    G --> H[Validation]
    H --> I[CrimeReportService]
    I --> J[CrimeReportDAO]
    J --> K[(SQLite Database)]
    K --> L[Report Submitted]
    L --> M["Case Status = SUBMITTED"]
```

---

## 10.7 Main Workflow 2 — Investigation

```mermaid
flowchart TD
    A[Admin] --> B[Review Crime Report]
    B --> C[Select Investigator]
    C --> D[Assignment Strategy]
    D --> E[Investigator Assigned]
    E --> F["Case State = ASSIGNED"]
    F --> G[Investigator Opens Case]
    G --> H["Case State = UNDER_INVESTIGATION"]
    H --> I[Add Evidence]
    I --> J[Add Findings]
    J --> K[Complete Investigation]
    K --> L["Case State = RESOLVED"]
```

---

## 10.8 Database Tables

```mermaid
erDiagram
    users {
        int user_id PK
        string name
        string email
        string password
        string role
    }
    crime_reports {
        int report_id PK
        int user_id FK
        string crime_type
        string location
        date report_date
        time report_time
        string description
        string status
    }
    investigators {
        int investigator_id PK
        string name
        string specialization
        string division
        string phone
    }
    investigations {
        int investigation_id PK
        int report_id FK
        int investigator_id FK
        date start_date
        date end_date
        string findings
    }
    evidence {
        int evidence_id PK
        int report_id FK
        string type
        string description
        string file_path
    }
    case_status_history {
        int history_id PK
        int report_id FK
        string old_status
        string new_status
        datetime changed_at
    }

    users ||--o{ crime_reports : submits
    crime_reports ||--o{ investigations : has
    crime_reports ||--o{ evidence : has
    crime_reports ||--o{ case_status_history : logs
    investigators ||--o{ investigations : conducts
```

---

## 10.9 Architecture

```mermaid
flowchart TD
    A[Presentation Layer<br/>JavaFX UI] --> B[Service Layer<br/>Business Logic / Workflow]
    B --> C[DAO Layer<br/>Database Operations]
    C --> D[Database Connection<br/>JDBC]
    D --> E[(SQLite Database)]
```

Design patterns operate mainly around the business/domain layer (Service Layer).

 ER Diagram

## Entity-Relationship Diagram

```mermaid
erDiagram
    USERS ||--o{ CRIME_REPORTS : files
    CRIME_REPORTS ||--o{ EVIDENCE : has
    CRIME_REPORTS ||--o{ INVESTIGATIONS : triggers
    CRIME_REPORTS ||--o{ CASE_STATUS_HISTORY : logs
    INVESTIGATORS ||--o{ INVESTIGATIONS : conducts

    USERS {
        int user_id PK
        string name
        string email
        string password
        string role
    }
    CRIME_REPORTS {
        int report_id PK
        int user_id FK
        string crime_type
        string location
        date report_date
        time report_time
        string description
        string status
    }
    EVIDENCE {
        int evidence_id PK
        int report_id FK
        string type
        string description
        string file_path
    }
    INVESTIGATIONS {
        int investigation_id PK
        int report_id FK
        int investigator_id FK
        date start_date
        date end_date
        string findings
    }
    INVESTIGATORS {
        int investigator_id PK
        string name
        string specialization
        string division
        string phone
    }
    CASE_STATUS_HISTORY {
        int history_id PK
        int report_id FK
        string old_status
        string new_status
        datetime changed_at
    }
```

## Entities and Relationships

| Relationship | Cardinality | Description |
|---|---|---|
| USERS → CRIME_REPORTS | 1 : N | A user can file many crime reports |
| CRIME_REPORTS → EVIDENCE | 1 : N | A report can have multiple pieces of evidence |
| CRIME_REPORTS → INVESTIGATIONS | 1 : N | A report can have multiple investigations opened against it |
| CRIME_REPORTS → CASE_STATUS_HISTORY | 1 : N | A report's status changes are logged over time |
| INVESTIGATORS → INVESTIGATIONS | 1 : N | An investigator can be assigned to multiple investigations |

## Notes

- `CRIME_REPORTS.status` holds the **current** status; `CASE_STATUS_HISTORY` is an audit log of every status transition (old → new, with timestamp).
- `EVIDENCE.file_path` implies file storage (e.g. photos, documents) is referenced, not stored directly in the database.
- All foreign keys (`FK`) reference the primary key (`PK`) of their parent entity.

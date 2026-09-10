package org.example.java;

import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:user_data.db";

    // Existing methods...
    public static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "gender TEXT NOT NULL," +
                "division TEXT NOT NULL," +
                "district TEXT NOT NULL," +
                "thana TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "phone TEXT NOT NULL" +
                ")";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createReportsTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "time TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "details TEXT," +
                "user_phone TEXT NOT NULL," +
                "status TEXT DEFAULT 'pending'" +
                ")";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // NEW: Create Money Laundering Table
    public static void createMoneyLaunderingTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS money_laundering_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                // Common complainant information
                "complainant_name TEXT NOT NULL," +
                "father_name TEXT," +
                "mother_name TEXT," +
                "complainant_phone TEXT NOT NULL," +
                "nid_bc TEXT," +
                "location TEXT NOT NULL," +
                "incident_date TEXT NOT NULL," +
                "incident_time TEXT NOT NULL," +
                "description TEXT," +

                // Financial Information
                "total_amount TEXT NOT NULL," +
                "currency_type TEXT," +

                // Source of Funds (stored as comma-separated values)
                "fund_sources TEXT NOT NULL," +
                "other_source_details TEXT," +

                // Account and Institution Information
                "bank_details TEXT," +
                "crypto_wallets TEXT," +
                "jurisdictions TEXT," +

                // Entities and Intermediaries
                "shell_companies TEXT," +
                "beneficial_owners TEXT," +

                // Transaction Patterns
                "start_date TEXT," +
                "end_date TEXT," +
                "transaction_frequency TEXT," +
                "transaction_details TEXT," +

                // Evidence (stored as comma-separated values)
                "available_evidence TEXT," +
                "evidence_description TEXT," +

                // Law Enforcement Actions
                "asset_freeze_recommendation TEXT," +
                "subpoena_recommendation TEXT," +
                "requested_actions TEXT," +

                // Additional Information
                "reported_elsewhere TEXT," +
                "urgency_level TEXT," +
                "additional_info TEXT," +

                // System fields
                "report_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'pending'," +

                // Foreign key to users table
                "FOREIGN KEY (complainant_phone) REFERENCES users(phone)" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Money laundering reports table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating money laundering table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Insert Money Laundering Report
    public static boolean insertMoneyLaunderingReport(
            // Common fields
            String complainantName, String fatherName, String motherName,
            String complainantPhone, String nidBc, String location,
            String incidentDate, String incidentTime, String description,

            // Financial information
            String totalAmount, String currencyType,

            // Fund sources
            String fundSources, String otherSourceDetails,

            // Account information
            String bankDetails, String cryptoWallets, String jurisdictions,

            // Entities
            String shellCompanies, String beneficialOwners,

            // Transaction patterns
            String startDate, String endDate, String transactionFrequency, String transactionDetails,

            // Evidence
            String availableEvidence, String evidenceDescription,

            // Law enforcement
            String assetFreezeRecommendation, String subpoenaRecommendation, String requestedActions,

            // Additional info
            String reportedElsewhere, String urgencyLevel, String additionalInfo
    ) {
        String sql = "INSERT INTO money_laundering_reports (" +
                "complainant_name, father_name, mother_name, complainant_phone, nid_bc, " +
                "location, incident_date, incident_time, description, " +
                "total_amount, currency_type, fund_sources, other_source_details, " +
                "bank_details, crypto_wallets, jurisdictions, " +
                "shell_companies, beneficial_owners, " +
                "start_date, end_date, transaction_frequency, transaction_details, " +
                "available_evidence, evidence_description, " +
                "asset_freeze_recommendation, subpoena_recommendation, requested_actions, " +
                "reported_elsewhere, urgency_level, additional_info" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setString(1, complainantName);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, motherName);
            pstmt.setString(4, complainantPhone);
            pstmt.setString(5, nidBc);
            pstmt.setString(6, location);
            pstmt.setString(7, incidentDate);
            pstmt.setString(8, incidentTime);
            pstmt.setString(9, description);
            pstmt.setString(10, totalAmount);
            pstmt.setString(11, currencyType);
            pstmt.setString(12, fundSources);
            pstmt.setString(13, otherSourceDetails);
            pstmt.setString(14, bankDetails);
            pstmt.setString(15, cryptoWallets);
            pstmt.setString(16, jurisdictions);
            pstmt.setString(17, shellCompanies);
            pstmt.setString(18, beneficialOwners);
            pstmt.setString(19, startDate);
            pstmt.setString(20, endDate);
            pstmt.setString(21, transactionFrequency);
            pstmt.setString(22, transactionDetails);
            pstmt.setString(23, availableEvidence);
            pstmt.setString(24, evidenceDescription);
            pstmt.setString(25, assetFreezeRecommendation);
            pstmt.setString(26, subpoenaRecommendation);
            pstmt.setString(27, requestedActions);
            pstmt.setString(28, reportedElsewhere);
            pstmt.setString(29, urgencyLevel);
            pstmt.setString(30, additionalInfo);

            int result = pstmt.executeUpdate();
            System.out.println("Money laundering report inserted successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting money laundering report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // NEW: Get Money Laundering Reports by Phone


    // NEW: Get All Money Laundering Reports
    public static ResultSet getAllMoneyLaunderingReports() {
        String sql = "SELECT * FROM money_laundering_reports ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error retrieving all money laundering reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Update Money Laundering Report Status
    public static boolean updateMoneyLaunderingReportStatus(int reportId, String status) {
        String sql = "UPDATE money_laundering_reports SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating report status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void createFraudTableIfNoExists() {
        String sql = "CREATE TABLE IF NOT EXISTS fraud_reports(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "complainant_name TEXT NOT NULL," +
                "father_name TEXT," +
                "mother_name TEXT," +
                "complainant_phone TEXT NOT NULL," +
                "nid_bc TEXT," +
                "location TEXT," +
                "incident_date TEXT NOT NULL," +
                "description_of_incident TEXT," +
                "accused_name TEXT," +
                "accused_phone TEXT," +
                "accused_email TEXT," +
                "accused_address TEXT," +
                "type_of_fraud TEXT NOT NULL," +
                "mode_of_communication TEXT NOT NULL," +
                "transaction_amount TEXT," +
                "transaction_method TEXT," +
                "supporting_documents TEXT," +
                "has_accused_promised_to_return TEXT," +
                "action_requested_from_police TEXT," +
                "report_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'pending'," +
                "FOREIGN KEY (complainant_phone) REFERENCES users(phone)" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Fraud reports table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating fraud table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean insertFraudReport(
            String complainantName, String fatherName, String motherName,
            String complainantPhone, String nidBc, String location,
            String incidentDate, String description,
            String accusedName, String accusedPhone, String accusedEmail, String accusedAddress,
            String fraudType, String modeOfCommunication, String transactionAmount, String transactionMethod,
            String supportingDocuments, String hasAccusedPromisedToReturn,
            String actionRequestedFromPolice
    ) {
        String sql = "INSERT INTO fraud_reports (" +
                "complainant_name, father_name, mother_name, complainant_phone, nid_bc, " +
                "location, incident_date, description_of_incident, " +
                "accused_name, accused_phone, accused_email, accused_address, " +
                "type_of_fraud, mode_of_communication, transaction_amount, transaction_method, " +
                "supporting_documents, has_accused_promised_to_return, action_requested_from_police" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, complainantName);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, motherName);
            pstmt.setString(4, complainantPhone);
            pstmt.setString(5, nidBc);
            pstmt.setString(6, location);
            pstmt.setString(7, incidentDate);
            pstmt.setString(8, description);
            pstmt.setString(9, accusedName);
            pstmt.setString(10, accusedPhone);
            pstmt.setString(11, accusedEmail);
            pstmt.setString(12, accusedAddress);
            pstmt.setString(13, fraudType);
            pstmt.setString(14, modeOfCommunication);
            pstmt.setString(15, transactionAmount);
            pstmt.setString(16, transactionMethod);
            pstmt.setString(17, supportingDocuments);
            pstmt.setString(18, hasAccusedPromisedToReturn);
            pstmt.setString(19, actionRequestedFromPolice);

            int result = pstmt.executeUpdate();
            System.out.println("Fraud report inserted successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting fraud report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getFraudReportsByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM fraud_reports WHERE complainant_phone = ? ORDER BY report_date DESC";
        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, phone);
        return pstmt.executeQuery();
    }

    public static ResultSet getAllFraudReports() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM fraud_reports ORDER BY report_date DESC");
    }

    public static boolean updateFraudReportStatus(int id, String status) {
        String sql = "UPDATE fraud_reports SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Existing methods...
    public static boolean updatePasswordByPhone(String phone, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE phone = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, phone);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(String phone, String password) {
        String sql = "SELECT * FROM users WHERE phone = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertUser(String firstName, String lastName, String gender,
                                  String division, String district, String thana,
                                  String password, String phone) {
        String sql = "INSERT INTO users(first_name, last_name, gender, division, district, thana, password, phone) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, gender);
            pstmt.setString(4, division);
            pstmt.setString(5, district);
            pstmt.setString(6, thana);
            pstmt.setString(7, password);
            pstmt.setString(8, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getReportsByType(String type) {
        String sql = "SELECT * FROM reports WHERE type = ?";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Create Kidnapping Table
    public static void createKidnappingTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS kidnapping_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +

                // Common complainant information
                "complainant_name TEXT NOT NULL," +
                "father_name TEXT," +
                "mother_name TEXT," +
                "complainant_phone TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "incident_date TEXT NOT NULL," +
                "incident_time TEXT NOT NULL," +
                "description TEXT," +
                "nid_bc TEXT," +
                "photo_path TEXT," +

                // Accused Information
                "accused_name TEXT," +
                "accused_phone TEXT," +
                "accused_email TEXT," +
                "accused_address TEXT," +

                // Victim Information
                "victim_name TEXT NOT NULL," +
                "victim_age INTEGER NOT NULL," +
                "victim_gender TEXT NOT NULL," +
                "victim_height TEXT," +
                "victim_clothing TEXT," +
                "victim_marks TEXT," +

                // Last Known Information
                "last_location TEXT NOT NULL," +
                "last_seen_time TEXT NOT NULL," +
                "last_activity TEXT," +

                // Kidnapper Information
                "kidnapper_known TEXT," +
                "kidnapper_description TEXT," +
                "relationship TEXT," +

                // Witness Information
                "witness_available TEXT," +
                "witness_details TEXT," +

                // Emergency Alert
                "amber_alert TEXT," +
                "urgency_level TEXT," +

                // Ransom Information
                "ransom_demand TEXT," +
                "ransom_details TEXT," +
                "ransom_amount TEXT," +

                // Additional Information
                "previous_report TEXT," +
                "motive_money INTEGER DEFAULT 0," +
                "motive_revenge INTEGER DEFAULT 0," +
                "motive_family INTEGER DEFAULT 0," +
                "motive_political INTEGER DEFAULT 0," +
                "motive_unknown INTEGER DEFAULT 0," +
                "motive_other INTEGER DEFAULT 0," +
                "action_request TEXT," +

                // System fields
                "report_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'pending'," +

                // Foreign key to users table
                "FOREIGN KEY (complainant_phone) REFERENCES users(phone)" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Kidnapping reports table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating kidnapping table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean insertKidnappingReport(
            // Common fields
            String complainantName, String fatherName, String motherName,
            String complainantPhone, String location, String incidentDate,
            String incidentTime, String description, String nidBc, String photoPath,

            // Accused information
            String accusedName, String accusedPhone, String accusedEmail, String accusedAddress,

            // Victim information
            String victimName, int victimAge, String victimGender, String victimHeight,
            String victimClothing, String victimMarks,

            // Last known information
            String lastLocation, String lastSeenTime, String lastActivity,

            // Kidnapper information
            String kidnapperKnown, String kidnapperDescription, String relationship,

            // Witness information
            String witnessAvailable, String witnessDetails,

            // Emergency alert
            String amberAlert, String urgencyLevel,

            // Ransom information
            String ransomDemand, String ransomDetails, String ransomAmount,

            // Additional information
            String previousReport, int motiveMoney, int motiveRevenge,
            int motiveFamily, int motivePolitical, int motiveUnknown,
            int motiveOther, String actionRequest
    ) {
        // Ensure table exists before inserting
        createKidnappingTableIfNotExists();

        String sql = "INSERT INTO kidnapping_reports (" +
                "complainant_name, father_name, mother_name, complainant_phone, location, " +
                "incident_date, incident_time, description, nid_bc, photo_path, " +
                "accused_name, accused_phone, accused_email, accused_address, " +
                "victim_name, victim_age, victim_gender, victim_height, victim_clothing, victim_marks, " +
                "last_location, last_seen_time, last_activity, " +
                "kidnapper_known, kidnapper_description, relationship, " +
                "witness_available, witness_details, " +
                "amber_alert, urgency_level, " +
                "ransom_demand, ransom_details, ransom_amount, " +
                "previous_report, motive_money, motive_revenge, motive_family, " +
                "motive_political, motive_unknown, motive_other, action_request" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setString(1, complainantName);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, motherName);
            pstmt.setString(4, complainantPhone);
            pstmt.setString(5, location);
            pstmt.setString(6, incidentDate);
            pstmt.setString(7, incidentTime);
            pstmt.setString(8, description);
            pstmt.setString(9, nidBc);
            pstmt.setString(10, photoPath);

            pstmt.setString(11, accusedName);
            pstmt.setString(12, accusedPhone);
            pstmt.setString(13, accusedEmail);
            pstmt.setString(14, accusedAddress);

            pstmt.setString(15, victimName);
            pstmt.setInt(16, victimAge);
            pstmt.setString(17, victimGender);
            pstmt.setString(18, victimHeight);
            pstmt.setString(19, victimClothing);
            pstmt.setString(20, victimMarks);

            pstmt.setString(21, lastLocation);
            pstmt.setString(22, lastSeenTime);
            pstmt.setString(23, lastActivity);

            pstmt.setString(24, kidnapperKnown);
            pstmt.setString(25, kidnapperDescription);
            pstmt.setString(26, relationship);

            pstmt.setString(27, witnessAvailable);
            pstmt.setString(28, witnessDetails);

            pstmt.setString(29, amberAlert);
            pstmt.setString(30, urgencyLevel);

            pstmt.setString(31, ransomDemand);
            pstmt.setString(32, ransomDetails);
            pstmt.setString(33, ransomAmount);

            pstmt.setString(34, previousReport);
            pstmt.setInt(35, motiveMoney);
            pstmt.setInt(36, motiveRevenge);
            pstmt.setInt(37, motiveFamily);
            pstmt.setInt(38, motivePolitical);
            pstmt.setInt(39, motiveUnknown);
            pstmt.setInt(40, motiveOther);
            pstmt.setString(41, actionRequest);

            int result = pstmt.executeUpdate();
            System.out.println("Kidnapping report inserted successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting kidnapping report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // NEW: Get Kidnapping Reports by Phone
    public static ResultSet getKidnappingReportsByPhone(String phone) {
        String sql = "SELECT * FROM kidnapping_reports WHERE complainant_phone = ? ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error retrieving kidnapping reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Get All Kidnapping Reports
    public static ResultSet getAllKidnappingReports() {
        String sql = "SELECT * FROM kidnapping_reports ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error retrieving all kidnapping reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Get High Priority Kidnapping Cases
    public static ResultSet getHighPriorityKidnappingCases() {
        String sql = "SELECT * FROM kidnapping_reports WHERE urgency_level IN ('Critical - Child under 12', 'Critical - Immediate danger') ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error retrieving high priority cases: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Update Kidnapping Report Status
    public static boolean updateKidnappingReportStatus(int reportId, String status) {
        String sql = "UPDATE kidnapping_reports SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating kidnapping report status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // UPDATED: Create Drug Offense Table (40 columns total including id, report_date, status)
    public static void createDrugOffenseTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS drug_offense_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +

                // Common complainant information
                "complainant_name TEXT NOT NULL," +
                "father_name TEXT," +
                "mother_name TEXT," +
                "complainant_phone TEXT NOT NULL," +
                "nid_bc TEXT," +
                "location TEXT NOT NULL," +
                "incident_date TEXT NOT NULL," +
                "incident_time TEXT NOT NULL," +
                "description TEXT," +
                "photo_path TEXT," +

                // Accused Information
                "accused_name TEXT," +
                "accused_phone TEXT," +
                "accused_email TEXT," +
                "accused_address TEXT," +

                // Drug Information
                "drug_type TEXT NOT NULL," +
                "other_drug_details TEXT," +
                "quantity TEXT NOT NULL," +
                "packaging_type TEXT," +

                // Incident Details
                "incident_types TEXT," +
                "location_type TEXT NOT NULL," +
                "location_details TEXT," +
                "discovery_method TEXT NOT NULL," +
                "discovery_other_details TEXT," +

                // Persons Involved
                "persons_count TEXT," +
                "age_groups TEXT," +
                "genders TEXT," +

                // Drug Activity
                "trafficking_observed TEXT NOT NULL," +
                "money_exchange_witnessed TEXT NOT NULL," +
                "transaction_amount TEXT," +

                // Evidence and Items
                "recovered_items TEXT," +
                "weapons_found TEXT," +

                // Suspicious Activities
                "suspicious_vehicles_persons TEXT," +
                "prior_involvement TEXT NOT NULL," +
                "threats_evidence_destruction TEXT," +

                // Witness Information
                "witness_available TEXT NOT NULL," +
                "witness_name TEXT," +
                "witness_phone TEXT," +
                "witness_relationship TEXT," +

                // Digital Evidence (combined into single field)
                "digital_evidence_available TEXT NOT NULL," +
                "video_evidence_path TEXT," +

                // System fields
                "report_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'pending'," +

                // Foreign key constraint
                "FOREIGN KEY (complainant_phone) REFERENCES users(phone)" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Drug offense reports table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating drug offense table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Insert Drug Offense Report
    public static boolean insertDrugOffenseReport(
            // Common fields (10 parameters)
            String complainantName, String fatherName, String motherName,
            String complainantPhone, String nidBc, String location,
            String incidentDate, String incidentTime, String description, String photoPath,

            // Accused information (4 parameters)
            String accusedName, String accusedPhone, String accusedEmail, String accusedAddress,

            // Drug information (4 parameters)
            String drugType, String otherDrugDetails, String quantity, String packagingType,

            // Incident details (5 parameters)
            String incidentTypes, String locationType, String locationDetails,
            String discoveryMethod, String discoveryOtherDetails,

            // Persons involved (3 parameters)
            String personsCount, String ageGroups, String genders,

            // Drug activity (3 parameters)
            String traffickingObserved, String moneyExchangeWitnessed, String transactionAmount,

            // Evidence and items (2 parameters)
            String recoveredItems, String weaponsFound,

            // Suspicious activities (3 parameters)
            String suspiciousVehiclesPersons, String priorInvolvement, String threatsEvidenceDestruction,

            // Witness information (4 parameters)
            String witnessAvailable, String witnessName, String witnessPhone, String witnessRelationship,

            // Digital evidence (2 parameters) - REDUCED FROM 3 TO 2
            String digitalEvidenceAvailable, String evidenceFilePath // Combined video and photo paths
    ) {
        // Ensure table exists before inserting
        createDrugOffenseTableIfNotExists();

        // SQL with exactly 39 columns (40 values including id but excluding auto-generated fields)
        String sql = "INSERT INTO drug_offense_reports (" +
                "complainant_name, father_name, mother_name, complainant_phone, nid_bc, " +
                "location, incident_date, incident_time, description, photo_path, " +
                "accused_name, accused_phone, accused_email, accused_address, " +
                "drug_type, other_drug_details, quantity, packaging_type, " +
                "incident_types, location_type, location_details, discovery_method, discovery_other_details, " +
                "persons_count, age_groups, genders, " +
                "trafficking_observed, money_exchange_witnessed, transaction_amount, " +
                "recovered_items, weapons_found, " +
                "suspicious_vehicles_persons, prior_involvement, threats_evidence_destruction, " +
                "witness_available, witness_name, witness_phone, witness_relationship, " +
                "digital_evidence_available, video_evidence_path" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set exactly 40 parameters (1-40)
            pstmt.setString(1, complainantName);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, motherName);
            pstmt.setString(4, complainantPhone);
            pstmt.setString(5, nidBc);
            pstmt.setString(6, location);
            pstmt.setString(7, incidentDate);
            pstmt.setString(8, incidentTime);
            pstmt.setString(9, description);
            pstmt.setString(10, photoPath);

            pstmt.setString(11, accusedName);
            pstmt.setString(12, accusedPhone);
            pstmt.setString(13, accusedEmail);
            pstmt.setString(14, accusedAddress);

            pstmt.setString(15, drugType);
            pstmt.setString(16, otherDrugDetails);
            pstmt.setString(17, quantity);
            pstmt.setString(18, packagingType);

            pstmt.setString(19, incidentTypes);
            pstmt.setString(20, locationType);
            pstmt.setString(21, locationDetails);
            pstmt.setString(22, discoveryMethod);
            pstmt.setString(23, discoveryOtherDetails);

            pstmt.setString(24, personsCount);
            pstmt.setString(25, ageGroups);
            pstmt.setString(26, genders);

            pstmt.setString(27, traffickingObserved);
            pstmt.setString(28, moneyExchangeWitnessed);
            pstmt.setString(29, transactionAmount);

            pstmt.setString(30, recoveredItems);
            pstmt.setString(31, weaponsFound);

            pstmt.setString(32, suspiciousVehiclesPersons);
            pstmt.setString(33, priorInvolvement);
            pstmt.setString(34, threatsEvidenceDestruction);

            pstmt.setString(35, witnessAvailable);
            pstmt.setString(36, witnessName);
            pstmt.setString(37, witnessPhone);
            pstmt.setString(38, witnessRelationship);

            pstmt.setString(39, digitalEvidenceAvailable);
            pstmt.setString(40, evidenceFilePath); // Combined evidence file path

            int result = pstmt.executeUpdate();
            System.out.println("Drug offense report inserted successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting drug offense report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // NEW: Get Drug Offense Reports by Phone
    public static ResultSet getDrugOffenseReportsByPhone(String phone) {
        String sql = "SELECT * FROM drug_offense_reports WHERE complainant_phone = ? ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error retrieving drug offense reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Get All Drug Offense Reports
    public static ResultSet getAllDrugOffenseReports() {
        String sql = "SELECT * FROM drug_offense_reports ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error retrieving all drug offense reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Update Drug Offense Report Status
    public static boolean updateDrugOffenseReportStatus(int reportId, String status) {
        String sql = "UPDATE drug_offense_reports SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating drug offense report status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ==================== EXTORTION METHODS ====================

    // NEW: Create Extortion Table
    public static void createExtortionTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS extortion_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +

                // Common complainant information
                "complainant_name TEXT NOT NULL," +
                "father_name TEXT," +
                "mother_name TEXT," +
                "complainant_phone TEXT NOT NULL," +
                "nid_bc TEXT," +
                "location TEXT NOT NULL," +
                "incident_date TEXT NOT NULL," +
                "incident_time TEXT NOT NULL," +
                "description TEXT," +
                "photo_path TEXT," +

                // Extortion specific information
                "extortion_type TEXT NOT NULL," +
                "threat_types TEXT," + // comma-separated values
                "threat_details TEXT," +
                "evidence_file_path TEXT," +

                // Demands information
                "demand_type TEXT," +
                "money_amount TEXT," +
                "demand_details TEXT," +

                // Deadline information
                "has_deadline TEXT," +
                "deadline_date TEXT," +
                "deadline_time TEXT," +

                // Extorter information
                "extorter_known TEXT," +
                "extorter_name TEXT," +
                "extorter_contact TEXT," +
                "extorter_description TEXT," +
                "relationship_to_victim TEXT," +

                // Witness information
                "has_witnesses TEXT," +
                "witness1_details TEXT," +
                "witness2_details TEXT," +

                // Prior history
                "reported_before TEXT," +
                "previous_report_details TEXT," +
                "paid_before TEXT," +
                "payment_details TEXT," +

                // Protection request
                "needs_protection TEXT," +
                "protection_types TEXT," +
                "protection_reason TEXT," +

                // System fields
                "report_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'pending'," +

                // Foreign key
                "FOREIGN KEY (complainant_phone) REFERENCES users(phone)" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Extortion reports table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating extortion table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Insert Extortion Report
    public static boolean insertExtortionReport(
            // Common fields
            String complainantName, String fatherName, String motherName,
            String complainantPhone, String nidBc, String location,
            String incidentDate, String incidentTime, String description, String photoPath,

            // Extortion specific
            String extortionType, String threatTypes, String threatDetails, String evidenceFilePath,

            // Demands
            String demandType, String moneyAmount, String demandDetails,

            // Deadline
            String hasDeadline, String deadlineDate, String deadlineTime,

            // Extorter info
            String extorterKnown, String extorterName, String extorterContact,
            String extorterDescription, String relationshipToVictim,

            // Witnesses
            String hasWitnesses, String witness1Details, String witness2Details,

            // Prior history
            String reportedBefore, String previousReportDetails, String paidBefore, String paymentDetails,

            // Protection
            String needsProtection, String protectionTypes, String protectionReason
    ) {
        // Ensure table exists
        createExtortionTableIfNotExists();

        String sql = "INSERT INTO extortion_reports (" +
                "complainant_name, father_name, mother_name, complainant_phone, nid_bc, " +
                "location, incident_date, incident_time, description, photo_path, " +
                "extortion_type, threat_types, threat_details, evidence_file_path, " +
                "demand_type, money_amount, demand_details, " +
                "has_deadline, deadline_date, deadline_time, " +
                "extorter_known, extorter_name, extorter_contact, extorter_description, relationship_to_victim, " +
                "has_witnesses, witness1_details, witness2_details, " +
                "reported_before, previous_report_details, paid_before, payment_details, " +
                "needs_protection, protection_types, protection_reason" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setString(1, complainantName);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, motherName);
            pstmt.setString(4, complainantPhone);
            pstmt.setString(5, nidBc);
            pstmt.setString(6, location);
            pstmt.setString(7, incidentDate);
            pstmt.setString(8, incidentTime);
            pstmt.setString(9, description);
            pstmt.setString(10, photoPath);

            pstmt.setString(11, extortionType);
            pstmt.setString(12, threatTypes);
            pstmt.setString(13, threatDetails);
            pstmt.setString(14, evidenceFilePath);

            pstmt.setString(15, demandType);
            pstmt.setString(16, moneyAmount);
            pstmt.setString(17, demandDetails);

            pstmt.setString(18, hasDeadline);
            pstmt.setString(19, deadlineDate);
            pstmt.setString(20, deadlineTime);

            pstmt.setString(21, extorterKnown);
            pstmt.setString(22, extorterName);
            pstmt.setString(23, extorterContact);
            pstmt.setString(24, extorterDescription);
            pstmt.setString(25, relationshipToVictim);

            pstmt.setString(26, hasWitnesses);
            pstmt.setString(27, witness1Details);
            pstmt.setString(28, witness2Details);

            pstmt.setString(29, reportedBefore);
            pstmt.setString(30, previousReportDetails);
            pstmt.setString(31, paidBefore);
            pstmt.setString(32, paymentDetails);

            pstmt.setString(33, needsProtection);
            pstmt.setString(34, protectionTypes);
            pstmt.setString(35, protectionReason);

            int result = pstmt.executeUpdate();
            System.out.println("Extortion report inserted successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting extortion report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // NEW: Get Extortion Reports by Phone
    public static ResultSet getExtortionReportsByPhone(String phone) {
        String sql = "SELECT * FROM extortion_reports WHERE complainant_phone = ? ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error retrieving extortion reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Get All Extortion Reports
    public static ResultSet getAllExtortionReports() {
        String sql = "SELECT * FROM extortion_reports ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error retrieving all extortion reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Update Extortion Report Status
    public static boolean updateExtortionReportStatus(int reportId, String status) {
        String sql = "UPDATE extortion_reports SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating extortion report status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update Initialize all tables method - ADD EXTORTION TABLE CREATION

    // ==================== ROBBERY METHODS ====================

    // NEW: Create Robbery Table
    public static void createRobberyTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS robbery_reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +

                // Common complainant information
                "complainant_name TEXT NOT NULL," +
                "father_name TEXT," +
                "mother_name TEXT," +
                "complainant_phone TEXT NOT NULL," +
                "nid_bc TEXT," +
                "location TEXT NOT NULL," +
                "incident_date TEXT NOT NULL," +
                "incident_time TEXT NOT NULL," +
                "description TEXT," +
                "photo_path TEXT," +

                // Robbery specific information
                "robbery_location TEXT NOT NULL," +
                "armed_robbery TEXT NOT NULL," + // "Armed" or "Unarmed"
                "weapon_type TEXT," +
                "number_of_robbers TEXT NOT NULL," +
                "robber_description TEXT NOT NULL," +

                // Incident details
                "robber_actions TEXT," +
                "items_stolen TEXT NOT NULL," +
                "injuries_occurred TEXT," +
                "vehicle_used TEXT," +

                // Witness and evidence
                "witnesses_available TEXT," +
                "suspicious_activities TEXT," +
                "reported_elsewhere TEXT," +
                "cctv_available TEXT," +
                "video_file_path TEXT," +

                // Additional information
                "targeted_or_random TEXT," +
                "threats_received TEXT," +

                // System fields
                "report_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'pending'," +

                // Foreign key
                "FOREIGN KEY (complainant_phone) REFERENCES users(phone)" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Robbery reports table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating robbery table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // NEW: Insert Robbery Report
    public static boolean insertRobberyReport(
            // Common fields
            String complainantName, String fatherName, String motherName,
            String complainantPhone, String nidBc, String location,
            String incidentDate, String incidentTime, String description, String photoPath,

            // Robbery specific
            String robberyLocation, String armedRobbery, String weaponType,
            String numberOfRobbers, String robberDescription,

            // Incident details
            String robberActions, String itemsStolen, String injuriesOccurred, String vehicleUsed,

            // Witness and evidence
            String witnessesAvailable, String suspiciousActivities, String reportedElsewhere,
            String cctvAvailable, String videoFilePath,

            // Additional info
            String targetedOrRandom, String threatsReceived
    ) {
        // Ensure table exists
        createRobberyTableIfNotExists();

        String sql = "INSERT INTO robbery_reports (" +
                "complainant_name, father_name, mother_name, complainant_phone, nid_bc, " +
                "location, incident_date, incident_time, description, photo_path, " +
                "robbery_location, armed_robbery, weapon_type, number_of_robbers, robber_description, " +
                "robber_actions, items_stolen, injuries_occurred, vehicle_used, " +
                "witnesses_available, suspicious_activities, reported_elsewhere, cctv_available, video_file_path, " +
                "targeted_or_random, threats_received" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setString(1, complainantName);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, motherName);
            pstmt.setString(4, complainantPhone);
            pstmt.setString(5, nidBc);
            pstmt.setString(6, location);
            pstmt.setString(7, incidentDate);
            pstmt.setString(8, incidentTime);
            pstmt.setString(9, description);
            pstmt.setString(10, photoPath);

            pstmt.setString(11, robberyLocation);
            pstmt.setString(12, armedRobbery);
            pstmt.setString(13, weaponType);
            pstmt.setString(14, numberOfRobbers);
            pstmt.setString(15, robberDescription);

            pstmt.setString(16, robberActions);
            pstmt.setString(17, itemsStolen);
            pstmt.setString(18, injuriesOccurred);
            pstmt.setString(19, vehicleUsed);

            pstmt.setString(20, witnessesAvailable);
            pstmt.setString(21, suspiciousActivities);
            pstmt.setString(22, reportedElsewhere);
            pstmt.setString(23, cctvAvailable);
            pstmt.setString(24, videoFilePath);

            pstmt.setString(25, targetedOrRandom);
            pstmt.setString(26, threatsReceived);

            int result = pstmt.executeUpdate();
            System.out.println("Robbery report inserted successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting robbery report: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // NEW: Get Robbery Reports by Phone
    public static ResultSet getRobberyReportsByPhone(String phone) {
        String sql = "SELECT * FROM robbery_reports WHERE complainant_phone = ? ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error retrieving robbery reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Get All Robbery Reports
    public static ResultSet getAllRobberyReports() {
        String sql = "SELECT * FROM robbery_reports ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error retrieving all robbery reports: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // NEW: Update Robbery Report Status
    public static boolean updateRobberyReportStatus(int reportId, String status) {
        String sql = "UPDATE robbery_reports SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating robbery report status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update Initialize all tables method - ADD ROBBERY TABLE CREATION
    public static void initializeDatabase() {
        createTableIfNotExists();
        createReportsTableIfNotExists();
        createFraudTableIfNoExists();
        createMoneyLaunderingTableIfNotExists();
        createKidnappingTableIfNotExists();
        createDrugOffenseTableIfNotExists();
        createExtortionTableIfNotExists();
        createRobberyTableIfNotExists(); // ADD THIS LINE
        System.out.println("Database initialized successfully!");
    }

    public static ResultSet getApprovedCasesByTable(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE status = 'approved' OR status = 'in_progress' OR status = 'solved' ORDER BY report_date DESC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error retrieving approved cases from " + tableName + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get case statistics for dashboard
     */
    public static CaseStatistics getCaseStatistics() {
        CaseStatistics stats = new CaseStatistics();

        try {
            // Count fraud cases
            stats.fraudTotal = countCasesByStatus("fraud_reports", null);
            stats.fraudApproved = countCasesByStatus("fraud_reports", "approved");
            stats.fraudInProgress = countCasesByStatus("fraud_reports", "in_progress");
            stats.fraudSolved = countCasesByStatus("fraud_reports", "solved");

            // Count money laundering cases
            stats.mlTotal = countCasesByStatus("money_laundering_reports", null);
            stats.mlApproved = countCasesByStatus("money_laundering_reports", "approved");
            stats.mlInProgress = countCasesByStatus("money_laundering_reports", "in_progress");
            stats.mlSolved = countCasesByStatus("money_laundering_reports", "solved");

            // Count kidnapping cases
            stats.kidnappingTotal = countCasesByStatus("kidnapping_reports", null);
            stats.kidnappingApproved = countCasesByStatus("kidnapping_reports", "approved");
            stats.kidnappingInProgress = countCasesByStatus("kidnapping_reports", "in_progress");
            stats.kidnappingSolved = countCasesByStatus("kidnapping_reports", "solved");

            // Count drug offense cases
            stats.drugTotal = countCasesByStatus("drug_offense_reports", null);
            stats.drugApproved = countCasesByStatus("drug_offense_reports", "approved");
            stats.drugInProgress = countCasesByStatus("drug_offense_reports", "in_progress");
            stats.drugSolved = countCasesByStatus("drug_offense_reports", "solved");

            // Count extortion cases
            stats.extortionTotal = countCasesByStatus("extortion_reports", null);
            stats.extortionApproved = countCasesByStatus("extortion_reports", "approved");
            stats.extortionInProgress = countCasesByStatus("extortion_reports", "in_progress");
            stats.extortionSolved = countCasesByStatus("extortion_reports", "solved");

            // Count robbery cases
            stats.robberyTotal = countCasesByStatus("robbery_reports", null);
            stats.robberyApproved = countCasesByStatus("robbery_reports", "approved");
            stats.robberyInProgress = countCasesByStatus("robbery_reports", "in_progress");
            stats.robberySolved = countCasesByStatus("robbery_reports", "solved");

        } catch (SQLException e) {
            System.err.println("Error getting case statistics: " + e.getMessage());
        }

        return stats;
    }

    /**
     * Helper method to count cases by status
     */
    private static int countCasesByStatus(String tableName, String status) throws SQLException {
        String sql;
        if (status == null) {
            sql = "SELECT COUNT(*) FROM " + tableName;
        } else {
            sql = "SELECT COUNT(*) FROM " + tableName + " WHERE status = ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (status != null) {
                pstmt.setString(1, status);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    /**
     * Search cases by multiple criteria
     */
    public static ResultSet searchCases(String tableName, String complainantName,
                                        String fromDate, String toDate, String location) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE 1=1");

        if (complainantName != null && !complainantName.trim().isEmpty()) {
            sql.append(" AND complainant_name LIKE ?");
        }
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql.append(" AND incident_date >= ?");
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sql.append(" AND incident_date <= ?");
        }
        if (location != null && !location.trim().isEmpty()) {
            sql.append(" AND location LIKE ?");
        }

        sql.append(" ORDER BY report_date DESC");

        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql.toString());

        int paramIndex = 1;
        if (complainantName != null && !complainantName.trim().isEmpty()) {
            pstmt.setString(paramIndex++, "%" + complainantName + "%");
        }
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            pstmt.setString(paramIndex++, fromDate);
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            pstmt.setString(paramIndex++, toDate);
        }
        if (location != null && !location.trim().isEmpty()) {
            pstmt.setString(paramIndex++, "%" + location + "%");
        }

        return pstmt.executeQuery();
    }

    /**
     * Create investigation notes table
     */
    public static void createInvestigationNotesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS investigation_notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "case_id INTEGER NOT NULL," +
                "case_type TEXT NOT NULL," +
                "investigator_name TEXT NOT NULL," +
                "notes TEXT NOT NULL," +
                "created_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "updated_date DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Investigation notes table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating investigation notes table: " + e.getMessage());
        }
    }

    /**
     * Save investigation notes
     */
    public static boolean saveInvestigationNotes(int caseId, String caseType,
                                                 String investigatorName, String notes) {
        String sql = "INSERT INTO investigation_notes (case_id, case_type, investigator_name, notes) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, caseId);
            pstmt.setString(2, caseType);
            pstmt.setString(3, investigatorName);
            pstmt.setString(4, notes);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saving investigation notes: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get investigation notes for a case
     */
    public static ResultSet getInvestigationNotes(int caseId, String caseType) throws SQLException {
        String sql = "SELECT * FROM investigation_notes WHERE case_id = ? AND case_type = ? ORDER BY created_date DESC";

        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, caseId);
        pstmt.setString(2, caseType);

        return pstmt.executeQuery();
    }

    /**
     * Create evidence table
     */
    public static void createEvidenceTable() {
        String sql = "CREATE TABLE IF NOT EXISTS case_evidence (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "case_id INTEGER NOT NULL," +
                "case_type TEXT NOT NULL," +
                "evidence_type TEXT NOT NULL," +
                "evidence_description TEXT NOT NULL," +
                "file_path TEXT," +
                "collected_by TEXT NOT NULL," +
                "collected_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "chain_of_custody TEXT" +
                ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Evidence table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating evidence table: " + e.getMessage());
        }
    }

    /**
     * Add evidence to a case
     */
    public static boolean addEvidence(int caseId, String caseType, String evidenceType,
                                      String description, String filePath, String collectedBy,
                                      String chainOfCustody) {
        String sql = "INSERT INTO case_evidence (case_id, case_type, evidence_type, evidence_description, " +
                "file_path, collected_by, chain_of_custody) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, caseId);
            pstmt.setString(2, caseType);
            pstmt.setString(3, evidenceType);
            pstmt.setString(4, description);
            pstmt.setString(5, filePath);
            pstmt.setString(6, collectedBy);
            pstmt.setString(7, chainOfCustody);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error adding evidence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get evidence for a case
     */
    public static ResultSet getCaseEvidence(int caseId, String caseType) throws SQLException {
        String sql = "SELECT * FROM case_evidence WHERE case_id = ? AND case_type = ? ORDER BY collected_date DESC";

        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, caseId);
        pstmt.setString(2, caseType);

        return pstmt.executeQuery();
    }

    /**
     * Update initializeDatabase method to include new tables
     */
    public static void initializeDatabaseWithInvestigationTables() {
        // Initialize existing tables
        initializeDatabase();

        // Initialize investigation-specific tables
        createInvestigationNotesTable();
        createEvidenceTable();

        System.out.println("Investigation database initialized successfully!");
    }

    /**
     * Case Statistics helper class
     */
    public static class CaseStatistics {
        public int fraudTotal, fraudApproved, fraudInProgress, fraudSolved;
        public int mlTotal, mlApproved, mlInProgress, mlSolved;
        public int kidnappingTotal, kidnappingApproved, kidnappingInProgress, kidnappingSolved;
        public int drugTotal, drugApproved, drugInProgress, drugSolved;
        public int extortionTotal, extortionApproved, extortionInProgress, extortionSolved;
        public int robberyTotal, robberyApproved, robberyInProgress, robberySolved;

        public int getTotalCases() {
            return fraudTotal + mlTotal + kidnappingTotal + drugTotal + extortionTotal + robberyTotal;
        }

        public int getTotalApproved() {
            return fraudApproved + mlApproved + kidnappingApproved + drugApproved + extortionApproved + robberyApproved;
        }

        public int getTotalInProgress() {
            return fraudInProgress + mlInProgress + kidnappingInProgress + drugInProgress + extortionInProgress + robberyInProgress;
        }

        public int getTotalSolved() {
            return fraudSolved + mlSolved + kidnappingSolved + drugSolved + extortionSolved + robberySolved;
        }

        // Add this method to DatabaseHelper for Money Laundering reports by phone
        public static ResultSet getMoneyLaunderingReportsByPhone(String phone) {
            String sql = "SELECT * FROM money_laundering_reports WHERE complainant_phone = ? ORDER BY report_date DESC";
            try {
                Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, phone);
                return pstmt.executeQuery();
            } catch (SQLException e) {
                System.err.println("Error retrieving money laundering reports: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

}

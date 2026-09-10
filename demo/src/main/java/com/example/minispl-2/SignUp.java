package org.example.java;

import com.example.oopproject.service.UserService;
import com.example.oopproject.model.User;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class SignUp extends Application {

    // Data structures for Bangladesh administrative divisions
    private Map<String, String[]> divisionToDistricts = new HashMap<>();
    private Map<String, String[]> districtToThanas = new HashMap<>();

    // Form controls
    private TextField firstNameField;
    private TextField lastNameField;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;
    private RadioButton otherRadio;
    private ToggleGroup genderGroup;
    private ComboBox<String> divisionCombo;
    private ComboBox<String> districtCombo;
    private ComboBox<String> thanaCombo;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button signUpButton;
    private Button clearButton;
    private TextField phoneField;



    @Override
    public void start(Stage primaryStage) {
        DatabaseHelper.createTableIfNotExists();
        initializeLocationData();

        primaryStage.setTitle("Safe Bangla Sign Up Portal");

        // Create animated background
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Create gradient background
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(0.5, Color.web("#764ba2")),
                new Stop(1, Color.web("#f093fb"))
        );

        Background background = new Background(new BackgroundFill(backgroundGradient, null, null));
        root.setBackground(background);

        // Create main form container
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setMaxWidth(600);

        // Glass-morphism effect for the form
        mainContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-border-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );

        // Title with animation
        Label titleLabel = new Label("✨ Create Your Account ✨");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(new Glow(0.8));

        // Add pulsing animation to title
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> titleLabel.setScaleX(1.0)),
                new KeyFrame(Duration.seconds(0), e -> titleLabel.setScaleY(1.0)),
                new KeyFrame(Duration.seconds(1), e -> titleLabel.setScaleX(1.05)),
                new KeyFrame(Duration.seconds(1), e -> titleLabel.setScaleY(1.05)),
                new KeyFrame(Duration.seconds(2), e -> titleLabel.setScaleX(1.0)),
                new KeyFrame(Duration.seconds(2), e -> titleLabel.setScaleY(1.0))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.play();

        // Create form grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        // Personal Information Section
        Label personalInfoLabel = new Label("👤 Personal Information");
        personalInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        personalInfoLabel.setTextFill(Color.web("#FF6B6B"));
        grid.add(personalInfoLabel, 0, 0, 2, 1);

        // First Name with custom styling
        Label firstNameLabel = createStyledLabel("First Name:");
        firstNameField = createStyledTextField("Enter your first name", "👤");
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);

        // Last Name
        Label lastNameLabel = createStyledLabel("Last Name:");
        lastNameField = createStyledTextField("Enter your last name", "👤");
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameField, 1, 2);



        // Gender with custom radio buttons
        Label genderLabel = createStyledLabel("Gender:");
        genderGroup = new ToggleGroup();
        maleRadio = createStyledRadioButton("♂️ Male", genderGroup);
        femaleRadio = createStyledRadioButton("♀️ Female", genderGroup);
        otherRadio = createStyledRadioButton("⚧️ Other", genderGroup);
        maleRadio.setSelected(true);

        HBox genderBox = new HBox(15);
        genderBox.getChildren().addAll(maleRadio, femaleRadio, otherRadio);
        grid.add(genderLabel, 0, 3);
        grid.add(genderBox, 1, 3);

        // Address Section
        Label addressLabel = new Label("🏠 Address Information");
        addressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        addressLabel.setTextFill(Color.web("#4ECDC4"));
        grid.add(addressLabel, 0, 4, 2, 1);

        // Division
        Label divisionLabel = createStyledLabel("Division:");
        divisionCombo = createStyledComboBox("Select Division", "🏛️");
        populateDivisions();
        grid.add(divisionLabel, 0, 5);
        grid.add(divisionCombo, 1, 5);

        // District
        Label districtLabel = createStyledLabel("District:");
        districtCombo = createStyledComboBox("Select District", "🏘️");
        districtCombo.setDisable(true);
        grid.add(districtLabel, 0, 6);
        grid.add(districtCombo, 1, 6);

        // Thana
        Label thanaLabel = createStyledLabel("Thana:");
        thanaCombo = createStyledComboBox("Select Thana", "🏪");
        thanaCombo.setDisable(true);
        grid.add(thanaLabel, 0, 7);
        grid.add(thanaCombo, 1, 7);

        // Security Section
        Label securityLabel = new Label("🔒 Security Information");
        securityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        securityLabel.setTextFill(Color.web("#FFD93D"));
        grid.add(securityLabel, 0, 8, 2, 1);

        // Password
        Label passwordLabel = createStyledLabel("Password:");
        passwordField = createStyledPasswordField("Enter password", "🔑");
        grid.add(passwordLabel, 0, 9);
        grid.add(passwordField, 1, 9);

        // Confirm Password
        Label confirmPasswordLabel = createStyledLabel("Confirm Password:");
        confirmPasswordField = createStyledPasswordField("Confirm password", "🔒");
        grid.add(confirmPasswordLabel, 0, 10);
        grid.add(confirmPasswordField, 1, 10);

        Label phoneLabel = createStyledLabel("Phone Number:");
        phoneField = createStyledTextField("Enter your phone number", "📞");
        // nice blue color for phone input
        grid.add(phoneLabel, 0, 11);
        grid.add(phoneField, 1, 11);
        // Buttons with hover effects
        signUpButton = createStyledButton("🚀 Sign Up", "#4CAF50", "#45A049");
        clearButton = createStyledButton("🗑️ Clear", "#FF5722", "#E64A19");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(signUpButton, clearButton);

        // Add components to main container
        mainContainer.getChildren().addAll(titleLabel, grid, buttonBox);

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), mainContainer);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Always show vertical scrollbar

        root.getChildren().add(scrollPane);





        // Event handlers
        setupEventHandlers();

        Scene scene = new Scene(root, 800, 750);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);
        label.setEffect(new DropShadow(5, Color.BLACK));
        return label;
    }

    private TextField createStyledTextField(String promptText, String icon) {
        TextField textField = new TextField();
        textField.setPromptText(icon + " " + promptText);
        textField.setPrefWidth(250);
        textField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 13px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );

        // Add focus effect
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle(textField.getStyle() + "-fx-border-color: #e74c3c;");
            } else {
                textField.setStyle(textField.getStyle().replace("-fx-border-color: #e74c3c;", "-fx-border-color: #3498db;"));
            }
        });

        return textField;
    }

    private PasswordField createStyledPasswordField(String promptText, String icon) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(icon + " " + promptText);
        passwordField.setPrefWidth(250);
        passwordField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 13px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );

        // Add focus effect
        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                passwordField.setStyle(passwordField.getStyle() + "-fx-border-color: #e74c3c;");
            } else {
                passwordField.setStyle(passwordField.getStyle().replace("-fx-border-color: #e74c3c;", "-fx-border-color: #3498db;"));
            }
        });

        return passwordField;
    }

    private ComboBox<String> createStyledComboBox(String promptText, String icon) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText(icon + " " + promptText);
        comboBox.setPrefWidth(250);
        comboBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 13px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );

        return comboBox;
    }

    private RadioButton createStyledRadioButton(String text, ToggleGroup group) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(group);
        radioButton.setTextFill(Color.WHITE);
        radioButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        radioButton.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);"
        );
        return radioButton;
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setPrefWidth(140);
        button.setPrefHeight(45);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle(
                "-fx-background-color: " + baseColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
        );

        // Add hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(baseColor, hoverColor));
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();
        });

        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(hoverColor, baseColor));
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });

        return button;
    }

    private void initializeLocationData() {
        // Initialize division to districts mapping
        divisionToDistricts.put("Dhaka", new String[]{"Dhaka", "Faridpur", "Gazipur", "Gopalganj", "Kishoreganj", "Madaripur", "Manikganj", "Munshiganj", "Narayanganj", "Narsingdi", "Rajbari", "Shariatpur", "Tangail"});
        divisionToDistricts.put("Chittagong", new String[]{"Bandarban", "Brahmanbaria", "Chandpur", "Chittagong", "Comilla", "Cox's Bazar", "Feni", "Khagrachhari", "Lakshmipur", "Noakhali", "Rangamati"});
        divisionToDistricts.put("Rajshahi", new String[]{"Bogra", "Joypurhat", "Naogaon", "Natore", "Chapainawabganj", "Pabna", "Rajshahi", "Sirajganj"});
        divisionToDistricts.put("Khulna", new String[]{"Bagerhat", "Chuadanga", "Jessore", "Jhenaidah", "Khulna", "Kushtia", "Magura", "Meherpur", "Narail", "Satkhira"});
        divisionToDistricts.put("Sylhet", new String[]{"Habiganj", "Moulvibazar", "Sunamganj", "Sylhet"});
        divisionToDistricts.put("Barisal", new String[]{"Barguna", "Barisal", "Bhola", "Jhalokati", "Patuakhali", "Pirojpur"});
        divisionToDistricts.put("Rangpur", new String[]{"Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari", "Panchagarh", "Rangpur", "Thakurgaon"});
        divisionToDistricts.put("Mymensingh", new String[]{"Jamalpur", "Mymensingh", "Netrakona", "Sherpur"});

        // Initialize district to thanas mapping (sample data - you can expand this)
        districtToThanas.put("Dhaka", new String[]{"Dhanmondi", "Gulshan", "Ramna", "Tejgaon", "Pallabi", "Shah Ali", "Turag", "Dakshinkhan", "Uttara", "Savar", "Keraniganj", "Dohar", "Nawabganj", "Dhamrai"});
        districtToThanas.put("Chittagong", new String[]{"Kotwali", "Panchlaish", "Double Mooring", "Pahartali", "Bayezid", "Chandgaon", "Karnaphuli", "Banshkhali", "Boalkhali", "Anwara", "Chandanaish", "Satkania", "Lohagara", "Hathazari", "Raozan", "Rangunia", "Sandwip", "Sitakunda", "Mirsharai", "Fatikchhari", "Patiya"});
        districtToThanas.put("Sylhet", new String[]{"Sylhet Sadar", "South Surma", "Balaganj", "Beanibazar", "Bishwanath", "Companiganj", "Dakshin Surma", "Fenchuganj", "Golapganj", "Gowainghat", "Jaintiapur", "Kanaighat", "Osmani Nagar", "Zakiganj"});
        districtToThanas.put("Rajshahi", new String[]{"Boalia", "Matihar", "Rajpara", "Shah Makhdum", "Bagha", "Bagmara", "Charghat", "Durgapur", "Godagari", "Mohanpur", "Paba", "Puthia", "Tanore"});
        districtToThanas.put("Khulna", new String[]{"Khulna Sadar", "Sonadanga", "Khalishpur", "Doulatpur", "Kotwali", "Aranghata", "Batiaghata", "Dacope", "Dumuria", "Dighalia", "Koyra", "Paikgachha", "Phultala", "Rupsa", "Terokhada"});
        districtToThanas.put("Barisal", new String[]{"Barisal Sadar", "Kotwali", "Bakerganj", "Babuganj", "Wazirpur", "Banaripara", "Gournadi", "Agailjhara", "Mehendiganj", "Muladi", "Hizla"});
        districtToThanas.put("Rangpur", new String[]{"Rangpur Sadar", "Kotwali", "Badarganj", "Gangachara", "Kaunia", "Mithapukur", "Pirgachha", "Pirganj", "Taraganj"});
        districtToThanas.put("Mymensingh", new String[]{"Mymensingh Sadar", "Kotwali", "Bhaluka", "Dhobaura", "Fulbaria", "Gaffargaon", "Gouripur", "Haluaghat", "Ishwarganj", "Muktagachha", "Nandail", "Phulpur", "Trishal"});
        districtToThanas.put("Jamalpur",new String[]{"Bokshiganj Thana", " Dewanganj Thana", "Islampur Thana", "Jamalpur Sadar Thana","Madarganj Thana","Melandaha Thana","Sarishabari Thana"});
        districtToThanas.put("Sherpur", new String[]{"Sherpur Sadar Thana","Nalitabari Thana","Sreebardi Thana","Jhenaigati Thana","Nakla Thana"});
        districtToThanas.put("Netrokona", new String[]{"Atpara Thana","Barhatta Thana","Durgapur Thana","Khaliajuri Thana","Kalmakanda Thana","Kendua Thana","Madan Thana","Mohanganj Thana","Netrokona Sadar Thana","Purbadhala Thana"});

        // Add more districts and thanas as needed
        districtToThanas.put("Gazipur", new String[]{"Gazipur Sadar", "Kaliakair", "Kapasia", "Sreepur", "Kaliganj", "Tongi"});
        districtToThanas.put("Narayanganj", new String[]{"Narayanganj Sadar", "Araihazar", "Bandar", "Rupganj", "Sonargaon"});
        districtToThanas.put("Tangail", new String[]{"Tangail Sadar", "Sakhipur", "Basail", "Madhupur", "Ghatail", "Kalihati", "Nagarpur", "Mirzapur", "Gopalpur", "Delduar", "Bhuapur", "Dhanbari"});
        districtToThanas.put("Kishoreganj", new String[]{"Kuliarchar Thana","Hossainpur Thana","Pakundia Thana","Kishoreganj Sadar Thana","Bajitpur Thana","Austagram Thana","Karimganj Thana","Katiadi Thana","Tarail Thana","Itna Thana","Nikli Thana","Mithamain Thana","Bhairab Thana"});
        districtToThanas.put("Faridpur", new String[]{"Alfadanga  ","Bhanga  ","Boalmari  ","Charbhadrasan  ","Faridpur Sadar  ","Madhukhali  ","Nagarkanda  ","Sadarpur  ","Saltha  "});
        districtToThanas.put("Madaripur", new String[]{"Madaripur Sadar  ","Kalkini  ","Rajoir  ","Shibchar  "});
        districtToThanas.put("Manikganj", new String[]{"Daulatpur  ","Ghior  ","Harirampur  ","Manikganj Sadar  ","Saturia  ","Shivalaya  ","Singair  "});  
        districtToThanas.put("Munshiganj", new String[]{"Munshiganj Sadar  ","Sreenagar  ","Sirajdikhan  ","Lohajang  ","Gazaria  ","Tongibari  "});  
        districtToThanas.put("Rajbari", new String[]{"Baliakandi  ","Goalanda  ","Kalukhali  ","Pangsha  ","Rajbari Sadar  "});
        districtToThanas.put("Shariatpur", new String[]{"Shariatpur Sadar  ","Bhedarganj  ","Damudya  ","Gosairhat  ","Naria  ","Zanjira  "});
        districtToThanas.put("Gopalganj",   new String[]{"Gopalganj Sadar   ","Kashiani   ","Kotalipara   ","Muksudpur   ","Tungipara   "});
        districtToThanas.put("Narsingdi",   new String[]{"Narsingdi Sadar   ","Belabo   ","Monohardi   ","Palash   ","Raipura   ","Shibpur   "});

        districtToThanas.put("Bandarban",    new String[]{"Ali Kadam   ","Bandarban Sadar   ","Lama   ","Naikhongchhari   ","Rowangchhari   ","Ruma   ","Thanchi   "});  // 7   s :contentReference[oaicite:0]{index=0}
        districtToThanas.put("Brahmanbaria", new String[]{"Akhaura   ","Bancharampur   ","Brahmanbaria Sadar   ","Kasba   ","Nabinagar   ","Nasirnagar   ","Sarail   ","Ashuganj   "});  // 8   s :contentReference[oaicite:1]{index=1}
        districtToThanas.put("Chandpur",     new String[]{"Chandpur Sadar   ","Faridganj   ","Haimchar   ","Haziganj   ","Kachua   ","Matlab    (North)","Matlab    (South)","Shahrasti   "});  // 8   s :contentReference[oaicite:2]{index=2}
       districtToThanas.put("Comilla",      new String[]{"Barura   ","Brahmanpara   ","Burichong   ","Chandina   ","Chauddagram   ","Daudkandi   ","Debidwar   ","Homna   ","Comilla Sadar Adarsha   ","Comilla Sadar South   ","Laksam   ","Muradnagar   ","Nangalkot   ","Titas   ","Meghna   ","Monoharganj   "});
        districtToThanas.put("Cox's Bazar",  new String[]{"Chakaria   ","Cox’s Bazar Sadar   ","Kutubdia   ","Maheshkhali   ","Ramu   ","Teknaf   ","Ukhia   ","Pekua   "});  // 8   s :contentReference[oaicite:5]{index=5}
        districtToThanas.put("Feni",         new String[]{"Chhagalnaiya   ","Daganbhuiyan   ","Feni Sadar   ","Parshuram   ","Sonagazi   ","Fulgazi   "});  // 6   s :contentReference[oaicite:6]{index=6}
        districtToThanas.put("Khagrachhari", new String[]{"Dighinala   ","Khagrachhari   ","Lakshmichhari   ","Mahalchhari   ","Manikchhari   ","Matiranga   ","Panchhari   ","Ramgarh   "});  // 8   s :contentReference[oaicite:7]{index=7}
        districtToThanas.put("Lakshmipur",   new String[]{"Lakshmipur Sadar   ","Raipur   ","Ramganj   ","Ramgati   "});  // 4   s :contentReference[oaicite:8]{index=8}
        districtToThanas.put("Noakhali",     new String[]{"Begumganj   ","Chatkhil   ","Companiganj   ","Hatiya   ","Senbagh   ","Noakhali Sadar   ","Subarnachar   "});  // 7   s :contentReference[oaicite:9]{index=9}
        districtToThanas.put("Rangamati",    new String[]{"Bagaichhari   ","Barkal   ","Kawkhali (Betbunia)   ","Belaichhari   ","Kaptai   ","Juraichhari   ","Langadu   ","Mannerchar   ","Rajasthali   ","Rangamati Sadar   "});  // 10   s :contentReference[oaicite:10]{index=10}


        districtToThanas.put("Bogra",           new String[]{"Bogra Sadar   ","Adamdighi   ","Dhunat   ","Dhupchanchia   ","Gabtali   ","Kahaloo   ","Nandigram   ","Sariakandi   ","Shajahanpur   ","Sherpur   ","Shibganj   ","Sonatala   "});
        districtToThanas.put("Joypurhat",       new String[]{"Joypurhat Sadar   ","Akkelpur   ","Kalai   ","Khetlal   ","Panchbibi   "});
        districtToThanas.put("Naogaon",         new String[]{"Naogaon Sadar   ","Atrai   ","Badalgachhi   ","Dhamoirhat   ","Manda   ","Mohadevpur   ","Niamatpur   ","Patnitala   ","Porsha   ","Raninagar   ","Sapahar   "});
        districtToThanas.put("Natore",          new String[]{"Natore Sadar   ","Bagatipara   ","Baraigram   ","Gurudaspur   ","Lalpur   ","Naldanga   ","Singra   "});
        districtToThanas.put("Chapai Nawabganj",new String[]{"Chapainawabganj Sadar   ","Gomastapur   ","Nachole   ","Bholahat   ","Shibganj   "});
        districtToThanas.put("Pabna",           new String[]{"Pabna Sadar   ","Atgharia   ","Bera   ","Bhangura   ","Chatmohar   ","Faridpur   ","Ishwardi   ","Santhia   ","Sujanagar   "});
        districtToThanas.put("Sirajganj",        new String[]{"Sirajganj Sadar   ","Kazipur   ","Ullahpara   ","Shahjadpur   ","Raiganj   ","Kamarkhanda   ","Tarash   ","Belkuchi   ","Chauhali   "});


        districtToThanas.put("Bagerhat",   new String[]{"Bagerhat Sadar   ","Chitalmari   ","Fakirhat   ","Kachua   ","Mollahat   ","Mongla   ","Morrelganj   ","Rampal   ","Sarankhola   "});  // :contentReference[oaicite:0]{index=0}
        districtToThanas.put("Chuadanga", new String[]{"Chuadanga Sadar   ","Alamdanga   ","Jibannagar   ","Damurhuda   "});  // :contentReference[oaicite:1]{index=1}
        districtToThanas.put("Jessore",    new String[]{"Abhaynagar   ","Bagherpara   ","Chaugachha   ","Jessore Sadar   ","Jhikargachha   ","Keshabpur   ","Manirampur   ","Sharsha   "});  // :contentReference[oaicite:2]{index=2}
        districtToThanas.put("Jhenaidah",  new String[]{"Jhenaidah Sadar   ","Maheshpur   ","Kaliganj   ","Kotchandpur   ","Shailkupa   ","Harinakunda   "});  // :contentReference[oaicite:3]{index=3}
        districtToThanas.put("Kushtia",    new String[]{"Kushtia Sadar   ","Kumarkhali   ","Khoksa   ","Mirpur   ","Bheramara   ","Daulatpur   "});  // :contentReference[oaicite:4]{index=4}
        districtToThanas.put("Magura",     new String[]{"Magura Sadar   ","Mohammadpur   ","Shalikha   ","Sreepur   "});  // :contentReference[oaicite:5]{index=5}
        districtToThanas.put("Meherpur",   new String[]{"Meherpur Sadar   ","Gangni   ","Mujibnagar   "});  // :contentReference[oaicite:6]{index=6}
        districtToThanas.put("Narail",     new String[]{"Narail Sadar   ","Kalia   ","Lohagara   "});  // :contentReference[oaicite:7]{index=7}
        districtToThanas.put("Satkhira",   new String[]{"Satkhira Sadar   ","Assasuni   ","Debhata   ","Tala   ","Kalaroa   ","Kaliganj   ","Shyamnagar   "});  // :contentReference[oaicite:8]{index=8}


        districtToThanas.put("Habiganj",    new String[]{"Habiganj Sadar   ","Lakhai   ","Madhabpur   ","Nabiganj   ","Chunarughat   ","Baniachang   ","Bahubal   ","Ajmiriganj   "});  // 8   s 
        districtToThanas.put("Moulvibazar", new String[]{"Moulvibazar Sadar   ","Sreemangal   ","Kulaura   ","Kamalganj   ","Juri   ","Barlekha   ","Rajnagar   "});         // 7   s
        districtToThanas.put("Sunamganj",   new String[]{"Sunamganj Sadar   ","Dakshin Sunamganj   ","Chhatak   ","Jagannathpur   ","Bishwamvarpur   ","Tahirpur   ","Derai   ","Dharampasha   ","Dowarabazar   ","Sulla   ","Jamalganj   "});  // 11   s :contentReference[oaicite:2]{index=2}


        districtToThanas.put("Barguna",    new String[]{"Barguna Sadar Upazila","Amtali Upazila","Bamna Upazila","Betagi Upazila","Patharghata Upazila","Taltali Upazila"});        // 6 upazilas :contentReference[oaicite:0]{index=0}
        districtToThanas.put("Bhola",      new String[]{"Bhola Sadar Upazila","Daulatkhan Upazila","Borhanuddin Upazila","Lalmohan Upazila","Tazumuddin Upazila","Manpura Upazila","Charfashion Upazila"}); // 7 upazilas :contentReference[oaicite:1]{index=1}
        districtToThanas.put("Jhalokati",  new String[]{"Jhalokati Sadar Upazila","Kathalia Upazila","Nalchity Upazila","Rajapur Upazila"});                                        // 4 upazilas :contentReference[oaicite:2]{index=2}
        districtToThanas.put("Patuakhali", new String[]{"Patuakhali Sadar Upazila","Dumki Upazila","Dashmina Upazila","Bauphal Upazila","Mirzaganj Upazila","Galachipa Upazila","Kalapara Upazila","Rangabali Upazila"}); // 8 upazilas :contentReference[oaicite:3]{index=3}
        districtToThanas.put("Pirojpur",   new String[]{"Pirojpur Sadar Upazila","Bhandaria Upazila","Mathbaria Upazila","Indurkani Upazila","Nazirpur Upazila","Nesarabad Upazila","Kawkhali Upazila"});                // 7 upazilas :contentReference[oaicite:4]{index=4}

        districtToThanas.put("Dinajpur",   new String[]{"Dinajpur Sadar Upazila","Birampur Upazila","Bochaganj Upazila","Birol Upazila","Chirirbandar Upazila","Ghoraghat Upazila","Hakimpur Upazila","Kaharole Upazila","Khansama Upazila","Parbatipur Upazila"});
        districtToThanas.put("Gaibandha",  new String[]{"Gaibandha Sadar Upazila","Palashbari Upazila","Gobindaganj Upazila","Saghata Upazila","Sadullapur Upazila","Fulchhari Upazila","Sundarganj Upazila"});
        districtToThanas.put("Kurigram",   new String[]{"Kurigram Sadar Upazila","Nageshwari Upazila","Phulbari Upazila","Ulipur Upazila","Chilmari Upazila","Rowmari Upazila","Rajarhat Upazila","Bhurungamari Upazila","Char Rajibpur Upazila"});
        districtToThanas.put("Lalmonirhat",new String[]{"Lalmonirhat Sadar Upazila","Aditmari Upazila","Hatibandha Upazila","Patgram Upazila","Kaliganj Upazila"});
        districtToThanas.put("Nilphamari", new String[]{"Nilphamari Sadar Upazila","Domar Upazila","Jaldhaka Upazila","Dimla Upazila","Saidpur Upazila","Kishoreganj Upazila"});
        districtToThanas.put("Panchagarh", new String[]{"Panchagarh Sadar Upazila","Atwari Upazila","Boda Upazila","Debiganj Upazila","Tetulia Upazila"});
        districtToThanas.put("Thakurgaon", new String[]{"Thakurgaon Sadar Upazila","Baliadangi Upazila","Haripur Upazila","Pirganj Upazila","Ranisankail Upazila"});

    }

    private void populateDivisions() {
        ObservableList<String> divisions = FXCollections.observableArrayList();
        divisions.addAll(divisionToDistricts.keySet());
        divisions.sort(String::compareTo);
        divisionCombo.setItems(divisions);
    }

    private void setupEventHandlers() {
        // Division selection handler
        divisionCombo.setOnAction(e -> {
            String selectedDivision = divisionCombo.getValue();
            if (selectedDivision != null) {
                // Enable district combo and populate it
                districtCombo.setDisable(false);
                districtCombo.getItems().clear();
                thanaCombo.getItems().clear();
                thanaCombo.setDisable(true);

                String[] districts = divisionToDistricts.get(selectedDivision);
                if (districts != null) {
                    ObservableList<String> districtList = FXCollections.observableArrayList();
                    districtList.addAll(Arrays.asList(districts));
                    districtList.sort(String::compareTo);
                    districtCombo.setItems(districtList);
                }

                // Add animation
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), districtCombo);
                fadeIn.setFromValue(0.5);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
        });

        // District selection handler
        districtCombo.setOnAction(e -> {
            String selectedDistrict = districtCombo.getValue();
            if (selectedDistrict != null) {
                // Enable thana combo and populate it
                thanaCombo.setDisable(false);
                thanaCombo.getItems().clear();

                String[] thanas = districtToThanas.get(selectedDistrict);
                if (thanas != null) {
                    ObservableList<String> thanaList = FXCollections.observableArrayList();
                    thanaList.addAll(Arrays.asList(thanas));
                    thanaList.sort(String::compareTo);
                    thanaCombo.setItems(thanaList);
                }

                // Add animation
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), thanaCombo);
                fadeIn.setFromValue(0.5);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
        });

        // Sign Up button handler
        signUpButton.setOnAction(e -> {
            if (validateForm()) {
                showSuccessMessage();
            }
        });

        // Clear button handler
        clearButton.setOnAction(e -> clearForm());
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        // Validate first name
        if (firstNameField.getText().trim().isEmpty()) {
            errors.append("• First name is required\n");
        }

        // Validate last name
        if (lastNameField.getText().trim().isEmpty()) {
            errors.append("• Last name is required\n");
        }

        // Validate address
        if (divisionCombo.getValue() == null) {
            errors.append("• Division is required\n");
        }
        if (districtCombo.getValue() == null) {
            errors.append("• District is required\n");
        }
        if (thanaCombo.getValue() == null) {
            errors.append("• Thana is required\n");
        }

        // Validate password
        if (passwordField.getText().isEmpty()) {
            errors.append("• Password is required\n");
        } else if (passwordField.getText().length() < 6) {
            errors.append("• Password must be at least 6 characters\n");
        }

        // Validate confirm password
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errors.append("• Passwords do not match\n");
        }

        if (errors.length() > 0) {
            showErrorMessage(errors.toString());
            return false;
        }

        return true;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ Validation Error");
        alert.setHeaderText("Please fix the following errors:");
        alert.setContentText(message);

        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff6b6b, #ee5a52);" +
                        "-fx-text-fill: white;"
        );

        alert.showAndWait();
    }

    private void showSuccessMessage() {
        String gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();

        DatabaseHelper.insertUser(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                gender,
                divisionCombo.getValue(),
                districtCombo.getValue(),
                thanaCombo.getValue(),
                passwordField.getText(),
                phoneField.getText()



        );

        String message = String.format(
                "🎉 Account created successfully!\n\n" +
                        "👤 Name: %s %s\n" +
                        "⚧️ Gender: %s\n" +
                        "🏠 Address: %s, %s, %s\n\n" +
                        "🌟 Welcome to our platform!",
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                gender,
                thanaCombo.getValue(),
                districtCombo.getValue(),
                divisionCombo.getValue()
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ Success!");
        alert.setHeaderText("Sign Up Successful!");
        alert.setContentText(message);

        // Style the success alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4CAF50, #45A049);" +
                        "-fx-text-fill: white;"
        );

        alert.showAndWait();

        // Clear form after successful registration
        clearForm();
    }

    private void clearForm() {
        // Add fade-out animation before clearing
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), firstNameField);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.3);
        fadeOut.setOnFinished(e -> {
            firstNameField.clear();
            lastNameField.clear();
            maleRadio.setSelected(true);
            divisionCombo.setValue(null);
            districtCombo.setValue(null);
            districtCombo.setDisable(true);
            thanaCombo.setValue(null);
            thanaCombo.setDisable(true);
            passwordField.clear();
            confirmPasswordField.clear();

            // Fade back in
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), firstNameField);
            fadeIn.setFromValue(0.3);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }




// Design-pattern integration: registration uses the Service Layer.
    private final UserService designPatternUserService = new UserService();

    public int registerWithService(String name, String email,
                                   String password, String role)
            throws java.sql.SQLException {
        return designPatternUserService.register(
                new User(name, email, password, role));
    }
}

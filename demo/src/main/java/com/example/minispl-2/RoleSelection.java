package org.example.java;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class RoleSelection extends Application{


    private Stage primaryStage;

    public void start(Stage primaryStage) {
        // Create main container
        this.primaryStage = primaryStage;
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        // Background gradient
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(1, Color.web("#764ba2"))
        );
        BackgroundFill backgroundFill = new BackgroundFill(backgroundGradient, null, null);
        root.setBackground(new Background(backgroundFill));

        // Main container card
        VBox mainContainer = createMainContainer();
        root.getChildren().add(mainContainer);

        // Add entrance animation
        addEntranceAnimation(mainContainer);

        // Create and show scene
        Scene scene = new Scene(root, 950, 730);
        primaryStage.setTitle("SafeJustice BD - Justice, Anywhere, Anytime");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private VBox createMainContainer() {
        VBox container = new VBox();
        container.setMaxWidth(800);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(0);

        // Rounded corners and shadow effect
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                "-fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);");

        // Create header
        VBox header = createHeader();

        // Create content
        VBox content = createContent();

        // Create footer
        HBox footer = createFooter();

        container.getChildren().addAll(header, content, footer);
        return container;
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30));
        header.setSpacing(10);

        // Header gradient background
        LinearGradient headerGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#2c3e50")),
                new Stop(1, Color.web("#3498db"))
        );
        BackgroundFill headerFill = new BackgroundFill(headerGradient,
                new CornerRadii(20, 20, 0, 0, false), null);
        header.setBackground(new Background(headerFill));

        // Logo
        Label logo = new Label("Online-Case Reporting System");
        logo.setFont(Font.font("System", FontWeight.BOLD, 36));
        logo.setTextFill(Color.WHITE);
        logo.setEffect(new DropShadow(5, Color.BLACK));

        // Motto
        Label motto = new Label("\"Your Complaint, Our Commitment.\"");
        motto.setFont(Font.font("System", FontWeight.NORMAL, 18));
        motto.setTextFill(Color.web("#ecf0f1"));
        motto.setStyle("-fx-font-style: italic;");

        header.getChildren().addAll(logo, motto);
        return header;
    }

    private VBox createContent() {
        VBox content = new VBox();
        content.setPadding(new Insets(40));
        content.setSpacing(30);

        // Role Selection Section
        VBox roleSection = createRoleSection();

        // About Section
        VBox aboutSection = createAboutSection();

        // Responsive Badge
        Label responsiveBadge = createResponsiveBadge();

        content.getChildren().addAll(roleSection, aboutSection);
        return content;
    }

    private VBox createRoleSection() {
        VBox roleSection = new VBox();
        roleSection.setSpacing(20);

        // Section title
        Label sectionTitle = new Label("🎯 Role Selection");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        sectionTitle.setTextFill(Color.web("#2c3e50"));

        // Role cards container
        HBox roleCards = new HBox();
        roleCards.setSpacing(20);
        roleCards.setAlignment(Pos.CENTER);

        // Create role cards
        Button complainantCard = createRoleCard("👤", "Complainant", "#667eea", "#764ba2");
        Button officerCard = createRoleCard("👮", "Investigating Officer", "#f093fb", "#f5576c");
        Button adminCard = createRoleCard("🛡️", "Admin", "#4facfe", "#00f2fe");

        roleCards.getChildren().addAll(complainantCard, officerCard, adminCard);

        roleSection.getChildren().addAll(sectionTitle, roleCards);
        return roleSection;
    }

    private Button createRoleCard(String icon, String title, String color1, String color2) {
        Button card = new Button();
        card.setPrefSize(200, 120);
        card.setAlignment(Pos.CENTER);

        // Create card content
        VBox cardContent = new VBox();
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setSpacing(10);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(36));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);

        cardContent.getChildren().addAll(iconLabel, titleLabel);
        card.setGraphic(cardContent);

        // Gradient background
        LinearGradient cardGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(color1)),
                new Stop(1, Color.web(color2))
        );
        BackgroundFill cardFill = new BackgroundFill(cardGradient,
                new CornerRadii(15), null);
        card.setBackground(new Background(cardFill));

        // Remove default button styling
        card.setStyle("-fx-border-width: 0; -fx-focus-color: transparent;");

        // Add shadow effect
        card.setEffect(new DropShadow(10, Color.web("#000000", 0.3)));

        // Add hover and click animations
        addCardAnimations(card, title);

        return card;
    }

    private void addCardAnimations(Button card, String title) {
        // Hover animations
        card.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), card);
            scaleUp.setToX(1.05);
            scaleUp.setToY(1.05);
            scaleUp.play();

            TranslateTransition moveUp = new TranslateTransition(Duration.millis(200), card);
            moveUp.setToY(-5);
            moveUp.play();
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), card);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();

            TranslateTransition moveDown = new TranslateTransition(Duration.millis(200), card);
            moveDown.setToY(0);
            moveDown.play();
        });

        // Click animation and action
        card.setOnAction(e -> {
            ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), card);
            clickScale.setToX(0.95);
            clickScale.setToY(0.95);
            clickScale.setOnFinished(event -> {
                ScaleTransition backScale = new ScaleTransition(Duration.millis(100), card);
                backScale.setToX(1.0);
                backScale.setToY(1.0);
                backScale.play();

                Login login = new Login();
                Scene loginScene = login.createLoginScene(primaryStage, title);
                primaryStage.setScene(loginScene);
                primaryStage.centerOnScreen();



            });
            clickScale.play();

        });
    }

    private void showRoleSelection(String role) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Role Selected");
        alert.setHeaderText("Selected role: " + role.substring(0, 1).toUpperCase() + role.substring(1));
        alert.showAndWait();
    }

    private VBox createAboutSection() {
        VBox aboutSection = new VBox();
        aboutSection.setSpacing(20);

        // Section title
        Label sectionTitle = new Label("📱 About This App");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        sectionTitle.setTextFill(Color.web("#2c3e50"));

        // Features grid
        GridPane featuresGrid = new GridPane();
        featuresGrid.setHgap(20);
        featuresGrid.setVgap(20);
        featuresGrid.setAlignment(Pos.CENTER);

        // Create feature items
        VBox feature1 = createFeatureItem("🏠 File complaints from home",
                "Submit your complaints easily from anywhere using our user-friendly interface.");
        VBox feature2 = createFeatureItem("📊 Officers track case progress",
                "Real-time case management system for investigating officers.");
        VBox feature3 = createFeatureItem("👨‍💼 Admin monitors & approves accounts",
                "Secure administrative oversight and account verification.");
        VBox feature4 = createFeatureItem("⚡ Real-time updates & document uploads",
                "Instant notifications and seamless document management.");

        featuresGrid.add(feature1, 0, 0);
        featuresGrid.add(feature2, 1, 0);
        featuresGrid.add(feature3, 0, 1);
        featuresGrid.add(feature4, 1, 1);

        aboutSection.getChildren().addAll(sectionTitle, featuresGrid);
        return aboutSection;
    }

    private VBox createFeatureItem(String title, String description) {
        VBox featureItem = new VBox();
        featureItem.setSpacing(8);
        featureItem.setPadding(new Insets(20));
        featureItem.setPrefWidth(250);
        featureItem.setAlignment(Pos.TOP_LEFT);

        // Background and border
        featureItem.setStyle("-fx-background-color: #f8f9fa; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #3498db; " +
                "-fx-border-width: 0 0 0 4; " +
                "-fx-border-radius: 10;");

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        titleLabel.setWrapText(true);

        // Description
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        descLabel.setTextFill(Color.web("#34495e"));
        descLabel.setWrapText(true);

        featureItem.getChildren().addAll(titleLabel, descLabel);

        // Add hover animation
        featureItem.setOnMouseEntered(e -> {
            TranslateTransition slideRight = new TranslateTransition(Duration.millis(200), featureItem);
            slideRight.setToX(5);
            slideRight.play();
        });

        featureItem.setOnMouseExited(e -> {
            TranslateTransition slideLeft = new TranslateTransition(Duration.millis(200), featureItem);
            slideLeft.setToX(0);
            slideLeft.play();
        });

        return featureItem;
    }

    private Label createResponsiveBadge() {
        Label badge = new Label("🔄 Responsive Design – works on PC & mobile");
        badge.setFont(Font.font("System", FontWeight.BOLD, 16));
        badge.setTextFill(Color.web("#2c3e50"));
        badge.setPadding(new Insets(15, 25, 15, 25));
        badge.setAlignment(Pos.CENTER);
        badge.setMaxWidth(Double.MAX_VALUE);

        // Gradient background
        LinearGradient badgeGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#a8edea")),
                new Stop(1, Color.web("#fed6e3"))
        );
        BackgroundFill badgeFill = new BackgroundFill(badgeGradient,
                new CornerRadii(50), null);
        badge.setBackground(new Background(badgeFill));

        // Add shadow
        badge.setEffect(new DropShadow(5, Color.web("#000000", 0.2)));

        return badge;
    }

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setSpacing(30);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20));

        // Footer background
        BackgroundFill footerFill = new BackgroundFill(Color.web("#34495e"),
                new CornerRadii(0, 0, 20, 20, false), null);
        footer.setBackground(new Background(footerFill));



        return footer;
    }

    private Hyperlink createFooterLink(String text) {
        Hyperlink link = new Hyperlink(text);
        link.setTextFill(Color.web("#ecf0f1"));
        link.setFont(Font.font("System", FontWeight.NORMAL, 14));
        link.setUnderline(false);

        // Hover effect
        link.setOnMouseEntered(e -> {
            link.setTextFill(Color.web("#3498db"));
            link.setUnderline(true);
        });

        link.setOnMouseExited(e -> {
            link.setTextFill(Color.web("#ecf0f1"));
            link.setUnderline(false);
        });

        return link;
    }

    private void addEntranceAnimation(VBox container) {
        // Initial state
        container.setOpacity(0);
        container.setTranslateY(30);

        // Fade in and slide up animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideUp = new TranslateTransition(Duration.millis(800), container);
        slideUp.setFromY(30);
        slideUp.setToY(0);

        fadeIn.play();
        slideUp.play();
    }




// Centralized role-to-dashboard decision used by the JavaFX navigation layer.
    public Class<?> getDashboardForRole(String role) {
        if (role == null) return null;
        return switch (role.trim().toLowerCase()) {
            case "admin" -> AdminDashboard.class;
            case "investigator" -> InvestigatorDashboard.class;
            case "user", "citizen" -> UserDashboard.class;
            default -> null;
        };
    }
}

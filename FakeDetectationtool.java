import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.regex.Pattern;

// Main Application Class
public class FakeDetectionTool {
    private static Map<String, UserCredentials> users = new HashMap<>();
    private static List<DetectionHistory> history = new ArrayList<>();
    
    public static Map<String, UserCredentials> getUsers() {
        return users;
    }
    
    public static List<DetectionHistory> getHistory() {
        return history;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

// Detection History Class
class DetectionHistory {
    String username;
    String content;
    String type;
    String status;
    String fakePercentage;
    String timestamp;
    
    DetectionHistory(String username, String content, String type, String status, String fakePercentage, String timestamp) {
        this.username = username;
        this.content = content;
        this.type = type;
        this.status = status;
        this.fakePercentage = fakePercentage;
        this.timestamp = timestamp;
    }
}

// User Credentials Class
class UserCredentials {
    String password;
    String role;
    String email;
    String fullName;
    
    UserCredentials(String password, String role, String email, String fullName) {
        this.password = password;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
    }
}

// Login Frame (16:9 ratio)
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("Fake Detection Tool - Secure Login");
        setSize(960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 23, 42));

        // Left Panel - Branding
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(30, 41, 59));
        leftPanel.setPreferredSize(new Dimension(400, 540));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(80, 40, 80, 40));
        
        JLabel logoLabel = new JLabel("🛡");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel brandLabel = new JLabel("Fake Detection Tool");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        brandLabel.setForeground(new Color(248, 250, 252));
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel taglineLabel = new JLabel("AI-Powered Authenticity Verification");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taglineLabel.setForeground(new Color(148, 163, 184));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setBackground(new Color(30, 41, 59));
        featuresPanel.setBorder(new EmptyBorder(40, 0, 0, 0));
        
        String[] features = {
            "✓ URL Detection",
            "✓ Email Verification",
            "✓ OTP Analysis",
            "✓ Message Scanning"
        };
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            featureLabel.setForeground(new Color(203, 213, 225));
            featureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featuresPanel.add(featureLabel);
            featuresPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }
        
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(brandLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(taglineLabel);
        leftPanel.add(featuresPanel);
        leftPanel.add(Box.createVerticalGlue());

        // Right Panel - Login Form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(15, 23, 42));
        rightPanel.setBorder(new EmptyBorder(60, 70, 60, 70));

        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(248, 250, 252));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Please login to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(148, 163, 184));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username Section
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usernameLabel.setForeground(new Color(226, 232, 240));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        usernameField.setBackground(new Color(30, 41, 59));
        usernameField.setForeground(new Color(248, 250, 252));
        usernameField.setCaretColor(new Color(248, 250, 252));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password Section
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(226, 232, 240));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setBackground(new Color(30, 41, 59));
        passwordField.setForeground(new Color(248, 250, 252));
        passwordField.setCaretColor(new Color(248, 250, 252));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(248, 113, 113));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBackground(new Color(99, 102, 241));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Register Button
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerButton.setBackground(new Color(16, 185, 129));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        registerButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Add components to right panel
        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(subtitleLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        rightPanel.add(usernameLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(usernameField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(passwordLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(statusLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        rightPanel.add(loginButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        rightPanel.add(registerButton);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Action Listeners
        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> openRegistration());
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠ Please enter both username and password");
            return;
        }

        Map<String, UserCredentials> users = FakeDetectionTool.getUsers();
        
        if (users.isEmpty()) {
            statusLabel.setText("⚠ No users registered. Please create an account first.");
            return;
        }
        
        UserCredentials credentials = users.get(username);
        
        if (credentials != null && credentials.password.equals(password)) {
            statusLabel.setText("✓ Login successful! Redirecting...");
            statusLabel.setForeground(new Color(134, 239, 172));
            
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            
            Timer timer = new Timer(800, e -> {
                new MainApplicationFrame(username, credentials.role).setVisible(true);
                LoginFrame.this.dispose();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            statusLabel.setText("✗ Invalid username or password");
            statusLabel.setForeground(new Color(248, 113, 113));
            passwordField.setText("");
        }
    }

    private void openRegistration() {
        new RegistrationFrame(this).setVisible(true);
        this.setVisible(false);
    }
}

// Registration Frame (16:9 ratio)
class RegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;
    private LoginFrame loginFrame;

    public RegistrationFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("Fake Detection Tool - Registration");
        setSize(1120, 630);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 23, 42));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(16, 185, 129));
        headerPanel.setPreferredSize(new Dimension(1120, 90));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Join the fight against fake content");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(236, 253, 245));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalGlue());

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(30, 41, 59));
        formPanel.setBorder(new EmptyBorder(30, 80, 30, 80));

        // Full Name
        addFormField(formPanel, "Full Name", fullNameField = createTextField());

        // Email
        addFormField(formPanel, "Email Address", emailField = createTextField());

        // Username
        addFormField(formPanel, "Username", usernameField = createTextField());

        // Password
        addFormField(formPanel, "Password", passwordField = createPasswordField());

        // Confirm Password
        addFormField(formPanel, "Confirm Password", confirmPasswordField = createPasswordField());

        // Role Selection
        JLabel roleLabel = new JLabel("Account Type");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        roleLabel.setForeground(new Color(226, 232, 240));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] roles = {"USER", "ADMIN"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        roleComboBox.setBackground(new Color(51, 65, 85));
        roleComboBox.setForeground(new Color(248, 250, 252));
        roleComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(roleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(roleComboBox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(248, 113, 113));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(statusLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 18)));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(new Color(30, 41, 59));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Register Button
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(16, 185, 129));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setPreferredSize(new Dimension(170, 45));

        // Back Button
        backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(100, 116, 139));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(170, 45));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(30, 41, 59));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Action Listeners
        registerButton.addActionListener(e -> performRegistration());
        backButton.addActionListener(e -> {
            loginFrame.setVisible(true);
            RegistrationFrame.this.dispose();
        });
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBackground(new Color(51, 65, 85));
        field.setForeground(new Color(248, 250, 252));
        field.setCaretColor(new Color(248, 250, 252));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBackground(new Color(51, 65, 85));
        field.setForeground(new Color(248, 250, 252));
        field.setCaretColor(new Color(248, 250, 252));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private void addFormField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(226, 232, 240));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
    }

    private void performRegistration() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showError("⚠ All fields are required");
            return;
        }

        if (username.length() < 4) {
            showError("⚠ Username must be at least 4 characters");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("⚠ Please enter a valid email address");
            return;
        }

        if (password.length() < 6) {
            showError("⚠ Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("⚠ Passwords do not match");
            return;
        }

        Map<String, UserCredentials> users = FakeDetectionTool.getUsers();
        if (users.containsKey(username)) {
            showError("⚠ Username already exists");
            return;
        }

        users.put(username, new UserCredentials(password, role, email, fullName));
        
        statusLabel.setText("✓ Registration successful!");
        statusLabel.setForeground(new Color(134, 239, 172));

        registerButton.setEnabled(false);
        backButton.setEnabled(false);

        JOptionPane.showMessageDialog(this,
            "Account created successfully!\n\n" +
            "Name: " + fullName + "\n" +
            "Username: " + username + "\n" +
            "Email: " + email + "\n" +
            "Role: " + role,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);

        Timer timer = new Timer(1000, e -> {
            loginFrame.setVisible(true);
            RegistrationFrame.this.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(248, 113, 113));
    }
}

// Main Application Frame (16:9 ratio)
class MainApplicationFrame extends JFrame {
    private String username;
    private String role;
    private JComboBox<String> detectionTypeCombo;
    private JTextArea inputArea;
    private JTextArea resultArea;
    private JButton checkButton;
    private JButton historyButton;
    private JButton clearButton;
    private JProgressBar progressBar;

    public MainApplicationFrame(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Fake Detection Tool - " + role + " Dashboard");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(15, 23, 42));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 41, 59));
        headerPanel.setBorder(new EmptyBorder(25, 40, 25, 40));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(30, 41, 59));
        
        JLabel titleLabel = new JLabel("🛡 Fake Detection Tool");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(248, 250, 252));
        
        JLabel roleLabel = new JLabel(role + " Dashboard");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleLabel.setForeground(new Color(148, 163, 184));
        
        titlePanel.add(titleLabel);
        titlePanel.add(roleLabel);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        userPanel.setBackground(new Color(30, 41, 59));
        
        JLabel userInfoLabel = new JLabel("👤 " + username);
        userInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userInfoLabel.setForeground(new Color(248, 250, 252));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(239, 68, 68));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(120, 45));
        logoutButton.addActionListener(e -> logout());
        
        userPanel.add(userInfoLabel);
        userPanel.add(logoutButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 25));
        contentPanel.setBackground(new Color(15, 23, 42));
        contentPanel.setBorder(new EmptyBorder(35, 40, 35, 40));

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(20, 20));
        inputPanel.setBackground(new Color(30, 41, 59));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 2),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        // Top section with label and dropdown
        JPanel topSection = new JPanel(new BorderLayout(15, 0));
        topSection.setBackground(new Color(30, 41, 59));
        
        JLabel inputLabel = new JLabel("🔍 Select Detection Type:");
        inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        inputLabel.setForeground(new Color(248, 250, 252));
        
        String[] detectionTypes = {"URL", "Email", "OTP/Message", "Phone Number"};
        detectionTypeCombo = new JComboBox<>(detectionTypes);
        detectionTypeCombo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        detectionTypeCombo.setBackground(new Color(51, 65, 85));
        detectionTypeCombo.setForeground(new Color(248, 250, 252));
        detectionTypeCombo.setPreferredSize(new Dimension(200, 45));
        
        topSection.add(inputLabel, BorderLayout.WEST);
        topSection.add(detectionTypeCombo, BorderLayout.EAST);
        
        // Text area for input
        JLabel textLabel = new JLabel("Enter content to analyze:");
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(new Color(203, 213, 225));
        
        inputArea = new JTextArea(4, 50);
        inputArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBackground(new Color(51, 65, 85));
        inputArea.setForeground(new Color(248, 250, 252));
        inputArea.setCaretColor(new Color(248, 250, 252));
        inputArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setBorder(null);
        
        JPanel textPanel = new JPanel(new BorderLayout(0, 10));
        textPanel.setBackground(new Color(30, 41, 59));
        textPanel.add(textLabel, BorderLayout.NORTH);
        textPanel.add(inputScrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(new Color(30, 41, 59));
        
        checkButton = new JButton("Analyze Content");
        checkButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkButton.setBackground(new Color(99, 102, 241));
        checkButton.setForeground(Color.WHITE);
        checkButton.setFocusPainted(false);
        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkButton.setPreferredSize(new Dimension(180, 50));
        checkButton.addActionListener(e -> performDetection());
        
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearButton.setBackground(new Color(100, 116, 139));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setPreferredSize(new Dimension(130, 50));
        clearButton.addActionListener(e -> clearResults());
        
        // History button for ALL users (moved from admin-only section)
        historyButton = new JButton("My History");
        historyButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyButton.setBackground(new Color(168, 85, 247));
        historyButton.setForeground(Color.WHITE);
        historyButton.setFocusPainted(false);
        historyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        historyButton.setPreferredSize(new Dimension(160, 50));
        historyButton.addActionListener(e -> showHistory());
        
        buttonPanel.add(checkButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(historyButton);
        
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(0, 10));
        progressBar.setBackground(new Color(30, 41, 59));
        progressBar.setForeground(new Color(99, 102, 241));
        
        JPanel inputBottomPanel = new JPanel(new BorderLayout(0, 15));
        inputBottomPanel.setBackground(new Color(30, 41, 59));
        inputBottomPanel.add(buttonPanel, BorderLayout.NORTH);
        inputBottomPanel.add(progressBar, BorderLayout.SOUTH);
        
        inputPanel.add(topSection, BorderLayout.NORTH);
        inputPanel.add(textPanel, BorderLayout.CENTER);
        inputPanel.add(inputBottomPanel, BorderLayout.SOUTH);

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout(0, 20));
        resultsPanel.setBackground(new Color(30, 41, 59));
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(51, 65, 85), 2),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel resultsLabel = new JLabel("📊 Analysis Results:");
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resultsLabel.setForeground(new Color(248, 250, 252));
        
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(15, 23, 42));
        resultArea.setForeground(new Color(203, 213, 225));
        resultArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resultArea.setText("No content analyzed yet. Enter content above and click 'Analyze Content' to begin detection.");
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
        scrollPane.setPreferredSize(new Dimension(0, 400));
        
        resultsPanel.add(resultsLabel, BorderLayout.NORTH);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(resultsPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void performDetection() {
        String content = inputArea.getText().trim();
        String detectionType = (String) detectionTypeCombo.getSelectedItem();
        
        if (content.isEmpty()) {
            resultArea.setText("⚠ ERROR: Please enter content to analyze");
            resultArea.setForeground(new Color(248, 113, 113));
            return;
        }

        checkButton.setEnabled(false);
        progressBar.setVisible(true);
        resultArea.setText("🔄 Analyzing " + detectionType + "...\n\nPlease wait...");
        resultArea.setForeground(new Color(203, 213, 225));

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                Thread.sleep(1500);
                return performAnalysis(content, detectionType);
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    resultArea.setText(result);
                    
                    if (result.contains("FAKE: 80%") || result.contains("FAKE: 90%") || result.contains("FAKE: 100%")) {
                        resultArea.setForeground(new Color(248, 113, 113));
                    } else if (result.contains("FAKE:")) {
                        resultArea.setForeground(new Color(251, 191, 36));
                    } else {
                        resultArea.setForeground(new Color(134, 239, 172));
                    }
                } catch (Exception e) {
                    resultArea.setText("✗ ERROR: " + e.getMessage());
                    resultArea.setForeground(new Color(248, 113, 113));
                }
                
                checkButton.setEnabled(true);
                progressBar.setVisible(false);
            }
        };
        
        worker.execute();
    }

    private String performAnalysis(String content, String type) {
        StringBuilder result = new StringBuilder();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int fakePercentage = 0;
        String status = "ANALYZING";
        
        result.append("═══════════════════════════════════════════════════════════════════════════\n");
        result.append("                    FAKE DETECTION ANALYSIS REPORT\n");
        result.append("═══════════════════════════════════════════════════════════════════════════\n\n");
        
        result.append("📋 Detection Type: ").append(type).append("\n");
        result.append("👤 Analyzed by: ").append(username).append(" (").append(role).append(")\n");
        result.append("🕒 Timestamp: ").append(timestamp).append("\n");
        result.append("📝 Content Length: ").append(content.length()).append(" characters\n\n");
        
        result.append("───────────────────────────────────────────────────────────────────────────\n");
        result.append("CONTENT PREVIEW\n");
        result.append("───────────────────────────────────────────────────────────────────────────\n\n");
        result.append(content.length() > 200 ? content.substring(0, 200) + "..." : content).append("\n\n");
        
        result.append("───────────────────────────────────────────────────────────────────────────\n");
        result.append("ANALYSIS METRICS\n");
        result.append("───────────────────────────────────────────────────────────────────────────\n\n");

        switch (type) {
            case "URL":
                fakePercentage = analyzeURL(content, result);
                break;
            case "Email":
                fakePercentage = analyzeEmail(content, result);
                break;
            case "OTP/Message":
                fakePercentage = analyzeMessage(content, result);
                break;
            case "Phone Number":
                fakePercentage = analyzePhoneNumber(content, result);
                break;
        }
        
        result.append("\n───────────────────────────────────────────────────────────────────────────\n");
        result.append("FINAL VERDICT\n");
        result.append("───────────────────────────────────────────────────────────────────────────\n\n");
        
        result.append("🎯 FAKE PROBABILITY: ").append(fakePercentage).append("%\n\n");
        
        result.append("╔═══════════════════════════════════════════════════════════════╗\n");
        if (fakePercentage >= 80) {
            result.append("║  ⚠  HIGHLY LIKELY FAKE - DANGER LEVEL: CRITICAL          ║\n");
            result.append("╚═══════════════════════════════════════════════════════════════╝\n\n");
            result.append("🚨 RECOMMENDATION: BLOCK/DELETE IMMEDIATELY\n");
            result.append("This content shows EXTREME signs of being fraudulent or malicious.\n");
            result.append("Do NOT interact, click links, or provide any information.\n");
            status = "FAKE (" + fakePercentage + "%)";
        } else if (fakePercentage >= 50) {
            result.append("║  ⚠  LIKELY FAKE - DANGER LEVEL: HIGH                     ║\n");
            result.append("╚═══════════════════════════════════════════════════════════════╝\n\n");
            result.append("⚠ RECOMMENDATION: AVOID AND VERIFY\n");
            result.append("This content shows significant signs of being fake or suspicious.\n");
            result.append("Exercise extreme caution before taking any action.\n");
            status = "SUSPICIOUS (" + fakePercentage + "%)";
        } else if (fakePercentage >= 25) {
            result.append("║  ⚠  POSSIBLY FAKE - DANGER LEVEL: MODERATE               ║\n");
            result.append("╚═══════════════════════════════════════════════════════════════╝\n\n");
            result.append("⚡ RECOMMENDATION: PROCEED WITH CAUTION\n");
            result.append("Some suspicious indicators detected. Verify authenticity before proceeding.\n");
            status = "CAUTION (" + fakePercentage + "%)";
        } else {
            result.append("║  ✅ APPEARS LEGITIMATE - DANGER LEVEL: LOW                ║\n");
            result.append("╚═══════════════════════════════════════════════════════════════╝\n\n");
            result.append("✓ RECOMMENDATION: LIKELY SAFE\n");
            result.append("No major red flags detected. However, always remain vigilant online.\n");
            status = "SAFE (" + fakePercentage + "%)";
        }
        
        result.append("\n═══════════════════════════════════════════════════════════════════════════\n");
        result.append("                         END OF REPORT\n");
        result.append("═══════════════════════════════════════════════════════════════════════════\n");
        
        // Save to history with FULL content
        FakeDetectionTool.getHistory().add(
            new DetectionHistory(username, content, type, status, fakePercentage + "%", timestamp)
        );
        
        return result.toString();
    }

    private int analyzeURL(String url, StringBuilder result) {
        int score = 0;
        
        // HTTPS Check
        boolean hasHttps = url.toLowerCase().startsWith("https://");
        result.append("🔒 HTTPS Encryption: ");
        if (!hasHttps) {
            result.append("✗ NO (Risk +25%)\n");
            score += 25;
        } else {
            result.append("✓ YES\n");
        }
        
        // Suspicious keywords
        String[] suspiciousWords = {"verify", "account", "secure", "login", "banking", "paypal", 
                                   "suspended", "confirm", "winner", "prize", "urgent", "click"};
        int suspiciousCount = 0;
        for (String word : suspiciousWords) {
            if (url.toLowerCase().contains(word)) suspiciousCount++;
        }
        result.append("⚠ Suspicious Keywords: ");
        if (suspiciousCount > 0) {
            result.append("✗ Found " + suspiciousCount + " (Risk +" + (suspiciousCount * 10) + "%)\n");
            score += suspiciousCount * 10;
        } else {
            result.append("✓ None detected\n");
        }
        
        // IP Address check
        boolean hasIP = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}").matcher(url).find();
        result.append("🌐 IP Address Usage: ");
        if (hasIP) {
            result.append("✗ YES (Risk +20%)\n");
            score += 20;
        } else {
            result.append("✓ Uses domain name\n");
        }
        
        // URL Length
        result.append("📏 URL Length: ");
        if (url.length() > 100) {
            result.append("✗ " + url.length() + " chars - Unusually long (Risk +15%)\n");
            score += 15;
        } else {
            result.append("✓ " + url.length() + " chars - Normal\n");
        }
        
        // Special characters
        long atCount = url.chars().filter(ch -> ch == '@').count();
        result.append("🔣 @ Symbol: ");
        if (atCount > 0) {
            result.append("✗ Found (Risk +20%)\n");
            score += 20;
        } else {
            result.append("✓ Not found\n");
        }
        
        // Shortened URL indicators
        String[] shorteners = {"bit.ly", "tinyurl", "goo.gl", "t.co", "ow.ly"};
        boolean isShortened = false;
        for (String shortener : shorteners) {
            if (url.toLowerCase().contains(shortener)) {
                isShortened = true;
                break;
            }
        }
        result.append("🔗 URL Shortener: ");
        if (isShortened) {
            result.append("✗ Detected (Risk +15%)\n");
            score += 15;
        } else {
            result.append("✓ Not detected\n");
        }
        
        return Math.min(100, score);
    }

    private int analyzeEmail(String email, StringBuilder result) {
        int score = 0;
        
        // Email format validation
        boolean validFormat = email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        result.append("📧 Email Format: ");
        if (!validFormat) {
            result.append("✗ Invalid format (Risk +30%)\n");
            score += 30;
        } else {
            result.append("✓ Valid format\n");
        }
        
        // Suspicious domains
        String[] fakeDomains = {"tempmail", "guerrillamail", "10minutemail", "throwaway", "fake"};
        boolean hasFakeDomain = false;
        for (String domain : fakeDomains) {
            if (email.toLowerCase().contains(domain)) {
                hasFakeDomain = true;
                break;
            }
        }
        result.append("🏢 Domain Reputation: ");
        if (hasFakeDomain) {
            result.append("✗ Disposable/Fake domain (Risk +40%)\n");
            score += 40;
        } else {
            result.append("✓ Standard domain\n");
        }
        
        // Random characters
        long digitCount = email.chars().filter(Character::isDigit).count();
        result.append("🔢 Numeric Characters: ");
        if (digitCount > email.length() * 0.4) {
            result.append("✗ Excessive (" + digitCount + ") - (Risk +20%)\n");
            score += 20;
        } else {
            result.append("✓ Normal amount\n");
        }
        
        // Username length
        String username = email.split("@")[0];
        result.append("👤 Username Length: ");
        if (username.length() < 3) {
            result.append("✗ Too short (Risk +15%)\n");
            score += 15;
        } else if (username.length() > 30) {
            result.append("✗ Unusually long (Risk +10%)\n");
            score += 10;
        } else {
            result.append("✓ Normal\n");
        }
        
        return Math.min(100, score);
    }

    private int analyzeMessage(String message, StringBuilder result) {
        int score = 0;
        
        // Urgency indicators
        String[] urgentWords = {"urgent", "immediately", "now", "hurry", "quick", "expire", "limited time"};
        int urgencyCount = 0;
        for (String word : urgentWords) {
            if (message.toLowerCase().contains(word)) urgencyCount++;
        }
        result.append("⏰ Urgency Indicators: ");
        if (urgencyCount > 0) {
            result.append("✗ Found " + urgencyCount + " (Risk +" + (urgencyCount * 15) + "%)\n");
            score += urgencyCount * 15;
        } else {
            result.append("✓ None detected\n");
        }
        
        // Financial keywords
        String[] moneyWords = {"money", "payment", "bank", "account", "credit card", "verify", "otp", 
                               "pin", "password", "cvv", "prize", "won", "winner", "claim"};
        int moneyCount = 0;
        for (String word : moneyWords) {
            if (message.toLowerCase().contains(word)) moneyCount++;
        }
        result.append("💰 Financial Keywords: ");
        if (moneyCount > 0) {
            result.append("✗ Found " + moneyCount + " (Risk +" + (moneyCount * 12) + "%)\n");
            score += moneyCount * 12;
        } else {
            result.append("✓ None detected\n");
        }
        
        // Links in message
        boolean hasLink = message.toLowerCase().contains("http") || message.contains("www.");
        result.append("🔗 Contains Links: ");
        if (hasLink) {
            result.append("✗ YES (Risk +20%)\n");
            score += 20;
        } else {
            result.append("✓ NO\n");
        }
        
        // Grammar/spelling issues (simple check)
        long capsCount = message.chars().filter(Character::isUpperCase).count();
        double capsRatio = (double) capsCount / message.length();
        result.append("🔤 Excessive CAPS: ");
        if (capsRatio > 0.3) {
            result.append("✗ YES (Risk +15%)\n");
            score += 15;
        } else {
            result.append("✓ Normal\n");
        }
        
        // Threat indicators
        String[] threats = {"block", "suspend", "legal action", "police", "arrest", "fine"};
        boolean hasThreat = false;
        for (String threat : threats) {
            if (message.toLowerCase().contains(threat)) {
                hasThreat = true;
                break;
            }
        }
        result.append("⚠ Threat Language: ");
        if (hasThreat) {
            result.append("✗ Detected (Risk +25%)\n");
            score += 25;
        } else {
            result.append("✓ Not detected\n");
        }
        
        return Math.min(100, score);
    }

    private int analyzePhoneNumber(String phone, StringBuilder result) {
        int score = 0;
        
        // Clean phone number
        String cleanPhone = phone.replaceAll("[^0-9+]", "");
        
        // Length check
        result.append("📱 Number Length: ");
        if (cleanPhone.length() < 10 || cleanPhone.length() > 15) {
            result.append("✗ Invalid (" + cleanPhone.length() + " digits) (Risk +30%)\n");
            score += 30;
        } else {
            result.append("✓ Valid (" + cleanPhone.length() + " digits)\n");
        }
        
        // Repeated digits
        boolean hasRepeatedDigits = cleanPhone.matches(".([0-9])\\1{4,}.");
        result.append("🔢 Repeated Digits: ");
        if (hasRepeatedDigits) {
            result.append("✗ Found (e.g., 11111) (Risk +25%)\n");
            score += 25;
        } else {
            result.append("✓ Normal pattern\n");
        }
        
        // Sequential digits
        boolean hasSequential = cleanPhone.contains("12345") || cleanPhone.contains("54321") || 
                               cleanPhone.contains("01234") || cleanPhone.contains("98765");
        result.append("🔄 Sequential Digits: ");
        if (hasSequential) {
            result.append("✗ Found (Risk +20%)\n");
            score += 20;
        } else {
            result.append("✓ Not found\n");
        }
        
        // Country code check
        result.append("🌍 Country Code: ");
        if (cleanPhone.startsWith("+")) {
            result.append("✓ Present\n");
        } else {
            result.append("⚠ Missing (Risk +10%)\n");
            score += 10;
        }
        
        // Known spam patterns
        String[] spamPrefixes = {"1234", "0000", "9999"};
        boolean isSpamPattern = false;
        for (String prefix : spamPrefixes) {
            if (cleanPhone.contains(prefix)) {
                isSpamPattern = true;
                break;
            }
        }
        result.append("🚫 Spam Pattern: ");
        if (isSpamPattern) {
            result.append("✗ Detected (Risk +35%)\n");
            score += 35;
        } else {
            result.append("✓ Not detected\n");
        }
        
        return Math.min(100, score);
    }

    private void clearResults() {
        inputArea.setText("");
        resultArea.setText("No content analyzed yet. Enter content above and click 'Analyze Content' to begin detection.");
        resultArea.setForeground(new Color(203, 213, 225));
    }

    private void showHistory() {
        JDialog historyDialog = new JDialog(this, username + "'s Detection History", true);
        historyDialog.setSize(1400, 700);
        historyDialog.setLocationRelativeTo(this);
        
        JPanel dialogPanel = new JPanel(new BorderLayout(0, 20));
        dialogPanel.setBackground(new Color(15, 23, 42));
        dialogPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel headerLabel = new JLabel("📜 My Detection History");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(248, 250, 252));
        
        String[] columnNames;
        DefaultTableModel tableModel;
        
        // Admin sees all history, users see only their own
        if (role.equals("ADMIN")) {
            headerLabel.setText("📜 Complete Detection History (All Users)");
            columnNames = new String[]{"#", "Username", "Full Content", "Type", "Status", "Fake %", "Timestamp"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            List<DetectionHistory> history = FakeDetectionTool.getHistory();
            for (int i = 0; i < history.size(); i++) {
                DetectionHistory record = history.get(i);
                tableModel.addRow(new Object[]{
                    (i + 1),
                    record.username,
                    record.content,
                    record.type,
                    record.status,
                    record.fakePercentage,
                    record.timestamp
                });
            }
        } else {
            // Regular user sees only their own history
            columnNames = new String[]{"#", "Full Content", "Type", "Status", "Fake %", "Timestamp"};
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            List<DetectionHistory> history = FakeDetectionTool.getHistory();
            int userRecordCount = 0;
            for (int i = 0; i < history.size(); i++) {
                DetectionHistory record = history.get(i);
                // Only show records for this user
                if (record.username.equals(username)) {
                    userRecordCount++;
                    tableModel.addRow(new Object[]{
                        userRecordCount,
                        record.content,
                        record.type,
                        record.status,
                        record.fakePercentage,
                        record.timestamp
                    });
                }
            }
        }
        
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setBackground(new Color(30, 41, 59));
        table.setForeground(new Color(248, 250, 252));
        table.setGridColor(new Color(51, 65, 85));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(99, 102, 241));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(79, 70, 229));
        table.setSelectionForeground(Color.WHITE);
        
        // Set column widths based on role
        if (role.equals("ADMIN")) {
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setPreferredWidth(120);
            table.getColumnModel().getColumn(2).setPreferredWidth(350);
            table.getColumnModel().getColumn(3).setPreferredWidth(120);
            table.getColumnModel().getColumn(4).setPreferredWidth(180);
            table.getColumnModel().getColumn(5).setPreferredWidth(80);
            table.getColumnModel().getColumn(6).setPreferredWidth(180);
        } else {
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setPreferredWidth(400);
            table.getColumnModel().getColumn(2).setPreferredWidth(120);
            table.getColumnModel().getColumn(3).setPreferredWidth(180);
            table.getColumnModel().getColumn(4).setPreferredWidth(80);
            table.getColumnModel().getColumn(5).setPreferredWidth(180);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 2));
        scrollPane.getViewport().setBackground(new Color(30, 41, 59));
        
        // Count user's records
        int userRecordCount = 0;
        if (role.equals("ADMIN")) {
            userRecordCount = FakeDetectionTool.getHistory().size();
        } else {
            for (DetectionHistory record : FakeDetectionTool.getHistory()) {
                if (record.username.equals(username)) {
                    userRecordCount++;
                }
            }
        }
        
        JLabel infoLabel = new JLabel("Total Records: " + userRecordCount);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(148, 163, 184));
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        closeButton.setBackground(new Color(100, 116, 139));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(130, 45));
        closeButton.addActionListener(e -> historyDialog.dispose());
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(15, 23, 42));
        bottomPanel.add(infoLabel, BorderLayout.WEST);
        bottomPanel.add(closeButton, BorderLayout.EAST);
        
        dialogPanel.add(headerLabel, BorderLayout.NORTH);
        dialogPanel.add(scrollPane, BorderLayout.CENTER);
        dialogPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        historyDialog.add(dialogPanel);
        historyDialog.setVisible(true);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RegistrationFrame extends JFrame {
    private JTextField txtUsername, txtEmail, txtFullName, txtAge, txtPhone;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cmbRole;
    private JButton btnRegister, btnBack;

    public RegistrationFrame() {
        setTitle("Aeroplane Reservation System - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        layoutComponents();
        attachListeners();
    }

    private void initComponents() {
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtConfirmPassword = new JPasswordField(15);
        txtEmail = new JTextField(15);
        txtFullName = new JTextField(15);
        txtAge = new JTextField(15);
        txtPhone = new JTextField(15);
        
        cmbRole = new JComboBox<>(new String[]{"user", "admin"});
        btnRegister = new JButton("Register");
        btnBack = new JButton("Back to Login");
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("User Registration", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, gbc);

        // Form fields
        gbc.gridwidth = 1;
        addField("Username:", txtUsername, 1, gbc);
        addField("Password:", txtPassword, 2, gbc);
        addField("Confirm Password:", txtConfirmPassword, 3, gbc);
        addField("Email:", txtEmail, 4, gbc);
        addField("Full Name:", txtFullName, 5, gbc);
        addField("Age:", txtAge, 6, gbc);
        addField("Phone:", txtPhone, 7, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        add(cmbRole, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBack);
        add(buttonPanel, gbc);
    }

    private void addField(String label, JComponent field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(field, gbc);
    }

    private void attachListeners() {
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }

    private void register() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText().trim();
        String fullName = txtFullName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String phone = txtPhone.getText().trim();
        String role = (String) cmbRole.getSelectedItem();

        // Validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || 
            fullName.isEmpty() || ageStr.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age");
            return;
        }

        // Check if username already exists
        List<User> users = CSVHelper.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists");
                return;
            }
        }

        // Create and save user
        User newUser = new User(username, password, email, fullName, age, phone, role);
        CSVHelper.saveUser(newUser);
        
        JOptionPane.showMessageDialog(this, "Registration successful!");
        new LoginFrame().setVisible(true);
        dispose();
    }
}
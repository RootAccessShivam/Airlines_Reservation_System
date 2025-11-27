import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public LoginFrame() {
        setTitle("Aeroplane Reservation System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        layoutComponents();
        attachListeners();
    }

    private void initComponents() {
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Aeroplane Reservation System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        add(buttonPanel, gbc);
    }

    private void attachListeners() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationFrame().setVisible(true);
                dispose();
            }
        });
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password");
            return;
        }

        List<User> users = CSVHelper.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                
                if (user.getRole().equals("admin")) {
                    new AdminDashboard(user).setVisible(true);
                } else {
                    new UserDashboard(user).setVisible(true);
                }
                dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid username or password");
    }
}
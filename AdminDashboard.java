import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboard extends JFrame {
    private User currentUser;
    private JTable flightsTable, usersTable, bookingsTable;
    private DefaultTableModel flightsModel, usersModel, bookingsModel;
    private JButton btnAddFlight, btnUpdateFlight, btnDeleteFlight, btnRefresh, btnLogout;

    public AdminDashboard(User user) {
        this.currentUser = user;
        setTitle("Admin Dashboard - Aeroplane Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
        loadAllData();
    }

    private void initComponents() {
        // Buttons
        btnAddFlight = new JButton("Add Flight");
        btnUpdateFlight = new JButton("Update Flight");
        btnDeleteFlight = new JButton("Delete Flight");
        btnRefresh = new JButton("Refresh All");
        btnLogout = new JButton("Logout");

        // Flights table
        String[] flightColumns = {"Flight No", "Airline", "Source", "Destination", "Date", "Time", "Price", "Seats"};
        flightsModel = new DefaultTableModel(flightColumns, 0);
        flightsTable = new JTable(flightsModel);
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Users table
        String[] userColumns = {"Username", "Email", "Full Name", "Age", "Phone", "Role"};
        usersModel = new DefaultTableModel(userColumns, 0);
        usersTable = new JTable(usersModel);

        // Bookings table
        String[] bookingColumns = {"Booking ID", "Username", "Flight No", "No of Seats", "Total Amount", "Status"};
        bookingsModel = new DefaultTableModel(bookingColumns, 0);
        bookingsTable = new JTable(bookingsModel);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel - Welcome and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "! (Administrator)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnLogout);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Main tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Flights Management Tab
        JPanel flightsPanel = new JPanel(new BorderLayout());
        JPanel flightButtons = new JPanel(new FlowLayout());
        flightButtons.add(btnAddFlight);
        flightButtons.add(btnUpdateFlight);
        flightButtons.add(btnDeleteFlight);
        
        flightsPanel.add(flightButtons, BorderLayout.NORTH);
        flightsPanel.add(new JScrollPane(flightsTable), BorderLayout.CENTER);

        // Users Management Tab
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        // Bookings Management Tab
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.add(new JScrollPane(bookingsTable), BorderLayout.CENTER);

        tabbedPane.addTab("Manage Flights", flightsPanel);
        tabbedPane.addTab("View Users", usersPanel);
        tabbedPane.addTab("View Bookings", bookingsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void attachListeners() {
        btnAddFlight.addActionListener(e -> addFlight());
        btnUpdateFlight.addActionListener(e -> updateFlight());
        btnDeleteFlight.addActionListener(e -> deleteFlight());
        btnRefresh.addActionListener(e -> loadAllData());
        btnLogout.addActionListener(e -> logout());
    }

    private void loadAllData() {
        loadFlights();
        loadUsers();
        loadBookings();
    }

    private void loadFlights() {
        flightsModel.setRowCount(0);
        List<Flight> flights = CSVHelper.getAllFlights();
        for (Flight flight : flights) {
            flightsModel.addRow(new Object[]{
                flight.getFlightNo(),
                flight.getAirlineName(),
                flight.getSource(),
                flight.getDestination(),
                flight.getDate(),
                flight.getTime(),
                flight.getPrice(),
                flight.getSeats()
            });
        }
    }

    private void loadUsers() {
        usersModel.setRowCount(0);
        List<User> users = CSVHelper.getAllUsers();
        for (User user : users) {
            usersModel.addRow(new Object[]{
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAge(),
                user.getPhone(),
                user.getRole()
            });
        }
    }

    private void loadBookings() {
        bookingsModel.setRowCount(0);
        List<Booking> bookings = CSVHelper.getAllBookings();
        for (Booking booking : bookings) {
            bookingsModel.addRow(new Object[]{
                booking.getBookingId(),
                booking.getUsername(),
                booking.getFlightNo(),
                booking.getNoOfSeats(),
                booking.getTotalAmount(),
                booking.getStatus()
            });
        }
    }

    private void addFlight() {
        JTextField txtFlightNo = new JTextField();
        JTextField txtAirline = new JTextField();
        JTextField txtSource = new JTextField();
        JTextField txtDestination = new JTextField();
        JTextField txtDate = new JTextField();
        JTextField txtTime = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtSeats = new JTextField();

        Object[] message = {
            "Flight No:", txtFlightNo,
            "Airline:", txtAirline,
            "Source:", txtSource,
            "Destination:", txtDestination,
            "Date (YYYY-MM-DD):", txtDate,
            "Time (HH:MM):", txtTime,
            "Price:", txtPrice,
            "Seats:", txtSeats
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Flight", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String flightNo = txtFlightNo.getText().trim();
                String airline = txtAirline.getText().trim();
                String source = txtSource.getText().trim();
                String destination = txtDestination.getText().trim();
                String date = txtDate.getText().trim();
                String time = txtTime.getText().trim();
                double price = Double.parseDouble(txtPrice.getText().trim());
                int seats = Integer.parseInt(txtSeats.getText().trim());

                if (flightNo.isEmpty() || airline.isEmpty() || source.isEmpty() || destination.isEmpty() || 
                    date.isEmpty() || time.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields");
                    return;
                }

                // Check if flight number already exists
                List<Flight> flights = CSVHelper.getAllFlights();
                for (Flight flight : flights) {
                    if (flight.getFlightNo().equals(flightNo)) {
                        JOptionPane.showMessageDialog(this, "Flight number already exists");
                        return;
                    }
                }

                Flight newFlight = new Flight(flightNo, airline, source, destination, date, time, price, seats);
                CSVHelper.saveFlight(newFlight);
                
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
                loadFlights();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and seats");
            }
        }
    }

    private void updateFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to update");
            return;
        }

        String flightNo = (String) flightsModel.getValueAt(selectedRow, 0);
        List<Flight> flights = CSVHelper.getAllFlights();
        Flight selectedFlight = null;

        for (Flight flight : flights) {
            if (flight.getFlightNo().equals(flightNo)) {
                selectedFlight = flight;
                break;
            }
        }

        if (selectedFlight == null) return;

        JTextField txtAirline = new JTextField(selectedFlight.getAirlineName());
        JTextField txtSource = new JTextField(selectedFlight.getSource());
        JTextField txtDestination = new JTextField(selectedFlight.getDestination());
        JTextField txtDate = new JTextField(selectedFlight.getDate());
        JTextField txtTime = new JTextField(selectedFlight.getTime());
        JTextField txtPrice = new JTextField(String.valueOf(selectedFlight.getPrice()));
        JTextField txtSeats = new JTextField(String.valueOf(selectedFlight.getSeats()));

        Object[] message = {
            "Airline:", txtAirline,
            "Source:", txtSource,
            "Destination:", txtDestination,
            "Date (YYYY-MM-DD):", txtDate,
            "Time (HH:MM):", txtTime,
            "Price:", txtPrice,
            "Seats:", txtSeats
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Flight", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                selectedFlight.setAirlineName(txtAirline.getText().trim());
                selectedFlight.setSource(txtSource.getText().trim());
                selectedFlight.setDestination(txtDestination.getText().trim());
                selectedFlight.setDate(txtDate.getText().trim());
                selectedFlight.setTime(txtTime.getText().trim());
                selectedFlight.setPrice(Double.parseDouble(txtPrice.getText().trim()));
                selectedFlight.setSeats(Integer.parseInt(txtSeats.getText().trim()));

                CSVHelper.updateFlights(flights);
                JOptionPane.showMessageDialog(this, "Flight updated successfully!");
                loadFlights();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and seats");
            }
        }
    }

    private void deleteFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete");
            return;
        }

        String flightNo = (String) flightsModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete flight " + flightNo + "?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            List<Flight> flights = CSVHelper.getAllFlights();
            flights.removeIf(flight -> flight.getFlightNo().equals(flightNo));
            CSVHelper.updateFlights(flights);
            
            JOptionPane.showMessageDialog(this, "Flight deleted successfully!");
            loadFlights();
        }
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
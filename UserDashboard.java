import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserDashboard extends JFrame {
    private User currentUser;
    private JTable flightsTable, bookingsTable;
    private DefaultTableModel flightsModel, bookingsModel;
    private JTextField txtSource, txtDestination, txtDate;
    private JButton btnSearch, btnBook, btnCancel, btnRefresh, btnLogout;

    public UserDashboard(User user) {
        this.currentUser = user;
        setTitle("User Dashboard - Aeroplane Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        attachListeners();
        loadFlights();
        loadBookings();
    }

    private void initComponents() {
        // Search panel components
        txtSource = new JTextField(10);
        txtDestination = new JTextField(10);
        txtDate = new JTextField(10);
        btnSearch = new JButton("Search Flights");
        btnBook = new JButton("Book Selected Flight");
        btnCancel = new JButton("Cancel Selected Booking");
        btnRefresh = new JButton("Refresh");
        btnLogout = new JButton("Logout");

        // Flights table
        String[] flightColumns = {"Flight No", "Airline", "Source", "Destination", "Date", "Time", "Price", "Seats"};
        flightsModel = new DefaultTableModel(flightColumns, 0);
        flightsTable = new JTable(flightsModel);
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Bookings table
        String[] bookingColumns = {"Booking ID", "Flight No", "No of Seats", "Total Amount", "Status"};
        bookingsModel = new DefaultTableModel(bookingColumns, 0);
        bookingsTable = new JTable(bookingsModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel - Welcome and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "! (" + currentUser.getRole() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(btnLogout, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Main tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Search and Book Flights Tab
        JPanel flightsPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Source:"));
        searchPanel.add(txtSource);
        searchPanel.add(new JLabel("Destination:"));
        searchPanel.add(txtDestination);
        searchPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        searchPanel.add(txtDate);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        flightsPanel.add(searchPanel, BorderLayout.NORTH);
        flightsPanel.add(new JScrollPane(flightsTable), BorderLayout.CENTER);
        flightsPanel.add(btnBook, BorderLayout.SOUTH);

        // My Bookings Tab
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.add(new JScrollPane(bookingsTable), BorderLayout.CENTER);
        bookingsPanel.add(btnCancel, BorderLayout.SOUTH);

        tabbedPane.addTab("Search & Book Flights", flightsPanel);
        tabbedPane.addTab("My Bookings", bookingsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void attachListeners() {
        btnSearch.addActionListener(e -> searchFlights());
        btnRefresh.addActionListener(e -> loadFlights());
        btnBook.addActionListener(e -> bookFlight());
        btnCancel.addActionListener(e -> cancelBooking());
        btnLogout.addActionListener(e -> logout());
    }

    private void loadFlights() {
        flightsModel.setRowCount(0);
        List<Flight> flights = CSVHelper.getAllFlights();
        for (Flight flight : flights) {
            if (flight.getSeats() > 0) { // Only show flights with available seats
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
    }

    private void searchFlights() {
        String source = txtSource.getText().trim().toLowerCase();
        String destination = txtDestination.getText().trim().toLowerCase();
        String date = txtDate.getText().trim();

        flightsModel.setRowCount(0);
        List<Flight> flights = CSVHelper.getAllFlights();
        for (Flight flight : flights) {
            boolean matches = true;
            if (!source.isEmpty() && !flight.getSource().toLowerCase().contains(source)) matches = false;
            if (!destination.isEmpty() && !flight.getDestination().toLowerCase().contains(destination)) matches = false;
            if (!date.isEmpty() && !flight.getDate().equals(date)) matches = false;
            if (matches && flight.getSeats() > 0) {
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
    }

    private void loadBookings() {
        bookingsModel.setRowCount(0);
        List<Booking> bookings = CSVHelper.getAllBookings();
        for (Booking booking : bookings) {
            if (booking.getUsername().equals(currentUser.getUsername())) {
                bookingsModel.addRow(new Object[]{
                    booking.getBookingId(),
                    booking.getFlightNo(),
                    booking.getNoOfSeats(),
                    booking.getTotalAmount(),
                    booking.getStatus()
                });
            }
        }
    }

    private void bookFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to book");
            return;
        }

        String flightNo = (String) flightsModel.getValueAt(selectedRow, 0);
        double price = (Double) flightsModel.getValueAt(selectedRow, 6);
        int availableSeats = (Integer) flightsModel.getValueAt(selectedRow, 7);

        String seatsStr = JOptionPane.showInputDialog(this, "Enter number of seats (Available: " + availableSeats + "):");
        if (seatsStr == null) return;

        try {
            int seats = Integer.parseInt(seatsStr);
            if (seats <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number of seats");
                return;
            }
            if (seats > availableSeats) {
                JOptionPane.showMessageDialog(this, "Not enough seats available");
                return;
            }

            // Create booking
            String bookingId = CSVHelper.generateBookingId();
            double totalAmount = price * seats;
            Booking booking = new Booking(bookingId, currentUser.getUsername(), flightNo, seats, totalAmount, "confirmed");
            CSVHelper.saveBooking(booking);

            // Update flight seats
            updateFlightSeats(flightNo, availableSeats - seats);

            JOptionPane.showMessageDialog(this, "Booking successful! Booking ID: " + bookingId);
            loadFlights();
            loadBookings();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number");
        }
    }

    private void updateFlightSeats(String flightNo, int newSeats) {
        List<Flight> flights = CSVHelper.getAllFlights();
        for (Flight flight : flights) {
            if (flight.getFlightNo().equals(flightNo)) {
                flight.setSeats(newSeats);
                break;
            }
        }
        CSVHelper.updateFlights(flights);
    }

    private void cancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel");
            return;
        }

        String bookingId = (String) bookingsModel.getValueAt(selectedRow, 0);
        String status = (String) bookingsModel.getValueAt(selectedRow, 4);

        if (status.equals("cancelled")) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this booking?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Update booking status
            List<Booking> bookings = CSVHelper.getAllBookings();
            for (Booking booking : bookings) {
                if (booking.getBookingId().equals(bookingId)) {
                    booking.setStatus("cancelled");
                    
                    // Restore flight seats
                    restoreFlightSeats(booking.getFlightNo(), booking.getNoOfSeats());
                    break;
                }
            }
            CSVHelper.updateBookings(bookings);
            
            JOptionPane.showMessageDialog(this, "Booking cancelled successfully");
            loadBookings();
            loadFlights();
        }
    }

    private void restoreFlightSeats(String flightNo, int seats) {
        List<Flight> flights = CSVHelper.getAllFlights();
        for (Flight flight : flights) {
            if (flight.getFlightNo().equals(flightNo)) {
                flight.setSeats(flight.getSeats() + seats);
                break;
            }
        }
        CSVHelper.updateFlights(flights);
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
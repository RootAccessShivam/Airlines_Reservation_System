import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    private static final String USER_FILE = "data/users.csv";
    private static final String FLIGHT_FILE = "data/flights.csv";
    private static final String BOOKING_FILE = "data/bookings.csv";

    static {
        // Create data directory and files if they don't exist
        new File("data").mkdirs();
        createFileIfNotExists(USER_FILE, "username,password,email,fullName,age,phone,role");
        createFileIfNotExists(FLIGHT_FILE, "flightNo,airlineName,source,destination,date,time,price,seats");
        createFileIfNotExists(BOOKING_FILE, "bookingId,username,flightNo,noOfSeats,totalAmount,status");
        
        // Add default admin user if not exists
        addDefaultAdmin();
    }

    private static void createFileIfNotExists(String filename, String header) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                FileWriter writer = new FileWriter(file);
                writer.write(header + "\n");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDefaultAdmin() {
        List<User> users = getAllUsers();
        boolean adminExists = users.stream().anyMatch(u -> u.getRole().equals("admin"));
        if (!adminExists) {
            User admin = new User("admin", "admin123", "admin@system.com", "System Admin", 30, "1234567890", "admin");
            saveUser(admin);
        }
    }

    // User methods
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                User user = User.fromString(line);
                if (user != null) users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUser(User user) {
        try (FileWriter fw = new FileWriter(USER_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Flight methods
    public static List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FLIGHT_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                Flight flight = Flight.fromString(line);
                if (flight != null) flights.add(flight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flights;
    }

    public static void saveFlight(Flight flight) {
        try (FileWriter fw = new FileWriter(FLIGHT_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(flight.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateFlights(List<Flight> flights) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FLIGHT_FILE))) {
            out.println("flightNo,airlineName,source,destination,date,time,price,seats");
            for (Flight flight : flights) {
                out.println(flight.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Booking methods
    public static List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKING_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                Booking booking = Booking.fromString(line);
                if (booking != null) bookings.add(booking);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static void saveBooking(Booking booking) {
        try (FileWriter fw = new FileWriter(BOOKING_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(booking.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateBookings(List<Booking> bookings) {
        try (PrintWriter out = new PrintWriter(new FileWriter(BOOKING_FILE))) {
            out.println("bookingId,username,flightNo,noOfSeats,totalAmount,status");
            for (Booking booking : bookings) {
                out.println(booking.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateBookingId() {
        List<Booking> bookings = getAllBookings();
        return "BK" + (bookings.size() + 1);
    }
}
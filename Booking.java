public class Booking {
    private String bookingId;
    private String username;
    private String flightNo;
    private int noOfSeats;
    private double totalAmount;
    private String status; // "confirmed", "cancelled"

    public Booking(String bookingId, String username, String flightNo, int noOfSeats, double totalAmount, String status) {
        this.bookingId = bookingId;
        this.username = username;
        this.flightNo = flightNo;
        this.noOfSeats = noOfSeats;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFlightNo() { return flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }

    public int getNoOfSeats() { return noOfSeats; }
    public void setNoOfSeats(int noOfSeats) { this.noOfSeats = noOfSeats; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return bookingId + "," + username + "," + flightNo + "," + noOfSeats + "," + totalAmount + "," + status;
    }

    public static Booking fromString(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length != 6) return null;
        
        return new Booking(
            data[0], // bookingId
            data[1], // username
            data[2], // flightNo
            Integer.parseInt(data[3]), // noOfSeats
            Double.parseDouble(data[4]), // totalAmount
            data[5]  // status
        );
    }
}
public class Flight {
    private String flightNo;
    private String airlineName;
    private String source;
    private String destination;
    private String date;
    private String time;
    private double price;
    private int seats;

    public Flight(String flightNo, String airlineName, String source, String destination, String date, String time, double price, int seats) {
        this.flightNo = flightNo;
        this.airlineName = airlineName;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
        this.seats = seats;
    }

    // Getters and Setters
    public String getFlightNo() { return flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    @Override
    public String toString() {
        return flightNo + "," + airlineName + "," + source + "," + destination + "," + date + "," + time + "," + price + "," + seats;
    }

    public static Flight fromString(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length != 8) return null;
        
        return new Flight(
            data[0], // flightNo
            data[1], // airlineName
            data[2], // source
            data[3], // destination
            data[4], // date
            data[5], // time
            Double.parseDouble(data[6]), // price
            Integer.parseInt(data[7]) // seats
        );
    }
}
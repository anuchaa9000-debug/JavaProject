import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

// 2.1 
class TicketManager {
    private final ArrayList<TrainTicket> tickets = new ArrayList<>();

    public void addTicket(TrainTicket ticket) {
        tickets.add(ticket);
    }

    public String displayTickets() {
        String output = "";
        for (TrainTicket t : tickets) {
            output += t.printTicket();
            output += " ";
        }
        return output;
    }

    // 3.3
    public String searchByDestination(String searchDestination) {
        String result = "";
        for (TrainTicket t : tickets) {
            if (t.getDestination().equalsIgnoreCase(searchDestination)) {
                result += t.printTicket();
            }
        }
        if (result.isEmpty()) {
            return "No tickets found for destination: " + searchDestination;
        }
        return result;
    }
}

// 2.2 
class TrainTicket {
    private final String passengerName;
    private final String destination;
    private final String ticketClass;
    private final double basePrice;

    public TrainTicket(String passengerName, String destination, String ticketClass, double basePrice) {
        this.passengerName = passengerName;
        this.destination = destination;
        this.ticketClass = ticketClass;
        this.basePrice = basePrice;
    }

    public String getDestination() {
        return destination;
    }

    public double calculateTotalPrice() {
        if (ticketClass.toLowerCase().contains("business")) {
            return basePrice * 1.5;
        } else {
            return basePrice;
        }
    }

    public String printTicket() {
        return "Passenger Name: " + passengerName +
               "  Destination: " + destination +
               "  Ticket Class: " + ticketClass +
               "  Total Cost: R" + calculateTotalPrice();
    }
}

// 3.2
class TicketDB {
    public void insertTicket(String passengerName, String destination, String ticketClass, double price) {
        String url = "jdbc:mysql://localhost:3306/train_db";
        String user = "root";
        String password = "password";

        String sql = "INSERT INTO tickets (passenger_name, destination, ticket_class, price) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, passengerName);
            pstmt.setString(2, destination);
            pstmt.setString(3, ticketClass);
            pstmt.setDouble(4, price);

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 2.3 
public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Train Ticket System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        TicketManager manager = new TicketManager();
        TicketDB db = new TicketDB();

        JTextField nameField = new JTextField(15);
        JTextField destinationField = new JTextField(15);
        JTextField classField = new JTextField(15);
        JTextField priceField = new JTextField(10);
        JTextField searchField = new JTextField(15);

        JTextArea displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);

        JButton addButton = new JButton("Add Ticket");
        JButton searchButton = new JButton("Search");

        frame.add(new JLabel("Passenger Name"));
        frame.add(nameField);

        frame.add(new JLabel("Destination"));
        frame.add(destinationField);

        frame.add(new JLabel("Ticket Class Economy or Business"));
        frame.add(classField);

        frame.add(new JLabel("Base Price"));
        frame.add(priceField);

        frame.add(addButton);

        frame.add(new JLabel("Search Destination"));
        frame.add(searchField);
        frame.add(searchButton);

        frame.add(new JScrollPane(displayArea));

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText().trim();
                String destination = destinationField.getText().trim();
                String ticketClass = classField.getText().trim();
                String priceText = priceField.getText().trim();

                if (name.isEmpty() || destination.isEmpty() || ticketClass.isEmpty() || priceText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields");
                    return;
                }

                if (!(ticketClass.equalsIgnoreCase("economy") || ticketClass.equalsIgnoreCase("business"))) {
                    JOptionPane.showMessageDialog(frame, "Enter Economy or Business");
                    return;
                }

                double price;
                try {
                    price = Double.parseDouble(priceText);
                    if (price <= 0) {
                        JOptionPane.showMessageDialog(frame, "Price must be greater than 0");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Enter a valid number");
                    return;
                }

                TrainTicket ticket = new TrainTicket(name, destination, ticketClass, price);
                manager.addTicket(ticket);

                db.insertTicket(name, destination, ticketClass, price);

                displayArea.setText(manager.displayTickets());

                nameField.setText("");
                destinationField.setText("");
                classField.setText("");
                priceField.setText("");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String search = searchField.getText().trim();
                displayArea.setText(manager.searchByDestination(search));
            }
        });

        frame.setVisible(true);
    }
}
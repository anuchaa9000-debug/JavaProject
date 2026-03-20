import java.util.Scanner;

// 1.1
class TrainTicket {
    private String passengerName;
    private String destination;
    private String ticketClass;
    private double basePrice;

    public TrainTicket(String passengerName, String destination, String ticketClass, double basePrice) {
        this.passengerName = passengerName;
        this.destination = destination;
        this.ticketClass = ticketClass;
        this.basePrice = basePrice;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getDestination() {
        return destination;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public double getBasePrice() {
        return basePrice;
    }

    // 1.2
    public double calculateTotalPrice() {
        if (ticketClass.toLowerCase().contains("business")) {
            return basePrice * 1.5;
        } else {
            return basePrice;
        }
    }

    // 1.4
    public void printTicket() {
        System.out.println("Here are your ticket details");
        System.out.println("Name: " + passengerName);
        System.out.println("Going to: " + destination);
        System.out.println("Ticket type: " + ticketClass);
        System.out.println("Total cost: R" + calculateTotalPrice());
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1.2
        System.out.print("Please enter the passenger name: ");
        String name = scanner.nextLine();

        System.out.print("Please enter the destination: ");
        String destination = scanner.nextLine();

        String ticketClass;

        while (true) {
            System.out.print("Enter ticket class (Economy or Business): ");
            ticketClass = scanner.nextLine().trim().toLowerCase();

            if (ticketClass.contains("economy") || ticketClass.contains("business")) {
                break;
            }

            System.out.println("That is not a valid option, please try again");
        }

        // 1.3
        double basePrice;

        while (true) {
            try {
                System.out.print("Enter the base price of the ticket: ");
                basePrice = Double.parseDouble(scanner.nextLine());

                if (basePrice > 0) {
                    break;
                }

                System.out.println("The price must be more than zero");
            } catch (NumberFormatException e) {
                System.out.println("That is not a valid number, please enter a number");
            }
        }

        TrainTicket ticket = new TrainTicket(name, destination, ticketClass, basePrice);

        ticket.printTicket();

        scanner.close();
    }
}
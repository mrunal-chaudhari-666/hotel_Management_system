import java.io.*;
import java.util.*;

public class HotelManagementSystem {
    static final String FILE_NAME = "guests.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== HOTEL MANAGEMENT SYSTEM ===");
            System.out.println("1. Book a Room");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Check Room Availability");
            System.out.println("4. View All Guests");
            System.out.println("5. Search Guest by Room");
            System.out.println("6. Update Guest Details");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> bookRoom(sc);
                case 2 -> cancelBooking(sc);
                case 3 -> checkAvailability(sc);
                case 4 -> viewAllGuests();
                case 5 -> searchGuest(sc);
                case 6 -> updateGuest(sc);
                case 7 -> System.out.println("Exiting system...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 7);
    }

    // Book a room
    private static void bookRoom(Scanner sc) {
        try {
            System.out.print("Enter guest name: ");
            String name = sc.nextLine();
            System.out.print("Enter contact: ");
            String contact = sc.nextLine();
            System.out.print("Enter room number: ");
            String room = sc.nextLine();

            if (!isRoomAvailable(room)) {
                System.out.println("Room is already booked!");
                return;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(name + "," + contact + "," + room);
            writer.newLine();
            writer.close();
            System.out.println("Room booked successfully!");

        } catch (IOException e) {
            System.out.println("Error booking room: " + e.getMessage());
        }
    }

    // Cancel booking
    private static void cancelBooking(Scanner sc) {
        try {
            System.out.print("Enter room number to cancel booking: ");
            String room = sc.nextLine();

            File inputFile = new File(FILE_NAME);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && !data[2].equals(room)) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    found = true;
                }
            }

            writer.close();
            reader.close();

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                System.out.println("Error processing file.");
            } else if (found) {
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Room not found.");
            }

        } catch (IOException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }

    // Check room availability
    private static void checkAvailability(Scanner sc) {
        System.out.print("Enter room number to check: ");
        String room = sc.nextLine();
        if (isRoomAvailable(room)) {
            System.out.println("Room is available.");
        } else {
            System.out.println("Room is already booked.");
        }
    }

    // View all guests
    private static void viewAllGuests() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\n--- Guest List ---");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    System.out.println("Name: " + data[0] + " | Contact: " + data[1] + " | Room: " + data[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading guest list: " + e.getMessage());
        }
    }

    // Search guest by room number
    private static void searchGuest(Scanner sc) {
        System.out.print("Enter room number to search: ");
        String room = sc.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[2].equals(room)) {
                    System.out.println("Guest Found: Name - " + data[0] + ", Contact - " + data[1]);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No guest found in that room.");
            }
        } catch (IOException e) {
            System.out.println("Error searching guest: " + e.getMessage());
        }
    }

    // Update guest details
    private static void updateGuest(Scanner sc) {
        System.out.print("Enter room number to update: ");
        String room = sc.nextLine();

        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[2].equals(room)) {
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter new contact: ");
                    String newContact = sc.nextLine();
                    writer.write(newName + "," + newContact + "," + room);
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                System.out.println("Error updating file.");
            } else if (updated) {
                System.out.println("Guest record updated.");
            } else {
                System.out.println("Room not found.");
            }

        } catch (IOException e) {
            System.out.println("Error updating guest: " + e.getMessage());
        }
    }

    // Utility: Check if a room is available
    private static boolean isRoomAvailable(String room) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3 && data[2].equals(room)) {
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
        return true;
    }
}

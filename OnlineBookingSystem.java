import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class OnlineBookingSystem {

    static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public boolean checkPassword(String inputPassword) {
            return this.password.equals(inputPassword);
        }
    }
    static class Booking {
        private static int counter = 1;
        private int bookingId;
        private String username;
        private String reservationDetails;

        public Booking(String username, String reservationDetails) {
            this.bookingId = counter++;
            this.username = username;
            this.reservationDetails = reservationDetails;
        }

        public int getBookingId() {
            return bookingId;
        }

        public String getUsername() {
            return username;
        }

        public String getReservationDetails() {
            return reservationDetails;
        }

        @Override
        public String toString() {
            return "Booking ID: " + bookingId + ", User: " + username + ", Details: " + reservationDetails;
        }
    }

    static class Database {
        public static List<User> users = new ArrayList<>();
        public static List<Booking> bookings = new ArrayList<>();

        public static User findUser(String username) {
            for (User user : users) {
                if (user.getUsername().equals(username)) return user;
            }
            return null;
        }

        public static void addUser(User user) {
            users.add(user);
        }

        public static void addBooking(Booking booking) {
            bookings.add(booking);
        }

        public static void viewBookings(String username) {
            boolean found = false;
            for (Booking booking : bookings) {
                if (booking.getUsername().equals(username)) {
                    System.out.println(booking);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No bookings found.");
            }
        }

        public static boolean cancelBooking(int id, String username) {
            Iterator<Booking> iterator = bookings.iterator();
            while (iterator.hasNext()) {
                Booking b = iterator.next();
                if (b.getBookingId() == id && b.getUsername().equals(username)) {
                    iterator.remove();
                    return true;
                }
            }
            return false;
        }
    }

    static Scanner scanner = new Scanner(System.in);
    static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Online Booking Reservation System ---");
            if (currentUser == null) {
                System.out.println("1. Register\n2. Login\n3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: register(); break;
                    case 2: login(); break;
                    case 3: System.exit(0);
                    default: System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("1. Book Reservation\n2. View My Bookings\n3. Cancel Booking\n4. Logout");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1: book(); break;
                    case 2: Database.viewBookings(currentUser.getUsername()); break;
                    case 3: cancel(); break;
                    case 4: currentUser = null; System.out.println("Logged out."); break;
                    default: System.out.println("Invalid choice.");
                }
            }
        }
    }

    public static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (Database.findUser(username) != null) {
            System.out.println("User already exists.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        Database.addUser(new User(username, password));
        System.out.println("Registration successful.");
    }

    public static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        User user = Database.findUser(username);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (user.checkPassword(password)) {
            currentUser = user;
            System.out.println("Login successful.");
        } else {
            System.out.println("Incorrect password.");
        }
    }

    public static void book() {
        System.out.print("Enter reservation details: ");
        String details = scanner.nextLine();
        Database.addBooking(new Booking(currentUser.getUsername(), details));
        System.out.println("Booking successful.");
    }

    public static void cancel() {
        System.out.print("Enter booking ID to cancel: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        boolean success = Database.cancelBooking(id, currentUser.getUsername());
        if (success) {
            System.out.println("Booking cancelled.");
        } else {
            System.out.println("Booking not found or not owned by you.");
        }
    }
  }

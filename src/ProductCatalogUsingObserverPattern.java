// Importing necessary libraries
import java.util.*;

// Interface for Observer
interface Observer {
    // Method to update the observer with a product name
    void update(String productName);
}

// Concrete class for User implementing the Observer interface
class User implements Observer {
    // User attributes
    private String name;
    private boolean subscribed;

    // Constructor to initialize user with name and subscription status
    public User(String name, boolean subscribed) {
        this.name = name;
        this.subscribed = subscribed;
    }

    // Getter method for user name
    public String getName() {
        return name;
    }

    // Method to check if the user is subscribed
    public boolean isSubscribed() {
        return subscribed;
    }

    // Method to set the subscription status of the user
    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    // Method to update the user with a product name
    @Override
    public void update(String productName) {
        // Display a notification if the user is subscribed
        if (subscribed) {
            System.out.println("User " + name + " received a notification - product: " + productName);
        }
    }
}

// Interface for Observable
interface CatalogObservable {
    // Method to add an observer
    void addObserver(Observer observer);
    // Method to remove an observer
    void removeObserver(Observer observer);
    // Method to notify all observers with a product name
    void notifyObservers(String productName);
}

// Concrete class for Catalog implementing the CatalogObservable interface
class Catalog implements CatalogObservable {
    // List to hold observers
    private List<Observer> observers = new ArrayList<>();
    // Map to hold user information (changed to protected access)
    Map<String, User> users = new HashMap<>();

    // Method to add a user to the catalog with observer pattern
    protected void addUser(User user) {
        observers.add(user);
        users.put(user.getName(), user);
    }

    // Method to add an observer to the catalog
    @Override
    public void addObserver(Observer observer) {
        addUser((User) observer); // Using the protected method to add the observer
    }

    // Method to remove a user from the catalog
    protected void removeUser(String userName) {
        User user = users.remove(userName);
        if (user != null) {
            observers.remove(user);
        }
    }

    // Method to remove an observer from the catalog
    @Override
    public void removeObserver(Observer observer) {
        removeUser(((User) observer).getName()); // Using the protected method to remove the observer
    }

    // Method to notify all observers with a product name
    @Override
    public void notifyObservers(String productName) {
        if (productName != null) {
            for (Observer observer : observers) {
                // Check if the observer is subscribed before updating
                if (((User) observer).isSubscribed()) {
                    observer.update(productName);
                }
            }
        }
    }

    // Method to add a product to the catalog and notify observers
    public void addProduct(String name, String productDescription, double productPrice) {
        if (name != null) {
            notifyObservers(name);
        }
    }
}

// Main class for executing the program
public class ProductCatalogUsingObserverPattern {
    public static void main(String[] args) {
        // Creating a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Creating a catalog instance
        Catalog catalog = new Catalog();

        // Getting user input for user name
        System.out.println("Type the user name: ");
        String name = scanner.nextLine();

        // Asking for user action
        System.out.println("Type 'cancel' to cancel the subscription or any other key to add a new product: ");
        String actionChoice = scanner.nextLine();

        // Handling user action
        if (actionChoice.equalsIgnoreCase("cancel")) {
            // Cancelling the subscription if the user exists
            if (catalog.users.containsKey(name) && catalog.users.get(name) != null) {
                catalog.removeObserver(catalog.users.get(name));
                System.out.println("Subscription canceled successfully.");
            } else {
                System.out.println("User not found.");
            }
        } else {
            // Adding a new product
            System.out.println("Type the product name: ");
            String productName = scanner.nextLine();
            System.out.println("Type the product description: ");
            String productDescription = scanner.nextLine();
            System.out.println("Type the product price: ");
            double productPrice;
            while (true) {
                try {
                    productPrice = scanner.nextDouble();
                    break;  // Exit the loop if valid input is provided
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid price.");
                    scanner.next();  // Clear the invalid input
                }
            }

            // Consuming the newline character after nextDouble
            scanner.nextLine();

            // Adding the product to the catalog
            catalog.addProduct(productName, productDescription, productPrice);
            System.out.println("Product added successfully.");
        }

        // To prevent resource leaks, you should close the Scanner object after its use. You can do this by adding the following line at the end of the main method:
        scanner.close();
    }
}

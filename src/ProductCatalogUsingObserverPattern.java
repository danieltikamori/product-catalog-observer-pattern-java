import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Interface Observer
interface Observer {
    void update(String productName);
}

// Classe concreta Observer
class User implements Observer {
    private String name;
    private boolean subscribed;

    public User(String name, boolean subscribed) {
        this.name = name;
        this.subscribed = subscribed;
    }

    public String getName() { // Added getter method for name
        return name;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public void update(String productName) {
        if (subscribed) {
            System.out.println("Notificacao recebida: Novo produto adicionado - " + productName);
        }
    }
}

// Interface Observable
interface CatalogObservable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String productName);
}

// Classe concreta Observable
class Catalog implements CatalogObservable {
    private List<Observer> observers = new ArrayList<>();
    Map<String, User> users = new HashMap<>(); // Changed to protected access

    protected void addUser(User user) { // Added protected method to add users
        observers.add(user);
        users.put(user.getName(), user);
    }

    @Override
    public void addObserver(Observer observer) {
        addUser((User) observer); // Use the protected method
    }

    protected void removeUser(String userName) { // Added protected method to remove users
        User user = users.remove(userName);
        if (user != null) {
            observers.remove(user);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        removeUser(((User) observer).getName()); // Use the protected method
    }

    @Override
    public void notifyObservers(String productName) {
        for (Observer observer : observers) {
            if (((User) observer).isSubscribed()) {
                observer.update(productName);
            }
        }
    }

    public void addProduct(String name, String description, double price) {
        notifyObservers(name);
    }
}

public class ProductCatalogUsingObserverPattern {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Criando catálogo e usuário
        Catalog catalog = new Catalog();

        // Entrada do nome do usuário
        System.out.println("Digite o seu nome: ");
        String name = scanner.nextLine();

        // Verificação da ação desejada
        System.out.println("Digite 'cancelar' para cancelar a assinatura ou qualquer outra tecla para continuar: ");
        String actionChoice = scanner.nextLine();

        if (actionChoice.equalsIgnoreCase("cancelar")) {
            // Cancelamento da assinatura
            if (catalog.users.containsKey(name)) {
                catalog.removeObserver(catalog.users.get(name));
                System.out.println("Assinatura cancelada com sucesso!");
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } else {
            // Adição de novo produto
            System.out.println("Digite o nome do produto: ");
            String productName = scanner.nextLine();
            System.out.println("Digite a descrição do produto: ");
            String productDescription = scanner.nextLine();
            System.out.println("Digite o preço do produto: ");
            double productPrice = scanner.nextDouble();

            scanner.nextLine(); // Consumir a quebra de linha após nextDouble

            // Adição do produto ao catálogo
            catalog.addProduct(productName, productDescription, productPrice);
            System.out.println("Produto adicionado com sucesso!");
        }
    }
}

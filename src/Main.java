public class Main {
    private static ProductCatalog catalog = new ProductCatalog();
    private static ShoppingCart cart = new ShoppingCart(catalog);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            catalog.saveProducts();
            cart.saveCart();
        }));

        System.out.println("=== SHOPPING SYSTEM ===");
        
        while (true) {
            System.out.println("\n1. Customer Menu");
            System.out.println("2. Admin Menu");
            System.out.println("3. Exit");
            
            int choice = UserInput.getIntInput("Select option: ");
            
            switch (choice) {
                case 1 -> customerMenu();
                case 2 -> adminMenu();
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void customerMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Browse Products");
            System.out.println("2. View Cart");
            System.out.println("3. Add to Cart");
            System.out.println("4. Back to Main Menu");

            int choice = UserInput.getIntInput("Choose option: ");
            
            switch (choice) {
                case 1 -> catalog.displayProducts();
                case 2 -> cart.displayCart();
                case 3 -> {
                    String id = UserInput.getStringInput("Enter product ID: ");
                    int qty = UserInput.getIntInput("Enter quantity: ");
                    cart.addItem(id, qty);
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void adminMenu() {
        if (!"admin123".equals(UserInput.getStringInput("Enter admin password: "))) {
            System.out.println("Access denied!");
            return;
        }

        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View Products");
            System.out.println("2. Add Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Restock Product");
            System.out.println("5. Back to Main Menu");

            int choice = UserInput.getIntInput("Choose option: ");
            
            switch (choice) {
                case 1 -> catalog.displayProducts();
                case 2 -> addProduct();
                case 3 -> deleteProduct();
                case 4 -> restockProduct();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void addProduct() {
        String id = UserInput.getStringInput("Enter product ID: ");
        String name = UserInput.getStringInput("Enter product name: ");
        String category = UserInput.getStringInput("Enter category: ");
        double price = UserInput.getDoubleInput("Enter price: ");
        int stock = UserInput.getIntInput("Enter stock quantity: ");
        
        if (catalog.addProduct(new Product(id, name, category, price, stock))) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Product ID already exists!");
        }
    }

    private static void deleteProduct() {
        catalog.displayProducts();
        String id = UserInput.getStringInput("Enter product ID to delete: ");
        if (UserInput.getConfirmation("Are you sure you want to permanently delete this product?")) {
            if (catalog.deleteProduct(id)) {
                System.out.println("Product deleted permanently!");
            } else {
                System.out.println("Product not found!");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void restockProduct() {
        catalog.displayProducts();
        String id = UserInput.getStringInput("Enter product ID to restock: ");
        int amount = UserInput.getIntInput("Enter restock amount: ");
        if (catalog.restockProduct(id, amount)) {
            System.out.println("Restock successful!");
        } else {
            System.out.println("Product not found!");
        }
    }
}
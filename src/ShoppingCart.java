import java.util.*;

public class ShoppingCart {
    private static final String DATA_FILE = "cart.dat";
    private Map<String, Integer> items = new HashMap<>();
    private ProductCatalog catalog;

    public ShoppingCart(ProductCatalog catalog) {
        this.catalog = catalog;
        loadCart();
        removeInvalidItems();
    }

    @SuppressWarnings("unchecked")
    private void loadCart() {
        Object loaded = FileHandler.loadData(DATA_FILE);
        if (loaded instanceof Map) {
            items = (Map<String, Integer>) loaded;
        }
    }

    public void saveCart() {
        FileHandler.saveData(DATA_FILE, items);
    }

    public void addItem(String productId, int quantity) {
        Product product = catalog.getProduct(productId);
        if (product == null) {
            System.out.println("Error: Product not found!");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Error: Quantity must be positive!");
            return;
        }
        if (product.getStock() < quantity) {
            System.out.printf("Error: Only %d available in stock!\n", product.getStock());
            return;
        }
        
        product.setStock(product.getStock() - quantity);
        items.merge(productId, quantity, Integer::sum);
        saveCart();
        catalog.saveProducts();
        System.out.printf("Added %d x %s to cart\n", quantity, product.getName());
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        System.out.println("\n=== YOUR CART ===");
        double total = 0;
        boolean invalidItems = false;
        
        // Create a copy to avoid concurrent modification
        Map<String, Integer> itemsCopy = new HashMap<>(items);
        
        for (Map.Entry<String, Integer> entry : itemsCopy.entrySet()) {
            Product p = catalog.getProduct(entry.getKey());
            if (p == null) {
                System.out.printf("Warning: Product ID %s not found - removing from cart\n", entry.getKey());
                items.remove(entry.getKey());
                invalidItems = true;
                continue;
            }
            
            int qty = entry.getValue();
            double subtotal = p.getPrice() * qty;
            System.out.printf("%-3d x %-15s $%-6.2f = $%.2f\n",
                qty, p.getName(), p.getPrice(), subtotal);
            total += subtotal;
        }
        
        if (invalidItems) {
            saveCart(); // Save after removing invalid items
        }
        System.out.printf("TOTAL: $%.2f\n", total);
    }

    private void removeInvalidItems() {
        items.keySet().removeIf(id -> catalog.getProduct(id) == null);
    }
}
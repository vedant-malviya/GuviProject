import java.util.*;

public class ProductCatalog {
    private static final String DATA_FILE = "products.dat";
    private Map<String, Product> products = new HashMap<>();

    public ProductCatalog() {
        loadProducts();
        if (products.isEmpty()) {
            initializeDefaultProducts();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadProducts() {
        Object loaded = FileHandler.loadData(DATA_FILE);
        if (loaded instanceof List) {
            for (Product p : (List<Product>) loaded) {
                products.put(p.getId(), p);
            }
        }
    }

    private void initializeDefaultProducts() {
        addProduct(new Product("P100", "T-Shirt", "Clothing", 19.99, 50));
        addProduct(new Product("P200", "Jeans", "Clothing", 49.99, 30));
        addProduct(new Product("P300", "Sneakers", "Footwear", 79.99, 20));
        saveProducts();
    }

    public void saveProducts() {
        FileHandler.saveData(DATA_FILE, new ArrayList<>(products.values()));
    }

    public void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("\n=== AVAILABLE PRODUCTS ===");
        products.values().forEach(p -> System.out.println(p.toDisplayString()));
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public boolean addProduct(Product product) {
        if (product == null || products.containsKey(product.getId())) {
            return false;
        }
        products.put(product.getId(), product);
        saveProducts();
        return true;
    }

    public boolean deleteProduct(String id) {
        if (products.remove(id) != null) {
            saveProducts();
            return true;
        }
        return false;
    }

    public boolean restockProduct(String id, int quantity) {
        Product p = products.get(id);
        if (p != null && quantity > 0) {
            p.setStock(p.getStock() + quantity);
            saveProducts();
            return true;
        }
        return false;
    }
}
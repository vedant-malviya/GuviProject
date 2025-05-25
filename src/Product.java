import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String name;
    private final String category;
    private final double price;
    private int stock;

    public Product(String id, String name, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    // Setters
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%.2f|%d", id, name, category, price, stock);
    }

    public static Product fromString(String text) {
        String[] parts = text.split("\\|");
        if (parts.length != 5) return null;
        
        try {
            return new Product(
                parts[0],
                parts[1],
                parts[2],
                Double.parseDouble(parts[3]),
                Integer.parseInt(parts[4]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String toDisplayString() {
        return String.format("[%s] %-15s %-10s $%-6.2f (Stock: %d)",
               id, name, category, price, stock);
    }
}
import java.io.*;
import java.util.*;

public class FileHandler {
    public static void saveData(String filename, Object data) {
        new File("data").mkdirs();
        
        if (data instanceof Map) {
            saveCartData(filename, (Map<?, ?>) data);
        } else if (data instanceof Collection) {
            saveProductData(filename, (Collection<?>) data);
        } else {
            System.err.println("Unsupported data type for saving");
        }
    }

    private static void saveCartData(String filename, Map<?, ?> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + filename))) {
            for (Map.Entry<?, ?> entry : data.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving cart data: " + e.getMessage());
        }
    }

    private static void saveProductData(String filename, Collection<?> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + filename))) {
            for (Object item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving product data: " + e.getMessage());
        }
    }

    public static Object loadData(String filename) {
        File file = new File("data/" + filename);
        if (!file.exists()) return null;

        if (filename.equals("cart.dat")) {
            return loadCartData(file);
        } else {
            return loadProductData(file);
        }
    }

    private static Map<String, Integer> loadCartData(File file) {
        Map<String, Integer> cart = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    cart.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading cart data: " + e.getMessage());
        }
        return cart;
    }

    private static List<Product> loadProductData(File file) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product product = Product.fromString(line);
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading product data: " + e.getMessage());
        }
        return products;
    }
}
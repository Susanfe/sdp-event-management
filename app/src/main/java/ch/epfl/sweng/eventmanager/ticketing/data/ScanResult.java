package ch.epfl.sweng.eventmanager.ticketing.data;

import android.support.annotation.Nullable;

import java.util.Map;

/**
 * The result of a scan agains the API. Any of the fields can be present, depending on the way the
 * remote service works.
 * @author Louis Vialar
 */
public class ScanResult extends ApiResult {
    /**
     * The product corresponding to the barcode, if the scan was successful
     */
    @Nullable
    private Product product;
    /**
     * Multiple products corresponding to the barcode, if the scan was successful
     */
    @Nullable
    private Map<Product, Integer> products;
    /**
     * Client responsible for the order, if the scan was successful
     */
    @Nullable
    private Client user;

    public static class Product {
        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Client {
        private String lastname;
        private String firstname;
        private String email;

        public String getLastname() {
            return lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getEmail() {
            return email;
        }
    }

    @Nullable
    public Product getProduct() {
        return product;
    }

    @Nullable
    public Map<Product, Integer> getProducts() {
        return products;
    }

    @Nullable
    public Client getUser() {
        return user;
    }
}

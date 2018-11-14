package ch.epfl.sweng.eventmanager.ticketing.data;

import androidx.annotation.Nullable;

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

    public ScanResult(@Nullable Product product, @Nullable Map<Product, Integer> products, @Nullable Client user) {
        super(product != null || products != null || user != null);
        this.product = product;
        this.products = products;
        this.user = user;
    }

    public ScanResult() {
    }

    public static class Product {
        private String name;
        private String description;

        public Product() {
        }

        public Product(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Product product = (Product) o;

            if (getName() != null ? !getName().equals(product.getName()) : product.getName() != null) return false;
            return getDescription() != null ? getDescription().equals(product.getDescription()) : product.getDescription() == null;
        }

        @Override
        public int hashCode() {
            int result = getName() != null ? getName().hashCode() : 0;
            result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
            return result;
        }
    }

    public static class Client {
        private String lastname;
        private String firstname;
        private String email;

        public Client() {
        }

        public Client(String lastname, String firstname, String email) {
            this.lastname = lastname;
            this.firstname = firstname;
            this.email = email;
        }

        public String getLastname() {
            return lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Client client = (Client) o;

            if (getLastname() != null ? !getLastname().equals(client.getLastname()) : client.getLastname() != null)
                return false;
            if (getFirstname() != null ? !getFirstname().equals(client.getFirstname()) : client.getFirstname() != null)
                return false;
            return getEmail() != null ? getEmail().equals(client.getEmail()) : client.getEmail() == null;
        }

        @Override
        public int hashCode() {
            int result = getLastname() != null ? getLastname().hashCode() : 0;
            result = 31 * result + (getFirstname() != null ? getFirstname().hashCode() : 0);
            result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
            return result;
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

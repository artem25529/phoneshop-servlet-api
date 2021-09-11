package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private long maxId;
    private final List<Product> products;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    private ArrayListProductDao() {
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        readLock.lock();
        Product result;
        try {
            result = products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            readLock.unlock();
        }
        return result;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        List<Product> result;
        try {
            Comparator<Product> comparator = getComparator(sortField);
            result = products.stream()
                    .filter(product -> query == null || query.isEmpty() || queryMatchesProduct(query, product))
                    .filter(product -> product.getPrice() != null && productIsInStock(product))
                    .sorted(SortOrder.asc == sortOrder ? comparator : comparator.reversed())
                    .collect(Collectors.toList());
            result.sort(getSearchComparator(query).reversed());
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    private Comparator<Product> getComparator(SortField sortField) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField == SortField.description) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });

        return comparator;
    }

    private Comparator<Product> getSearchComparator(String query) {
        return Comparator.comparing(product -> {
            int matchesCounter = 0;
            String description = product.getDescription().toLowerCase();
            for (String word : query.split(" ")) {
                if (description.contains(word)) matchesCounter++;
            }
            return matchesCounter;
        });
    }

    private boolean productIsInStock(Product product) {
        return product.getStock() > 0;
    }

    @Override
    public void save(Product product) {
        writeLock.lock();
        try {
            if (product.getId() == null) {
                product.setId(maxId++);
            }
            products.add(product);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) throws ProductNotFoundException {
        writeLock.lock();
        try {
            Product product = getProduct(id);
            products.remove(product);
        } finally {
            writeLock.unlock();
        }
    }

    private boolean queryMatchesProduct(String query, Product product) {
        query = query.toLowerCase();
        String description = product.getDescription().toLowerCase();
        String[] words = query.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (description.contains(words[i])) {
                return true;
            } else if (i < words.length - 1) {
                description = description.substring(description.indexOf(' ') + 1);
            }
        }
        return false;
    }
}

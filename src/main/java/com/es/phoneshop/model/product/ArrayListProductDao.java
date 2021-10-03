package com.es.phoneshop.model.product;

import com.es.phoneshop.model.dao.GenericDaoImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericDaoImpl<Product> implements ProductDao {
    private final List<Product> products;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    private static class SingletonHelper {
        private static final ProductDao INSTANCE = new ArrayListProductDao();
    }

    public static ProductDao getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private ArrayListProductDao() {
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        products = new ArrayList<>();
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        List<Product> result;
        try {
            Comparator<Product> comparator = getComparator(query, sortField, sortOrder);
            result = itemsList.stream()
                    .filter(product -> query == null || query.isEmpty() || queryMatchesDescription(query, product))
                    .filter(product -> product.getPrice() != null && productIsInStock(product))
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    private Comparator<Product> getComparator(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField == SortField.description) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }

        });
        if (sortField == null && query != null && !query.isEmpty()) return getSearchComparator(query).reversed();
        return sortOrder == SortOrder.asc ? comparator : comparator.reversed();
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
    public void delete(Long id) throws Exception {
        writeLock.lock();
        try {
            Product product = get(id);
            itemsList.remove(product);
        } finally {
            writeLock.unlock();
        }
    }

    private boolean queryMatchesDescription(String query, Product product) {
        String[] words = query.trim().toLowerCase().split("\\s");
        String description = product.getDescription().toLowerCase();
        for (String word : words) {
            Pattern pattern = Pattern.compile("(^" + word + "\\s)|(\\s" + word + "\\s)|(\\s" + word + "$)");
            Matcher matcher = pattern.matcher(description);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}

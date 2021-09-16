package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException{
    private Long id;

    public ProductNotFoundException (Long id) {
        this.id = id;
    }

    public ProductNotFoundException(){

    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public Long getId() {
        return id;
    }



}

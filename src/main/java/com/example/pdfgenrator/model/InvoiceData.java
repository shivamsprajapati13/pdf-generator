package com.example.pdfgenrator.model;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceData {
    private String seller;
    private String sellerGstin;
    private String sellerAddress;

    private String buyer;
    private String buyerGstin;
    private String buyerAddress;

    private List<Item> items;
}

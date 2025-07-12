
package com.example.pdfgenrator.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceRequest {

    @NotBlank  private String invoiceNumber;
    @NotBlank  private String customerName;
    @NotNull   private List<@NotBlank String> items;

    @NotNull   @Positive
    private BigDecimal total;


}

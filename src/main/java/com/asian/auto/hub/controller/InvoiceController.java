package com.asian.auto.hub.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asian.auto.hub.apiresponse.ApiResponse;
import com.asian.auto.hub.dto.InvoiceResponseDto;
import com.asian.auto.hub.serviceimpl.InvoicePdfServiceImpl;
import com.asian.auto.hub.serviceimpl.InvoiceServiceImpl;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceServiceImpl invoiceService;
    private final InvoicePdfServiceImpl invoicePdfService;

    
    @GetMapping("/{carPurchaseId}/data")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> getInvoiceData(
            @PathVariable Long carPurchaseId) {
        return ResponseEntity.ok(
                ApiResponse.success(invoiceService.buildInvoiceData(carPurchaseId)));
    }

    
    @GetMapping("/{carPurchaseId}/pdf")
    public ResponseEntity<ByteArrayResource> downloadInvoicePdf(
            @PathVariable Long carPurchaseId) throws Exception {

        byte[] pdfBytes = invoicePdfService.generatePdf(carPurchaseId);

        InvoiceResponseDto data = invoiceService.buildInvoiceData(carPurchaseId);
        String filename = "invoice_" + data.getNumberPlate() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(new ByteArrayResource(pdfBytes));
    }
}
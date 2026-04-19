package com.asian.auto.hub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.asian.auto.hub.apiresponse.ApiResponse;
import com.asian.auto.hub.dto.MonthlyStatementResponseDto;
import com.asian.auto.hub.serviceimpl.MonthlyStatementPdfServiceImpl;
import com.asian.auto.hub.serviceimpl.MonthlyStatementServiceImpl;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/monthly-statement")
@RequiredArgsConstructor
public class MonthlyStatementController {

    private final MonthlyStatementServiceImpl    statementService;
    private final MonthlyStatementPdfServiceImpl pdfService;

    // JSON preview
    @GetMapping("/data")
    public ResponseEntity<ApiResponse<MonthlyStatementResponseDto>> getData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        return ResponseEntity.ok(ApiResponse.success(
                statementService.generateStatement(fromDate, toDate)));
    }

    // PDF download
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate)
            throws Exception {

        MonthlyStatementResponseDto data =
                statementService.generateStatement(fromDate, toDate);
        byte[] pdf = pdfService.generatePdf(data);

        String filename = "statement_" + fromDate + "_to_" + toDate + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

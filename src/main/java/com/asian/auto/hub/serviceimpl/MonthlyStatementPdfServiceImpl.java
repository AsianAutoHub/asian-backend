package com.asian.auto.hub.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.asian.auto.hub.dto.MonthlyStatementResponseDto;
import com.asian.auto.hub.dto.MonthlyStatementResponseDto.CarPurchaseStatDto;
import com.asian.auto.hub.dto.MonthlyStatementResponseDto.UserMetricRangeDto;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class MonthlyStatementPdfServiceImpl {

    public byte[] generatePdf(MonthlyStatementResponseDto data) throws Exception {
        String html = buildHtml(data);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
            return os.toByteArray();
        }
    }

    private String buildHtml(MonthlyStatementResponseDto data) {
        StringBuilder sb = new StringBuilder();

        sb.append("""
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body { font-family: Arial, sans-serif; font-size: 11px;
                       color: #37474f; padding: 20px; }
                .header { background-color: #1a237e; color: white;
                          text-align: center; padding: 16px; margin-bottom: 16px; }
                .header h1 { font-size: 22px; letter-spacing: 2px; }
                .header p  { font-size: 10px; color: #9fa8da; margin-top: 4px; }
                .date-range { background: #e8eaf6; text-align: center;
                              padding: 8px; margin-bottom: 16px; font-size: 11px;
                              font-weight: bold; color: #1a237e; }
                .section-title { background-color: #283593; color: white;
                                 padding: 7px 12px; font-size: 12px;
                                 font-weight: bold; margin: 14px 0 8px; }
                .summary-grid { width: 100%; border-collapse: collapse; margin-bottom: 12px; }
                .summary-grid td { padding: 8px 12px; border: 1px solid #e0e0e0;
                                   font-size: 11px; }
                .summary-grid td.lbl { background: #f5f5f5; font-weight: bold;
                                       width: 30%; color: #37474f; }
                .summary-grid td.val { font-weight: 600; }
                .profit { color: #1b5e20; }
                .loss   { color: #b71c1c; }
                table.data { width: 100%; border-collapse: collapse; margin-bottom: 14px; }
                table.data th { background: #1a237e; color: white; padding: 6px 8px;
                                font-size: 9px; text-align: left; }
                table.data td { padding: 5px 8px; font-size: 9px;
                                border: 1px solid #e0e0e0; }
                table.data tr.even td { background: #f5f5f5; }
                table.data tr.odd  td { background: #ffffff; }
                .chip-sold  { color: #1b5e20; font-weight: bold; }
                .chip-avail { color: #e65100; font-weight: bold; }
                .text-right { text-align: right; }
                .footer { text-align: center; color: #9e9e9e; font-size: 9px;
                          margin-top: 20px; border-top: 1px solid #eee;
                          padding-top: 8px; }
            </style>
            </head><body>
        """);

        // Header
        sb.append("""
            <div class="header">
                <h1>ASIAN AUTO HUB</h1>
                <p>Monthly Statement Report</p>
            </div>
        """);

        // Date Range
        sb.append("<div class=\"date-range\">")
          .append("Statement Period : ").append(data.getFromDate())
          .append(" &nbsp; to &nbsp; ").append(data.getToDate())
          .append("</div>");

        // ── Overall Summary ──
        sb.append("<div class=\"section-title\">Overall Summary</div>");
        sb.append("<table class=\"summary-grid\"><tbody>");
        addSummaryRow(sb, "Total Cars Purchased", String.valueOf(data.getTotalCarsPurchased()));
        addSummaryRow(sb, "Total Cars Sold",       String.valueOf(data.getTotalCarsSold()));
        addSummaryRow(sb, "Total Purchase Amount", fmt(data.getTotalPurchaseAmount()));
        addSummaryRow(sb, "Total Sale Amount",     fmt(data.getTotalSaleAmount()));
        addSummaryRow(sb, "Total DR Expenses",     fmt(data.getTotalDrInRange()));
        addSummaryRow(sb, "Total CR Expenses",     fmt(data.getTotalCrInRange()));
        String profitClass = data.getTotalProfit() >= 0 ? "profit" : "loss";
        sb.append("<tr><td class=\"lbl\">Net Profit / Loss</td>")
          .append("<td class=\"val ").append(profitClass).append("\">")
          .append(fmt(data.getTotalProfit())).append("</td></tr>");
        sb.append("</tbody></table>");

        // ── Car Purchase Details ──
        sb.append("<div class=\"section-title\">Car Purchase Details</div>");
        sb.append("<table class=\"data\"><thead><tr>")
          .append("<th>#</th><th>Plate</th><th>Model</th><th>Purchased From</th>")
          .append("<th>Purchase Amt</th><th>Sale Amt</th><th>Expenses</th>")
          .append("<th>Net Profit</th><th>Purchased By</th><th>Status</th>")
          .append("</tr></thead><tbody>");

        int i = 0;
        for (CarPurchaseStatDto car : data.getCarPurchases()) {
            String rowClass    = (i % 2 == 0) ? "even" : "odd";
            String profitStyle = car.getNetProfit() >= 0 ? "color:#1b5e20" : "color:#b71c1c";
            String statusClass = "SOLD".equals(car.getStatus()) ? "chip-sold" : "chip-avail";
            sb.append("<tr class=\"").append(rowClass).append("\">")
              .append("<td>").append(++i).append("</td>")
              .append("<td><b>").append(car.getNumberPlate()).append("</b></td>")
              .append("<td>").append(car.getCarModel()).append("</td>")
              .append("<td>").append(car.getPurchaseFrom()).append("</td>")
              .append("<td>").append(fmt(car.getPurchaseAmount())).append("</td>")
              .append("<td>").append(car.getSaledAmount() != null
                      ? fmt(car.getSaledAmount()) : "—").append("</td>")
              .append("<td style=\"color:#b71c1c\">")
                      .append(fmt(car.getTotalExpenses())).append("</td>")
              .append("<td style=\"").append(profitStyle).append(";font-weight:bold\">")
                      .append(fmt(car.getNetProfit())).append("</td>")
              .append("<td>").append(car.getPurchasedByName()).append("</td>")
              .append("<td class=\"").append(statusClass).append("\">")
                      .append(car.getStatus()).append("</td>")
              .append("</tr>");
        }
        sb.append("</tbody></table>");

        // ── User Metrics ──
        sb.append("<div class=\"section-title\">User Metrics in Range</div>");
        sb.append("<table class=\"data\"><thead><tr>")
          .append("<th>#</th><th>User</th><th>Invested</th>")
          .append("<th>DR (Out)</th><th>CR (In)</th>")
          .append("<th>Net Expense</th><th>Balance</th>")
          .append("</tr></thead><tbody>");

        int j = 0;
        for (UserMetricRangeDto u : data.getUserMetrics()) {
            String rowClass    = (j % 2 == 0) ? "even" : "odd";
            String balClass    = u.getBalanceAmount() >= 0
                                     ? "color:#1b5e20" : "color:#b71c1c";
            sb.append("<tr class=\"").append(rowClass).append("\">")
              .append("<td>").append(++j).append("</td>")
              .append("<td><b>").append(u.getUserName()).append("</b></td>")
              .append("<td>").append(fmt(u.getAmountInvested())).append("</td>")
              .append("<td style=\"color:#b71c1c\">").append(fmt(u.getTotalDr())).append("</td>")
              .append("<td style=\"color:#1b5e20\">").append(fmt(u.getTotalCr())).append("</td>")
              .append("<td>").append(fmt(u.getNetExpense())).append("</td>")
              .append("<td style=\"").append(balClass).append(";font-weight:bold\">")
                      .append(fmt(u.getBalanceAmount())).append("</td>")
              .append("</tr>");
        }
        sb.append("</tbody></table>");

        // Footer
        sb.append("""
            <div class="footer">
                <p>Generated by Asian Auto Hub Management System</p>
                <p>This is a system-generated report.</p>
            </div>
            </body></html>
        """);

        return sb.toString();
    }

    private void addSummaryRow(StringBuilder sb, String label, String value) {
        sb.append("<tr>")
          .append("<td class=\"lbl\">").append(label).append("</td>")
          .append("<td class=\"val\">").append(value).append("</td>")
          .append("</tr>");
    }

    private String fmt(Double v) {
        if (v == null) return "N/A";
        return String.format("Rs. %,.2f", v);
    }
}
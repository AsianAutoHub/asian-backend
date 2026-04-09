package com.asian.auto.hub.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.asian.auto.hub.dto.InvoiceResponseDto;
import com.asian.auto.hub.dto.InvoiceResponseDto.UserExpenseSummary;
import com.asian.auto.hub.dto.InvoiceResponseDto.UserExpenseSummary.ExpenseDetail;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class InvoicePdfServiceImpl {

	private final InvoiceServiceImpl invoiceService;

	public byte[] generatePdf(Long carPurchaseId) throws Exception {
		InvoiceResponseDto data = invoiceService.buildInvoiceData(carPurchaseId);
		String html = buildHtml(data);

		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(html);
			renderer.layout();
			renderer.createPDF(os);
			return os.toByteArray();
		}
	}

	private String buildHtml(InvoiceResponseDto data) {
		StringBuilder sb = new StringBuilder();

		sb.append("""
				    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
				        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
				    <html xmlns="http://www.w3.org/1999/xhtml">
				    <head>
				    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
				    <style>
				        * { margin: 0; padding: 0; box-sizing: border-box; }
				        body {
				            font-family: Arial, sans-serif;
				            font-size: 12px;
				            color: #37474f;
				            padding: 24px;
				            background: #ffffff;
				        }

				        /* Header */
				        .header {
				            background-color: #1a237e;
				            color: white;
				            text-align: center;
				            padding: 18px 10px 12px;
				        }
				        .header h1 {
				            font-size: 26px;
				            letter-spacing: 3px;
				            margin-bottom: 4px;
				        }
				        .header p { font-size: 11px; color: #c5cae9; }

				        /* Section Title */
				        .section-title {
				            background-color: #283593;
				            color: white;
				            padding: 8px 12px;
				            font-size: 13px;
				            font-weight: bold;
				            margin: 18px 0 8px 0;
				        }

				        /* Info Grid */
				        .info-table {
				            width: 100%;
				            border-collapse: collapse;
				            margin-bottom: 4px;
				        }
				        .info-table td {
				            padding: 6px 10px;
				            font-size: 11px;
				            border: 1px solid #cfd8dc;
				        }
				        .info-table td.lbl {
				            background-color: #f5f5f5;
				            font-weight: bold;
				            width: 20%;
				            color: #37474f;
				        }
				        .info-table td.val { width: 30%; }

				        /* User Block */
				        .user-block {
				            background-color: #e8eaf6;
				            color: #1a237e;
				            font-weight: bold;
				            padding: 7px 12px;
				            font-size: 11px;
				            border: 1px solid #9fa8da;
				            margin-top: 10px;
				        }

				        /* Expense Table */
				        .expense-table {
				            width: 100%;
				            border-collapse: collapse;
				        }
				        .expense-table th {
				            background-color: #1a237e;
				            color: white;
				            padding: 7px 8px;
				            font-size: 10px;
				            text-align: center;
				        }
				        .expense-table td {
				            padding: 5px 8px;
				            font-size: 10px;
				            border: 1px solid #cfd8dc;
				        }
				        .even td { background-color: #f5f5f5; }
				        .odd  td { background-color: #ffffff; }
				        .subtotal td {
				            background-color: #1a237e;
				            color: white;
				            font-weight: bold;
				            text-align: right;
				            border: none;
				            padding: 5px 8px;
				        }
				        .text-right  { text-align: right; }
				        .text-center { text-align: center; }

				        /* Divider */
				        .divider {
				            border-top: 1.5px solid #1a237e;
				            margin: 16px 0 10px 0;
				        }

				        /* Summary */
				        .summary-table {
				            width: 100%;
				            border-collapse: collapse;
				        }
				        .summary-table td {
				            padding: 5px 8px;
				            font-size: 11px;
				            text-align: right;
				        }
				        .sum-label { color: #546e7a; font-weight: bold; width: 75%; }
				        .sum-value { width: 25%; }
				        .profit td {
				            background-color: #e8f5e9;
				            color: #1b5e20;
				            font-weight: bold;
				            font-size: 13px;
				            border-top: 2px solid #1a237e;
				            padding: 8px;
				        }
				        .loss td {
				            background-color: #ffebee;
				            color: #b71c1c;
				            font-weight: bold;
				            font-size: 13px;
				            border-top: 2px solid #1a237e;
				            padding: 8px;
				        }

				        /* Footer */
				        .footer {
				            text-align: center;
				            color: #9e9e9e;
				            font-size: 9px;
				            margin-top: 20px;
				            border-top: 1px solid #eeeeee;
				            padding-top: 8px;
				        }
				    </style>
				    </head>
				    <body>
				""");

		// ══ HEADER ══
		sb.append("""
				    <div class="header">
				        <h1>ASIAN AUTO HUB</h1>
				        <p>Vehicle Purchase and Expense Invoice</p>
				    </div>
				""");

		// ══ SECTION 1 ══
		sb.append("<div class=\"section-title\">Section 1 : Car Purchase Details</div>");
		sb.append("<table class=\"info-table\"><tbody>");
		sb.append("<tr>").append(infoCell("Registration Number", data.getNumberPlate()))
				.append(infoCell("Make Model", data.getCarModel())).append("</tr>");

		sb.append("<tr>").append(infoCell("Colour", data.getColour()))
				.append(infoCell("Fuel Type", data.getFuelType().toString())).append("</tr>");

		sb.append("<tr>").append(infoCell("Kmps", data.getKmps().toString()))
				.append(infoCell("Manufactured Year", data.getManufacturedYear().toString())).append("</tr>");

		sb.append("<tr>").append(infoCell("Purchased From", data.getPurchaseFrom()))
				.append(infoCell("Purchased By", data.getPurchasedByName())).append("</tr>");
		sb.append("<tr>").append(infoCell("Purchase Date", str(data.getPurchaseDate())))
				.append(infoCell("Purchase Amount", fmt(data.getPurchaseAmount()))).append("</tr>");
		sb.append("<tr>").append(infoCell("Sale Date", str(data.getSaledDate())))
				.append(infoCell("Sale Amount", fmt(data.getSaledAmount()))).append("</tr>");
		sb.append("</tbody></table>");

		// ══ SECTION 2 ══
		sb.append("<div class=\"section-title\">Section 2 : Expense Details by User</div>");

		for (UserExpenseSummary summary : data.getUserExpenseSummaries()) {
			sb.append("<div class=\"user-block\">").append(summary.getUserName()).append("</div>");

			sb.append("<table class=\"expense-table\">").append("<thead><tr>").append("<th style=\"width:16%;\">Date</th>")
					.append("<th style=\"width:32%; text-align:left;\">Purpose</th>")
					.append("<th style=\"width:18%;\">Payment Mode</th>").append("<th style=\"width:18%;\">Payment Type</th>")

					.append("<th style=\"width:16%;\">Amount</th>").append("</tr></thead><tbody>");

			int i = 0;
			for (ExpenseDetail exp : summary.getExpenses()) {
				String rowClass = (i++ % 2 == 0) ? "even" : "odd";
				sb.append("<tr class=\"").append(rowClass).append("\">").append("<td class=\"text-center\">")
						.append(str(exp.getExpenseDate())).append("</td>").append("<td>").append(exp.getPurpose()).append("</td>")
						.append("<td class=\"text-center\">").append(exp.getPaymentMode()).append("</td>")
						.append("<td class=\"text-center\">").append(exp.getPaymentType()).append("</td>")

						.append("<td class=\"text-right\">").append(fmt(exp.getAmount())).append("</td>").append("</tr>");
			}

			sb.append("<tr class=\"subtotal\">").append("<td colspan=\"3\"></td>").append("<td>Subtotal</td>").append("<td>")
					.append(fmt(summary.getUserTotalAmount())).append("</td>").append("</tr>");

			sb.append("</tbody></table>");
		}

		// ══ SUMMARY ══
		sb.append("<hr class=\"divider\"/>");
		double netProfit = data.getNetProfit() != null ? data.getNetProfit() : 0;
		String profitClass = netProfit >= 0 ? "profit" : "loss";
		String profitLabel = netProfit >= 0 ? "Net Profit" : "Net Loss";

		sb.append("<table class=\"summary-table\"><tbody>");
		addSummaryRow(sb, "Purchase Amount", fmt(data.getPurchaseAmount()));
		// addSummaryRow(sb, "Sale Amount", fmt(data.getSaledAmount()));
		addSummaryRow(sb, "Landing Price", fmt(data.getTotalExpenseAmount()));
		sb.append("<tr class=\"").append(profitClass).append("\">")
//        .append("<td class=\"sum-label\">").append(profitLabel).append("</td>")
//        .append("<td class=\"sum-value\">").append(fmt(netProfit)).append("</td>")
				.append("</tr>");
		sb.append("</tbody></table>");

		// ══ FOOTER ══
		sb.append("""
				    <div class="footer">
				        <p>Thank you for choosing Asian Auto Hub</p>
				        <p>This is a system-generated invoice. No signature required.</p>
				    </div>
				    </body>
				    </html>
				""");

		return sb.toString();
	}

	// ── Helpers ──
	private String infoCell(String label, String value) {
		return "<td class=\"lbl\">" + label + "</td>" + "<td class=\"val\">" + (value != null ? value : "N/A") + "</td>";
	}

	private void addSummaryRow(StringBuilder sb, String label, String value) {
		sb.append("<tr>").append("<td class=\"sum-label\">").append(label).append("</td>")
				.append("<td class=\"sum-value\">").append(value).append("</td>").append("</tr>");
	}

	private String fmt(Double amount) {
		if (amount == null)
			return "N/A";
		return String.format("Rs. %,.2f", amount);
	}

	private String str(Object val) {
		return val != null ? val.toString() : "N/A";
	}
}

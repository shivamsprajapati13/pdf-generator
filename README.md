# 🧾 PDF Generator API (Spring Boot)

This Spring Boot application provides a REST API to generate PDF invoices using Thymeleaf templates and stores them locally for reuse. If the same data is sent again, the server returns a download link instead of regenerating the PDF.

---

## 🚀 Features

- Generate invoice PDFs from structured JSON input
- Thymeleaf HTML-to-PDF conversion using OpenHTMLToPDF
- Caching: reuses previously generated PDFs for identical requests
- Download PDFs via REST endpoints
- Clean, modular service/controller structure

---

## 📦 Technologies Used

- Java 17
- Spring Boot 3.5.3
- Thymeleaf (Template Engine)
- OpenHTMLToPDF (PDF rendering)
- Jackson (for JSON processing)
- Lombok
- Maven

---

## 📁 Directory Structure
```
src/
└── main/
├── java/
│ └── com/example/pdfgenrator/
│ ├── controller/
│ │ └── PdfController.java
│ ├── model/
│ │ └── InvoiceData.java
| | └── Item.java
│ ├── service/
│ │ └── PdfService.java
│ └── util/
│ └── FileStore.java
└── resources/
├── templates/
│ └── invoice.html
└── pdf-store
```


---

## 📄 Sample Request Payload
```
{
  "seller": "XYZ Pvt. Ltd.",
  "sellerGstin": "29AABBCCDD121ZD",
  "sellerAddress": "New Delhi, India",
  "buyer": "Vedant Computers",
  "buyerGstin": "29AABBCCDD131ZD",
  "buyerAddress": "New Delhi, India",
  "items": [
    {
      "name": "Product 1",
      "quantity": "12 Nos",
      "rate": 123.00,
      "amount": 1476.00
    }
  ]
}
```

## 🔌 API Endpoints
🧾 POST /pdf/download

Generates and returns a PDF invoice based on the request.
If an identical invoice was previously submitted, returns a JSON link to download instead of regenerating.

### ✅ Response (New PDF):
- HTTP 200 OK
- Content-Type: application/pdf
- Headers:
  - Content-Disposition: attachment; filename="b21d5951fd7095cd19fe1a3252bfc0b66cf9dd19d722f18aee90332a0b2fc65e.pdf"
  - X-Filename: b21d5951fd7095cd19fe1a3252bfc0b66cf9dd19d722f18aee90332a0b2fc65e.pdf

### ✅ Response (Already Exists):
```
{
    "downloadUrl": "/pdf/b21d5951fd7095cd19fe1a3252bfc0b66cf9dd19d722f18aee90332a0b2fc65e",
    "filename": "b21d5951fd7095cd19fe1a3252bfc0b66cf9dd19d722f18aee90332a0b2fc65e.pdf",
    "message": "PDF already exists"
}
```

### 📥 GET /pdf/{id}
Downloads the PDF from the cache using its ID.
- Example: GET /pdf/0e2860ac125ec5db55ba72728efc95ac00b71e04da985f119e580107691f3b79

### 🛠️ Setup & Run
#### 1. Clone the repository:
```
git clone https://github.com/shivamsprajapati13/pdf-generator.git
cd pdf-generator
```

#### 2. Build and run:
```
mvn spring-boot:run
```

#### 3. The app will start at:
```
http://localhost:9090
```

## 📝 Notes
- PDFs are stored in src/main/resources/pdf-store/ with hashed filenames based on input JSON.

- Template file: src/main/resources/templates/invoice.html

- You can modify the template to customize invoice layout.

- Uses ObjectMapper to hash the request body and avoid regenerating for duplicate inputs.
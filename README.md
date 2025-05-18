<p align="center">
  <!-- Dark mode -->
  <img src="https://github.com/magnusgbjerke/accounting-system-backend/blob/main/logo/accounting-book-svgrepo-com-dark-mode.svg#gh-dark-mode-only" alt="accounting-book-svgrepo-com-dark-mode" width="170" />

  <!-- Light mode -->
  <img src="https://github.com/magnusgbjerke/accounting-system-backend/blob/main/logo/accounting-book-svgrepo-com-light-mode.svg#gh-light-mode-only" alt="accounting-book-svgrepo-com-light-mode" width="170" />
</p>

<h3 align="center">Accounting System</h3>

<p align="center">
    <i>Accounting system built on Norwegian bookkeeping principles.</i>
    <br />
<br />
    <a href="#Introduction"><strong>Introduction</strong></a> ·
    <a href="#Features"><strong>Features</strong></a> ·
    <a href="#Technologies"><strong>Technologies</strong></a> ·
    <a href="#Concept-Clarification"><strong>Concept Clarification</strong></a> ·
    <a href="#Getting-Started"><strong>Getting Started</strong></a>
</p>

## Introduction
An accounting system built on Norwegian bookkeeping principles, designed to provide a user-centric experience, while ensuring full control over financial transactions.
It includes a comprehensive logging system that tracks every stage of a voucher's lifecycle, from initial creation to updates 
and even deletion (reversal), ensuring complete transparency and traceability.

## Features
- **Vouchers**: Create, get, update and delete vouchers
- **Temporary Vouchers**: Create, get, update and delete temporary vouchers
- **Report**: Get financial transactions
- Preloaded with 200 voucher entries, providing a realistic dataset for testing and development.
- Logging feature that tracks each voucher's lifecycle.
- Documentation with Swagger
- Includes validation of fields and proper api responses

## Technologies

- **Backend**: Spring Boot with Maven and Java
- **API Docs**: Swagger UI and OpenAPI v3
- **DB**: PostgreSQL

## Concept Clarification
**Voucher:** Voucher that are posted. They follow conventional voucher rules.

**TempVoucher:** Temporary vouchers that are not yet posted. They are created for provisional use and may not necessarily follow conventional voucher rules. Can be deleted.

**ReverseVoucher:** A special type of voucher specifically designed to reverse another voucher. It mirrors the original voucher with opposite (negative) values to effectively cancel its impact.

**UpdateVoucher:** A special type of voucher used to modify or adjust an existing voucher.

**PostingSets:** Groups of multiple postings, characterized by sharing the same date.

**FMS:** File Management System. The fms folder serves as an example of a basic file management system.

## Getting Started
Run with Docker.

### Prerequisites
- Docker Desktop installed.

### Run with Docker
1. Clone the repository:

   ```bash
   git clone https://github.com/magnusgbjerke/accounting-system-backend.git
   ```

2. Run Docker Compose Up

    ```bash
    cd accounting-system-backend
    docker compose up -d
   ```

   The app should now be running on localhost:8080.

   Swagger UI --> [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

   OpenAPI v3 --> [localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

   PgAdmin(Web-version) --> [localhost:80](http://localhost:80).

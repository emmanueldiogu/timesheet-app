# Timesheet API Documentation

## Overview
Employee timesheet management system with Keycloak JWT authentication.

**Base URL:** `http://localhost:8080`
**Authentication:** Bearer JWT token from Keycloak

---

## Employees

### Get All Employees
**Endpoint:** `GET /api/employees`
**Role:** `ADMIN`
**Description:** Retrieve paginated list of employees with optional filters

**Query Parameters:**
- `search` (optional): Search term for filtering employees
- `status` (optional): Filter by employee status (ACTIVE, INACTIVE)
- `role` (optional): Filter by employee role
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sort` (optional): Sort field and direction

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "uuid",
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "roles": ["EMPLOYEE"],
      "status": "ACTIVE",
      "hireDate": "2024-01-01",
      "baseSalary": 50000.00,
      "hourlyRate": 25.00,
      "createdAt": "2024-01-01T00:00:00Z",
      "updatedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 100,
  "totalPages": 5
}
```

---

### Get Current Employee
**Endpoint:** `GET /api/employees/me`
**Role:** `ADMIN` or `EMPLOYEE`
**Description:** Get authenticated user's employee profile. Creates profile if not exists.

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["EMPLOYEE"],
  "status": "ACTIVE",
  "hireDate": "2024-01-01",
  "baseSalary": 50000.00,
  "hourlyRate": 25.00,
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T00:00:00Z"
}
```

---

## Time Entries

### Clock In
**Endpoint:** `POST /api/time-entries/clock-in`
**Role:** `ADMIN` or `EMPLOYEE`
**Description:** Record clock-in time for authenticated user. Creates new time entry for today.

**Request:** No body required

**Response:** `201 Created`
```json
{
  "id": "uuid",
  "employeeId": "uuid",
  "employeeName": "",
  "workDate": "2026-02-21",
  "clockIn": "2026-02-21T09:00:00",
  "clockOut": null,
  "totalHours": null,
  "source": "AUTO",
  "status": "PENDING",
  "notes": null,
  "createdAt": "2026-02-21T09:00:00",
  "updatedAt": "2026-02-21T09:00:00"
}
```

**Error Responses:**
- `409 Conflict`: Already clocked in today
- `401 Unauthorized`: Invalid or missing JWT token

---

### Clock Out
**Endpoint:** `POST /api/time-entries/clock-out`
**Role:** `ADMIN` or `EMPLOYEE`
**Description:** Record clock-out time for authenticated user. Calculates total hours worked.

**Request:** No body required

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "employeeId": "uuid",
  "employeeName": "",
  "workDate": "2026-02-21",
  "clockIn": "2026-02-21T09:00:00",
  "clockOut": "2026-02-21T17:30:00",
  "totalHours": 8.50,
  "source": "AUTO",
  "status": "PENDING",
  "notes": null,
  "createdAt": "2026-02-21T09:00:00",
  "updatedAt": "2026-02-21T17:30:00"
}
```

**Error Responses:**
- `409 Conflict`: Already clocked out or no active clock-in found
- `401 Unauthorized`: Invalid or missing JWT token

---

### Get Recent Entries
**Endpoint:** `GET /api/time-entries/recent`
**Role:** `ADMIN` or `EMPLOYEE`
**Description:** Retrieve 10 most recent time entries for authenticated user

**Response:** `200 OK`
```json
[
  {
    "id": "uuid",
    "employeeId": "uuid",
    "employeeName": "",
    "workDate": "2026-02-21",
    "clockIn": "2026-02-21T09:00:00",
    "clockOut": "2026-02-21T17:30:00",
    "totalHours": 8.50,
    "source": "AUTO",
    "status": "PENDING",
    "notes": null,
    "createdAt": "2026-02-21T09:00:00",
    "updatedAt": "2026-02-21T17:30:00"
  }
]
```

---

## Authentication

All endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-keycloak-jwt-token>
```

### Obtaining a Token
1. Login to Keycloak at `http://localhost:9080/realms/timesheet`
2. Use the OAuth2/OIDC token endpoint to get a JWT token
3. Include the token in all API requests

---

## API Documentation

**Swagger UI:** `http://localhost:8080/swagger-ui.html`
**OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

---

## Status Codes

- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request parameters
- `401 Unauthorized`: Missing or invalid JWT token
- `403 Forbidden`: Insufficient permissions
- `409 Conflict`: Resource conflict (e.g., already clocked in)
- `500 Internal Server Error`: Unexpected server error

---

## Enums

### Employee Status
- `ACTIVE`: Employee is currently active
- `INACTIVE`: Employee is inactive

### Time Entry Status
- `PENDING`: Entry awaiting approval
- `APPROVED`: Entry has been approved
- `REJECTED`: Entry has been rejected

### Time Entry Source
- `AUTO`: Automatically created by clock-in/out
- `ADMIN_EDIT`: Manually edited by admin

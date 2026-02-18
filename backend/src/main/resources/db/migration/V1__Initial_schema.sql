-- 1. Employees (links to Keycloak user ID)
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    keycloak_user_id VARCHAR(255) UNIQUE NOT NULL,  -- JWT 'sub'
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    hire_date DATE,
    base_salary NUMERIC(10,2),
    hourly_rate NUMERIC(8,2),
    role VARCHAR(20) CHECK (role IN ('ADMIN', 'EMPLOYEE')) NOT NULL DEFAULT 'EMPLOYEE',
    status VARCHAR(20) CHECK (status IN ('ACTIVE', 'INACTIVE')) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_employees_keycloak_user_id ON employees(keycloak_user_id);
CREATE INDEX idx_employees_email ON employees(email);

-- 2. Time entries (clock in/out)
CREATE TABLE time_entries (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT REFERENCES employees(id) ON DELETE CASCADE,
    work_date DATE NOT NULL,
    clock_in TIMESTAMP NOT NULL,
    clock_out TIMESTAMP,
    total_hours NUMERIC(5,2),
    source VARCHAR(20) CHECK (source IN ('AUTO', 'ADMIN_EDIT')) DEFAULT 'AUTO',
    status VARCHAR(20) CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')) DEFAULT 'PENDING',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_time_entries_employee_date ON time_entries(employee_id, work_date);
CREATE INDEX idx_time_entries_status ON time_entries(status);

-- 3. Leave types
CREATE TABLE leave_types (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE CHECK (code IN ('VACATION', 'SICK', 'PTO')),
    name VARCHAR(50) NOT NULL,
    is_paid BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed leave types
INSERT INTO leave_types (code, name, is_paid) VALUES 
('VACATION', 'Vacation', true),
('SICK', 'Sick Leave', true),
('PTO', 'Personal Time Off', true);

-- 4. Leave requests
CREATE TABLE leave_requests (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT REFERENCES employees(id) ON DELETE CASCADE,
    leave_type_id BIGINT REFERENCES leave_types(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_days NUMERIC(4,2) NOT NULL,
    status VARCHAR(20) CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')) DEFAULT 'PENDING',
    reason TEXT,
    approved_by BIGINT REFERENCES employees(id),
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_leave_requests_employee_status ON leave_requests(employee_id, status);
CREATE INDEX idx_leave_requests_dates ON leave_requests(start_date, end_date);

-- 5. Payroll periods
CREATE TABLE payroll_periods (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,  -- '2026-W07'
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    type VARCHAR(20) CHECK (type IN ('WEEKLY', 'BIWEEKLY', 'MONTHLY')) DEFAULT 'WEEKLY',
    status VARCHAR(20) CHECK (status IN ('OPEN', 'CLOSED', 'PAID')) DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(start_date, end_date)
);

CREATE INDEX idx_payroll_periods_dates ON payroll_periods(start_date, end_date);

-- 6. Payslips
CREATE TABLE payslips (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT REFERENCES employees(id) ON DELETE CASCADE,
    payroll_period_id BIGINT REFERENCES payroll_periods(id),
    gross_pay NUMERIC(10,2) NOT NULL,
    total_hours NUMERIC(6,2),
    taxes NUMERIC(10,2) DEFAULT 0,
    deductions NUMERIC(10,2) DEFAULT 0,
    net_pay NUMERIC(10,2) NOT NULL,
    details JSONB,  -- Line items
    status VARCHAR(20) CHECK (status IN ('DRAFT', 'APPROVED', 'PAID')) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payslips_employee_period ON payslips(employee_id, payroll_period_id);

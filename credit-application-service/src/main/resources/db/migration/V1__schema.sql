-- Create affiliates table
CREATE TABLE affiliates (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(20),
    salary DECIMAL(12, 2) NOT NULL CHECK (salary > 0),
    affiliation_date DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    affiliate_id BIGINT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create roles table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Create user_roles junction table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- Create credit_applications table
CREATE TABLE credit_applications (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    requested_amount DECIMAL(12, 2) NOT NULL CHECK (requested_amount > 0),
    term_months INTEGER NOT NULL CHECK (term_months > 0),
    purpose VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    application_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create risk_evaluations table
CREATE TABLE risk_evaluations (
    id BIGSERIAL PRIMARY KEY,
    credit_application_id BIGINT UNIQUE NOT NULL,
    risk_score INTEGER NOT NULL CHECK (risk_score >= 0 AND risk_score <= 1000),
    risk_level VARCHAR(20) NOT NULL,
    evaluation_details VARCHAR(1000),
    evaluation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_affiliates_document ON affiliates(document);
CREATE INDEX idx_affiliates_active ON affiliates(active);
CREATE INDEX idx_affiliates_email ON affiliates(email);
CREATE INDEX idx_credit_applications_status ON credit_applications(status);
CREATE INDEX idx_credit_applications_affiliate_id ON credit_applications(affiliate_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

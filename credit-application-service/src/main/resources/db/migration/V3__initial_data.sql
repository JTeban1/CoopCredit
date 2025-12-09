-- Insert default roles
INSERT INTO roles (name) VALUES 
    ('ROLE_AFILIADO'),
    ('ROLE_ANALISTA'),
    ('ROLE_ADMIN');

-- Insert admin user (password: admin123 - bcrypt encoded)
INSERT INTO users (username, password, email, enabled) VALUES 
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@coopcredit.com', TRUE);

-- Assign ADMIN role to admin user
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- Insert analyst user (password: analyst123 - bcrypt encoded)
INSERT INTO users (username, password, email, enabled) VALUES 
    ('analyst', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'analyst@coopcredit.com', TRUE);

-- Assign ANALISTA role to analyst user
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'analyst' AND r.name = 'ROLE_ANALISTA';

-- Insert sample affiliate
INSERT INTO affiliates (document, first_name, last_name, email, phone, salary, affiliation_date, active) VALUES
    ('1234567890', 'Juan', 'Pérez', 'juan.perez@email.com', '3001234567', 3000000.00, '2024-01-15', TRUE);

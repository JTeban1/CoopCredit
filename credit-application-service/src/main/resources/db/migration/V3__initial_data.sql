-- Insert default roles (only if they don't exist)
INSERT INTO roles (name) VALUES ('ROLE_AFILIADO')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name) VALUES ('ROLE_ANALISTA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name) VALUES ('ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;

-- Insert admin user (password: admin123 - bcrypt encoded) - only if doesn't exist
INSERT INTO users (username, password, email, enabled) VALUES 
    ('admin', '$2a$10$ikbU6fn59ME24k8/mn8kcOBXdc3pGGAyW3RU.ZVFBWPOFYkt13eBy', 'admin@coopcredit.com', TRUE)
ON CONFLICT (username) DO NOTHING;

-- Assign ADMIN role to admin user (only if not already assigned)
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur2 
    WHERE ur2.user_id = u.id AND ur2.role_id = r.id
);

-- Insert analyst user (password: analyst123 - bcrypt encoded) - only if doesn't exist
INSERT INTO users (username, password, email, enabled) VALUES 
    ('analyst', '$2a$10$PXIBg.gzA0dYbeE3umDeFeFKiSRNzFGk1a10SeifbzyYn7kQIPBxy', 'analyst@coopcredit.com', TRUE)
ON CONFLICT (username) DO NOTHING;

-- Assign ANALISTA role to analyst user (only if not already assigned)
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'analyst' AND r.name = 'ROLE_ANALISTA'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur2 
    WHERE ur2.user_id = u.id AND ur2.role_id = r.id
);

-- Insert sample affiliate (only if doesn't exist)
INSERT INTO affiliates (document, first_name, last_name, email, phone, salary, affiliation_date, active) VALUES
    ('1234567890', 'Juan', 'Pérez', 'juan.perez@email.com', '3001234567', 3000000.00, '2024-01-15', TRUE)
ON CONFLICT (document) DO NOTHING;

-- Insert affiliate user (password: affiliate123 - bcrypt encoded) - only if doesn't exist
-- This user is linked to the sample affiliate above
INSERT INTO users (username, password, email, enabled, affiliate_id) VALUES 
    ('affiliate', '$2a$10$2Uo8x/RH18YDeUOme2N2sePtlQBgIfCTMfnP07pTByUA74RJdzUQq', 'affiliate@coopcredit.com', TRUE,
     (SELECT id FROM affiliates WHERE document = '1234567890'))
ON CONFLICT (username) DO NOTHING;

-- Assign AFILIADO role to affiliate user (only if not already assigned)
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'affiliate' AND r.name = 'ROLE_AFILIADO'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur2 
    WHERE ur2.user_id = u.id AND ur2.role_id = r.id
);

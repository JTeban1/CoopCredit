-- Add foreign key constraints
ALTER TABLE users 
    ADD CONSTRAINT fk_users_affiliate 
    FOREIGN KEY (affiliate_id) 
    REFERENCES affiliates(id) 
    ON DELETE SET NULL;

ALTER TABLE user_roles 
    ADD CONSTRAINT fk_user_roles_user 
    FOREIGN KEY (user_id) 
    REFERENCES users(id) 
    ON DELETE CASCADE;

ALTER TABLE user_roles 
    ADD CONSTRAINT fk_user_roles_role 
    FOREIGN KEY (role_id) 
    REFERENCES roles(id) 
    ON DELETE CASCADE;

ALTER TABLE credit_applications 
    ADD CONSTRAINT fk_credit_applications_affiliate 
    FOREIGN KEY (affiliate_id) 
    REFERENCES affiliates(id) 
    ON DELETE CASCADE;

ALTER TABLE risk_evaluations 
    ADD CONSTRAINT fk_risk_evaluations_credit_application 
    FOREIGN KEY (credit_application_id) 
    REFERENCES credit_applications(id) 
    ON DELETE CASCADE;

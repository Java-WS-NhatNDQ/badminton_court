-- tao. 3 user tung` role voi' pass default la`: 12345678
INSERT INTO users (username, password, full_name, email, phone_number, role, is_enabled) VALUES
('admin_user', '$2a$10$UPeqeNecFLfQ9Dx9DeNSXuAjJkEZ8vapf86BIfAXSvCsiWtXVmZMe', 'System Administrator', 'admin@gmail.com', '0987654321', 'ADMIN', 1),
('manager_user', '$2a$10$UPeqeNecFLfQ9Dx9DeNSXuAjJkEZ8vapf86BIfAXSvCsiWtXVmZMe', 'Manager', 'manager@gmail.com', '0987654321', 'MANAGER', 1),
('customer_user', '$2a$10$UPeqeNecFLfQ9Dx9DeNSXuAjJkEZ8vapf86BIfAXSvCsiWtXVmZMe', 'Customer', 'customer1@gmail.com', '0987654321', 'CUSTOMER', 1);
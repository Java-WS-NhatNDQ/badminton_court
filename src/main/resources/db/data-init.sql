-- default password: 12345678
INSERT IGNORE INTO users (username, password, full_name, email, phone_number, role, is_enabled) VALUES
('admin_user', '$2a$10$UPeqeNecFLfQ9Dx9DeNSXuAjJkEZ8vapf86BIfAXSvCsiWtXVmZMe', 'System Administrator', 'admin@gmail.com', '0987654321', 'ADMIN', 1),
('manager_user', '$2a$10$UPeqeNecFLfQ9Dx9DeNSXuAjJkEZ8vapf86BIfAXSvCsiWtXVmZMe', 'Manager', 'manager@gmail.com', '0987654321', 'MANAGER', 1),
('customer_user', '$2a$10$UPeqeNecFLfQ9Dx9DeNSXuAjJkEZ8vapf86BIfAXSvCsiWtXVmZMe', 'Customer', 'customer1@gmail.com', '0987654321', 'CUSTOMER', 1);

INSERT INTO time_slots (id, start_time, end_time, label, is_active, base_price) VALUES
(1, '05:00:00', '06:00:00', '05:00 - 06:00', 1, 60000),
(2, '06:00:00', '07:00:00', '06:00 - 07:00', 1, 60000),
(3, '07:00:00', '08:00:00', '07:00 - 08:00', 1, 60000),
(4, '08:00:00', '09:00:00', '08:00 - 09:00', 1, 70000),
(5, '09:00:00', '10:00:00', '09:00 - 10:00', 1, 70000),
(6, '10:00:00', '11:00:00', '10:00 - 11:00', 1, 70000),
(7, '14:00:00', '15:00:00', '14:00 - 15:00', 1, 80000),
(8, '15:00:00', '16:00:00', '15:00 - 16:00', 1, 80000),
(9, '16:00:00', '17:00:00', '16:00 - 17:00', 1, 90000),
(10, '17:00:00', '18:00:00', '17:00 - 18:00', 1, 90000),
(11, '18:00:00', '19:00:00', '18:00 - 19:00', 1, 100000),
(12, '19:00:00', '20:00:00', '19:00 - 20:00', 1, 100000),
(13, '20:00:00', '21:00:00', '20:00 - 21:00', 1, 100000),
(14, '21:00:00', '22:00:00', '21:00 - 22:00', 1, 90000)
ON DUPLICATE KEY UPDATE
start_time = VALUES(start_time),
end_time = VALUES(end_time),
label = VALUES(label),
is_active = VALUES(is_active),
base_price = VALUES(base_price);

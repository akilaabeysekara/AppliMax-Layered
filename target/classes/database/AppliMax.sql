-- DROP AND CREATE DATABASE
DROP DATABASE IF EXISTS AppliMax;
CREATE DATABASE AppliMax;
USE AppliMax;

-- EMPLOYEE
CREATE TABLE employee (
                          employee_id VARCHAR(10) PRIMARY KEY,
                          name        VARCHAR(50) NOT NULL,
                          nic         VARCHAR(12) UNIQUE NOT NULL,
                          email       VARCHAR(50) UNIQUE,
                          phone_no    VARCHAR(10) UNIQUE,
                          address     VARCHAR(100),
                          role        VARCHAR(20)
);

-- APP_USER
CREATE TABLE app_user (
                          user_id     VARCHAR(10) PRIMARY KEY,
                          employee_id VARCHAR(10) NOT NULL,
                          username    VARCHAR(50) NOT NULL,
                          password    VARCHAR(100) NOT NULL,
                          email       VARCHAR(50) UNIQUE,
                          FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE
);

-- CUSTOMER
CREATE TABLE customer (
                          customer_id VARCHAR(10) PRIMARY KEY,
                          name        VARCHAR(50) NOT NULL,
                          nic         VARCHAR(12) NOT NULL,
                          email       VARCHAR(50) UNIQUE,
                          phone_no    VARCHAR(10) UNIQUE,
                          address     VARCHAR(100)
);

-- CUSTOMER MANAGEMENT
CREATE TABLE customer_management (
                                     user_id           VARCHAR(10),
                                     customer_id       VARCHAR(10),
                                     assigned_date     DATE,
                                     relationship_type VARCHAR(50),
                                     status            VARCHAR(20),
                                     note              TEXT,
                                     PRIMARY KEY (user_id, customer_id),
                                     FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
                                     FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
);

-- ITEM
CREATE TABLE item (
                      item_id   VARCHAR(10) PRIMARY KEY,
                      name      VARCHAR(50) NOT NULL,
                      category  VARCHAR(50),
                      quantity  INT CHECK (quantity >= 1),
                      brand     VARCHAR(50),
                      price     DECIMAL(10,2) NOT NULL
);

-- ORDER
CREATE TABLE order_table (
                             order_id     VARCHAR(10) PRIMARY KEY,
                             customer_id  VARCHAR(10) NOT NULL,
                             order_date   DATE NOT NULL,
                             quantity     INT CHECK (quantity >= 1),
                             unit_price   DECIMAL(10,2) NOT NULL,
                             total_amount DECIMAL(10,2),
                             FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
);

-- ORDER DETAIL
CREATE TABLE order_detail (
                              order_id   VARCHAR(10),
                              item_id    VARCHAR(10),
                              quantity   INT CHECK (quantity >= 1),
                              unit_price DECIMAL(10,2) NOT NULL,
                              PRIMARY KEY (order_id, item_id),
                              FOREIGN KEY (order_id) REFERENCES order_table(order_id) ON DELETE CASCADE,
                              FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
);

-- SUPPLIER
CREATE TABLE supplier (
                          supplier_id VARCHAR(10) PRIMARY KEY,
                          name        VARCHAR(50) NOT NULL,
                          nic         VARCHAR(12) UNIQUE,
                          phone_no    VARCHAR(10),
                          email       VARCHAR(50) UNIQUE,
                          address     VARCHAR(100)
);

-- SUPPLIER EXPENSES
CREATE TABLE supplier_expenses (
                                   expenses_id     VARCHAR(10) PRIMARY KEY,
                                   supplier_id     VARCHAR(10) NOT NULL,
                                   pay_amount      DECIMAL(10,2) NOT NULL,
                                   payment_method  VARCHAR(50),
                                   payment_date    DATE NOT NULL,
                                   FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON DELETE CASCADE
);

-- INVENTORY
CREATE TABLE inventory (
                           inventory_id   VARCHAR(10) PRIMARY KEY,
                           item_id        VARCHAR(10) NOT NULL,
                           supplier_id    VARCHAR(10) NOT NULL,
                           received_date  TIMESTAMP,
                           FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE,
                           FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON DELETE CASCADE
);

-- SALE
CREATE TABLE sale (
                      sale_id      VARCHAR(10) PRIMARY KEY,
                      order_id     VARCHAR(10),
                      item_id      VARCHAR(10),
                      sale_date    DATE NOT NULL,
                      unit_price   DECIMAL(10,2) NOT NULL,
                      quantity     INT NOT NULL CHECK (quantity >= 1),
                      total_amount DECIMAL(10,2) NOT NULL,
                      FOREIGN KEY (order_id) REFERENCES order_table(order_id) ON DELETE SET NULL,
                      FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
);

-- SALES REPORT
CREATE TABLE sales_report (
                              sales_report_id VARCHAR(10) PRIMARY KEY,
                              item_id         VARCHAR(10) NOT NULL,
                              report_date     DATE,
                              unit_price      DECIMAL(10,2) NOT NULL,
                              FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
);

-- ATTENDANCE
CREATE TABLE attendance (
                            attendance_id   VARCHAR(10) PRIMARY KEY,
                            employee_id     VARCHAR(10) NOT NULL,
                            attendance_date DATE,
                            in_time         TIME,
                            out_time        TIME,
                            status          VARCHAR(20),
                            wrk_hours       INT CHECK (wrk_hours >= 0),
                            FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE
);

-- SALARY
CREATE TABLE salary (
                        salary_id     VARCHAR(10) PRIMARY KEY,
                        employee_id   VARCHAR(10) NOT NULL,
                        attendance_id VARCHAR(10),
                        salary_date   VARCHAR(20) NOT NULL,
                        basic_salary  DECIMAL(10,2) NOT NULL,
                        allowances    DECIMAL(10,2),
                        total_salary  DECIMAL(10,2) NOT NULL,
                        FOREIGN KEY (attendance_id) REFERENCES attendance(attendance_id) ON DELETE SET NULL,
                        FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE
);

-- EMAIL LOG
CREATE TABLE email (
                       email_id      VARCHAR(10) PRIMARY KEY,
                       email_address VARCHAR(50) NOT NULL,
                       email_time    TIME,
                       email_date    DATE
);

-- EMPLOYEE
INSERT INTO employee VALUES
                         ('E001', 'Akila Abeysekara', '199915400598', 'akila@gmail.com', '0775051485', 'Colombo', 'admin'),
                         ('E002', 'Kamal Silva', '983938099V', 'kamal@gmail.com', '0773938093', 'Colombo', 'admin'),
                         ('E003', 'Sunil Rathnayake', '198548730487', 'sunil@gmail.com', '0775745634', 'Kandy', 'supervisor'),
                         ('E004', 'Ravi Jayasuriya', '198885747897', 'ravi@gmail.com', '0774967485', 'Matara', 'supervisor');

-- APP_USER
INSERT INTO app_user VALUES
                         ('U001', 'E001','1', '1', 'akila@gmail.com'),
                         ('U002', 'E002','Kamal Silva', 'kamal7890',  'kamal@gmail.com');

-- CUSTOMER
INSERT INTO customer VALUES
                         ('C001', 'Dilani Fernando', '198736574568', 'dilani@gmail.com', '0765268456', 'Negombo'),
                         ('C002', 'Shehan Madushanka', '200348756478', 'shehan@gmail.com', '0762948364', 'Kurunegala'),
                         ('C003', 'Ishara Lakshani', '200165386759', 'ishara@gmail.com', '0769374658', 'Anuradhapura'),
                         ('C004', 'Roshan Silva', '981927357V', 'roshan@gmail.com', '0762948673', 'Jaffna');

-- CUSTOMER MANAGEMENT
INSERT INTO customer_management VALUES
                                    ('U001', 'C001', '2025-05-01', 'primary', 'active', 'Long-term customer'),
                                    ('U001', 'C002', '2025-05-02', 'secondary', 'active', 'Requested email updates'),
                                    ('U001', 'C003', '2025-05-03', 'primary', 'inactive', 'Changed contact preferences'),
                                    ('U001', 'C004', '2025-05-04', 'secondary', 'active', 'Interested in bulk purchases');

-- ITEM
INSERT INTO item VALUES
                     ('I001', 'Fan-70W', 'Fan', 25, 'USHA', 40000.00),
                     ('I002', 'SmartTV-21inches', 'TV', 20, 'Samsung', 45000.00),
                     ('I003', 'Printer-40W', 'Printer', 20, 'Canon', 45000.00),
                     ('I004', 'Speaker-300W', 'Speaker', 15, 'JBL', 90000.00);

-- ORDER
INSERT INTO order_table VALUES
                            ('O001', 'C001', '2025-05-10', 2, 150000.00, 300000.00),
                            ('O002', 'C002', '2025-05-11', 1, 95000.00, 95000.00),
                            ('O003', 'C003', '2025-05-12', 3, 45000.00, 135000.00),
                            ('O004', 'C004', '2025-05-13', 1, 18000.00, 18000.00);

-- ORDER DETAIL
INSERT INTO order_detail VALUES
                             ('O001', 'I001', 2, 150000.00),
                             ('O002', 'I002', 1, 95000.00),
                             ('O003', 'I003', 3, 45000.00),
                             ('O004', 'I004', 1, 18000.00);

-- SUPPLIER
INSERT INTO supplier VALUES
                         ('SU001', 'TechDistributors', '991234567V', '0714736548', 'techdis@gmail.com', 'Colombo7'),
                         ('SU002', 'Electron', '992234567V', '0712946384', 'electron2@gmail.com', 'Colombo5'),
                         ('SU003', 'Fortune', '993234567V', '0711324980', 'fortune0@gmail.com', 'Colombo7'),
                         ('SU004', 'Treasure', '994234567V', '0713027348', 'treasure@gmail.com', 'Colombo8');

-- SUPPLIER EXPENSES
INSERT INTO supplier_expenses VALUES
                                  ('SE001', 'SU001', 150000.00, 'Bank Transfer', '2025-05-05'),
                                  ('SE002', 'SU002', 85000.00, 'Cash', '2025-05-06'),
                                  ('SE003', 'SU003', 110000.00, 'Credit Card', '2025-05-07'),
                                  ('SE004', 'SU004', 20000.00, 'Cheque', '2025-05-08'),
                                  ('SE005', 'SU001', 120000.00, 'Bank Transfer', '2025-05-09');

-- INVENTORY
INSERT INTO inventory VALUES
                          ('IN001', 'I001', 'SU001', '2025-05-01'),
                          ('IN002', 'I002', 'SU002', '2025-05-02'),
                          ('IN003', 'I003', 'SU003', '2025-05-03'),
                          ('IN004', 'I004', 'SU004', '2025-05-04');

-- SALE
INSERT INTO sale VALUES
                     ('S001', 'O001', 'I001', '2025-05-10', 150000.00, 2, 300000.00),
                     ('S002', 'O002', 'I002', '2025-05-11', 95000.00, 1, 95000.00),
                     ('S003', 'O003', 'I003', '2025-05-12', 45000.00, 3, 135000.00),
                     ('S004', 'O004', 'I004', '2025-05-13', 18000.00, 1, 18000.00);

-- SALES REPORT
INSERT INTO sales_report VALUES
                             ('SR001', 'I001', '2025-05-10', 150000.00),
                             ('SR002', 'I002', '2025-05-11', 95000.00),
                             ('SR003', 'I003', '2025-05-12', 45000.00),
                             ('SR004', 'I004', '2025-05-13', 18000.00);

-- ATTENDANCE
INSERT INTO attendance VALUES
                           ('A001', 'E001', '2025-06-01', '09:00:00', '17:00:00', 'Present', 8),
                           ('A002', 'E002', '2025-06-01', '09:15:00', '17:15:00', 'Present', 8),
                           ('A003', 'E003', '2025-06-01', NULL, NULL, 'Absent', 0),
                           ('A004', 'E001', '2025-06-02', '09:05:00', '17:00:00', 'Present', 8),
                           ('A005', 'E002', '2025-06-02', '09:00:00', '12:00:00', 'Half Day', 3);

-- SALARY
INSERT INTO salary VALUES
                       ('SL001', 'E001', 'A001', '2025-06-01', 50000.00, 10000.00, 60000.00),
                       ('SL002', 'E002', 'A002', '2025-06-01', 45000.00,  8000.00, 53000.00),
                       ('SL003', 'E003', 'A003', '2025-06-01', 55000.00, 12000.00, 67000.00);

-- EMAIL
INSERT INTO email VALUES
                      ('EM001', 'dilani@gmail.com', '09:00:00', '2025-05-10'),
                      ('EM002', 'shehan@gmail.com', '10:00:00', '2025-05-11'),
                      ('EM003', 'ishara@gmail.com', '11:00:00', '2025-05-12'),
                      ('EM004', 'roshan@gmail.com', '12:00:00', '2025-05-13');


UPDATE app_user
SET username = '1',
    password = '1',
    email = 'akila@gmail.com'
WHERE user_id = 'U001';

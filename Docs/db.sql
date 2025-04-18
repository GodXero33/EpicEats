DROP DATABASE IF EXISTS epiceats;
CREATE DATABASE epiceats;
USE epiceats;

CREATE TABLE menu_category (
    id INT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE menu_item (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    img VARCHAR(255),
    category_id INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES menu_category(id),
    PRIMARY KEY (id)
);

CREATE TABLE sales_package (
    id BIGINT AUTO_INCREMENT,
    package_name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    discount_percentage DECIMAL(5, 2) NOT NULL DEFAULT 0.0,
    is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE sales_package_item (
    package_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (package_id, menu_item_id),
    FOREIGN KEY (package_id) REFERENCES sales_package(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_item(id)
);

CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255),
    salary DECIMAL(10, 2) NOT NULL DEFAULT 0.0,
    role ENUM('CASHIER', 'MANAGER', 'CHEF', 'WAITER', 'SUPERVISOR') NOT NULL,
    dob DATE NOT NULL,
    employee_since DATE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE promotion_history (
    id BIGINT AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    old_role ENUM('CASHIER', 'MANAGER', 'CHEF', 'WAITER', 'SUPERVISOR') NOT NULL,
    new_role ENUM('CASHIER', 'MANAGER', 'CHEF', 'WAITER', 'SUPERVISOR') NOT NULL,
    promotion_date DATE NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    PRIMARY KEY (id)
);

CREATE TABLE `user` (
    employee_id BIGINT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL DEFAULT '1234',
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	last_login DATETIME,
	deleted_at DATETIME,
	is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    PRIMARY KEY (employee_id)
);

CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    address VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE `order` (
    id BIGINT AUTO_INCREMENT,
	placed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0.0,
    customer_id BIGINT,
    employee_id BIGINT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    PRIMARY KEY (id)
);

CREATE TABLE order_item (
    item_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    price_per_unit DECIMAL(10, 2) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (item_id) REFERENCES menu_item(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    PRIMARY KEY (item_id, order_id)
);

CREATE TABLE receipt (
    id BIGINT AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
	amount_given DECIMAL(10, 2) NOT NULL,
	is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    PRIMARY KEY (id)
);

CREATE TABLE inventory (
    id BIGINT AUTO_INCREMENT,
    item_name VARCHAR(255) NOT NULL,
	description TEXT,
    quantity INT NOT NULL,
    unit VARCHAR(50) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE supplier (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    address VARCHAR(255),
	is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE inventory_purchase (
    id BIGINT AUTO_INCREMENT,
    inventory_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    cost DECIMAL(10, 2) NOT NULL,
    purchased_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (inventory_id) REFERENCES inventory(id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id),
    PRIMARY KEY (id)
);

CREATE TABLE employee_shift (
    id BIGINT AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    shift_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
	is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    PRIMARY KEY (id)
);

CREATE TABLE restaurant_table (
    id INT AUTO_INCREMENT,
    table_number INT NOT NULL UNIQUE,
    capacity INT NOT NULL,
	booking_count INT DEFAULT 0,
	last_booked DATE DEFAULT NULL,
    is_occupied BOOLEAN DEFAULT FALSE,
	is_available BOOLEAN DEFAULT TRUE,
	is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT,
    expense_type ENUM('RENT', 'SALARIES', 'UTILITIES', 'MAINTENANCE', 'OTHER') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    expense_date DATE NOT NULL,
    description VARCHAR(255),
	is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE report (
    id BIGINT AUTO_INCREMENT,
    generated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    report_type ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'ANNUAL', 'CUSTOM') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,  
    generated_by BIGINT NOT NULL,
	title VARCHAR(255) NOT NULL,
    description TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (generated_by) REFERENCES employee(id),
    PRIMARY KEY (id)
);

DESC menu_category;
DESC menu_item;
DESC sales_package;
DESC sales_package_item;
DESC employee;
DESC promotion_history;
DESC `user`;
DESC customer;
DESC `order`;
DESC order_item;
DESC receipt;
DESC inventory;
DESC supplier;
DESC inventory_purchase;
DESC employee_shift;
DESC restaurant_table;
DESC expense;
DESC report;

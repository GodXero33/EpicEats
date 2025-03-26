# EpicEats Project

EpicEats is a food ordering and management system designed to provide a seamless experience for both customers and restaurant staff. The project consists of two main parts:

- **Frontend**: User-facing web application for browsing menu items, placing orders, and booking tables.
- **Backend**: A Spring Boot API that handles business logic, database operations, and authentication.

## Project Structure

```
EpicEats/
│
├── Docs/                    # Documentation related to the project
│
├── EpicEats_Backend/         # Spring Boot Backend project
│   ├── src/
│   └── ...
│
├── EpicEats_Frontend/        # Frontend project (Angular)
│   ├── src/
│   └── ...
│
└── README.md                # This file
```
## Database Schema Overview

The project uses a relational database (MySQL) to store essential information. For more details on the schema design,
please refer to the **[db.sql](Docs/db.sql)** file and the **[EpicEats_ERD.pdf](Docs/EpicEats_ERD.pdf)** in the docs folder.

### Key Tables Overview:
1. **menu_item**: Contains details about the menu items (e.g., burgers, pizzas, beverages).
2. **sales_package**: Defines combo offers like Lunch Specials, Family Deals, etc.
3. **employee**: Stores information about restaurant staff (e.g., cashier, chef, waiter).
4. **order**: Records customer orders, including which items were purchased and which employee handled the order.
5. **inventory**: Tracks stock for ingredients and other restaurant supplies.
6. **restaurant_table**: Manages table bookings and their availability in the restaurant.

For a more comprehensive look at the database schema, please check the **`db.sql`** file for table definitions and the **`EpicEats_ERD.pdf`** for the entity-relationship diagram.

### ERD (Entity-Relationship Diagram)

The ERD is included in the database setup and defines relationships such as:

- Menu items being linked to sales packages through the `sales_package_item` table.
- Orders containing multiple items and linking to customers and employees.
- Employees having user accounts and roles for system access.

## Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/GodXero33/EpicEats.git
   ```

2. Navigate to the `EpicEats_Backend/` directory.

3. Make sure your database is set up and configured correctly (refer to the ERD and schema for table setup).

4. Build and run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

5. The backend API should now be running at `http://localhost:8080`.

## Frontend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/GodXero33/EpicEats.git
   ```

2. Navigate to the `EpicEats_Frontend/` directory.

3. Install the required dependencies (assuming it's a Node.js project):
   ```bash
   npm install
   ```

4. Start the frontend development server:
   ```bash
   npm start
   ```

5. The frontend should now be available at `http://localhost:3000`.

## Documentation

All project-related documentation is stored in the `Docs/` folder. It includes technical documentation, API references, and user guides.

EpicEats Project - [https://github.com/GodXero33/EpicEats](https://github.com/GodXero33/EpicEats)

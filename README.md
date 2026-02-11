
Endterm Project Report
Course: Object-Oriented Programming (Java) Instructor: Traxel Xeniya Alexandrovna Project Topic: Food Delivery & Canteen Ordering System Team Members:

Kairatuly Miras

Temirkhan Sagat

1. Project Topic and Main User Flows
1.1 Project Description
Our project is a console-based Food Delivery & Canteen Ordering System developed in Java. It mimics a real-world backend service that allows users to browse menus, build complex orders, and select various delivery methods (Pickup, Delivery, Dine-in). The system persists data using a PostgreSQL database accessed via JDBC.

The core technical focus was to implement OOD Principles (SOLID) and GoF Design Patterns (Singleton, Builder, Factory) while using modern Java features like Generics, Lambdas, and Streams.

1.2 Main User Flows
The system supports the following key scenarios:

Menu Browsing & Filtering:

The user requests the menu.

The system allows filtering items by specific criteria (e.g., price range or category) using Java Streams and Lambdas (MenuItemFilter interface).

Code Reference: MenuService.searchMenuItems()

Order Creation (Builder Pattern):

The user starts an order.

Items are added one by one. The system validates availability.

Pricing and taxes are applied automatically using the Singleton PricingRules.

The OrderBuilder ensures the order object is only created when it is valid and complete.

Code Reference: OrderService.createOrderWithBuilder()

Checkout & Delivery Selection (Factory Pattern):

The user finalizes the order and chooses a delivery type (Delivery, Pickup, or Dine-in).

The DeliveryOrderFactory instantiates the correct class (DeliveryOrderImpl, PickupOrder, DineInOrder) and calculates the final delivery time and cost based on the selection.

Code Reference: DeliveryOrderFactory.createOrder()

2. Database Schema
The project uses a relational database (PostgreSQL) hosted on Supabase.

2.1 Entity Relationship Description
Customers: Stores user information.

Menu_Items: Stores food products, prices, and availability status.

Orders: The central entity linking a Customer to their Transaction.

Order_Items: A join table connecting Orders and Menu_Items (Many-to-Many relationship), storing the quantity of each item in a specific order.

2.2 Table Structures
Table: customers

id (PK, Integer)

name (Varchar)

email (Varchar)

Table: menu_items

id (PK, Integer)

name (Varchar)

price (Double)

available (Boolean)

category (Varchar)

Table: orders

id (PK, Integer)

customer_id (FK, Integer)

status (Varchar: PENDING, ACTIVE, COMPLETED)

created_at (Timestamp)

Table: order_items

id (PK, Integer)

order_id (FK, Integer)

menu_item_id (FK, Integer)

quantity (Integer)

3. Architecture and Design Patterns
3.1 Package Structure
The project follows a Layered Architecture to separate concerns:

Models: POJOs representing database entities (Order, MenuItem).

Repositories: Data Access Layer using JDBC and Generics (IRepository<T, ID>). Handles all SQL operations.

Service: Business Logic Layer (OrderService, MenuService). Bridges the gap between the Controller (Main) and Data.

Patterns: Contains Design Pattern implementations (OrderBuilder, PricingRules, DeliveryOrderFactory).

Interfaces: Defines contracts for repositories and functional interfaces (OrderCallback).

3.2 Applied Design Patterns
Singleton Pattern (PricingRules.java)

Purpose: We needed a single, global point of configuration for tax rates (taxRate) and delivery charges (deliveryCharge).

Implementation: The constructor is private, and a static getInstance() method returns the sole instance.

Builder Pattern (OrderBuilder.java)

Purpose: Order objects are complex, requiring a customer, a list of items, status, and timestamps. Using a constructor with 7+ arguments is error-prone.

Implementation: The builder allows chaining methods (.addItem().withStatus().build()) to construct the object step-by-step.

Factory Pattern (DeliveryOrderFactory.java)

Purpose: To decouple the logic of creating delivery types from the client code.

> Miqas:
Implementation: The factory takes a string type ("PICKUP", "DELIVERY") and returns the appropriate polymorphic object (DeliveryOrder subclass).

4. Component Principles (REP, CCP, CRP)
We organized our classes into logical components to ensure maintainability and reusability.

4.1 Identified Components
Ordering Component:

Classes: OrderService, OrderRepository, OrderBuilder, Order, OrderItem.

Responsibility: Handles the lifecycle of a transaction.

Menu Management Component:

Classes: MenuService, MenuRepository, MenuItem, MenuItemFilter.

Responsibility: Manages product data and catalog browsing.

Billing & Configuration Component:

Classes: PricingRules.

Responsibility: Manages financial rules (taxes, fees).

4.2 Adherence to Principles
CCP (Common Closure Principle):

Definition: Classes that change for the same reason should be grouped together.

Application: If we change the database schema for Orders, we only need to modify the Ordering Component (OrderRepository and Order model). The Menu Component remains untouched. This minimizes the risk of breaking unrelated features during updates.

CRP (Common Reuse Principle):

Definition: Classes that are used together should be grouped together. Users should not be forced to depend on things they don't use.

Application: The Menu Component is independent. It can be reused in a different application (e.g., a "Digital Menu Board" for a TV screen) without dragging in the complex Ordering logic or Database connection logic required for processing payments.

REP (Reuse/Release Equivalence Principle):

Definition: The granule of reuse is the granule of release.

Application: Each package (Repositories, Patterns) is designed so that classes inside share a cohesive purpose, making them releasable as distinct modules if the project were to scale into Microservices.

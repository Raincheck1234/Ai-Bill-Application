# Structure and Function
Swing GUI (Controller) <- Service <- DAO <-data(CSV/JSON)

## Service (Business logic)
Responsibilities:
Transaction CRUD operations, calling the DAO layer to read and write local files,
User-defined classification logic (e.g. modifying transaction categories)

AIService (Intelligent Analytics) Responsibilities: Generate budget recommendations based
on historical data, and detect cost-cutting recommendations for seasonal spending in China
(e.g. Spring Festival) (e.g. marking high-frequency small spending)

## DAO (File Storage)

Responsibilities: Save transaction data in CSV or JSON format and provide interfaces for adding,
deleting, and modifying in-memory data

## Controller (Swing GUI£)

Responsibilities: Manage user interface components (buttons, tables, text boxes) to listen for 
events (clicks, inputs) and call service layer methods to update the interface data display

## attention
encoding with UTF-8

# eyPOS

eyPOS is a Java Swing point-of-sale (POS) desktop application built for school use.  
It includes inventory, checkout, sales tracking, dashboard stats, and user management.

## Features

- Login and register flow
- Role-based access (`admin` and `user`)
- Product management (add, edit, delete, stock updates)
- Checkout workflow with cart and receipt totals
- Sales tracking and dashboard summaries
- Excel export for sales data
- Price history tracking

## Tech Stack

- Java 17
- Java Swing UI
- MySQL / MariaDB
- Apache Ant (NetBeans project)
- Libraries: MySQL Connector/J, MigLayout, Timing Framework, Apache POI

## Project Structure

```
src/
├── DAOstuff/          # Data models and DAO classes (products, sales, users)
├── MenuPanels/        # Main feature panels (checkout, dashboard, products, etc.)
├── MenuStuff/         # App windows (login and main menu)
├── componentStuff/    # Reusable UI components and DB utility
└── imagestuff/        # Image resources
```

## Requirements

- JDK 17+
- MySQL or MariaDB
- Apache Ant (or NetBeans IDE)

## Database Setup

1. Create a database named `eyPOS` (or `eypos`).
2. Import the SQL file:
   - `/home/runner/work/eyPOS/eyPOS/eypos.sql`
3. Verify DB connection settings in:
   - `/home/runner/work/eyPOS/eyPOS/src/componentStuff/DatabaseUtil.java`

Default values in `DatabaseUtil`:
- URL: `jdbc:mysql://localhost:3306/eyPOS`
- User: `root`
- Password: empty

## Run the Application

### Option 1: NetBeans

1. Open `/home/runner/work/eyPOS/eyPOS` as a project.
2. Build and run.
3. Main class is:
   - `MenuStuff.LoginMenu`

### Option 2: Ant (CLI)

From `/home/runner/work/eyPOS/eyPOS`:

```bash
ant clean
ant run
```

## Notes

- This project currently stores passwords in plain text and is intended for educational use.
- If database login fails, update `DatabaseUtil.java` with your local credentials.

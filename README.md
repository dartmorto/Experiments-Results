# Experiments-Results

# Scientific Experiment Data Manager

An application for structured storage and management of scientific experiment data. Designed to help researchers efficiently organize, track, and analyze experimental results.

## Visual Diagram: Experiment → Runs → Results

```text
+-------------------+
|   Experiment 1    |
+-------------------+
          │
          ▼
    +------------+           +------------+
    |   Run 1    |──────────>|   Run 2    |
    +------------+           +------------+
       │       │                 │
       ▼       ▼                 ▼
  +--------+ +--------+     +--------+
  | Result | | Result |     | Result |
  |  1     | |  2     |     |  1     |
  +--------+ +--------+     +--------+
````

Experiment — main entity representing the scientific experiment
Run — single attempt or session of the experiment
Result — measured data from the run

Features
1. Data Management (CRUD)
Create new experiments, launches, and results with validated input.
Read/View detailed information about experiments and their related launches/results.
Update/Edit existing records while maintaining data integrity.
Delete experiments, launches, or results with confirmation to prevent accidental loss.
2. Complex Connectivity Structure
Implements a one-to-many hierarchy:
Experiment → Launches → Results
Nested collections allow easy navigation and organization of related data.
Supports advanced queries and filtering based on experiment parameters.
3. Data Validation
Ensures all input data meets predefined constraints.
Handles invalid inputs with informative error messages.
Maintains consistency across related entities.
4. Exception Handling
Robust error handling for database operations and user input.
Provides clear feedback for troubleshooting and debugging.

Technologies Used
Java (Core)
Collections framework for structured storage
Exception handling and validation libraries
Optional: JSON/XML for data persistence

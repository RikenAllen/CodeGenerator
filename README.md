#  CodeGenerator

This repository provides a Java-based utility for generating source code from pseudo-UML diagrams exported as `.drawio` files. It parses the XML structure of your diagrams to automatically create class files, including support for inheritance and composition.

---

## Overview

The CodeGenerator reads XML-based diagram files and converts visual components—such as "Things" and "Abstract Things"—into a structured Java project.

### Input and Output

| File/Directory | Description |
| --- | --- |
| `Diagrams/*.drawio` | XML diagram files defining the classes and relationships. |
| `src/` | The core Java logic for parsing and file generation. |
| `GeneratedCode/` | The destination directory for the generated `.java` source files. |

---

## 📏 Diagram Syntax Rules

To ensure successful code generation, diagrams must follow specific visual rules defined in the system legend:

* **Classes**: Represented by standard rectangles (rounded=0). These generate standard Java classes.
* **Abstract Classes**: Represented by rounded rectangles (rounded=1). These generate `public abstract class` definitions.
* **"Has-a" (Composition)**: Use a **flex arrow** (shape=flexArrow) with cardinality (e.g., `(1)` or `(N)`) pointing from the owner to the component. These generate private fields in the parent class.
* **"Is-a" (Inheritance)**: Use a **standard arrow** pointing from the subclass to the superclass. These generate the `extends` keyword in the subclass.

---

## 🛠️ Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd CodeGenerator

```

### 2. Configure Paths

Open `src/Main.java` and update the local file paths to match your environment:

```java
final String filePath = "C:\\path\\to\\your\\Diagram.drawio";
final String outputDirectory = "C:\\path\\to\\output\\folder\\";

```

### 3. Compile the Source

Ensure you have a Java Development Kit (JDK) installed, then compile the project classes:

```bash
javac src/*.java

```

---

##  Usage

The tool is designed to process individual diagram files and generate a complete set of Java classes based on the detected vertices and edges.

### Generate from Diagram

Run the compiled `Main` class to execute the parsing and generation logic:

```bash
java -cp src Main

```

### Example Output

If processing a diagram with a "Restaurant" class composed of "Chef" and "Waiter", the tool will generate `Restaurant.java` with the following structure:

```java
public class Restaurant {
    private Chef chef;
    private Waiter waiter;
    // ...
}

```

> [!TIP]
> **Cardinality Support**: The generator uses flex arrows to determine which classes are "contained" within others, allowing for rapid prototyping of complex system architectures.

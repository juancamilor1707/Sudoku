# 🎮 Sudoku Game - JavaFX

A feature-rich 6x6 Sudoku game built with JavaFX, implementing clean MVC architecture and the Singleton pattern for efficient window management.
## ✨ Features

- **6x6 Sudoku Grid**: Simplified Sudoku variant with 2x3 blocks
- **Automatic Puzzle Generation**: Random puzzle generation using backtracking algorithm
- **Real-time Validation**: Instant feedback on valid/invalid moves
- **Visual Feedback System**:
  - 🔴 Red highlight for invalid input (out of range or non-numeric)
  - 🟡 Yellow highlight for repeated numbers (Sudoku rule violations)
  - 🔵 Blue text for hints provided by the system
- **Hint System**: Get correct values for empty cells when stuck
- **User Profiles**: Personalized experience with nickname entry
- **Victory Screen**: Congratulatory screen upon puzzle completion
- **Help Section**: In-game instructions and rules
- **Custom UI**: Beautiful themed interfaces with hover effects and custom styling

## 🖼️ Screenshots

### Welcome Screen
- Custom background image with title
- Nickname input field with custom styling
- Play and Help buttons with hover effects

### Game Screen
- 6x6 interactive grid with visual feedback
- Hint button with custom icon
- Clean, focused gaming interface

### Victory Screen
- Personalized congratulations message
- Back to menu button
- Celebratory background

### Help Screen
- Game instructions and rules
- Easy navigation back to menu

## 🏗️ Architecture

The project follows the **Model-View-Controller (MVC)** pattern:

### Model (`com.example.demosudoku.model`)
- **Board**: Manages puzzle generation, validation, and solution tracking
- **Game**: Core game logic, move validation, and win condition detection
- **User**: Player information management

### View (`com.example.demosudoku.view`)
- **SudokuWelcomeStage**: Main menu with nickname input (sudoku-welcome-view.fxml)
- **SudokuGameStage**: Main game board interface (sudoku-game-view.fxml)
- **SudokuHelpStage**: Game instructions and rules (sudoku-help-view.fxml)
- **SudokuWinStage**: Victory screen (sudoku-win-view.fxml)

All stages implement the **Singleton pattern** for efficient resource management.

### Controller (`com.example.demosudoku.controller`)
- **SudokuWelcomeController**: Handles welcome screen interactions
- **SudokuGameController**: Manages game board and hint functionality
- **SudokuHelpController**: Controls help screen navigation
- **SudokuWinController**: Manages victory screen and return to menu

### Utils (`com.example.demosudoku.utils`)
- **AlertBox**: Standardized alert dialog utility
- **stylesheet.css**: Custom CSS styling for all UI components

## 🚀 Getting Started

### Prerequisites

- Java JDK 11 or higher
- JavaFX SDK 11 or higher
- Maven (optional, for dependency management)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/sudoku-javafx.git
cd sudoku-javafx
```

2. Ensure you have the required resources folder structure:
```
src/main/resources/com/example/demosudoku/
├── sudoku-welcome-view.fxml
├── sudoku-game-view.fxml
├── sudoku-help-view.fxml
├── sudoku-win-view.fxml
├── stylesheet.css
├── favicon.png
└── Img/
    ├── Title.png
    ├── ButtomPlay.png
    ├── ButtomHelp.png
    ├── ButtomBack.png
    ├── Clues.png
    ├── menuImg.jpg
    ├── gameImg.png
    ├── helpImg.png
    ├── VictoryImage.jpg
    └── textFieldMenu.png
```

3. Configure JavaFX in your IDE or build tool

4. Run the application:
```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar sudoku-game.jar
```

Or simply run the `Main.java` class from your IDE.

## 🎯 How to Play

1. **Enter Your Nickname**: Start by entering your name on the welcome screen
2. **Fill the Grid**: Click on empty cells and enter numbers 1-6
3. **Follow Sudoku Rules**:
   - Each row must contain numbers 1-6 without repetition
   - Each column must contain numbers 1-6 without repetition
   - Each 2x3 block must contain numbers 1-6 without repetition
4. **Use Hints**: Click the hint button (bulb icon) if you're stuck
5. **Win**: Complete the puzzle correctly to see your victory screen!

## 🎨 Visual Feedback

- **Red Border**: Invalid input (number out of range 1-6 or non-numeric)
- **Yellow Border**: Number already exists in row, column, or block
- **Black Text**: Initial clues (non-editable)
- **Blue Text**: Hints provided by the system
- **Normal Text**: Player's valid entries

## 🎨 UI/UX Features

### Custom Styling
- **stylesheet.css** provides:
  - Hover effects with scale and shadow animations
  - Custom button backgrounds using images
  - Themed interfaces for each screen
  - Custom text field styling
  - Transparent backgrounds for seamless integration

### FXML Views
- **sudoku-welcome-view.fxml**: 
  - Centered nickname input with custom styling
  - Image-based buttons for Play and Help
  - Title image overlay
  
- **sudoku-game-view.fxml**: 
  - 6x6 GridPane with visible grid lines
  - Custom hint button with icon
  - Responsive layout with AnchorPane constraints
  
- **sudoku-help-view.fxml**: 
  - Clean help interface
  - Back button for easy navigation
  
- **sudoku-win-view.fxml**: 
  - Victory celebration background
  - Back to menu functionality

## 🧩 Puzzle Generation Algorithm

The game uses a **backtracking algorithm** to generate valid Sudoku puzzles:

1. Creates a complete valid solution using recursive backtracking
2. Randomly shuffles numbers for variety
3. Selects exactly 2 clues per block for the puzzle
4. Ensures no conflicts between selected clues
5. Stores the complete solution for validation and hints

## 📁 Project Structure

```
com.example.demosudoku/
├── controller/
│   ├── SudokuGameController.java
│   ├── SudokuWelcomeController.java
│   ├── SudokuHelpController.java
│   └── SudokuWinController.java
├── model/
│   ├── board/
│   │   ├── IBoard.java
│   │   └── Board.java
│   ├── game/
│   │   ├── IGame.java
│   │   ├── GameAbstract.java
│   │   └── Game.java
│   └── user/
│       └── User.java
├── view/
│   ├── SudokuWelcomeStage.java
│   ├── SudokuGameStage.java
│   ├── SudokuHelpStage.java
│   └── SudokuWinStage.java
├── utils/
│   ├── IAlertBox.java
│   └── AlertBox.java
├── Main.java
└── resources/
    ├── sudoku-welcome-view.fxml
    ├── sudoku-game-view.fxml
    ├── sudoku-help-view.fxml
    ├── sudoku-win-view.fxml
    ├── stylesheet.css
    ├── favicon.png
    └── Img/
        └── [image assets]
```

## 🔧 Key Design Patterns

### Singleton Pattern
All Stage classes use the Singleton pattern with lazy initialization:
- Ensures only one instance of each window exists
- Efficient memory management
- Controlled access to window instances

### MVC Pattern
Clear separation of concerns:
- **Model**: Business logic and data management
- **View**: FXML files and Stage classes for UI
- **Controller**: Event handling and view-model coordination

### Interface Segregation
- `IBoard`: Contract for board operations
- `IGame`: Contract for game initialization
- `IAlertBox`: Contract for alert dialogs

### Template Method
`GameAbstract` provides a template for game implementations with `startGame()` method.

## 🛠️ Technologies Used

- **Java 11+**: Core programming language
- **JavaFX**: UI framework for desktop application
- **FXML**: XML-based UI markup
- **CSS**: Custom styling for JavaFX components
- **Maven**: Dependency management (optional)

## 📝 Code Highlights

### Board Generation
- Implements backtracking algorithm for puzzle generation
- Guarantees unique solutions
- Configurable difficulty through clue count

### Real-time Validation
- Immediate move validation
- Visual feedback within 1 second
- Automatic cell clearing on invalid input

### Win Detection
- Checks board completion after each valid move
- Smooth transition to victory screen
- Personalized congratulations message

### Custom Styling
- CSS hover effects with scale and shadow
- Image-based backgrounds and buttons
- Themed interfaces for different screens

## 🎮 Controls

- **Mouse Click**: Select cells and buttons
- **Keyboard**: Type numbers 1-6 in selected cells
- **Hint Button**: Request assistance when stuck
- **Back Button**: Navigate between screens

## 🤝 Contributing

Contributions are welcome! Feel free to:

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author
Juan Camilo Ramos Hoyos (juancamiloramos01@gmail.com)
Miguel Angel Martinez Eraso (miguelangel.m.101@gmail.com)

Developed with ☕ and JavaFX

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- Sudoku enthusiasts worldwide
- Open source contributors


**Enjoy solving Sudoku puzzles! 🎉**

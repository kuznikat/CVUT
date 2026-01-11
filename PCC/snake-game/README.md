# Snake Game - Semester Project

A classic Snake game implementation in C++ using POSIX terminals with multi-threaded architecture.

## Features

- **Classic Snake Gameplay**: Control a snake that grows as it eats fruits
- **Two Difficulty Levels**: Easy (slower) and Hard (faster)
- **Pause Functionality**: Pause/unpause the game at any time
- **Multi-threaded Architecture**: Three threads for input, rendering, and game logic
- **Thread-safe Design**: Proper synchronization using mutexes and condition variables
- **ANSI Terminal Graphics**: Colorful display using ANSI escape sequences
- **Score System**: 10 points per fruit consumed

## Requirements

- C++17 compatible compiler (g++, clang++)
- CMake 3.10 or higher
- POSIX-compatible system (Linux, macOS, or WSL on Windows)
- pthread library (usually included)

## Building

```bash
mkdir build
cd build
cmake ..
make
```

## Running

```bash
# Run with default settings (Easy difficulty, 40x20)
./snake

# Run with custom difficulty
./snake --difficulty hard

# Run with custom size
./snake --width 50 --height 25

# Show help
./snake --help
```

## Controls

- **W/A/S/D**: Move snake (Up/Left/Down/Right)
- **P**: Pause/Unpause game
- **Q or ESC**: Quit game

## Game Rules

- Don't hit walls or your own tail
- Eat fruits (*) to grow and gain 10 points
- Game speed increases with difficulty level
- The snake grows longer after each fruit consumed

## Testing

Run the test suite:

```bash
cd build
make test_snake
./test_snake
```

Or use CTest:

```bash
cd build
ctest
```

## Architecture

The game uses a three-thread architecture:

1. **Input Thread**: Handles keyboard input (W/A/S/D, P, Q)
2. **Render Thread**: Draws the game state using ANSI escape sequences
3. **Main Thread**: Manages game logic and timing

Threads communicate through a shared `Game` object protected by mutexes and condition variables.

## Project Structure

```
Semestral/
├── CMakeLists.txt          # Build configuration
├── README.md               # This file
├── docs/
│   └── documentation.md    # Detailed documentation
├── src/
│   ├── main.cpp           # Entry point
│   ├── game.h/cpp         # Game logic
│   ├── terminal.h/cpp     # Terminal utilities
│   ├── input.h/cpp        # Input thread
│   └── render.h/cpp       # Render thread
└── tests/
    └── test_game.cpp      # Unit tests
```

## Language Extensions Used

- **POSIX**: Terminal control, file I/O
- **pthread**: Threading support
- **C++17**: Modern C++ features (atomic, mutex, condition_variable)

## Memory Checking

To check for memory leaks using valgrind:

```bash
valgrind --leak-check=full --show-leak-kinds=all ./snake
```

## Documentation

See `docs/documentation.md` for detailed documentation including:
- Architecture overview
- Thread synchronization details
- Testing procedures
- Code structure




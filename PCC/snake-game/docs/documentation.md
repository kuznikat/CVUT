# Snake Game - Detailed Documentation

**Commit Hash**: [TODO: Fill in commit hash after first commit]

## Table of Contents

1. [Description of Implemented Algorithms](#description-of-implemented-algorithms)
2. [Description of Solution Process](#description-of-solution-process)
3. [How to Compile and Run](#how-to-compile-and-run)
4. [Command-Line Switches Description](#command-line-switches-description)
5. [How to Use the Application](#how-to-use-the-application)
6. [Program Testing](#program-testing)
7. [Comparison of Implemented Algorithms](#comparison-of-implemented-algorithms)

---

## Description of Implemented Algorithms

### Snake Movement Algorithm

The snake movement is implemented using a queue-based data structure where the snake's body is represented as a sequence of coordinates. The algorithm works as follows:

1. **Direction Update**: The current direction is stored and can be changed by user input (W/A/S/D keys)
2. **Head Movement**: Calculate the new head position based on current direction:
   - Up (W): `(x, y-1)`
   - Down (S): `(x, y+1)`
   - Left (A): `(x-1, y)`
   - Right (D): `(x+1, y)`
3. **Body Update**: Add the new head position to the front of the snake body
4. **Tail Management**: If no fruit was eaten, remove the tail segment; otherwise, keep the tail (snake grows)

**Time Complexity**: O(1) for movement, O(n) for rendering where n is the snake length

### Collision Detection Algorithm

Collision detection is performed after each movement:

1. **Wall Collision**: Check if the new head position is outside the game boundaries:
   ```
   if (head_x < 0 || head_x >= width || head_y < 0 || head_y >= height)
       → Game Over
   ```
2. **Self Collision**: Check if the new head position overlaps with any body segment:
   ```
   for each segment in snake_body (except head):
       if (head_position == segment_position)
           → Game Over
   ```
3. **Fruit Collision**: Check if the head position matches the fruit position:
   ```
   if (head_position == fruit_position)
       → Increase score, spawn new fruit, grow snake
   ```

**Time Complexity**: O(n) where n is the snake length (for self-collision check)

### Fruit Spawning Algorithm

Fruits are spawned at random positions that are not occupied by the snake:

1. **Random Position Generation**: Generate random coordinates within game boundaries
2. **Collision Check**: Verify the position is not occupied by any snake segment
3. **Retry Logic**: If collision detected, generate new random position (up to maximum retries)
4. **Placement**: Set fruit position to the valid random coordinate

**Time Complexity**: O(n) in worst case where n is snake length, but typically O(1) with low collision probability

### Thread Synchronization Algorithm

The game uses a multi-threaded architecture with three threads that need to synchronize:

1. **Shared State Protection**: The `Game` object is protected by a mutex (`std::mutex`)
2. **Condition Variables**: Used to notify threads of state changes:
   - Input thread notifies when direction changes
   - Main thread notifies when game state updates
   - Render thread waits for update notifications
3. **Atomic Operations**: Frequently accessed flags (like `isRunning`, `isPaused`) use `std::atomic` for lock-free access
4. **Lock Strategy**:
   - Input thread: Acquires lock only when updating direction
   - Main thread: Acquires lock for game state updates
   - Render thread: Acquires lock when reading game state for rendering

**Synchronization Pattern**:
```
Input Thread:  [Lock] → Update Direction → [Unlock] → Notify
Main Thread:   [Lock] → Update Game State → [Unlock] → Notify
Render Thread: [Lock] → Read Game State → Render → [Unlock]
```

---

## Description of Solution Process

### Architecture Overview

The solution follows a multi-threaded architecture with clear separation of concerns:

- **Game Logic**: Centralized in the `Game` class
- **Input Handling**: Isolated in `InputThread` class
- **Rendering**: Isolated in `RenderThread` class
- **Terminal Management**: Abstracted in `Terminal` class

### Class Descriptions

#### Game Class (`game.h`/`game.cpp`)

The core game logic class that manages the game state.

**Key Methods**:
- `Game(int width, int height, Difficulty difficulty)`: Constructor initializes game board
- `void update()`: Updates game state (movement, collision, scoring)
- `void changeDirection(Direction dir)`: Changes snake direction (thread-safe)
- `bool isGameOver() const`: Returns game over status
- `int getScore() const`: Returns current score
- `void pause()`: Toggles pause state
- `void spawnFruit()`: Spawns a new fruit at random position

**Data Members**:
- `std::vector<Position> snake`: Snake body segments
- `Position fruit`: Current fruit position
- `Direction direction`: Current movement direction
- `int score`: Current score
- `bool gameOver`: Game over flag
- `bool paused`: Pause state
- `std::mutex mtx`: Mutex for thread synchronization
- `std::condition_variable cv`: Condition variable for notifications

#### Terminal Class (`terminal.h`/`terminal.cpp`)

Manages terminal I/O operations and terminal mode settings.

**Key Methods**:
- `Terminal()`: Constructor sets up terminal in raw mode
- `~Terminal()`: Destructor restores terminal to normal mode
- `void clearScreen()`: Clears the terminal screen
- `void setCursor(int x, int y)`: Moves cursor to position
- `void hideCursor()`: Hides the cursor
- `void showCursor()`: Shows the cursor
- `std::pair<int, int> getSize()`: Gets terminal dimensions
- `char readChar()`: Reads a single character (non-blocking)

**POSIX Features Used**:
- `termios.h`: Terminal control structure
- `sys/ioctl.h`: Terminal size detection
- `unistd.h`: File I/O operations

#### InputThread Class (`input.h`/`input.cpp`)

Handles keyboard input in a separate thread.

**Key Methods**:
- `InputThread(Game& game, Terminal& terminal)`: Constructor
- `void start()`: Starts the input thread
- `void stop()`: Stops the input thread
- `void run()`: Main input loop (runs in separate thread)

**Thread Behavior**:
- Continuously reads keyboard input
- Maps keys to game actions:
  - W/A/S/D → Direction changes
  - P → Pause toggle
  - Q/ESC → Quit game
- Updates game state in a thread-safe manner

#### RenderThread Class (`render.h`/`render.cpp`)

Handles game rendering in a separate thread.

**Key Methods**:
- `RenderThread(Game& game, Terminal& terminal)`: Constructor
- `void start()`: Starts the render thread
- `void stop()`: Stops the render thread
- `void run()`: Main render loop (runs in separate thread)

**Rendering Features**:
- Uses ANSI escape sequences for colors and positioning
- Renders game board with borders
- Displays snake, fruit, and score
- Updates at regular intervals (frame rate control)

**VLA Usage**: Uses Variable Length Arrays (C99 extension) for temporary rendering buffers.

### Solution Process

1. **Initialization Phase**:
   - Parse command-line arguments
   - Initialize Terminal object (sets raw mode)
   - Create Game object with specified parameters
   - Create InputThread and RenderThread objects

2. **Thread Startup**:
   - Start InputThread (begins reading input)
   - Start RenderThread (begins rendering)
   - Main thread enters game loop

3. **Game Loop (Main Thread)**:
   - Wait for game tick interval (based on difficulty)
   - Acquire lock on Game object
   - Call `game.update()` to advance game state
   - Release lock and notify waiting threads
   - Check for game over condition

4. **Cleanup Phase**:
   - Stop InputThread and RenderThread
   - Join threads
   - Restore terminal to normal mode
   - Display final score

---

## How to Compile and Run

### Prerequisites

- C++17 compatible compiler (g++ or clang++)
- CMake 3.10 or higher
- POSIX-compatible system (Linux, macOS, or WSL)
- pthread library (usually included with system)

### Compilation Steps

#### Method 1: Using Build Script

```bash
chmod +x build.sh
./build.sh
```

This script will:
1. Create a `build` directory
2. Run CMake to generate build files
3. Compile the project using make
4. Run tests automatically

#### Method 2: Manual Build

```bash
# Create build directory
mkdir build
cd build

# Generate build files
cmake ..

# Compile
make

# Optional: Run tests
make test_snake
./test_snake
```

### Build Output

After successful compilation, you will find:
- `build/snake`: Main game executable
- `build/test_snake`: Test executable

### Running the Game

```bash
# From build directory
./snake

# Or from project root
./build/snake
```

### Troubleshooting

**CMake not found**: Install CMake using your package manager
- macOS: `brew install cmake`
- Linux: `sudo apt-get install cmake` (Debian/Ubuntu)

**pthread errors**: Usually pthread is included by default. If not, install `libpthread-dev` (Linux)

**Terminal size issues**: Ensure your terminal is at least 42x22 characters (40x20 game + borders)

---

## Command-Line Switches Description

The game supports the following command-line options:

### `--help` or `-h`

**Description**: Displays a help message with usage information, available options, controls, and game rules.

**Usage**:
```bash
./snake --help
./snake -h
```

**Output**: Shows:
- Program description
- Available command-line options
- Game controls
- Game rules

### `--difficulty <level>` or `-d <level>`

**Description**: Sets the game difficulty level, which affects the game speed.

**Arguments**:
- `easy`: Slower game speed (default)
- `hard`: Faster game speed

**Usage**:
```bash
./snake --difficulty hard
./snake -d easy
```

**Default**: `easy`

### `--width <n>` or `-w <n>`

**Description**: Sets the width of the game board in characters.

**Arguments**: Positive integer (recommended: 20-80)

**Usage**:
```bash
./snake --width 50
./snake -w 30
```

**Default**: `40`

**Constraints**: Must be positive and fit within terminal width

### `--height <n>` or `-h <n>`

**Description**: Sets the height of the game board in characters.

**Arguments**: Positive integer (recommended: 10-30)

**Usage**:
```bash
./snake --height 25
./snake -h 15
```

**Default**: `20`

**Constraints**: Must be positive and fit within terminal height

### Combined Options

You can combine multiple options:

```bash
./snake --difficulty hard --width 50 --height 25
./snake -d hard -w 50 -h 25
```

### Option Parsing

- Options can be specified in any order
- If conflicting options are provided, the last one takes precedence
- Invalid values result in error messages and program termination

---

## How to Use the Application

### Starting the Game

1. Compile the game (see [How to Compile and Run](#how-to-compile-and-run))
2. Run the executable: `./build/snake`
3. The game will start with default settings (Easy difficulty, 40x20 board)

### Game Controls

| Key | Action |
|-----|--------|
| **W** | Move snake up |
| **A** | Move snake left |
| **S** | Move snake down |
| **D** | Move snake right |
| **P** | Pause/Unpause game |
| **Q** | Quit game |
| **ESC** | Quit game |

### Game Rules

1. **Objective**: Control the snake to eat fruits (*) and grow longer
2. **Scoring**: Each fruit consumed gives 10 points
3. **Growth**: The snake grows by one segment after eating a fruit
4. **Game Over Conditions**:
   - Snake hits a wall (boundary collision)
   - Snake hits its own tail (self-collision)
5. **Difficulty Levels**:
   - **Easy**: Slower movement speed, easier to control
   - **Hard**: Faster movement speed, more challenging

### Gameplay Tips

- Plan your moves ahead to avoid trapping yourself
- Use pause (P) to take breaks and plan your next moves
- Start with Easy difficulty to learn the controls
- The game gets more challenging as the snake grows longer

### Visual Elements

- **Snake**: Represented by colored segments (typically green)
- **Fruit**: Represented by `*` character (typically red)
- **Walls**: Represented by border characters
- **Score**: Displayed at the top of the screen

### Exiting the Game

- Press **Q** or **ESC** to quit at any time
- The game will display your final score before exiting
- Terminal will be restored to normal mode automatically

---

## Program Testing

### Testing Methodology

The project includes comprehensive unit tests and integration testing procedures.

### Unit Tests

Located in `tests/test_game.cpp`, the test suite includes 8 test cases:

#### Test 1: Game Initialization
- **Input**: Create Game object with width=10, height=10, difficulty=Easy
- **Expected Output**: 
  - Snake starts at center position
  - Initial score is 0
  - Game is not over
  - Fruit is spawned at valid position

#### Test 2: Snake Movement
- **Input**: Move snake in each direction (Up, Down, Left, Right)
- **Expected Output**: 
  - Snake head position updates correctly
  - Snake body follows head correctly
  - Direction changes are applied

#### Test 3: Wall Collision Detection
- **Input**: Move snake toward wall boundary
- **Expected Output**: 
  - Game over flag is set when snake hits wall
  - Collision detected at correct boundary

#### Test 4: Self Collision Detection
- **Input**: Move snake to intersect with its own body
- **Expected Output**: 
  - Game over flag is set
  - Collision detected correctly

#### Test 5: Fruit Consumption
- **Input**: Move snake to fruit position
- **Expected Output**: 
  - Score increases by 10
  - Snake grows by one segment
  - New fruit spawns at different position

#### Test 6: Fruit Spawning
- **Input**: Spawn multiple fruits
- **Expected Output**: 
  - Fruits spawn at valid positions (not on snake)
  - Fruits are within game boundaries

#### Test 7: Pause Functionality
- **Input**: Toggle pause state
- **Expected Output**: 
  - Pause state toggles correctly
  - Game state is preserved when paused

#### Test 8: Difficulty Levels
- **Input**: Create games with Easy and Hard difficulty
- **Expected Output**: 
  - Different update intervals for different difficulties
  - Hard difficulty has faster game speed

### Running Tests

#### Using CTest:
```bash
cd build
ctest
```

#### Running Test Executable Directly:
```bash
cd build
./test_snake
```

#### Expected Test Output:
```
Running 8 tests...
[PASS] Test 1: Game Initialization
[PASS] Test 2: Snake Movement
[PASS] Test 3: Wall Collision Detection
[PASS] Test 4: Self Collision Detection
[PASS] Test 5: Fruit Consumption
[PASS] Test 6: Fruit Spawning
[PASS] Test 7: Pause Functionality
[PASS] Test 8: Difficulty Levels

All tests passed (8/8)
```

### Integration Testing

#### Test Scenario 1: Full Game Session
- **Procedure**: 
  1. Start game with default settings
  2. Play until game over
  3. Verify score calculation
  4. Verify game over detection
- **Expected**: Game runs smoothly, score increments correctly, game ends properly

#### Test Scenario 2: Thread Synchronization
- **Procedure**: 
  1. Start game
  2. Rapidly change directions
  3. Pause/unpause multiple times
  4. Verify no race conditions or deadlocks
- **Expected**: All threads synchronize correctly, no crashes or hangs

#### Test Scenario 3: Terminal Interaction
- **Procedure**: 
  1. Start game in different terminal sizes
  2. Verify rendering adapts correctly
  3. Test all control keys
- **Expected**: Game adapts to terminal size, all controls work

### Memory Leak Testing

#### Using Valgrind:
```bash
cd build
valgrind --leak-check=full --show-leak-kinds=all ./snake
```

#### Expected Results:
- **No memory leaks**: All allocated memory is properly freed
- **No invalid reads/writes**: All memory accesses are valid
- **RAII compliance**: Destructors properly clean up resources

#### Valgrind Output Example:
```
==12345== HEAP SUMMARY:
==12345==     in use at exit: 0 bytes in 0 blocks
==12345==   total heap usage: X allocs, X frees, Y bytes allocated
==12345==
==12345== All heap blocks were freed -- no leaks are possible
```

### Performance Testing

- **Frame Rate**: Render thread maintains consistent frame rate
- **Input Responsiveness**: Input thread responds to keys within acceptable latency
- **Game Loop**: Main thread maintains consistent game tick intervals

---

## Comparison of Implemented Algorithms

### Difficulty Level Comparison

#### Easy Difficulty
- **Update Interval**: ~200ms per game tick
- **Characteristics**: 
  - Slower snake movement
  - More time to react
  - Easier for beginners
  - Lower risk of self-collision

#### Hard Difficulty
- **Update Interval**: ~100ms per game tick
- **Characteristics**: 
  - Faster snake movement
  - Requires quick reflexes
  - More challenging gameplay
  - Higher risk of mistakes

**Trade-offs**: 
- Easy mode provides better control but less excitement
- Hard mode is more challenging but requires more skill

### Algorithm Performance Analysis

#### Snake Movement Algorithm
- **Time Complexity**: O(1) for movement, O(n) for rendering
- **Space Complexity**: O(n) where n is snake length
- **Optimization**: Using `std::vector` for efficient insertion/deletion at ends

#### Collision Detection Algorithm
- **Time Complexity**: O(n) for self-collision check
- **Optimization Potential**: Could use spatial hashing for O(1) average case, but current O(n) is acceptable for typical snake lengths (< 100 segments)

#### Fruit Spawning Algorithm
- **Time Complexity**: O(n) worst case, O(1) average case
- **Optimization**: Retry limit prevents infinite loops on full board

### Threading Approach Comparison

#### Current Implementation (Three-Thread Model)
- **Advantages**:
  - Clear separation of concerns
  - Responsive input handling
  - Smooth rendering independent of game logic
  - Better resource utilization

- **Disadvantages**:
  - More complex synchronization
  - Potential for race conditions if not careful
  - Slightly higher memory overhead

#### Alternative: Single-Thread Model
- **Advantages**:
  - Simpler implementation
  - No synchronization overhead
  - Easier to debug

- **Disadvantages**:
  - Input blocking can affect rendering
  - Less responsive
  - Poor resource utilization

**Conclusion**: The three-thread model provides better user experience and responsiveness, justifying the added complexity.

### Synchronization Strategy Comparison

#### Mutex-Based Locking (Current)
- **Advantages**:
  - Simple to understand
  - Prevents race conditions
  - Standard C++17 support

- **Performance**: Minimal overhead for this use case

#### Lock-Free Approach (Alternative)
- **Advantages**:
  - Potentially faster
  - No blocking

- **Disadvantages**:
  - More complex to implement correctly
  - Requires careful memory ordering
  - May not provide significant benefit for this application

**Conclusion**: Mutex-based approach is appropriate for this game's synchronization needs, providing safety with acceptable performance.

---

## Additional Notes

### Language Extensions Used

1. **POSIX Extensions**:
   - `termios.h`: Terminal control
   - `sys/ioctl.h`: Terminal size detection
   - `unistd.h`: File I/O operations

2. **C++17 Features**:
   - `std::atomic`: Atomic operations for flags
   - `std::mutex`: Mutex for synchronization
   - `std::condition_variable`: Condition variables for notifications
   - `std::thread`: Thread management

3. **C99 Extensions**:
   - Variable Length Arrays (VLA) in render.cpp

### Non-Portable Dependencies

- **POSIX**: Required for terminal control and I/O
- **pthread**: Required for threading support (via C++ std::thread)

### Platform Compatibility

- ✅ **Linux**: Fully supported
- ✅ **macOS**: Fully supported
- ✅ **WSL (Windows)**: Supported
- ❌ **Native Windows**: Not supported (requires POSIX)

---

**Document Version**: 1.0  
**Last Updated**: [TODO: Update date]


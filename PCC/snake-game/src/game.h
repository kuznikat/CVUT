#ifndef GAME_H
#define GAME_H

#include <vector>
#include <mutex>
#include <condition_variable>
#include <atomic>
#include <random>
#include <cstdint>

struct Position {
    int x, y;
    Position(int x = 0, int y = 0) : x(x), y(y) {}
    bool operator==(const Position& other) const {
        return x == other.x && y == other.y;
    }
};

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE
};

enum class Difficulty {
    EASY,
    HARD
};

enum class GameState {
    RUNNING,
    PAUSED,
    GAME_OVER,
    QUIT
};

class Game {
public:
    Game(int width, int height, Difficulty difficulty);
    ~Game() = default;
    
    // Game state access (thread-safe)
    GameState getState() const;
    void setState(GameState state);
    
    // Direction control
    void setDirection(Direction dir);
    Direction getDirection() const;
    
    // Game logic
    void update();
    void spawnFruit();
    
    // Getters (thread-safe)
    const std::vector<Position>& getSnake() const;
    Position getFruit() const;
    int getScore() const;
    int getWidth() const;
    int getHeight() const;
    Difficulty getDifficulty() const;
    int getGameSpeed() const; // Returns delay in milliseconds
    
    // Mutex for thread synchronization
    mutable std::mutex gameMutex;
    std::condition_variable stateChanged;
    
private:
    int width;
    int height;
    Difficulty difficulty;
    std::atomic<GameState> state;
    std::atomic<Direction> currentDirection;
    std::atomic<Direction> nextDirection;
    
    std::vector<Position> snake;
    Position fruit;
    int score;
    
    std::mt19937 rng;
    
    bool checkCollision() const;
    Position getRandomPosition(); // Not const because rng modifies state
};

#endif // GAME_H


#include "game.h"
#include <algorithm>
#include <chrono>

Game::Game(int width, int height, Difficulty difficulty)
    : width(width), height(height), difficulty(difficulty),
      state(GameState::RUNNING),
      currentDirection(Direction::RIGHT),
      nextDirection(Direction::RIGHT),
      score(0),
      rng(std::chrono::steady_clock::now().time_since_epoch().count()) {
    
    // Initialize snake in the middle
    int startX = width / 2;
    int startY = height / 2;
    snake.push_back(Position(startX, startY));
    snake.push_back(Position(startX - 1, startY));
    snake.push_back(Position(startX - 2, startY));
    
    spawnFruit();
}

GameState Game::getState() const {
    return state.load();
}

void Game::setState(GameState newState) {
    state.store(newState);
    stateChanged.notify_all();
}

void Game::setDirection(Direction dir) {
    Direction current = currentDirection.load();
    
    // Prevent reversing into itself
    if ((current == Direction::UP && dir == Direction::DOWN) ||
        (current == Direction::DOWN && dir == Direction::UP) ||
        (current == Direction::LEFT && dir == Direction::RIGHT) ||
        (current == Direction::RIGHT && dir == Direction::LEFT)) {
        return;
    }
    
    nextDirection.store(dir);
}

Direction Game::getDirection() const {
    return currentDirection.load();
}

void Game::update() {
    if (state.load() != GameState::RUNNING) {
        return;
    }
    
    // Update direction
    currentDirection.store(nextDirection.load());
    Direction dir = currentDirection.load();
    
    if (dir == Direction::NONE) {
        return;
    }
    
    // Calculate new head position
    Position head = snake[0];
    Position newHead = head;
    
    switch (dir) {
        case Direction::UP:
            newHead.y--;
            break;
        case Direction::DOWN:
            newHead.y++;
            break;
        case Direction::LEFT:
            newHead.x--;
            break;
        case Direction::RIGHT:
            newHead.x++;
            break;
        case Direction::NONE:
            return;
    }
    
    // Check wall collision
    if (newHead.x < 0 || newHead.x >= width || 
        newHead.y < 0 || newHead.y >= height) {
        setState(GameState::GAME_OVER);
        return;
    }
    
    // Check self collision
    if (std::find(snake.begin(), snake.end(), newHead) != snake.end()) {
        setState(GameState::GAME_OVER);
        return;
    }
    
    // Add new head
    snake.insert(snake.begin(), newHead);
    
    // Check if fruit eaten
    if (newHead == fruit) {
        score += 10;
        spawnFruit();
    } else {
        // Remove tail
        snake.pop_back();
    }
}

void Game::spawnFruit() {
    Position newFruit;
    int attempts = 0;
    
    do {
        newFruit = getRandomPosition();
        attempts++;
    } while (std::find(snake.begin(), snake.end(), newFruit) != snake.end() && attempts < 100);
    
    fruit = newFruit;
}

const std::vector<Position>& Game::getSnake() const {
    return snake;
}

Position Game::getFruit() const {
    return fruit;
}

int Game::getScore() const {
    return score;
}

int Game::getWidth() const {
    return width;
}

int Game::getHeight() const {
    return height;
}

Difficulty Game::getDifficulty() const {
    return difficulty;
}

bool Game::checkCollision() const {
    if (snake.empty()) return true;
    
    Position head = snake[0];
    
    // Wall collision
    if (head.x < 0 || head.x >= width || head.y < 0 || head.y >= height) {
        return true;
    }
    
    // Self collision
    for (size_t i = 1; i < snake.size(); i++) {
        if (snake[i] == head) {
            return true;
        }
    }
    
    return false;
}

Position Game::getRandomPosition() {
    std::uniform_int_distribution<int> distX(0, width - 1);
    std::uniform_int_distribution<int> distY(0, height - 1);
    return Position(distX(rng), distY(rng));
}

int Game::getGameSpeed() const {
    // Easy: slower (200ms), Hard: faster (100ms)
    return difficulty == Difficulty::EASY ? 200 : 100;
}


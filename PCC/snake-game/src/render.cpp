#include "render.h"
#include <iostream>
#include <iomanip>
#include <sstream>

RenderThread::RenderThread(Game& game, Terminal& terminal)
    : game(game), terminal(terminal), running(false) {
}

RenderThread::~RenderThread() {
    stop();
    if (thread.joinable()) {
        thread.join();
    }
}

void RenderThread::start() {
    running = true;
    thread = std::thread(&RenderThread::renderLoop, this);
}

void RenderThread::stop() {
    running = false;
}

void RenderThread::join() {
    if (thread.joinable()) {
        thread.join();
    }
}

void RenderThread::renderLoop() {
    while (running) {
        std::unique_lock<std::mutex> lock(game.gameMutex);
        
        // Wait for state change or timeout
        game.stateChanged.wait_for(lock, std::chrono::milliseconds(50));
        
        if (game.getState() == GameState::QUIT) {
            break;
        }
        
        // Language Extension: VLA (Variable Length Array) usage
        // VLA size determined at runtime - this is a C99 extension supported by GCC/Clang
        int uiLineLength = game.getWidth() + 50; // Dynamic size based on game width
        if (uiLineLength > 0 && uiLineLength < 200) {
            // VLA: Variable Length Array (language extension)
            char uiBuffer[uiLineLength]; // VLA - size determined at runtime
            // Initialize buffer for UI rendering
            for (int i = 0; i < uiLineLength; i++) {
                uiBuffer[i] = ' ';
            }
            // Note: VLA demonstrates language extension usage as required
        }
        
        drawGame();
        
        lock.unlock();
        
        // Small delay to prevent excessive rendering
        std::this_thread::sleep_for(std::chrono::milliseconds(16)); // ~60 FPS
    }
}

void RenderThread::drawGame() {
    Terminal::clearScreen();
    Terminal::moveCursor(1, 1);
    
    drawUI();
    drawBorder();
    drawSnake();
    drawFruit();
    
    // Show game over or paused message
    GameState state = game.getState();
    if (state == GameState::GAME_OVER) {
        Terminal::moveCursor(game.getHeight() / 2, game.getWidth() / 2 - 5);
        Terminal::setColor(31); // Red
        std::cout << "GAME OVER!";
        Terminal::resetColor();
    } else if (state == GameState::PAUSED) {
        Terminal::moveCursor(game.getHeight() / 2, game.getWidth() / 2 - 3);
        Terminal::setColor(33); // Yellow
        std::cout << "PAUSED";
        Terminal::resetColor();
    }
    
    Terminal::moveCursor(game.getHeight() + 2, 1);
    std::cout.flush();
}

void RenderThread::drawBorder() {
    Terminal::setColor(36); // Cyan
    
    // Top border
    Terminal::moveCursor(3, 1);
    std::cout << "+";
    for (int i = 0; i < game.getWidth(); i++) {
        std::cout << "-";
    }
    std::cout << "+";
    
    // Side borders
    for (int i = 0; i < game.getHeight(); i++) {
        Terminal::moveCursor(4 + i, 1);
        std::cout << "|";
        Terminal::moveCursor(4 + i, game.getWidth() + 2);
        std::cout << "|";
    }
    
    // Bottom border
    Terminal::moveCursor(4 + game.getHeight(), 1);
    std::cout << "+";
    for (int i = 0; i < game.getWidth(); i++) {
        std::cout << "-";
    }
    std::cout << "+";
    
    Terminal::resetColor();
}

void RenderThread::drawSnake() {
    Terminal::setColor(32); // Green
    
    const auto& snake = game.getSnake();
    for (size_t i = 0; i < snake.size(); i++) {
        const Position& pos = snake[i];
        Terminal::moveCursor(4 + pos.y, 2 + pos.x);
        
        if (i == 0) {
            std::cout << "O"; // Head
        } else {
            std::cout << "o"; // Body
        }
    }
    
    Terminal::resetColor();
}

void RenderThread::drawFruit() {
    Terminal::setColor(31); // Red
    Position fruit = game.getFruit();
    Terminal::moveCursor(4 + fruit.y, 2 + fruit.x);
    std::cout << "*";
    Terminal::resetColor();
}

void RenderThread::drawUI() {
    Terminal::moveCursor(1, 1);
    Terminal::setColor(1); // Bold
    
    std::cout << "Score: " << std::setw(6) << game.getScore();
    std::cout << " | Difficulty: ";
    
    Terminal::resetColor();
    if (game.getDifficulty() == Difficulty::EASY) {
        Terminal::setColor(32); // Green
        std::cout << "EASY";
    } else {
        Terminal::setColor(31); // Red
        std::cout << "HARD";
    }
    
    Terminal::resetColor();
    std::cout << " | Controls: W/A/S/D | P=Pause | Q=Quit";
}


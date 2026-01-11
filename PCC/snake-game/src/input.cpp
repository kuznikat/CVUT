#include "input.h"
#include <iostream>

InputThread::InputThread(Game& game, Terminal& terminal)
    : game(game), terminal(terminal), running(false) {
}

InputThread::~InputThread() {
    stop();
    if (thread.joinable()) {
        thread.join();
    }
}

void InputThread::start() {
    running = true;
    thread = std::thread(&InputThread::inputLoop, this);
}

void InputThread::stop() {
    running = false;
}

void InputThread::join() {
    if (thread.joinable()) {
        thread.join();
    }
}

void InputThread::inputLoop() {
    while (running) {
        char c = terminal.readChar();
        
        if (c == 0) {
            continue;
        }
        
        std::lock_guard<std::mutex> lock(game.gameMutex);
        
        GameState currentState = game.getState();
        
        // Handle pause/unpause
        if (c == 'p' || c == 'P') {
            if (currentState == GameState::RUNNING) {
                game.setState(GameState::PAUSED);
            } else if (currentState == GameState::PAUSED) {
                game.setState(GameState::RUNNING);
            }
            continue;
        }
        
        // Handle quit
        if (c == 'q' || c == 'Q' || c == 27) { // ESC key
            game.setState(GameState::QUIT);
            break;
        }
        
        // Only process movement if game is running
        if (currentState != GameState::RUNNING) {
            continue;
        }
        
        // Handle movement
        Direction dir = Direction::NONE;
        switch (c) {
            case 'w':
            case 'W':
                dir = Direction::UP;
                break;
            case 's':
            case 'S':
                dir = Direction::DOWN;
                break;
            case 'a':
            case 'A':
                dir = Direction::LEFT;
                break;
            case 'd':
            case 'D':
                dir = Direction::RIGHT;
                break;
        }
        
        if (dir != Direction::NONE) {
            game.setDirection(dir);
        }
    }
}


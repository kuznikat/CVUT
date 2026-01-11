#include <iostream>
#include <string>
#include <cstring>
#include "game.h"
#include "terminal.h"
#include "input.h"
#include "render.h"
#include <thread>
#include <chrono>

void printHelp() {
    std::cout << "Snake Game - Semester Project\n";
    std::cout << "\n";
    std::cout << "Usage: snake [OPTIONS]\n";
    std::cout << "\n";
    std::cout << "Options:\n";
    std::cout << "  --help, -h          Show this help message\n";
    std::cout << "  --difficulty, -d    Set difficulty (easy|hard) [default: easy]\n";
    std::cout << "  --width, -w         Set game width [default: 40]\n";
    std::cout << "  --height, -h        Set game height [default: 20]\n";
    std::cout << "\n";
    std::cout << "Controls:\n";
    std::cout << "  W/A/S/D            Move snake (Up/Left/Down/Right)\n";
    std::cout << "  P                  Pause/Unpause game\n";
    std::cout << "  Q or ESC           Quit game\n";
    std::cout << "\n";
    std::cout << "Rules:\n";
    std::cout << "  - Don't hit walls or your own tail\n";
    std::cout << "  - Eat fruits (*) to grow and gain 10 points\n";
    std::cout << "  - Game speed increases with difficulty\n";
    std::cout << "\n";
}

Difficulty parseDifficulty(const std::string& arg) {
    if (arg == "easy" || arg == "EASY") {
        return Difficulty::EASY;
    } else if (arg == "hard" || arg == "HARD") {
        return Difficulty::HARD;
    }
    return Difficulty::EASY;
}

int main(int argc, char* argv[]) {
    // Parse arguments
    Difficulty difficulty = Difficulty::EASY;
    int width = 40;
    int height = 20;
    
    for (int i = 1; i < argc; i++) {
        std::string arg = argv[i];
        
        if (arg == "--help" || arg == "-h") {
            printHelp();
            return 0;
        } else if (arg == "--difficulty" || arg == "-d") {
            if (i + 1 < argc) {
                difficulty = parseDifficulty(argv[++i]);
            } else {
                std::cerr << "Error: --difficulty requires an argument (easy|hard)\n";
                return 1;
            }
        } else if (arg == "--width" || arg == "-w") {
            if (i + 1 < argc) {
                width = std::stoi(argv[++i]);
                if (width < 10 || width > 100) {
                    std::cerr << "Error: Width must be between 10 and 100\n";
                    return 1;
                }
            } else {
                std::cerr << "Error: --width requires an argument\n";
                return 1;
            }
        } else if (arg == "--height" || arg == "-h") {
            if (i + 1 < argc) {
                height = std::stoi(argv[++i]);
                if (height < 10 || height > 50) {
                    std::cerr << "Error: Height must be between 10 and 50\n";
                    return 1;
                }
            } else {
                std::cerr << "Error: --height requires an argument\n";
                return 1;
            }
        }
    }
    
    // Initialize terminal
    Terminal terminal;
    terminal.enableRawMode();
    terminal.clearScreen();
    terminal.hideCursor();
    
    // Initialize game
    Game game(width, height, difficulty);
    
    // Create threads
    InputThread inputThread(game, terminal);
    RenderThread renderThread(game, terminal);
    
    // Start threads
    inputThread.start();
    renderThread.start();
    
    // Game loop (main thread handles game logic/timing)
    while (game.getState() != GameState::QUIT) {
        std::unique_lock<std::mutex> lock(game.gameMutex);
        
        GameState state = game.getState();
        
        if (state == GameState::RUNNING) {
            game.update();
            lock.unlock();
            
            // Sleep based on difficulty
            int speed = game.getGameSpeed();
            std::this_thread::sleep_for(std::chrono::milliseconds(speed));
        } else if (state == GameState::GAME_OVER) {
            lock.unlock();
            // Wait a bit before allowing quit
            std::this_thread::sleep_for(std::chrono::seconds(2));
        } else {
            lock.unlock();
            std::this_thread::sleep_for(std::chrono::milliseconds(100));
        }
    }
    
    // Stop threads
    inputThread.stop();
    renderThread.stop();
    
    // Wait for threads to finish
    inputThread.join();
    renderThread.join();
    
    // Restore terminal
    terminal.showCursor();
    terminal.clearScreen();
    terminal.disableRawMode();
    
    std::cout << "Final Score: " << game.getScore() << "\n";
    std::cout << "Thanks for playing!\n";
    
    return 0;
}


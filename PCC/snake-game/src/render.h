#ifndef RENDER_H
#define RENDER_H

#include "game.h"
#include "terminal.h"
#include <thread>
#include <atomic>

class RenderThread {
public:
    RenderThread(Game& game, Terminal& terminal);
    ~RenderThread();
    
    void start();
    void stop();
    void join();
    
private:
    void renderLoop();
    void drawGame();
    void drawBorder();
    void drawSnake();
    void drawFruit();
    void drawUI();
    
    Game& game;
    [[maybe_unused]] Terminal& terminal; // Stored for potential future use
    std::thread thread;
    std::atomic<bool> running;
};

#endif // RENDER_H


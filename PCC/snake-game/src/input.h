#ifndef INPUT_H
#define INPUT_H

#include "game.h"
#include "terminal.h"
#include <thread>
#include <atomic>

class InputThread {
public:
    InputThread(Game& game, Terminal& terminal);
    ~InputThread();
    
    void start();
    void stop();
    void join();
    
private:
    void inputLoop();
    
    Game& game;
    Terminal& terminal;
    std::thread thread;
    std::atomic<bool> running;
};

#endif // INPUT_H


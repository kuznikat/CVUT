#ifndef TERMINAL_H
#define TERMINAL_H

#include <termios.h>
#include <unistd.h>
#include <iostream>
#include <string>

class Terminal {
public:
    Terminal();
    ~Terminal();
    
    // Disable copy constructor and assignment
    Terminal(const Terminal&) = delete;
    Terminal& operator=(const Terminal&) = delete;
    
    // Enable/disable raw mode
    void enableRawMode();
    void disableRawMode();
    
    // ANSI escape sequences
    static void clearScreen();
    static void hideCursor();
    static void showCursor();
    static void moveCursor(int row, int col);
    static void setColor(int color);
    static void resetColor();
    
    // Get terminal size
    static int getWidth();
    static int getHeight();
    
    // Read character (non-blocking)
    char readChar();
    
private:
    struct termios original_termios;
    bool raw_mode_enabled;
};

#endif // TERMINAL_H


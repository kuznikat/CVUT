#include "terminal.h"
#include <sys/ioctl.h>
#include <fcntl.h>
#include <cstring>

Terminal::Terminal() : raw_mode_enabled(false) {
    tcgetattr(STDIN_FILENO, &original_termios);
}

Terminal::~Terminal() {
    disableRawMode();
    showCursor();
}

void Terminal::enableRawMode() {
    if (raw_mode_enabled) return;
    
    struct termios raw = original_termios;
    
    // Disable echo, canonical mode, signals
    raw.c_iflag &= ~(BRKINT | ICRNL | INPCK | ISTRIP | IXON);
    raw.c_oflag &= ~(OPOST);
    raw.c_cflag |= (CS8);
    raw.c_lflag &= ~(ECHO | ICANON | IEXTEN | ISIG);
    
    // Set minimum number of characters for read
    raw.c_cc[VMIN] = 0;
    raw.c_cc[VTIME] = 1; // 0.1 second timeout
    
    tcsetattr(STDIN_FILENO, TCSAFLUSH, &raw);
    raw_mode_enabled = true;
}

void Terminal::disableRawMode() {
    if (!raw_mode_enabled) return;
    
    tcsetattr(STDIN_FILENO, TCSAFLUSH, &original_termios);
    raw_mode_enabled = false;
}

void Terminal::clearScreen() {
    std::cout << "\033[2J";
    std::cout.flush();
}

void Terminal::hideCursor() {
    std::cout << "\033[?25l";
    std::cout.flush();
}

void Terminal::showCursor() {
    std::cout << "\033[?25h";
    std::cout.flush();
}

void Terminal::moveCursor(int row, int col) {
    std::cout << "\033[" << row << ";" << col << "H";
    std::cout.flush();
}

void Terminal::setColor(int color) {
    std::cout << "\033[" << color << "m";
    std::cout.flush();
}

void Terminal::resetColor() {
    std::cout << "\033[0m";
    std::cout.flush();
}

int Terminal::getWidth() {
    struct winsize w;
    ioctl(STDOUT_FILENO, TIOCGWINSZ, &w);
    return w.ws_col > 0 ? w.ws_col : 80;
}

int Terminal::getHeight() {
    struct winsize w;
    ioctl(STDOUT_FILENO, TIOCGWINSZ, &w);
    return w.ws_row > 0 ? w.ws_row : 24;
}

char Terminal::readChar() {
    char c = 0;
    if (read(STDIN_FILENO, &c, 1) == 1) {
        return c;
    }
    return 0;
}


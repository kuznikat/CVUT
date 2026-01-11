cmake_minimum_required(VERSION 3.10)
project(SnakeGame VERSION 1.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Compiler flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra -pedantic")

# Find pthread
find_package(Threads REQUIRED)

# Source files
set(SOURCES
    src/main.cpp
    src/game.cpp
    src/terminal.cpp
    src/input.cpp
    src/render.cpp
)

set(HEADERS
    src/game.h
    src/terminal.h
    src/input.h
    src/render.h
)

# Main executable
add_executable(snake ${SOURCES} ${HEADERS})
target_link_libraries(snake PRIVATE Threads::Threads)

# Test executable
enable_testing()
add_executable(test_snake tests/test_game.cpp src/game.cpp src/terminal.cpp)
target_link_libraries(test_snake PRIVATE Threads::Threads)
add_test(NAME GameTests COMMAND test_snake)


#!/bin/bash

# Build script for Snake Game

echo "Building Snake Game..."

# Create build directory if it doesn't exist
mkdir -p build
cd build

# Run CMake
echo "Running CMake..."
cmake ..

# Build the project
echo "Building project..."
make

# Run tests
echo "Running tests..."
if [ -f test_snake ]; then
    ./test_snake
else
    echo "Test executable not found. Build may have failed."
    exit 1
fi

echo ""
echo "Build complete! Run './snake' to play the game."


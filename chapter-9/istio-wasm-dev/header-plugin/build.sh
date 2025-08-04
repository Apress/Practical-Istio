#!/bin/bash
set -e

# Configuration
SDK_PATH="${PROXY_WASM_CPP_SDK:-$HOME/istio-wasm-dev/proxy-wasm-cpp-sdk}"
OUTPUT_NAME="header_plugin.wasm"

# Ensure we have all the tools we need
if ! command -v emcc &> /dev/null; then
    echo "Error: Emscripten compiler (emcc) not found!"
    echo "Please install and activate Emscripten SDK first."
    exit 1
fi

if [ ! -d "$SDK_PATH" ]; then
    echo "Error: Proxy-Wasm C++ SDK not found at $SDK_PATH"
    echo "Please set PROXY_WASM_CPP_SDK to the correct path or clone the SDK:"
    echo "git clone https://github.com/proxy-wasm/proxy-wasm-cpp-sdk.git"
    exit 1
fi

echo "=== Building WebAssembly Plugin ==="
echo "SDK Path: $SDK_PATH"

# Create build directory
mkdir -p build

# Compile the plugin
echo "Compiling plugin..."
emcc -s WASM=1 \
     -s TOTAL_MEMORY=65536 \
     -s TOTAL_STACK=10240 \
     -s ALLOW_MEMORY_GROWTH=0 \
     -s WASM_BIGINT=1 \
     -s ERROR_ON_UNDEFINED_SYMBOLS=0 \
     -s INITIAL_MEMORY=65536 \
     -s "EXPORTED_FUNCTIONS=['_malloc']" \
     --std=c++17 \
     -O3 \
     -flto \
     -fno-exceptions \
     -fno-rtti \
     -DNDEBUG \
     -I"$SDK_PATH" \
     src/header_plugin.cc \
     -o build/$OUTPUT_NAME

if [ $? -eq 0 ]; then
    echo "=== Build Successful ==="
    echo "Output: build/$OUTPUT_NAME"
    ls -la build/$OUTPUT_NAME
else
    echo "=== Build Failed ==="
    exit 1
fi

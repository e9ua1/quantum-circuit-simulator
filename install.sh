#!/bin/bash

echo "🎨 Quantum Circuit Visualizer - Setup"
echo "====================================="

if ! command -v python3 &> /dev/null; then
    echo "❌ Python3 not found. Please install Python 3.9+"
    exit 1
fi

echo "✅ Python3 found: $(python3 --version)"

echo ""
echo "📦 Installing dependencies..."
pip install -r src/main/python/requirements.txt

echo ""
echo "✅ Setup complete!"
echo ""
echo "🚀 Usage:"
echo "  python3 src/main/python/main.py output/circuit_result.json"

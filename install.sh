#!/bin/bash

echo "🎨 Quantum Circuit Visualizer - Setup"
echo "====================================="

# Python 3.9+ 체크
if ! command -v python3 &> /dev/null; then
    echo "❌ Python3 not found. Please install Python 3.9+"
    exit 1
fi

PYTHON_VERSION=$(python3 --version 2>&1 | awk '{print $2}')
echo "✅ Python3 found: $PYTHON_VERSION"

# Python 버전 체크 (3.9 이상)
MAJOR=$(echo $PYTHON_VERSION | cut -d. -f1)
MINOR=$(echo $PYTHON_VERSION | cut -d. -f2)

if [ "$MAJOR" -lt 3 ] || ([ "$MAJOR" -eq 3 ] && [ "$MINOR" -lt 9 ]); then
    echo "⚠️  Warning: Python 3.9+ recommended (found $PYTHON_VERSION)"
fi

echo ""
echo "📦 Installing dependencies..."

# macOS 호환 설치 (--break-system-packages 시도 후 일반 설치)
if pip3 install -r src/main/python/requirements.txt --break-system-packages 2>/dev/null; then
    echo ""
    echo "✅ Setup complete!"
elif pip3 install -r src/main/python/requirements.txt; then
    echo ""
    echo "✅ Setup complete!"
else
    echo ""
    echo "❌ Installation failed!"
    echo ""
    echo "💡 Try manual installation:"
    echo "   pip3 install --user -r src/main/python/requirements.txt"
    echo ""
    echo "   Or use virtual environment:"
    echo "   python3 -m venv venv"
    echo "   source venv/bin/activate  # Windows: venv\\Scripts\\activate"
    echo "   pip install -r src/main/python/requirements.txt"
    exit 1
fi

echo ""
echo "🚀 Ready to use!"
echo "   Run: ./gradlew run"
echo ""
echo "💡 Python visualizer will be called automatically"
echo "   Manual test: python3 src/main/python/main.py output/circuit_result.json"

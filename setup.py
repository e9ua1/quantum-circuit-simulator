#!/usr/bin/env python3
from setuptools import setup, find_packages
from pathlib import Path

# requirements.txt 읽기 (에러 처리)
requirements_path = Path('src/main/python/requirements.txt')
if requirements_path.exists():
    requirements = requirements_path.read_text().splitlines()
    # 빈 줄과 주석 제거
    requirements = [r.strip() for r in requirements if r.strip() and not r.startswith('#')]
else:
    requirements = []

setup(
    name='quantum-circuit-visualizer',
    version='1.0.0',
    description='Quantum Circuit Visualization Tool for Quantum Circuit Simulator',
    author='e9ua1',
    url='https://github.com/e9ua1/quantum-circuit-simulator',
    packages=find_packages(where='src/main/python'),
    package_dir={'': 'src/main/python'},
    install_requires=requirements,
    python_requires='>=3.9',
)

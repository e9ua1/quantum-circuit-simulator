#!/usr/bin/env python3
from setuptools import setup, find_packages

with open('src/main/python/requirements.txt') as f:
    requirements = f.read().splitlines()

setup(
    name='quantum-circuit-visualizer',
    version='1.0.0',
    description='Quantum Circuit Visualization Tool',
    packages=find_packages(where='src/main/python'),
    package_dir={'': 'src/main/python'},
    install_requires=requirements,
    python_requires='>=3.9',
)

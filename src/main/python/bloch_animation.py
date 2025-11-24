import json
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation, PillowWriter
from qutip import Bloch
import sys

def load_circuit_result(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        return json.load(f)

def probability_to_bloch_vector(prob_one):
    if prob_one == 0.0:
        return np.array([0, 0, 1])
    elif prob_one == 1.0:
        return np.array([0, 0, -1])
    elif abs(prob_one - 0.5) < 1e-6:
        return np.array([1, 0, 0])
    else:
        theta = 2 * np.arcsin(np.sqrt(prob_one))
        x = np.sin(theta)
        z = np.cos(theta)
        return np.array([x, 0, z])

def interpolate_vectors(v1, v2, num_frames):
    """SLERP: Spherical Linear Interpolation"""
    v1_norm = v1 / np.linalg.norm(v1)
    v2_norm = v2 / np.linalg.norm(v2)
    
    dot = np.clip(np.dot(v1_norm, v2_norm), -1.0, 1.0)
    theta = np.arccos(dot)
    
    if theta < 1e-6:
        t = np.linspace(0, 1, num_frames)
        return [v1_norm * (1-ti) + v2_norm * ti for ti in t]
    
    sin_theta = np.sin(theta)
    t = np.linspace(0, 1, num_frames)
    
    interpolated = []
    for ti in t:
        a = np.sin((1 - ti) * theta) / sin_theta
        b = np.sin(ti * theta) / sin_theta
        v = a * v1_norm + b * v2_norm
        interpolated.append(v)
    
    return interpolated

def create_bloch_animation(circuit_result, output_path, fps=20):
    qubit_count = circuit_result['qubit_count']
    step_states = circuit_result['step_states']
    qubit_idx = 0
    
    bloch_vectors = []
    descriptions = []
    
    for step_state in step_states:
        prob_one = step_state['qubit_probabilities'][str(qubit_idx)]
        vector = probability_to_bloch_vector(prob_one)
        bloch_vectors.append(vector)
        descriptions.append(step_state['description'])
    
    frames_per_transition = fps
    all_frames = []
    all_colors = []
    all_descriptions = []
    colors = ['green', 'blue', 'red', 'purple', 'orange']
    
    for i in range(len(bloch_vectors) - 1):
        interpolated = interpolate_vectors(
            bloch_vectors[i], 
            bloch_vectors[i + 1], 
            frames_per_transition
        )
        all_frames.extend(interpolated)
        
        color_start = colors[i % len(colors)]
        all_colors.extend([color_start] * frames_per_transition)
        all_descriptions.extend([descriptions[i + 1]] * frames_per_transition)
    
    hold_frames = fps // 2
    all_frames.extend([bloch_vectors[-1]] * hold_frames)
    all_colors.extend([colors[(len(bloch_vectors)-1) % len(colors)]] * hold_frames)
    all_descriptions.extend([descriptions[-1]] * hold_frames)
    
    fig = plt.figure(figsize=(8, 8))
    
    def animate(frame_idx):
        plt.clf()
        
        b = Bloch(fig=fig)
        vector = all_frames[frame_idx]
        color = all_colors[frame_idx]
        description = all_descriptions[frame_idx]
        
        b.add_vectors([vector])
        b.vector_color = [color]
        b.vector_width = 3
        
        step_num = frame_idx // frames_per_transition
        b.fig.suptitle(
            f'Qubit {qubit_idx} State Evolution\n{description}',
            fontsize=14,
            fontweight='bold'
        )
        
        b.render()
        return b.fig,
    
    total_frames = len(all_frames)
    
    print(f"Creating Bloch sphere animation...")
    print(f"  Total frames: {total_frames}")
    print(f"  Duration: {total_frames/fps:.1f}s")
    print(f"  Steps: {' → '.join(descriptions)}")
    
    anim = FuncAnimation(
        fig, 
        animate, 
        frames=total_frames,
        interval=1000/fps,
        blit=False
    )
    
    writer = PillowWriter(fps=fps)
    anim.save(output_path, writer=writer)
    print(f"  Saved: {output_path}")
    
    plt.close()

def main():
    if len(sys.argv) < 2:
        print("Usage: python bloch_animation.py <circuit_result.json>")
        sys.exit(1)
    
    input_file = sys.argv[1]
    output_file = 'output/bloch_evolution.gif'
    
    print(f"Loading circuit result from: {input_file}")
    circuit_result = load_circuit_result(input_file)
    
    circuit_name = circuit_result.get('circuit_name', 'Unknown')
    print(f"Circuit: {circuit_name}")
    print(f"Qubits: {circuit_result['qubit_count']}")
    print()
    
    create_bloch_animation(circuit_result, output_file, fps=20)
    
    print()
    print("✅ Animation complete!")
    print(f"   {output_file}")

if __name__ == '__main__':
    main()

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
        return np.array([np.sin(theta), 0, np.cos(theta)])

def calculate_entanglement_strength(system_state, q0_prob, q1_prob):
    bell_states = ['00', '11']
    bell_prob = sum(system_state.get(s, 0.0) for s in bell_states)
    independent_prob = abs(q0_prob - 0.5) + abs(q1_prob - 0.5)

    if bell_prob > 0.9:
        return min(1.0, bell_prob)

    return max(0.0, bell_prob - independent_prob * 0.5)

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

def create_entanglement_animation(circuit_result, output_path, fps=20):
    step_states = circuit_result['step_states']
    qubit_count = circuit_result['qubit_count']

    if qubit_count < 2:
        print("⚠️  Entanglement visualization requires at least 2 qubits")
        return

    q0_vectors = []
    q1_vectors = []
    entanglement_strengths = []
    descriptions = []

    for step_state in step_states:
        q0_prob = step_state['qubit_probabilities']['0']
        q1_prob = step_state['qubit_probabilities']['1']
        system_state = step_state['system_state']

        q0_vec = probability_to_bloch_vector(q0_prob)
        q1_vec = probability_to_bloch_vector(q1_prob)
        entanglement = calculate_entanglement_strength(system_state, q0_prob, q1_prob)

        q0_vectors.append(q0_vec)
        q1_vectors.append(q1_vec)
        entanglement_strengths.append(entanglement)
        descriptions.append(step_state['description'])

    frames_per_transition = fps
    all_q0_frames = []
    all_q1_frames = []
    all_entanglement_frames = []
    all_descriptions = []

    for i in range(len(q0_vectors) - 1):
        q0_interp = interpolate_vectors(q0_vectors[i], q0_vectors[i+1], frames_per_transition)
        q1_interp = interpolate_vectors(q1_vectors[i], q1_vectors[i+1], frames_per_transition)
        entanglement_interp = np.linspace(
            entanglement_strengths[i],
            entanglement_strengths[i+1],
            frames_per_transition
        )

        all_q0_frames.extend(q0_interp)
        all_q1_frames.extend(q1_interp)
        all_entanglement_frames.extend(entanglement_interp)
        all_descriptions.extend([descriptions[i+1]] * frames_per_transition)

    hold_frames = fps // 2
    all_q0_frames.extend([q0_vectors[-1]] * hold_frames)
    all_q1_frames.extend([q1_vectors[-1]] * hold_frames)
    all_entanglement_frames.extend([entanglement_strengths[-1]] * hold_frames)
    all_descriptions.extend([descriptions[-1]] * hold_frames)

    fig = plt.figure(figsize=(16, 7))

    def animate(frame_idx):
        plt.clf()

        ax_left = fig.add_subplot(1, 2, 1, projection='3d')
        ax_right = fig.add_subplot(1, 2, 2, projection='3d')

        b_q0 = Bloch(fig=fig, axes=ax_left)
        b_q1 = Bloch(fig=fig, axes=ax_right)

        q0_vec = all_q0_frames[frame_idx]
        q1_vec = all_q1_frames[frame_idx]
        entanglement = all_entanglement_frames[frame_idx]
        description = all_descriptions[frame_idx]

        b_q0.add_vectors([q0_vec])
        b_q1.add_vectors([q1_vec])

        color = 'red' if entanglement > 0.3 else 'blue'
        b_q0.vector_color = [color]
        b_q1.vector_color = [color]
        b_q0.vector_width = 3
        b_q1.vector_width = 3

        ax_left.set_title('Qubit 0', fontsize=14, fontweight='bold', pad=20)
        ax_right.set_title('Qubit 1', fontsize=14, fontweight='bold', pad=20)

        b_q0.render()
        b_q1.render()

        if entanglement > 0.1:
            fig.text(0.5, 0.95, f'⚡ Entanglement: {entanglement:.2f}',
                    ha='center', fontsize=16, fontweight='bold',
                    color='red',
                    bbox=dict(boxstyle='round', facecolor='yellow', alpha=0.8))

        fig.text(0.5, 0.05, description, ha='center', fontsize=13, fontweight='bold')

        step_num = frame_idx // frames_per_transition
        fig.text(0.02, 0.95, f'Step {step_num}/{len(descriptions)-1}',
                fontsize=12, va='top',
                bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.8))

        return fig,

    total_frames = len(all_q0_frames)

    print(f"Creating entanglement animation...")
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

def create_entanglement_static(circuit_result, output_path):
    step_states = circuit_result['step_states']
    qubit_count = circuit_result['qubit_count']

    if qubit_count < 2:
        print("⚠️  Entanglement visualization requires at least 2 qubits")
        return

    num_steps = len(step_states)
    cols = min(3, num_steps)
    rows = (num_steps + cols - 1) // cols

    fig = plt.figure(figsize=(6 * cols, 5 * rows))

    for idx, step_state in enumerate(step_states):
        q0_prob = step_state['qubit_probabilities']['0']
        q1_prob = step_state['qubit_probabilities']['1']
        system_state = step_state['system_state']
        description = step_state['description']

        q0_vec = probability_to_bloch_vector(q0_prob)
        q1_vec = probability_to_bloch_vector(q1_prob)
        entanglement = calculate_entanglement_strength(system_state, q0_prob, q1_prob)

        ax_left = fig.add_subplot(rows, cols * 2, idx * 2 + 1, projection='3d')
        ax_right = fig.add_subplot(rows, cols * 2, idx * 2 + 2, projection='3d')

        b_q0 = Bloch(fig=fig, axes=ax_left)
        b_q1 = Bloch(fig=fig, axes=ax_right)

        b_q0.add_vectors([q0_vec])
        b_q1.add_vectors([q1_vec])

        color = 'red' if entanglement > 0.3 else 'blue'
        b_q0.vector_color = [color]
        b_q1.vector_color = [color]

        ax_left.set_title(f'Q0 | {description}', fontsize=10, fontweight='bold')
        ax_right.set_title(f'Q1 | Ent: {entanglement:.2f}', fontsize=10, fontweight='bold')

        b_q0.render()
        b_q1.render()

    plt.tight_layout()
    plt.savefig(output_path, dpi=150, bbox_inches='tight')
    plt.close()

    print(f"Visualizing Entanglement Steps ({num_steps} steps)")
    print(f"  - {output_path}")

def main():
    if len(sys.argv) < 2:
        print("Usage: python entanglement_visualizer.py <circuit_result.json>")
        sys.exit(1)

    input_file = sys.argv[1]
    output_gif = 'output/entanglement_evolution.gif'
    output_png = 'output/entanglement_steps.png'

    print(f"Loading circuit result from: {input_file}")
    circuit_result = load_circuit_result(input_file)

    circuit_name = circuit_result.get('circuit_name', 'Unknown')
    print(f"Circuit: {circuit_name}")
    print(f"Qubits: {circuit_result['qubit_count']}")
    print()

    create_entanglement_static(circuit_result, output_png)
    print()
    create_entanglement_animation(circuit_result, output_gif, fps=20)

    print()
    print("✅ Entanglement visualization complete!")
    print(f"   {output_png}")
    print(f"   {output_gif}")

if __name__ == '__main__':
    main()

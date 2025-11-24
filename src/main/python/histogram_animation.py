import json
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation, PillowWriter
import sys

def load_circuit_result(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        return json.load(f)

def interpolate_probabilities(prob_dict1, prob_dict2, num_frames):
    all_states = sorted(set(prob_dict1.keys()) | set(prob_dict2.keys()))
    t = np.linspace(0, 1, num_frames)
    interpolated = []
    
    for ti in t:
        frame_probs = {}
        for state in all_states:
            p1 = prob_dict1.get(state, 0.0)
            p2 = prob_dict2.get(state, 0.0)
            frame_probs[state] = p1 * (1 - ti) + p2 * ti
        interpolated.append(frame_probs)
    
    return interpolated

def create_histogram_animation(circuit_result, output_path, fps=20):
    step_states = circuit_result['step_states']
    qubit_count = circuit_result['qubit_count']
    
    num_states = 2 ** qubit_count
    all_states = [format(i, f'0{qubit_count}b') for i in range(num_states)]
    
    distributions = []
    descriptions = []
    
    for step_state in step_states:
        distributions.append(step_state['system_state'])
        descriptions.append(step_state['description'])
    
    frames_per_transition = fps
    all_frames = []
    all_descriptions = []
    
    for i in range(len(distributions) - 1):
        interpolated = interpolate_probabilities(
            distributions[i],
            distributions[i + 1],
            frames_per_transition
        )
        all_frames.extend(interpolated)
        all_descriptions.extend([descriptions[i + 1]] * frames_per_transition)
    
    hold_frames = fps // 2
    all_frames.extend([distributions[-1]] * hold_frames)
    all_descriptions.extend([descriptions[-1]] * hold_frames)
    
    fig, ax = plt.subplots(figsize=(10, 6))
    x_pos = np.arange(len(all_states))
    
    def animate(frame_idx):
        ax.clear()
        
        probs_dict = all_frames[frame_idx]
        description = all_descriptions[frame_idx]
        probabilities = [probs_dict.get(state, 0.0) for state in all_states]
        
        bars = ax.bar(x_pos, probabilities, color='steelblue', alpha=0.8, edgecolor='black')
        
        for i, (state, prob) in enumerate(zip(all_states, probabilities)):
            if prob > 0.01:
                ax.text(i, prob + 0.02, f'{prob:.3f}', 
                       ha='center', va='bottom', fontsize=11, fontweight='bold')
        
        ax.set_xlabel('Quantum State', fontsize=13, fontweight='bold')
        ax.set_ylabel('Probability', fontsize=13, fontweight='bold')
        ax.set_title(f'Quantum State Distribution\n{description}', 
                    fontsize=14, fontweight='bold', pad=20)
        ax.set_xticks(x_pos)
        ax.set_xticklabels([f'|{state}⟩' for state in all_states], fontsize=11)
        ax.set_ylim(0, 1.05)
        ax.grid(axis='y', alpha=0.3, linestyle='--')
        
        step_num = frame_idx // frames_per_transition
        ax.text(0.02, 0.98, f'Step {step_num}/{len(descriptions)-1}', 
               transform=ax.transAxes, fontsize=12,
               verticalalignment='top',
               bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.8))
        
        return bars,
    
    total_frames = len(all_frames)
    
    print(f"Creating histogram animation...")
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
        print("Usage: python histogram_animation.py <circuit_result.json>")
        sys.exit(1)
    
    input_file = sys.argv[1]
    output_file = 'output/histogram_evolution.gif'
    
    print(f"Loading circuit result from: {input_file}")
    circuit_result = load_circuit_result(input_file)
    
    circuit_name = circuit_result.get('circuit_name', 'Unknown')
    print(f"Circuit: {circuit_name}")
    print(f"Qubits: {circuit_result['qubit_count']}")
    print()
    
    create_histogram_animation(circuit_result, output_file, fps=20)
    
    print()
    print("✅ Animation complete!")
    print(f"   {output_file}")

if __name__ == '__main__':
    main()

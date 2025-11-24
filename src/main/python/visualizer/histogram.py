import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.animation import FuncAnimation


class StateHistogramVisualizer:
    
    @staticmethod
    def visualize_static(system_state, output_path='output/histogram.png'):
        print("Visualizing State Histogram")
        
        states = sorted(system_state.keys())
        probabilities = [system_state[s] for s in states]
        
        fig, ax = plt.subplots(figsize=(10, 6))
        bars = ax.bar(states, probabilities, color='steelblue', alpha=0.8)
        
        ax.set_xlabel('Quantum State', fontsize=12)
        ax.set_ylabel('Probability', fontsize=12)
        ax.set_title('Quantum State Distribution', fontsize=14, fontweight='bold')
        ax.set_ylim(0, 1.0)
        ax.grid(axis='y', alpha=0.3)
        
        for bar, prob in zip(bars, probabilities):
            if prob > 0.01:
                height = bar.get_height()
                ax.text(bar.get_x() + bar.get_width()/2., height,
                        f'{prob:.3f}',
                        ha='center', va='bottom', fontsize=9)
        
        plt.tight_layout()
        plt.savefig(output_path, dpi=150)
        plt.close()
        
        print(f"  - {output_path}")
    
    @staticmethod
    def animate_transition(system_state_start, system_state_end,
                          num_frames=30, output_path='output/histogram_transition.gif'):
        print("Animating Histogram transition")
        
        states = sorted(system_state_start.keys())
        probs_start = np.array([system_state_start[s] for s in states])
        probs_end = np.array([system_state_end[s] for s in states])
        
        fig, ax = plt.subplots(figsize=(10, 6))
        bars = ax.bar(states, probs_start, color='steelblue', alpha=0.8)
        
        ax.set_xlabel('Quantum State', fontsize=12)
        ax.set_ylabel('Probability', fontsize=12)
        ax.set_title('Quantum State Distribution', fontsize=14, fontweight='bold')
        ax.set_ylim(0, 1.0)
        ax.grid(axis='y', alpha=0.3)
        
        def update(frame):
            t = frame / num_frames
            current_probs = probs_start * (1 - t) + probs_end * t
            
            for bar, prob in zip(bars, current_probs):
                bar.set_height(prob)
            
            return bars
        
        anim = FuncAnimation(fig, update, frames=num_frames, interval=50, blit=False)
        anim.save(output_path, writer='pillow', fps=20)
        plt.close()
        
        print(f"  - {output_path}")
    
    @staticmethod
    def animate_steps(step_states, output_path='output/histogram_steps.png'):
        print(f"Visualizing Histogram Steps ({len(step_states)} steps)")
        
        states = sorted(step_states[0]['system_state'].keys())
        num_steps = len(step_states)
        cols = min(3, num_steps)
        rows = (num_steps + cols - 1) // cols
        
        fig, axes = plt.subplots(rows, cols, figsize=(5 * cols, 4 * rows))
        if num_steps == 1:
            axes = [axes]
        else:
            axes = axes.flatten() if rows > 1 else axes
        
        for idx, step_state in enumerate(step_states):
            ax = axes[idx] if num_steps > 1 else axes[0]
            probs = [step_state['system_state'][s] for s in states]
            
            bars = ax.bar(states, probs, color='steelblue', alpha=0.8)
            ax.set_xlabel('Quantum State', fontsize=10)
            ax.set_ylabel('Probability', fontsize=10)
            ax.set_title(f"Step {idx}: {step_state['description']}",
                        fontsize=11, fontweight='bold')
            ax.set_ylim(0, 1.0)
            ax.grid(axis='y', alpha=0.3)
            
            for bar, prob in zip(bars, probs):
                if prob > 0.01:
                    height = bar.get_height()
                    ax.text(bar.get_x() + bar.get_width()/2., height,
                           f'{prob:.2f}',
                           ha='center', va='bottom', fontsize=8)
        
        for idx in range(num_steps, len(axes)):
            fig.delaxes(axes[idx])
        
        plt.tight_layout()
        plt.savefig(output_path, dpi=150, bbox_inches='tight')
        plt.close()
        
        print(f"  - {output_path}")
        print(f"  Steps: {' → '.join([s['description'] for s in step_states])}")

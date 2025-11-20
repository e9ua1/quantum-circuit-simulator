import matplotlib.pyplot as plt
import matplotlib.animation as animation
import numpy as np


class StateHistogramVisualizer:
    
    def __init__(self):
        self.fig, self.ax = plt.subplots(figsize=(10, 6))
        
    def visualize_static(self, state_probabilities, save_path=None):
        states = list(state_probabilities.keys())
        probs = list(state_probabilities.values())
        
        colors = ['#3498db' if p > 0.01 else '#ecf0f1' for p in probs]
        
        self.ax.clear()
        bars = self.ax.bar(states, probs, color=colors, edgecolor='#2c3e50', linewidth=2)
        
        for bar, prob in zip(bars, probs):
            height = bar.get_height()
            self.ax.text(
                bar.get_x() + bar.get_width() / 2.,
                height + 0.02,
                f'{prob*100:.1f}%',
                ha='center',
                va='bottom',
                fontsize=10,
                fontweight='bold'
            )
        
        self.ax.set_xlabel('Basis States', fontsize=14, fontweight='bold')
        self.ax.set_ylabel('Probability', fontsize=14, fontweight='bold')
        self.ax.set_title('Quantum State Distribution', fontsize=16, fontweight='bold')
        self.ax.set_ylim(0, 1.1)
        self.ax.grid(axis='y', alpha=0.3)
        
        plt.tight_layout()
        
        if save_path:
            plt.savefig(save_path, dpi=300, bbox_inches='tight')
        else:
            plt.show()
    
    def animate_transition(self, start_probs, end_probs, frames=30, save_path=None):
        states = list(start_probs.keys())
        
        start_vals = np.array(list(start_probs.values()))
        end_vals = np.array(list(end_probs.values()))
        
        def update(frame):
            self.ax.clear()
            
            alpha = frame / frames
            current_probs = start_vals + (end_vals - start_vals) * alpha
            
            colors = ['#3498db' if p > 0.01 else '#ecf0f1' for p in current_probs]
            bars = self.ax.bar(states, current_probs, color=colors, edgecolor='#2c3e50', linewidth=2)
            
            for bar, prob in zip(bars, current_probs):
                height = bar.get_height()
                if height > 0.05:
                    self.ax.text(
                        bar.get_x() + bar.get_width() / 2.,
                        height + 0.02,
                        f'{prob*100:.1f}%',
                        ha='center',
                        va='bottom',
                        fontsize=10,
                        fontweight='bold'
                    )
            
            self.ax.set_xlabel('Basis States', fontsize=14, fontweight='bold')
            self.ax.set_ylabel('Probability', fontsize=14, fontweight='bold')
            self.ax.set_title(f'Quantum State Distribution (Step {frame}/{frames})', 
                            fontsize=16, fontweight='bold')
            self.ax.set_ylim(0, 1.1)
            self.ax.grid(axis='y', alpha=0.3)
        
        anim = animation.FuncAnimation(
            self.fig,
            update,
            frames=frames,
            interval=50,
            repeat=False
        )
        
        if save_path:
            anim.save(save_path, writer='pillow', fps=20)
        else:
            plt.show()
        
        return anim


def visualize_bell_state_histogram():
    visualizer = StateHistogramVisualizer()
    
    initial_state = {'00': 1.0, '01': 0.0, '10': 0.0, '11': 0.0}
    bell_state = {'00': 0.5, '01': 0.0, '10': 0.0, '11': 0.5}
    
    visualizer.animate_transition(
        initial_state,
        bell_state,
        frames=30,
        save_path='output/bell_state_histogram.gif'
    )


if __name__ == '__main__':
    visualizer = StateHistogramVisualizer()
    
    bell_state = {'00': 0.5, '01': 0.0, '10': 0.0, '11': 0.5}
    visualizer.visualize_static(bell_state)

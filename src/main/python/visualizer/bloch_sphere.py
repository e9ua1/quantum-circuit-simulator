import numpy as np
from qutip import Bloch
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation


class BlochSphereVisualizer:
    
    def __init__(self):
        self.bloch = Bloch()
        self.fig = plt.figure(figsize=(8, 8))
        
    def calculate_vector(self, prob_one):
        theta = 2 * np.arcsin(np.sqrt(prob_one))
        phi = 0
        
        x = np.sin(theta) * np.cos(phi)
        y = np.sin(theta) * np.sin(phi)
        z = np.cos(theta)
        
        return [x, y, z]
    
    def visualize_static(self, prob_one, save_path=None):
        vector = self.calculate_vector(prob_one)
        
        self.bloch.clear()
        self.bloch.add_vectors(vector)
        self.bloch.vector_color = ['r']
        self.bloch.sphere_alpha = 0.1
        
        if save_path:
            self.bloch.save(save_path)
        else:
            self.bloch.show()
    
    def animate_transition(self, prob_start, prob_end, frames=30, save_path=None):
        probs = np.linspace(prob_start, prob_end, frames)
        vectors = [self.calculate_vector(p) for p in probs]
        
        self.bloch.clear()
        self.bloch.point_color = ['r']
        self.bloch.point_marker = ['o']
        self.bloch.point_size = [30]
        
        def update(frame):
            self.bloch.clear()
            self.bloch.add_vectors(vectors[frame])
            
            if frame > 0:
                trail_vectors = vectors[:frame]
                for v in trail_vectors:
                    self.bloch.add_points([v])
            
            self.bloch.make_sphere()
            return self.bloch.fig
        
        anim = FuncAnimation(
            self.bloch.fig, 
            update, 
            frames=frames,
            interval=50,
            blit=False
        )
        
        if save_path:
            anim.save(save_path, writer='pillow', fps=20)
        else:
            plt.show()
        
        return anim


def visualize_bell_state_animation():
    visualizer = BlochSphereVisualizer()
    
    visualizer.animate_transition(
        prob_start=0.0,
        prob_end=0.5,
        frames=30,
        save_path='output/bell_state_bloch.gif'
    )


if __name__ == '__main__':
    visualizer = BlochSphereVisualizer()
    visualizer.visualize_static(0.0)
    visualizer.visualize_static(0.5)
    visualizer.visualize_static(1.0)

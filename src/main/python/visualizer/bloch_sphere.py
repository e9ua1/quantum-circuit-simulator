import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from qutip import Bloch
import numpy as np


class BlochSphereVisualizer:
    
    @staticmethod
    def visualize_static(prob_one, output_path='output/bloch_sphere.png'):
        print(f"Visualizing Bloch Sphere (P(|1⟩) = {prob_one:.3f})")
        
        b = Bloch()
        theta = 2 * np.arcsin(np.sqrt(prob_one))
        phi = 0
        
        x = np.sin(theta) * np.cos(phi)
        y = np.sin(theta) * np.sin(phi)
        z = np.cos(theta)
        
        b.add_vectors([x, y, z])
        b.vector_color = ['r']
        b.sphere_alpha = 0.1
        b.point_size = [30]
        
        b.save(output_path)
        plt.close()
        
        print(f"  - {output_path}")
    
    @staticmethod
    def animate_transition(prob_start, prob_end, num_frames=30, output_path='output/bloch_transition.gif'):
        print(f"Animating Bloch Sphere transition ({prob_start:.3f} → {prob_end:.3f})")
        
        b = Bloch()
        probs = np.linspace(prob_start, prob_end, num_frames)
        
        points_x = []
        points_y = []
        points_z = []
        
        for prob in probs:
            theta = 2 * np.arcsin(np.sqrt(prob))
            phi = 0
            
            x = np.sin(theta) * np.cos(phi)
            y = np.sin(theta) * np.sin(phi)
            z = np.cos(theta)
            
            points_x.append(x)
            points_y.append(y)
            points_z.append(z)
        
        b.add_points([points_x, points_y, points_z])
        b.add_vectors([points_x[-1], points_y[-1], points_z[-1]])
        
        b.point_color = ['b']
        b.vector_color = ['r']
        b.sphere_alpha = 0.1
        
        b.save(output_path)
        plt.close()
        
        print(f"  - {output_path}")
    
    @staticmethod
    def animate_steps(step_states, qubit_index=0, output_path='output/bloch_steps.png'):
        print(f"Visualizing Bloch Sphere Steps (Qubit {qubit_index}, {len(step_states)} steps)")
        
        b = Bloch()
        
        points_x = []
        points_y = []
        points_z = []
        
        for step in step_states:
            prob_one = step['qubit_probabilities'][str(qubit_index)]
            theta = 2 * np.arcsin(np.sqrt(prob_one))
            phi = 0
            
            x = np.sin(theta) * np.cos(phi)
            y = np.sin(theta) * np.sin(phi)
            z = np.cos(theta)
            
            points_x.append(x)
            points_y.append(y)
            points_z.append(z)
        
        b.add_points([points_x, points_y, points_z])
        b.add_vectors([points_x[0], points_y[0], points_z[0]])
        
        if len(points_x) > 1:
            b.add_vectors([points_x[-1], points_y[-1], points_z[-1]])
        
        b.point_color = ['b']
        b.point_size = [20]
        b.vector_color = ['g', 'r']
        b.sphere_alpha = 0.1
        
        b.save(output_path)
        plt.close()
        
        print(f"  - {output_path}")
        print(f"  Steps: {' → '.join([s['description'] for s in step_states])}")

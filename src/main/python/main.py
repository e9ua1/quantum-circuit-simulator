import json
import sys
import os
import platform
import subprocess
import webbrowser

sys.path.append(os.path.join(os.path.dirname(__file__), 'visualizer'))

from visualizer.bloch_sphere import BlochSphereVisualizer
from visualizer.histogram import StateHistogramVisualizer

def load_circuit_result(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        return json.load(f)

def create_bloch_animation_internal(circuit_result, output_path, fps=20):
    import numpy as np
    import matplotlib.pyplot as plt
    from matplotlib.animation import FuncAnimation, PillowWriter
    from qutip import Bloch

    step_states = circuit_result['step_states']
    descriptions = [s['description'] for s in step_states]

    def prob_to_vector(prob_one):
        if prob_one == 0.0:
            return np.array([0, 0, 1])
        elif prob_one == 1.0:
            return np.array([0, 0, -1])
        elif abs(prob_one - 0.5) < 1e-6:
            return np.array([1, 0, 0])
        else:
            theta = 2 * np.arcsin(np.sqrt(prob_one))
            return np.array([np.sin(theta), 0, np.cos(theta)])

    vectors = [prob_to_vector(s['qubit_probabilities']['0']) for s in step_states]

    def slerp(v1, v2, num_frames):
        v1 = v1 / np.linalg.norm(v1)
        v2 = v2 / np.linalg.norm(v2)
        dot = np.clip(np.dot(v1, v2), -1.0, 1.0)
        theta = np.arccos(dot)
        if theta < 1e-6:
            return [v1 * (1-t) + v2 * t for t in np.linspace(0, 1, num_frames)]
        sin_theta = np.sin(theta)
        return [
            (np.sin((1-t)*theta)/sin_theta)*v1 + (np.sin(t*theta)/sin_theta)*v2
            for t in np.linspace(0, 1, num_frames)
        ]

    frames_per = fps
    all_frames = []
    all_colors = []
    all_descs = []
    colors = ['green', 'blue', 'red', 'purple', 'orange']

    for i in range(len(vectors) - 1):
        interp = slerp(vectors[i], vectors[i+1], frames_per)
        all_frames.extend(interp)
        all_colors.extend([colors[i % len(colors)]] * frames_per)
        all_descs.extend([descriptions[i+1]] * frames_per)

    all_frames.extend([vectors[-1]] * (fps//2))
    all_colors.extend([colors[(len(vectors)-1) % len(colors)]] * (fps//2))
    all_descs.extend([descriptions[-1]] * (fps//2))

    fig = plt.figure(figsize=(8, 8))

    def animate(idx):
        plt.clf()
        b = Bloch(fig=fig)
        b.add_vectors([all_frames[idx]])
        b.vector_color = [all_colors[idx]]
        b.vector_width = 3
        b.fig.suptitle(f'Qubit 0 State Evolution\n{all_descs[idx]}',
                      fontsize=14, fontweight='bold')
        b.render()
        return b.fig,

    print(f"  Creating Bloch animation: {len(all_frames)} frames, {len(all_frames)/fps:.1f}s")
    anim = FuncAnimation(fig, animate, frames=len(all_frames),
                        interval=1000/fps, blit=False)
    writer = PillowWriter(fps=fps)
    anim.save(output_path, writer=writer)
    plt.close()
    print(f"    ✅ {output_path}")

def create_histogram_animation_internal(circuit_result, output_path, fps=20):
    import numpy as np
    import matplotlib.pyplot as plt
    from matplotlib.animation import FuncAnimation, PillowWriter

    step_states = circuit_result['step_states']
    qubit_count = circuit_result['qubit_count']
    all_states = [format(i, f'0{qubit_count}b') for i in range(2**qubit_count)]

    distributions = [s['system_state'] for s in step_states]
    descriptions = [s['description'] for s in step_states]

    def interpolate(d1, d2, num_frames):
        result = []
        for t in np.linspace(0, 1, num_frames):
            frame = {s: d1.get(s, 0)*(1-t) + d2.get(s, 0)*t for s in all_states}
            result.append(frame)
        return result

    frames_per = fps
    all_frames = []
    all_descs = []

    for i in range(len(distributions) - 1):
        interp = interpolate(distributions[i], distributions[i+1], frames_per)
        all_frames.extend(interp)
        all_descs.extend([descriptions[i+1]] * frames_per)

    all_frames.extend([distributions[-1]] * (fps//2))
    all_descs.extend([descriptions[-1]] * (fps//2))

    fig, ax = plt.subplots(figsize=(10, 6))
    x_pos = np.arange(len(all_states))

    def animate(idx):
        ax.clear()
        probs_dict = all_frames[idx]
        probs = [probs_dict.get(s, 0) for s in all_states]

        bars = ax.bar(x_pos, probs, color='steelblue', alpha=0.8, edgecolor='black')

        for i, (s, p) in enumerate(zip(all_states, probs)):
            if p > 0.01:
                ax.text(i, p+0.02, f'{p:.3f}', ha='center', va='bottom',
                       fontsize=11, fontweight='bold')

        ax.set_xlabel('Quantum State', fontsize=13, fontweight='bold')
        ax.set_ylabel('Probability', fontsize=13, fontweight='bold')
        ax.set_title(f'Quantum State Distribution\n{all_descs[idx]}',
                    fontsize=14, fontweight='bold', pad=20)
        ax.set_xticks(x_pos)
        ax.set_xticklabels([f'|{s}⟩' for s in all_states], fontsize=11)
        ax.set_ylim(0, 1.05)
        ax.grid(axis='y', alpha=0.3, linestyle='--')

        step_num = idx // frames_per
        ax.text(0.02, 0.98, f'Step {step_num}/{len(descriptions)-1}',
               transform=ax.transAxes, fontsize=12, va='top',
               bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.8))
        return bars,

    print(f"  Creating histogram animation: {len(all_frames)} frames, {len(all_frames)/fps:.1f}s")
    anim = FuncAnimation(fig, animate, frames=len(all_frames),
                        interval=1000/fps, blit=False)
    writer = PillowWriter(fps=fps)
    anim.save(output_path, writer=writer)
    plt.close()
    print(f"    ✅ {output_path}")

def create_entanglement_visualization(circuit_result):
    import numpy as np
    import matplotlib.pyplot as plt
    from matplotlib.animation import FuncAnimation, PillowWriter
    from qutip import Bloch

    qubit_count = circuit_result['qubit_count']
    if qubit_count < 2:
        return

    step_states = circuit_result['step_states']

    def prob_to_vector(prob_one):
        if prob_one == 0.0:
            return np.array([0, 0, 1])
        elif prob_one == 1.0:
            return np.array([0, 0, -1])
        elif abs(prob_one - 0.5) < 1e-6:
            return np.array([1, 0, 0])
        else:
            theta = 2 * np.arcsin(np.sqrt(prob_one))
            return np.array([np.sin(theta), 0, np.cos(theta)])

    def calc_entanglement(system_state, q0_prob, q1_prob):
        bell_states = ['00', '11']
        bell_prob = sum(system_state.get(s, 0.0) for s in bell_states)
        independent_prob = abs(q0_prob - 0.5) + abs(q1_prob - 0.5)
        if bell_prob > 0.9:
            return min(1.0, bell_prob)
        return max(0.0, bell_prob - independent_prob * 0.5)

    num_steps = len(step_states)
    cols = min(3, num_steps)
    rows = (num_steps + cols - 1) // cols

    fig = plt.figure(figsize=(6 * cols, 5 * rows))

    for idx, step_state in enumerate(step_states):
        q0_prob = step_state['qubit_probabilities']['0']
        q1_prob = step_state['qubit_probabilities']['1']
        system_state = step_state['system_state']
        description = step_state['description']

        q0_vec = prob_to_vector(q0_prob)
        q1_vec = prob_to_vector(q1_prob)

        entanglement = calc_entanglement(system_state, q0_prob, q1_prob)

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
    plt.savefig('output/entanglement_steps.png', dpi=150, bbox_inches='tight')
    plt.close()

    print(f"  Creating entanglement steps: {num_steps} steps")
    print(f"    ✅ output/entanglement_steps.png")

    def slerp(v1, v2, num_frames):
        v1 = v1 / np.linalg.norm(v1)
        v2 = v2 / np.linalg.norm(v2)
        dot = np.clip(np.dot(v1, v2), -1.0, 1.0)
        theta = np.arccos(dot)
        if theta < 1e-6:
            return [v1 * (1-t) + v2 * t for t in np.linspace(0, 1, num_frames)]
        sin_theta = np.sin(theta)
        return [
            (np.sin((1-t)*theta)/sin_theta)*v1 + (np.sin(t*theta)/sin_theta)*v2
            for t in np.linspace(0, 1, num_frames)
        ]

    q0_vectors = []
    q1_vectors = []
    entanglements = []
    descriptions = []

    for step_state in step_states:
        q0_prob = step_state['qubit_probabilities']['0']
        q1_prob = step_state['qubit_probabilities']['1']
        system_state = step_state['system_state']

        q0_vec = prob_to_vector(q0_prob)
        q1_vec = prob_to_vector(q1_prob)
        ent = calc_entanglement(system_state, q0_prob, q1_prob)

        q0_vectors.append(q0_vec)
        q1_vectors.append(q1_vec)
        entanglements.append(ent)
        descriptions.append(step_state['description'])

    fps = 20
    frames_per = fps

    all_q0 = []
    all_q1 = []
    all_ent = []
    all_desc = []

    for i in range(len(q0_vectors) - 1):
        q0_interp = slerp(q0_vectors[i], q0_vectors[i+1], frames_per)
        q1_interp = slerp(q1_vectors[i], q1_vectors[i+1], frames_per)
        ent_interp = np.linspace(entanglements[i], entanglements[i+1], frames_per)

        all_q0.extend(q0_interp)
        all_q1.extend(q1_interp)
        all_ent.extend(ent_interp)
        all_desc.extend([descriptions[i+1]] * frames_per)

    all_q0.extend([q0_vectors[-1]] * (fps//2))
    all_q1.extend([q1_vectors[-1]] * (fps//2))
    all_ent.extend([entanglements[-1]] * (fps//2))
    all_desc.extend([descriptions[-1]] * (fps//2))

    fig = plt.figure(figsize=(16, 7))

    def animate(idx):
        plt.clf()

        ax_left = fig.add_subplot(1, 2, 1, projection='3d')
        ax_right = fig.add_subplot(1, 2, 2, projection='3d')

        b_q0 = Bloch(fig=fig, axes=ax_left)
        b_q1 = Bloch(fig=fig, axes=ax_right)

        q0_vec = all_q0[idx]
        q1_vec = all_q1[idx]
        ent = all_ent[idx]
        desc = all_desc[idx]

        b_q0.add_vectors([q0_vec])
        b_q1.add_vectors([q1_vec])

        color = 'red' if ent > 0.3 else 'blue'
        b_q0.vector_color = [color]
        b_q1.vector_color = [color]
        b_q0.vector_width = 3
        b_q1.vector_width = 3

        ax_left.set_title('Qubit 0', fontsize=14, fontweight='bold', pad=20)
        ax_right.set_title('Qubit 1', fontsize=14, fontweight='bold', pad=20)

        b_q0.render()
        b_q1.render()

        if ent > 0.1:
            fig.text(0.5, 0.95, f'⚡ Entanglement: {ent:.2f}',
                    ha='center', fontsize=16, fontweight='bold',
                    color='red',
                    bbox=dict(boxstyle='round', facecolor='yellow', alpha=0.8))

        fig.text(0.5, 0.05, desc, ha='center', fontsize=13, fontweight='bold')

        step_num = idx // frames_per
        fig.text(0.02, 0.95, f'Step {step_num}/{len(descriptions)-1}',
                fontsize=12, va='top',
                bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.8))

        return fig,

    total_frames = len(all_q0)

    print(f"  Creating entanglement animation: {total_frames} frames, {total_frames/fps:.1f}s")
    anim = FuncAnimation(fig, animate, frames=total_frames,
                        interval=1000/fps, blit=False)
    writer = PillowWriter(fps=fps)
    anim.save('output/entanglement_evolution.gif', writer=writer)
    plt.close()
    print(f"    ✅ output/entanglement_evolution.gif")

def create_html_viewer(circuit_name, num_qubits):
    html_content = f"""<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quantum Circuit Visualization - {circuit_name}</title>
    <style>
        * {{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }}
        body {{
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }}
        .container {{
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            padding: 40px;
        }}
        h1 {{
            text-align: center;
            color: #2d3748;
            margin-bottom: 10px;
            font-size: 2.5em;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }}
        .subtitle {{
            text-align: center;
            color: #718096;
            margin-bottom: 40px;
            font-size: 1.2em;
        }}
        .animation-grid {{
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
            gap: 30px;
            margin-bottom: 30px;
        }}
        .animation-card {{
            background: #f7fafc;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }}
        .animation-card:hover {{
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.15);
        }}
        .animation-title {{
            font-size: 1.3em;
            color: #2d3748;
            margin-bottom: 15px;
            font-weight: 600;
            text-align: center;
        }}
        .animation-container {{
            width: 100%;
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: inset 0 2px 4px rgba(0,0,0,0.06);
        }}
        .animation-container img {{
            width: 100%;
            height: auto;
            display: block;
        }}
        .info-box {{
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 15px;
            margin-top: 30px;
            text-align: center;
        }}
        .info-box h3 {{
            margin-bottom: 10px;
            font-size: 1.5em;
        }}
        .info-box p {{
            font-size: 1.1em;
            opacity: 0.9;
        }}
        .badge {{
            display: inline-block;
            background: rgba(255,255,255,0.2);
            padding: 5px 15px;
            border-radius: 20px;
            margin: 5px;
            font-size: 0.9em;
        }}
        @media (max-width: 768px) {{
            .animation-grid {{
                grid-template-columns: 1fr;
            }}
            h1 {{
                font-size: 2em;
            }}
        }}
    </style>
</head>
<body>
    <div class="container">
        <h1>🌌 Quantum Circuit Visualization</h1>
        <p class="subtitle">{circuit_name} | {num_qubits} Qubit{"s" if num_qubits > 1 else ""}</p>

        <div class="animation-grid">
            <div class="animation-card">
                <div class="animation-title">🔵 Bloch Sphere Evolution</div>
                <div class="animation-container">
                    <img src="bloch_evolution.gif" alt="Bloch Sphere Animation">
                </div>
            </div>

            <div class="animation-card">
                <div class="animation-title">📊 State Probability Distribution</div>
                <div class="animation-container">
                    <img src="histogram_evolution.gif" alt="Histogram Animation">
                </div>
            </div>

            {"" if num_qubits < 2 else '''
            <div class="animation-card">
                <div class="animation-title">⚡ Quantum Entanglement</div>
                <div class="animation-container">
                    <img src="entanglement_evolution.gif" alt="Entanglement Animation">
                </div>
            </div>
            '''}
        </div>

        <div class="info-box">
            <h3>✨ Visualization Info</h3>
            <p>
                <span class="badge">🎬 2.5s Animation</span>
                <span class="badge">🎨 SLERP Interpolation</span>
                <span class="badge">📈 Real-time State Tracking</span>
            </p>
            <p style="margin-top: 15px; font-size: 0.95em;">
                Generated by Quantum Circuit Simulator | Powered by QuTiP & Matplotlib
            </p>
        </div>
    </div>
</body>
</html>"""

    html_path = 'output/visualization.html'
    with open(html_path, 'w', encoding='utf-8') as f:
        f.write(html_content)

    return html_path

def open_visualization_in_browser(html_path):
    abs_path = os.path.abspath(html_path)
    file_url = f'file://{abs_path}'

    print(f"\n🌐 웹 브라우저에서 시각화를 여시겠습니까?")
    print(f"   {html_path}")

    webbrowser.open(file_url)
    print(f"  ✅ 브라우저에서 열림!")

def main():
    if len(sys.argv) < 2:
        print("Usage: python main.py <circuit_result_json>")
        sys.exit(1)

    input_file = sys.argv[1]

    print(f"Loading circuit result from: {input_file}")
    circuit_result = load_circuit_result(input_file)

    circuit_name = circuit_result.get('circuit_name', 'Unknown')
    qubit_count = circuit_result['qubit_count']

    print(f"Circuit: {circuit_name}")
    print(f"Qubits: {qubit_count}")

    print("\n=== Creating Static Images ===")
    step_states = circuit_result.get('step_states', [])
    print(f"Found {len(step_states)} steps")

    if qubit_count >= 1:
        try:
            BlochSphereVisualizer.animate_steps(step_states, 0, 'output/bloch_steps.png')
        except Exception as e:
            print(f"  ⚠️  Bloch steps failed: {e}")

    try:
        StateHistogramVisualizer.animate_steps(step_states, 'output/histogram_steps.png')
    except Exception as e:
        print(f"  ⚠️  Histogram steps failed: {e}")

    if step_states:
        final_state = step_states[-1]

        if qubit_count >= 1:
            prob_one = final_state['qubit_probabilities'].get('0', 0.0)
            try:
                BlochSphereVisualizer.visualize_static(prob_one, 'output/bloch_sphere.png')
            except Exception as e:
                print(f"  ⚠️  Bloch sphere failed: {e}")

        system_state = final_state.get('system_state', {})
        try:
            StateHistogramVisualizer.visualize_static(system_state, 'output/histogram.png')
        except Exception as e:
            print(f"  ⚠️  Histogram failed: {e}")

    print("\n=== Creating Animations ===")

    try:
        create_bloch_animation_internal(circuit_result, 'output/bloch_evolution.gif', fps=20)
    except Exception as e:
        print(f"  ⚠️  Bloch animation failed: {e}")

    try:
        create_histogram_animation_internal(circuit_result, 'output/histogram_evolution.gif', fps=20)
    except Exception as e:
        print(f"  ⚠️  Histogram animation failed: {e}")

    if qubit_count >= 2:
        try:
            create_entanglement_visualization(circuit_result)
        except Exception as e:
            print(f"  ⚠️  Entanglement visualization failed: {e}")

    print("\n✅ Visualization complete!")
    print("  Static images:")
    print("    - output/bloch_sphere.png")
    print("    - output/histogram.png")
    print("    - output/bloch_steps.png")
    print("    - output/histogram_steps.png")
    if qubit_count >= 2:
        print("    - output/entanglement_steps.png")
    print("  Animations:")
    print("    - output/bloch_evolution.gif")
    print("    - output/histogram_evolution.gif")
    if qubit_count >= 2:
        print("    - output/entanglement_evolution.gif")

    print("\n=== Creating HTML Viewer ===")
    try:
        html_path = create_html_viewer(circuit_name, qubit_count)
        print(f"  ✅ {html_path}")
        open_visualization_in_browser(html_path)
    except Exception as e:
        print(f"  ⚠️  HTML viewer failed: {e}")

if __name__ == '__main__':
    main()

"""
양자 회로 시각화 메인 스크립트 (애니메이션 포함)
- 정적 이미지 (PNG) 4개
- 애니메이션 (GIF) 2개
"""
import json
import sys
import os

# 서브 모듈 임포트
sys.path.append(os.path.join(os.path.dirname(__file__), 'visualizer'))

from visualizer.bloch_sphere import BlochSphereVisualizer
from visualizer.histogram import StateHistogramVisualizer

def load_circuit_result(filepath):
    """회로 실행 결과 JSON 로드"""
    with open(filepath, 'r', encoding='utf-8') as f:
        return json.load(f)

def create_bloch_animation_internal(circuit_result, output_path, fps=20):
    """블로흐 구면 애니메이션 생성"""
    import numpy as np
    import matplotlib.pyplot as plt
    from matplotlib.animation import FuncAnimation, PillowWriter
    from qutip import Bloch

    step_states = circuit_result['step_states']
    descriptions = [s['description'] for s in step_states]

    # 첫 번째 큐비트 벡터들
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

    # SLERP 보간
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

    # 전체 프레임 생성
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

    # 애니메이션
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
    """히스토그램 애니메이션 생성"""
    import numpy as np
    import matplotlib.pyplot as plt
    from matplotlib.animation import FuncAnimation, PillowWriter

    step_states = circuit_result['step_states']
    qubit_count = circuit_result['qubit_count']
    all_states = [format(i, f'0{qubit_count}b') for i in range(2**qubit_count)]

    distributions = [s['system_state'] for s in step_states]
    descriptions = [s['description'] for s in step_states]

    # 선형 보간
    def interpolate(d1, d2, num_frames):
        result = []
        for t in np.linspace(0, 1, num_frames):
            frame = {s: d1.get(s, 0)*(1-t) + d2.get(s, 0)*t for s in all_states}
            result.append(frame)
        return result

    # 전체 프레임
    frames_per = fps
    all_frames = []
    all_descs = []

    for i in range(len(distributions) - 1):
        interp = interpolate(distributions[i], distributions[i+1], frames_per)
        all_frames.extend(interp)
        all_descs.extend([descriptions[i+1]] * frames_per)

    all_frames.extend([distributions[-1]] * (fps//2))
    all_descs.extend([descriptions[-1]] * (fps//2))

    # 애니메이션
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

    # 1. 단계별 정적 이미지
    print("\n=== Creating Static Images ===")
    step_states = circuit_result.get('step_states', [])
    print(f"Found {len(step_states)} steps")

    # 블로흐 구면 단계별
    if qubit_count >= 1:
        try:
            BlochSphereVisualizer.animate_steps(step_states, 0, 'output/bloch_steps.png')
        except Exception as e:
            print(f"  ⚠️  Bloch steps failed: {e}")

    # 히스토그램 단계별
    try:
        StateHistogramVisualizer.animate_steps(step_states, 'output/histogram_steps.png')
    except Exception as e:
        print(f"  ⚠️  Histogram steps failed: {e}")

    # 최종 상태 이미지
    if step_states:
        final_state = step_states[-1]

        # 블로흐 구면
        if qubit_count >= 1:
            prob_one = final_state['qubit_probabilities'].get('0', 0.0)
            try:
                BlochSphereVisualizer.visualize_static(prob_one, 'output/bloch_sphere.png')
            except Exception as e:
                print(f"  ⚠️  Bloch sphere failed: {e}")

        # 히스토그램
        system_state = final_state.get('system_state', {})
        try:
            StateHistogramVisualizer.visualize_static(system_state, 'output/histogram.png')
        except Exception as e:
            print(f"  ⚠️  Histogram failed: {e}")

    # 2. 애니메이션 생성 (GIF) 🎬
    print("\n=== Creating Animations ===")

    try:
        create_bloch_animation_internal(circuit_result, 'output/bloch_evolution.gif', fps=20)
    except Exception as e:
        print(f"  ⚠️  Bloch animation failed: {e}")

    try:
        create_histogram_animation_internal(circuit_result, 'output/histogram_evolution.gif', fps=20)
    except Exception as e:
        print(f"  ⚠️  Histogram animation failed: {e}")

    # 완료
    print("\n✅ Visualization complete!")
    print("  Static images:")
    print("    - output/bloch_sphere.png")
    print("    - output/histogram.png")
    print("    - output/bloch_steps.png")
    print("    - output/histogram_steps.png")
    print("  Animations: 🎬")
    print("    - output/bloch_evolution.gif")
    print("    - output/histogram_evolution.gif")

if __name__ == '__main__':
    main()

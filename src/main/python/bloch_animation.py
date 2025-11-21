"""
블로흐 구면 애니메이션 생성
단계별 상태 변화를 부드러운 애니메이션으로 시각화
"""
import json
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation, PillowWriter
from qutip import Bloch
import sys

def load_circuit_result(filepath):
    """JSON 파일에서 회로 결과 로드"""
    with open(filepath, 'r', encoding='utf-8') as f:
        return json.load(f)

def probability_to_bloch_vector(prob_one):
    """
    P(|1⟩) 확률을 블로흐 벡터로 변환
    
    순수 상태의 경우:
    - |0⟩: (0, 0, 1) - 북극
    - |+⟩ = (|0⟩+|1⟩)/√2: (1, 0, 0) - x축 양의 방향
    - |1⟩: (0, 0, -1) - 남극
    
    P(|1⟩) = sin²(θ/2) 에서 θ를 구하고
    블로흐 구면 상의 점 계산
    """
    if prob_one == 0.0:
        # |0⟩ 상태 - 북극
        return np.array([0, 0, 1])
    elif prob_one == 1.0:
        # |1⟩ 상태 - 남극
        return np.array([0, 0, -1])
    elif abs(prob_one - 0.5) < 1e-6:
        # 중첩 상태 (|+⟩ 가정) - 적도 x축
        return np.array([1, 0, 0])
    else:
        # 일반적인 경우
        theta = 2 * np.arcsin(np.sqrt(prob_one))
        # x-z 평면 상의 점 (phi=0 가정)
        x = np.sin(theta)
        z = np.cos(theta)
        return np.array([x, 0, z])

def interpolate_vectors(v1, v2, num_frames):
    """
    두 블로흐 벡터 사이를 구면 선형 보간 (SLERP)
    
    Args:
        v1: 시작 벡터 (3D)
        v2: 끝 벡터 (3D)
        num_frames: 프레임 수
    
    Returns:
        보간된 벡터들의 리스트
    """
    # 정규화
    v1_norm = v1 / np.linalg.norm(v1)
    v2_norm = v2 / np.linalg.norm(v2)
    
    # 두 벡터 사이 각도
    dot = np.clip(np.dot(v1_norm, v2_norm), -1.0, 1.0)
    theta = np.arccos(dot)
    
    # theta가 매우 작으면 선형 보간
    if theta < 1e-6:
        t = np.linspace(0, 1, num_frames)
        return [v1_norm * (1-ti) + v2_norm * ti for ti in t]
    
    # 구면 선형 보간
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
    """
    블로흐 구면 애니메이션 생성
    
    Args:
        circuit_result: 회로 결과 JSON
        output_path: 출력 파일 경로
        fps: 초당 프레임 수
    """
    qubit_count = circuit_result['qubit_count']
    step_states = circuit_result['step_states']
    
    # 첫 번째 큐비트만 시각화 (2큐비트 이상이면)
    qubit_idx = 0
    
    # 각 단계의 블로흐 벡터 계산
    bloch_vectors = []
    descriptions = []
    
    for step_state in step_states:
        prob_one = step_state['qubit_probabilities'][str(qubit_idx)]
        vector = probability_to_bloch_vector(prob_one)
        bloch_vectors.append(vector)
        descriptions.append(step_state['description'])
    
    # 단계 사이 프레임 수 (각 전환에 1초)
    frames_per_transition = fps
    
    # 전체 애니메이션 프레임 생성
    all_frames = []
    all_colors = []
    all_descriptions = []
    
    colors = ['green', 'blue', 'red', 'purple', 'orange']
    
    for i in range(len(bloch_vectors) - 1):
        # 현재 단계에서 다음 단계로 보간
        interpolated = interpolate_vectors(
            bloch_vectors[i], 
            bloch_vectors[i + 1], 
            frames_per_transition
        )
        all_frames.extend(interpolated)
        
        # 색상도 보간
        color_start = colors[i % len(colors)]
        all_colors.extend([color_start] * frames_per_transition)
        all_descriptions.extend([descriptions[i + 1]] * frames_per_transition)
    
    # 마지막 상태 유지 (0.5초)
    hold_frames = fps // 2
    all_frames.extend([bloch_vectors[-1]] * hold_frames)
    all_colors.extend([colors[(len(bloch_vectors)-1) % len(colors)]] * hold_frames)
    all_descriptions.extend([descriptions[-1]] * hold_frames)
    
    # 애니메이션 생성
    fig = plt.figure(figsize=(8, 8))
    
    def animate(frame_idx):
        plt.clf()
        
        # 블로흐 구면 생성
        b = Bloch(fig=fig)
        
        # 현재 프레임의 벡터와 색상
        vector = all_frames[frame_idx]
        color = all_colors[frame_idx]
        description = all_descriptions[frame_idx]
        
        # 벡터 추가
        b.add_vectors([vector])
        b.vector_color = [color]
        b.vector_width = 3
        
        # 제목
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
    
    # GIF로 저장
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
    
    # 블로흐 구면 애니메이션 생성
    create_bloch_animation(circuit_result, output_file, fps=20)
    
    print()
    print("✅ Animation complete!")
    print(f"   {output_file}")

if __name__ == '__main__':
    main()

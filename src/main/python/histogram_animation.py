"""
히스토그램 애니메이션 생성
단계별 양자 상태 분포 변화를 부드러운 애니메이션으로 시각화
"""
import json
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation, PillowWriter
import sys

def load_circuit_result(filepath):
    """JSON 파일에서 회로 결과 로드"""
    with open(filepath, 'r', encoding='utf-8') as f:
        return json.load(f)

def interpolate_probabilities(prob_dict1, prob_dict2, num_frames):
    """
    두 확률 분포 사이를 선형 보간
    
    Args:
        prob_dict1: 시작 확률 분포 {'00': 1.0, '01': 0.0, ...}
        prob_dict2: 끝 확률 분포
        num_frames: 프레임 수
    
    Returns:
        보간된 확률 분포 리스트
    """
    # 모든 상태 키 수집
    all_states = sorted(set(prob_dict1.keys()) | set(prob_dict2.keys()))
    
    # 보간
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
    """
    히스토그램 애니메이션 생성
    
    Args:
        circuit_result: 회로 결과 JSON
        output_path: 출력 파일 경로
        fps: 초당 프레임 수
    """
    step_states = circuit_result['step_states']
    qubit_count = circuit_result['qubit_count']
    
    # 모든 가능한 상태 (2^n개)
    num_states = 2 ** qubit_count
    all_states = [format(i, f'0{qubit_count}b') for i in range(num_states)]
    
    # 각 단계의 확률 분포
    distributions = []
    descriptions = []
    
    for step_state in step_states:
        distributions.append(step_state['system_state'])
        descriptions.append(step_state['description'])
    
    # 단계 사이 프레임 수
    frames_per_transition = fps
    
    # 전체 애니메이션 프레임 생성
    all_frames = []
    all_descriptions = []
    
    for i in range(len(distributions) - 1):
        # 현재 단계에서 다음 단계로 보간
        interpolated = interpolate_probabilities(
            distributions[i],
            distributions[i + 1],
            frames_per_transition
        )
        all_frames.extend(interpolated)
        all_descriptions.extend([descriptions[i + 1]] * frames_per_transition)
    
    # 마지막 상태 유지 (0.5초)
    hold_frames = fps // 2
    all_frames.extend([distributions[-1]] * hold_frames)
    all_descriptions.extend([descriptions[-1]] * hold_frames)
    
    # 애니메이션 생성
    fig, ax = plt.subplots(figsize=(10, 6))
    
    # x축 위치
    x_pos = np.arange(len(all_states))
    
    def animate(frame_idx):
        ax.clear()
        
        # 현재 프레임의 확률 분포
        probs_dict = all_frames[frame_idx]
        description = all_descriptions[frame_idx]
        
        # 상태별 확률 추출 (없으면 0)
        probabilities = [probs_dict.get(state, 0.0) for state in all_states]
        
        # 막대 그래프
        bars = ax.bar(x_pos, probabilities, color='steelblue', alpha=0.8, edgecolor='black')
        
        # 막대 위에 확률 값 표시 (0.01 이상인 것만)
        for i, (state, prob) in enumerate(zip(all_states, probabilities)):
            if prob > 0.01:
                ax.text(i, prob + 0.02, f'{prob:.3f}', 
                       ha='center', va='bottom', fontsize=11, fontweight='bold')
        
        # 축 설정
        ax.set_xlabel('Quantum State', fontsize=13, fontweight='bold')
        ax.set_ylabel('Probability', fontsize=13, fontweight='bold')
        ax.set_title(f'Quantum State Distribution\n{description}', 
                    fontsize=14, fontweight='bold', pad=20)
        ax.set_xticks(x_pos)
        ax.set_xticklabels([f'|{state}⟩' for state in all_states], fontsize=11)
        ax.set_ylim(0, 1.05)
        ax.grid(axis='y', alpha=0.3, linestyle='--')
        
        # 스텝 번호 표시
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
    
    # GIF로 저장
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
    
    # 히스토그램 애니메이션 생성
    create_histogram_animation(circuit_result, output_file, fps=20)
    
    print()
    print("✅ Animation complete!")
    print(f"   {output_file}")

if __name__ == '__main__':
    main()

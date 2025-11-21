"""
양자 회로 시각화 메인 스크립트

Java에서 생성한 JSON 파일을 읽어 블로흐 구면과 히스토그램을 생성합니다.
"""

import sys
import json
from visualizer.bloch_sphere import BlochSphereVisualizer
from visualizer.histogram import StateHistogramVisualizer


def load_circuit_result(json_path):
    """JSON 파일에서 회로 결과를 로드"""
    with open(json_path, 'r') as f:
        return json.load(f)


def visualize_final_state(data):
    """최종 상태만 시각화 (기존 방식)"""
    print("\n=== Visualizing Final State ===")

    # 블로흐 구면 (첫 번째 큐비트)
    if 'qubit_probabilities' in data:
        prob_one = data['qubit_probabilities']['0']
        BlochSphereVisualizer.visualize_static(prob_one, 'output/bloch_sphere.png')

    # 히스토그램
    if 'system_state' in data:
        StateHistogramVisualizer.visualize_static(data['system_state'], 'output/histogram.png')


def visualize_step_by_step(data):
    """단계별 상태 변화 시각화"""
    print("\n=== Visualizing Step-by-Step Animation ===")

    if 'step_states' not in data:
        print("No step_states found in data. Using final state visualization.")
        visualize_final_state(data)
        return

    step_states = data['step_states']
    print(f"Found {len(step_states)} steps")

    # 블로흐 구면 궤적 시각화 (PNG)
    BlochSphereVisualizer.animate_steps(
        step_states,
        qubit_index=0,
        output_path='output/bloch_steps.png'
    )

    # 히스토그램 단계별 시각화 (PNG)
    StateHistogramVisualizer.animate_steps(
        step_states,
        output_path='output/histogram_steps.png'
    )

    # 최종 상태 정적 이미지도 함께 생성
    final_state = step_states[-1]
    prob_one = final_state['qubit_probabilities']['0']
    BlochSphereVisualizer.visualize_static(prob_one, 'output/bloch_sphere.png')
    StateHistogramVisualizer.visualize_static(final_state['system_state'], 'output/histogram.png')


def main():
    """메인 실행 함수"""
    if len(sys.argv) < 2:
        print("Usage: python main.py <circuit_result.json>")
        sys.exit(1)

    json_path = sys.argv[1]
    print(f"Loading circuit result from: {json_path}")

    # JSON 로드
    data = load_circuit_result(json_path)

    print(f"\nCircuit: {data.get('circuit_name', 'Unknown')}")
    print(f"Qubits: {data.get('qubit_count', 0)}")

    # step_states가 있으면 단계별 시각화, 없으면 최종 상태만
    if 'step_states' in data:
        visualize_step_by_step(data)
    else:
        visualize_final_state(data)

    print("\nVisualization complete!")
    print("  - output/bloch_sphere.png")
    print("  - output/histogram.png")
    if 'step_states' in data:
        print("  - output/bloch_steps.png")
        print("  - output/histogram_steps.png")


if __name__ == '__main__':
    main()

"""
상태 히스토그램 시각화 모듈

전체 양자 시스템의 상태 분포를 히스토그램으로 시각화합니다.
"""

import matplotlib
matplotlib.use('Agg')  # GUI 없이 사용
import matplotlib.pyplot as plt
import numpy as np


class StateHistogramVisualizer:
    """상태 히스토그램 시각화 클래스"""

    @staticmethod
    def visualize_static(system_state, output_path='output/histogram.png'):
        """
        전체 시스템 상태 분포를 정적 히스토그램으로 표시

        Args:
            system_state (dict): 상태별 확률 딕셔너리 (예: {'00': 0.5, '01': 0.0, ...})
            output_path (str): 저장할 파일 경로
        """
        print("Visualizing State Histogram")

        # 상태와 확률 추출
        states = sorted(system_state.keys())
        probabilities = [system_state[s] for s in states]

        # 그래프 생성
        fig, ax = plt.subplots(figsize=(10, 6))

        # 막대 그래프
        bars = ax.bar(states, probabilities, color='steelblue', alpha=0.8)

        # 스타일 설정
        ax.set_xlabel('Quantum State', fontsize=12)
        ax.set_ylabel('Probability', fontsize=12)
        ax.set_title('Quantum State Distribution', fontsize=14, fontweight='bold')
        ax.set_ylim(0, 1.0)
        ax.grid(axis='y', alpha=0.3)

        # 확률 레이블 추가
        for bar, prob in zip(bars, probabilities):
            if prob > 0.01:  # 1% 이상만 표시
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
        """
        두 상태 분포 사이의 전환을 애니메이션으로 표시

        Args:
            system_state_start (dict): 시작 상태 분포
            system_state_end (dict): 종료 상태 분포
            num_frames (int): 애니메이션 프레임 수
            output_path (str): 저장할 파일 경로
        """
        print("Animating Histogram transition")

        # 상태 키 추출 (정렬)
        states = sorted(system_state_start.keys())

        # 시작/종료 확률
        probs_start = np.array([system_state_start[s] for s in states])
        probs_end = np.array([system_state_end[s] for s in states])

        # 그래프 설정
        fig, ax = plt.subplots(figsize=(10, 6))

        bars = ax.bar(states, probs_start, color='steelblue', alpha=0.8)
        ax.set_xlabel('Quantum State', fontsize=12)
        ax.set_ylabel('Probability', fontsize=12)
        ax.set_title('Quantum State Distribution', fontsize=14, fontweight='bold')
        ax.set_ylim(0, 1.0)
        ax.grid(axis='y', alpha=0.3)

        # 애니메이션 업데이트 함수
        def update(frame):
            t = frame / num_frames  # 0.0 ~ 1.0
            current_probs = probs_start * (1 - t) + probs_end * t

            for bar, prob in zip(bars, current_probs):
                bar.set_height(prob)

            return bars

        # 애니메이션 생성
        anim = FuncAnimation(fig, update, frames=num_frames, interval=50, blit=False)

        # 저장
        anim.save(output_path, writer='pillow', fps=20)
        plt.close()

        print(f"  - {output_path}")

    @staticmethod
    def animate_steps(step_states, output_path='output/histogram_steps.png'):
        """
        단계별 상태 변화를 여러 서브플롯으로 표시

        Args:
            step_states (list): 단계별 상태 정보 리스트
            output_path (str): 저장할 파일 경로
        """
        print(f"Visualizing Histogram Steps ({len(step_states)} steps)")

        # 모든 가능한 상태 키 추출 (첫 단계에서)
        states = sorted(step_states[0]['system_state'].keys())

        # 서브플롯 생성 (각 단계마다 하나씩)
        num_steps = len(step_states)
        cols = min(3, num_steps)  # 최대 3열
        rows = (num_steps + cols - 1) // cols

        fig, axes = plt.subplots(rows, cols, figsize=(5 * cols, 4 * rows))
        if num_steps == 1:
            axes = [axes]
        else:
            axes = axes.flatten() if rows > 1 else axes

        # 각 단계별로 그래프 생성
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

            # 확률 레이블
            for bar, prob in zip(bars, probs):
                if prob > 0.01:
                    height = bar.get_height()
                    ax.text(bar.get_x() + bar.get_width()/2., height,
                           f'{prob:.2f}',
                           ha='center', va='bottom', fontsize=8)

        # 빈 서브플롯 제거
        for idx in range(num_steps, len(axes)):
            fig.delaxes(axes[idx])

        plt.tight_layout()
        plt.savefig(output_path, dpi=150, bbox_inches='tight')
        plt.close()

        print(f"  - {output_path}")
        print(f"  Steps: {' → '.join([s['description'] for s in step_states])}")


# 테스트 코드
if __name__ == '__main__':
    # 테스트 데이터
    test_state_00 = {'00': 1.0, '01': 0.0, '10': 0.0, '11': 0.0}
    test_state_bell = {'00': 0.5, '01': 0.0, '10': 0.0, '11': 0.5}

    # 정적 시각화 테스트
    StateHistogramVisualizer.visualize_static(test_state_bell, 'test_histogram.png')

    # 전환 애니메이션 테스트
    StateHistogramVisualizer.animate_transition(
        test_state_00, test_state_bell, 30, 'test_histogram_transition.gif'
    )

    print("Test complete!")

"""
블로흐 구면 시각화 모듈

단일 큐비트 상태를 블로흐 구면 위의 점으로 시각화합니다.
"""

import matplotlib
matplotlib.use('Agg')  # GUI 없이 사용
import matplotlib.pyplot as plt
from qutip import Bloch
import numpy as np


class BlochSphereVisualizer:
    """블로흐 구면 시각화 클래스"""

    @staticmethod
    def visualize_static(prob_one, output_path='output/bloch_sphere.png'):
        """
        단일 큐비트 상태를 블로흐 구면에 정적으로 표시

        Args:
            prob_one (float): |1⟩ 상태의 확률
            output_path (str): 저장할 파일 경로
        """
        print(f"Visualizing Bloch Sphere (P(|1⟩) = {prob_one:.3f})")

        # 블로흐 구면 생성
        b = Bloch()

        # 확률을 블로흐 구면 좌표로 변환
        theta = 2 * np.arcsin(np.sqrt(prob_one))
        phi = 0  # 단순화를 위해 x-z 평면에 표시

        x = np.sin(theta) * np.cos(phi)
        y = np.sin(theta) * np.sin(phi)
        z = np.cos(theta)

        # 벡터 추가
        b.add_vectors([x, y, z])

        # 스타일 설정
        b.vector_color = ['r']
        b.sphere_alpha = 0.1
        b.point_size = [30]

        # 저장
        b.save(output_path)
        plt.close()

        print(f"  - {output_path}")

    @staticmethod
    def animate_transition(prob_start, prob_end, num_frames=30, output_path='output/bloch_transition.gif'):
        """
        두 상태 사이의 전환을 애니메이션으로 표시

        Args:
            prob_start (float): 시작 |1⟩ 확률
            prob_end (float): 종료 |1⟩ 확률
            num_frames (int): 애니메이션 프레임 수
            output_path (str): 저장할 파일 경로
        """
        print(f"Animating Bloch Sphere transition ({prob_start:.3f} → {prob_end:.3f})")

        # 블로흐 구면 생성
        b = Bloch()

        # 확률 변화 생성
        probs = np.linspace(prob_start, prob_end, num_frames)

        # 궤적 계산
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

        # 궤적 추가
        b.add_points([points_x, points_y, points_z])

        # 최종 위치 벡터
        b.add_vectors([points_x[-1], points_y[-1], points_z[-1]])

        # 스타일 설정
        b.point_color = ['b']
        b.vector_color = ['r']
        b.sphere_alpha = 0.1

        # 저장
        b.save(output_path)
        plt.close()

        print(f"  - {output_path}")

    @staticmethod
    def animate_steps(step_states, qubit_index=0, output_path='output/bloch_steps.png'):
        """
        단계별 상태 변화를 궤적으로 표시

        Args:
            step_states (list): 단계별 상태 정보 리스트
            qubit_index (int): 시각화할 큐비트 인덱스
            output_path (str): 저장할 파일 경로
        """
        print(f"Visualizing Bloch Sphere Steps (Qubit {qubit_index}, {len(step_states)} steps)")

        # 블로흐 구면 생성
        b = Bloch()

        # 각 단계의 좌표 계산
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

        # 궤적 추가 (점으로 표시)
        b.add_points([points_x, points_y, points_z])

        # 시작 위치 (녹색)
        b.add_vectors([points_x[0], points_y[0], points_z[0]])

        # 최종 위치 (빨간색)
        if len(points_x) > 1:
            b.add_vectors([points_x[-1], points_y[-1], points_z[-1]])

        # 스타일 설정
        b.point_color = ['b']
        b.point_size = [20]
        b.vector_color = ['g', 'r']  # 시작=녹색, 끝=빨간색
        b.sphere_alpha = 0.1

        # PNG로 저장 (qutip은 GIF를 지원하지 않음)
        b.save(output_path)
        plt.close()

        print(f"  - {output_path}")
        print(f"  Steps: {' → '.join([s['description'] for s in step_states])}")


# 테스트 코드
if __name__ == '__main__':
    # 정적 시각화 테스트
    BlochSphereVisualizer.visualize_static(0.5, 'test_bloch_static.png')

    # 전환 애니메이션 테스트
    BlochSphereVisualizer.animate_transition(0.0, 0.5, 30, 'test_bloch_transition.gif')

    print("Test complete!")

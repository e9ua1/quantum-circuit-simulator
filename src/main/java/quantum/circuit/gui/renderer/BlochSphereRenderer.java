package quantum.circuit.gui.renderer;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * 블로흐 구면(Bloch Sphere) 3D 렌더러
 * 단일 큐비트의 양자 상태를 3D 구면 위의 점으로 시각화합니다.
 */
public class BlochSphereRenderer {

    private static final double SPHERE_RADIUS = 100.0;
    private static final double AXIS_RADIUS = 2.0;
    private static final double AXIS_LENGTH = 150.0;
    private static final double STATE_MARKER_RADIUS = 8.0;

    private static final Color SPHERE_COLOR = Color.rgb(52, 152, 219, 0.3);
    private static final Color X_AXIS_COLOR = Color.RED;
    private static final Color Y_AXIS_COLOR = Color.GREEN;
    private static final Color Z_AXIS_COLOR = Color.BLUE;
    private static final Color STATE_COLOR = Color.rgb(231, 76, 60);

    /**
     * 주어진 확률로 블로흐 구면을 렌더링합니다.
     *
     * @param probabilityOfOne |1⟩ 상태의 확률 (0.0 ~ 1.0)
     * @return 블로흐 구면 3D SubScene
     */
    public SubScene render(double probabilityOfOne) {
        Group root = new Group();

        // 블로흐 구면 생성
        Sphere blochSphere = createBlochSphere();
        root.getChildren().add(blochSphere);

        // 좌표축 생성 (X, Y, Z)
        root.getChildren().addAll(
                createXAxis(),
                createYAxis(),
                createZAxis()
        );

        // 상태 벡터 표시
        Point3D stateVector = calculateStateVector(probabilityOfOne);
        Sphere stateMarker = createStateMarker(stateVector);
        root.getChildren().add(stateMarker);

        // 조명 추가
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(200);
        light.setTranslateY(-200);
        light.setTranslateZ(-200);
        root.getChildren().add(light);

        // 카메라 설정
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-400);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);

        // SubScene 생성
        SubScene subScene = new SubScene(root, 300, 300, true, javafx.scene.SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.rgb(245, 245, 245));

        // 회전 가능하도록 설정
        setupRotation(root, subScene);

        return subScene;
    }

    private Sphere createBlochSphere() {
        Sphere sphere = new Sphere(SPHERE_RADIUS);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(SPHERE_COLOR);
        material.setSpecularColor(Color.LIGHTBLUE);
        sphere.setMaterial(material);
        return sphere;
    }

    private Cylinder createXAxis() {
        return createAxis(X_AXIS_COLOR, new Rotate(90, Rotate.Z_AXIS));
    }

    private Cylinder createYAxis() {
        return createAxis(Y_AXIS_COLOR, new Rotate(0, Rotate.Y_AXIS));
    }

    private Cylinder createZAxis() {
        return createAxis(Z_AXIS_COLOR, new Rotate(90, Rotate.X_AXIS));
    }

    private Cylinder createAxis(Color color, Rotate rotation) {
        Cylinder axis = new Cylinder(AXIS_RADIUS, AXIS_LENGTH);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color);
        axis.setMaterial(material);
        axis.getTransforms().add(rotation);
        return axis;
    }

    private Point3D calculateStateVector(double probabilityOfOne) {
        // |ψ⟩ = cos(θ/2)|0⟩ + sin(θ/2)|1⟩
        // probabilityOfOne = sin²(θ/2)
        // θ = 2 * arcsin(√probabilityOfOne)

        double theta = 2 * Math.asin(Math.sqrt(probabilityOfOne));
        double phi = 0; // 단순화: 위상은 0으로 고정

        double x = SPHERE_RADIUS * Math.sin(theta) * Math.cos(phi);
        double y = SPHERE_RADIUS * Math.sin(theta) * Math.sin(phi);
        double z = SPHERE_RADIUS * Math.cos(theta);

        return new Point3D(x, y, z);
    }

    private Sphere createStateMarker(Point3D position) {
        Sphere marker = new Sphere(STATE_MARKER_RADIUS);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(STATE_COLOR);
        material.setSpecularColor(Color.WHITE);
        marker.setMaterial(material);

        marker.setTranslateX(position.getX());
        marker.setTranslateY(position.getY());
        marker.setTranslateZ(position.getZ());

        return marker;
    }

    private void setupRotation(Group root, SubScene subScene) {
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        root.getTransforms().addAll(rotateX, rotateY);

        // 마우스 드래그로 회전
        final double[] mousePos = {0, 0};

        subScene.setOnMousePressed(event -> {
            mousePos[0] = event.getSceneX();
            mousePos[1] = event.getSceneY();
        });

        subScene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mousePos[0];
            double deltaY = event.getSceneY() - mousePos[1];

            rotateY.setAngle(rotateY.getAngle() + deltaX * 0.5);
            rotateX.setAngle(rotateX.getAngle() - deltaY * 0.5);

            mousePos[0] = event.getSceneX();
            mousePos[1] = event.getSceneY();
        });
    }
}

package vfc;

import processing.core.*;
import remixlab.dandelion.geom.*;
import remixlab.proscene.*;

public class ViewFrsutumCulling extends PApplet {
    OctreeNode root;
    Scene scene, auxScene;
    PGraphics canvas, auxCanvas;

    //Choose one of P3D for a 3D scene, or P2D or JAVA2D for a 2D scene
    String renderer = P3D;
    int w = 1110;
    int h = 1110;

    public void settings() {
        size(w, h, renderer);
    }

    @Override
    public void setup() {
        // declare and build the octree hierarchy
        Vec p = new Vec(100, 70, 130);
        root = new OctreeNode(p, Vec.multiply(p, -1.0f));
        root.buildBoxHierarchy(4);

        canvas = createGraphics(w, h/2, P3D);
        scene = new Scene(this, canvas);
        scene.enableBoundaryEquations();
        scene.setGridVisualHint(false);

        auxCanvas = createGraphics(w, h/2, P3D);
        // Note that we pass the upper left corner coordinates where the scene
        // is to be drawn (see drawing code below) to its constructor.
        auxScene = new Scene(this, auxCanvas, 0, h/2);
        //auxScene.camera().setType(Camera.Type.ORTHOGRAPHIC);
        auxScene.setAxesVisualHint(false);
        auxScene.setGridVisualHint(false);
        auxScene.setRadius(200);
        auxScene.showAll();
    }

    @Override
    public void draw() {
        background(0);
        scene.beginDraw();
        canvas.background(0);
        root.drawIfAllChildrenAreVisible(scene.pg(), scene.camera());
        scene.endDraw();
        scene.display();

        auxScene.beginDraw();
        auxCanvas.background(0);
        root.drawIfAllChildrenAreVisible(auxScene.pg(), scene.camera());
        auxScene.pg().pushStyle();
        auxScene.pg().stroke(255, 255, 0);
        auxScene.pg().fill(255, 255, 0, 160);
        auxScene.drawEye(scene.eye());
        auxScene.pg().popStyle();
        auxScene.endDraw();
        auxScene.display();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"vfc.ViewFrsutumCulling"});
    }
}

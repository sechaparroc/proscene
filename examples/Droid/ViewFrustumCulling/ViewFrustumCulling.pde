/**
 * View Frustum Culling.
 * by Jean Pierre Charalambos.
 * 
 * Android version of the Eye.ViewFrustumCulling example.
 * 
 * This example requires the Processing Android Mode and an Android device.
 */

import remixlab.proscene.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;
import android.view.MotionEvent;

OctreeNode root;
Scene scene, auxScene;
PGraphics canvas, auxCanvas;

int w;
int h;

void setup() {
  fullScreen(P3D, 1);
  w = width;
  h = height;
  // declare and build the octree hierarchy
  Vec p = new Vec(100, 70, 130);
  root = new OctreeNode(p, Vec.multiply(p, -1.0f));
  root.buildBoxHierarchy(3);

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

void draw() {
  background(0);
  handleMouse();
  canvas.beginDraw();
  scene.beginDraw();
  canvas.background(0);
  root.drawIfAllChildrenAreVisible(scene.pg(), scene.camera());
  scene.endDraw();
  canvas.endDraw();
  image(canvas, 0, 0);

  auxCanvas.beginDraw();
  auxScene.beginDraw();
  auxCanvas.background(0);
  root.drawIfAllChildrenAreVisible(auxScene.pg(), scene.camera());
  auxScene.pg().pushStyle();
  auxScene.pg().stroke(255, 255, 0);
  auxScene.pg().fill(255, 255, 0, 160);
  auxScene.drawEye(scene.eye());
  auxScene.pg().popStyle();
  auxScene.endDraw();
  auxCanvas.endDraw();
  // We retrieve the scene upper left coordinates defined above.
  image(auxCanvas, auxScene.originCorner().x(), auxScene.originCorner().y());
}

void handleMouse() {
  if (mouseY < h/2) {
    scene.enableMotionAgent();
    scene.enableKeyboardAgent();
    auxScene.disableMotionAgent();
    auxScene.disableKeyboardAgent();
  } 
  else {
    scene.disableMotionAgent();
    scene.disableKeyboardAgent();
    auxScene.enableMotionAgent();
    auxScene.enableKeyboardAgent();
  }
}

// Processing currently doesn't support registering Android MotionEvent. 
// This method thus needs to be declared.
public boolean surfaceTouchEvent(MotionEvent event) {
  scene.droidTouchAgent().touchEvent(event);
  return true;
}
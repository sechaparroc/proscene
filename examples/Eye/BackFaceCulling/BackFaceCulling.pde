/**
 * Back-Face Culling.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates various back face camera culling routines to early
 * discard primitive processing.
 * 
 * Press 'h' to display the key shortcuts and mouse bindings in the console.
 */

import remixlab.proscene.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

float            size        = 50;
Scene            scene, auxScene;
PGraphics        canvas, auxCanvas;

// PGraphics3D g3;
Vec              normalXPos  = new Vec(1, 0, 0);
Vec              normalYPos  = new Vec(0, 1, 0);
Vec              normalZPos  = new Vec(0, 0, 1);
Vec              normalXNeg  = new Vec(-1, 0, 0);
Vec              normalYNeg  = new Vec(0, -1, 0);
Vec              normalZNeg  = new Vec(0, 0, -1);
ArrayList<Vec>  normals;

public void setup() {
  size(640, 720, P3D);
  normals = new ArrayList<Vec>();
  normals.add(normalZPos);
  normals.add(normalXPos);
  normals.add(normalYPos);

  canvas = createGraphics(640, 360, P3D);
  scene = new Scene(this, canvas);
  scene.setGridVisualHint(false);
  scene.addDrawHandler(this, "mainDrawing");

  auxCanvas = createGraphics(640, 360, P3D);
  auxScene = new Scene(this, (PGraphicsOpenGL) auxCanvas);
  auxScene.camera().setType(Camera.Type.ORTHOGRAPHIC);
  auxScene.setAxesVisualHint(false);
  auxScene.setGridVisualHint(false);
  auxScene.setRadius(350);
  auxScene.camera().setPosition(new Vec(125,125,125));
  auxScene.camera().lookAt(auxScene.center());
  auxScene.addDrawHandler(this, "auxiliarDrawing");
  colorMode(RGB, 1);
  handleMouse();
}

void draw() {
  handleMouse();
  canvas.beginDraw();
  scene.beginDraw();
  scene.endDraw();
  canvas.endDraw();
  image(canvas, 0, 0);

  auxCanvas.beginDraw();
  auxScene.beginDraw();
  auxScene.endDraw();
  auxCanvas.endDraw();
  image(auxCanvas, 0, 360);
}

public void mainDrawing(Scene s) {
  PGraphics p = s.pg();
  p.background(0);
  drawScene(p);
}

public void auxiliarDrawing(Scene s) {
  mainDrawing(s);
  s.pg().pushStyle();
  s.pg().fill(0, 255, 255, 120);
  s.pg().stroke(0, 255, 255);
  s.drawEye(scene.eye());
  s.pg().popStyle();
}

void drawScene(PGraphics p) {
  p.noStroke();
  p.beginShape(QUADS);

  //1. Attempt to discard three faces at once using a cone of normals.
  if (!scene.camera().isBackFacing(new Vec(size, size, size), normals)) {
    // z-axis
    p.fill(0, size, size);
    p.vertex(-size, size, size);
    p.fill(size, size, size);
    p.vertex(size, size, size);
    p.fill(size, 0, size);
    p.vertex(size, -size, size);
    p.fill(0, 0, size);
    p.vertex(-size, -size, size);

    // x-axis
    p.fill(size, size, size);
    p.vertex(size, size, size);
    p.fill(size, size, 0);
    p.vertex(size, size, -size);
    p.fill(size, 0, 0);
    p.vertex(size, -size, -size);
    p.fill(size, 0, size);
    p.vertex(size, -size, size);

    // y-axis
    p.fill(0, size, 0);
    p.vertex(-size, size, -size);
    p.fill(size, size, 0);
    p.vertex(size, size, -size);
    p.fill(size, size, size);
    p.vertex(size, size, size);
    p.fill(0, size, size);
    p.vertex(-size, size, size);
  } // cone condition

  //2. Attempt to discard a single face using one of its vertices and its normal.
  // -z-axis
  if (!scene.camera().isBackFacing(new Vec(size, size, -size), normalZNeg)) {
    p.fill(size, size, 0);
    p.vertex(size, size, -size);
    p.fill(0, size, 0);
    p.vertex(-size, size, -size);
    p.fill(0, 0, 0);
    p.vertex(-size, -size, -size);
    p.fill(size, 0, 0);
    p.vertex(size, -size, -size);
  }

  //3. Attempt to discard a single face using three of its vertices. 
  // -x-axis
  if (!scene.camera().isBackFacing(new Vec(-size, size, -size), new Vec(-size, -size, -size), 
  new Vec(-size, -size, size))) {
    p.fill(0, size, 0);
    p.vertex(-size, size, -size);
    p.fill(0, size, size);
    p.vertex(-size, -size, -size);
    p.fill(0, 0, size);
    p.vertex(-size, -size, size);
    p.fill(0, 0, 0);
    p.vertex(-size, size, size);
  }

  //4. Attempt to discard a single face using one of its vertices and its normal.
  // -y-axis
  if (!scene.camera().isBackFacing(new Vec(-size, -size, -size), normalYNeg)) {
    p.fill(0, 0, 0);
    p.vertex(-size, -size, -size);
    p.fill(size, 0, 0);
    p.vertex(size, -size, -size);
    p.fill(size, 0, size);
    p.vertex(size, -size, size);
    p.fill(0, 0, size);
    p.vertex(-size, -size, size);
  }

  p.endShape();
}

void keyPressed() {
  if (key == 'u')
    scene.flip();
}

void handleMouse() {
  if (mouseY < 360) {
    scene.enableMouseAgent();
    scene.enableKeyboardAgent();
    auxScene.disableMouseAgent();
    auxScene.disableKeyboardAgent();
  } else {
    scene.disableMouseAgent();
    scene.disableKeyboardAgent();
    auxScene.enableMouseAgent();
    auxScene.enableKeyboardAgent();
  }
}
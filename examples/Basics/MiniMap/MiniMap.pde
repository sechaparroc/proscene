/**
 * Mini Map
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates how to use proscene off-screen rendering to build
 * a mini-map of the main Scene where all objetcs are interactive. It also
 * shows Frame syncing among views. 
 *
 * Press 'h' to display the key shortcuts and mouse bindings in the console.
 * Press 'x' and 'y' to change the mini-map eye representation.
 */

import remixlab.proscene.*;

Scene scene, auxScene;
PGraphics canvas, auxCanvas;  
InteractiveFrame frame1, auxFrame1, frame2, auxFrame2, frame3, auxFrame3;
InteractiveFrame iFrame;

int                w       = 200;
int                h       = 120;
int                oX      = 640 - w;
int                oY      = 360 - h;
boolean            showMiniMap  = true;

//Choose one of P3D for a 3D scene, or P2D or JAVA2D for a 2D scene
String renderer = P2D;
void setup() {
  size(640, 360, renderer);
  canvas = createGraphics(640, 360, renderer);
  scene = new Scene(this, canvas);
  frame1 = new InteractiveFrame(scene, this, "frameDrawing");
  frame1.translate(30, 30);
  frame2 = new InteractiveFrame(scene, frame1, this, "frameDrawing");
  frame2.translate(40, 0);
  frame3 = new InteractiveFrame(scene, frame2, this, "frameDrawing");
  frame3.translate(40, 0);

  auxCanvas = createGraphics(w, h, renderer);
  auxScene = new Scene(this, auxCanvas, oX, oY);
  auxScene.setRadius(200);
  auxScene.showAll();
  
  auxFrame1 = new InteractiveFrame(auxScene);
  auxFrame1.fromFrame(frame1);
  auxFrame2 = new InteractiveFrame(auxScene, auxFrame1);
  auxFrame2.fromFrame(frame2);
  auxFrame3 = new InteractiveFrame(auxScene, auxFrame2);
  auxFrame3.fromFrame(frame3);

  iFrame = new InteractiveFrame(auxScene);
  iFrame.fromFrame(scene.eyeFrame());
  handleAgents();
}

void draw() {
  handleAgents();
  InteractiveFrame.sync(scene.eyeFrame(), iFrame);
  InteractiveFrame.sync(frame1, auxFrame1);
  InteractiveFrame.sync(frame2, auxFrame2);
  InteractiveFrame.sync(frame3, auxFrame3);
  canvas.beginDraw();
  scene.beginDraw();
  canvas.background(0);
  scene.drawFrames();
  scene.endDraw();
  canvas.endDraw();
  image(canvas, 0, 0);
  if (showMiniMap) {
    auxCanvas.beginDraw();
    auxScene.beginDraw();
    auxCanvas.background(29,153,243);
    auxScene.pg().pushStyle();
    auxScene.pg().stroke(255, 255, 0);
    auxScene.pg().fill(255, 255, 0, 160);
    auxScene.pg().popStyle();
    //auxScene.drawFrames();
    //works but we want to customize the iFrame color:
    for(InteractiveFrame frame : auxScene.frames()) {
      if(frame == iFrame) {
        auxScene.pg().stroke(255, 255, 0);
        auxScene.pg().fill(255, 255, 0, 160);
      }
      frame.draw();
    }
    auxScene.endDraw();
    auxCanvas.endDraw();
    // We retrieve the scene upper left coordinates defined above.
    image(auxCanvas, auxScene.originCorner().x(), auxScene.originCorner().y());
  }
}

void keyPressed() {
  if(key == ' ')
    showMiniMap = !showMiniMap;
  if(key == 'x')
    iFrame.setShape(this, "eyeDrawing");
  if(key == 'y')
    iFrame.setShape(scene.eyeFrame());
}

void frameDrawing(PGraphics pg) {
  pg.fill(random(0,255), random(0,255), random(0,255));
  if(scene.is3D())
	pg.box(40, 10, 5);
  else
	pg.rect(0, 0, 40, 10, 5);
}

void eyeDrawing(PGraphics pg) {
  if(auxScene.is3D())
    pg.box(200);
  else {
    pg.pushStyle();
    pg.rectMode(CENTER);
    pg.rect(0, 0, 200, 200);
    pg.popStyle();
  }
}

void handleAgents() {
  scene.enableMotionAgent();
  auxScene.disableMotionAgent();
  scene.enableKeyboardAgent();
  auxScene.disableKeyboardAgent();
  if ((oX < mouseX) && (oY < mouseY) && showMiniMap) {
    scene.disableMotionAgent();
    auxScene.enableMotionAgent();
    scene.disableKeyboardAgent();
    auxScene.enableKeyboardAgent();
  }
}
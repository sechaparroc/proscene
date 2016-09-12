/**
 * Frame Interaction.
 * by Jean Pierre Charalambos.
 * 
 * Android version of the Frame.FrameInteraction example.
 * 
 * This example requires the Processing Android Mode and an Android device.
 */

import remixlab.proscene.*;
import android.view.MotionEvent;

Scene scene;
InteractiveFrame frame1, frame2, frame3, frame4;

String renderer = P3D;

void setup() {
  fullScreen(P3D, 1);
  scene = new Scene(this);
  scene.eyeFrame().setDamping(0);
  scene.setPickingVisualHint(true);

  //frame 1
  frame1 = new InteractiveFrame(scene, "drawTorusSolenoid");
  frame1.setPickingPrecision(InteractiveFrame.PickingPrecision.ADAPTIVE);
  frame1.setGrabsInputThreshold(scene.radius()/4);
  frame1.translate(50, 50);

  // frame 2
  // Thanks to the Processing Foundation for providing the rocket shape
  frame2 = new InteractiveFrame(scene, loadShape("rocket.obj"));
  frame2.setPickingPrecision(InteractiveFrame.PickingPrecision.ADAPTIVE);
  frame2.setGrabsInputThreshold(scene.radius()*4);
  frame2.scale(0.2);
  // comment the previous 4 lines and do it with a cylinder:
  //frame2 = new InteractiveFrame(scene, "cylinder");

  //frame 3
  frame3 = new InteractiveFrame(scene);
  frame3.setFrontShape("drawAxes");
  frame3.setPickingShape(this, "sphere");
  frame3.setPickingPrecision(InteractiveFrame.PickingPrecision.ADAPTIVE);
  frame3.setHighlightingMode(InteractiveFrame.HighlightingMode.FRONT_PICKING_SHAPES);
  frame3.translate(-100, -50);
  
  //frame 4
  //frame4 will behave as frame3 since the latter is passed as its
  //referenceFrame() in its constructor 
  frame4 = new InteractiveFrame(scene, frame3);
  frame4.setPickingPrecision(InteractiveFrame.PickingPrecision.ADAPTIVE);
  frame4.setShape(this, "box");
  frame4.translate(0, 100);
  
  // If picking is not too slow in your Android, comment to enable it
  scene.disablePickingBuffer();
}

void cylinder(PGraphics pg) {
  pg.fill(0,0,255,125);
  scene.drawCylinder(pg);
}

void box(PGraphics pg) {
  pg.fill(0,255,0,125);
  pg.strokeWeight(3);
  pg.box(30);
}

void sphere(PGraphics pg) {
  pg.noStroke();
  pg.fill(0,255,255,125);
  pg.strokeWeight(3);
  pg.sphere(20);
}

void draw() {
  background(0);
  // Set the torus fill color
  fill(255,255,0,125);
  scene.drawFrames();
}

// Processing currently doesn't support registering Android MotionEvent. 
// This method thus needs to be declared.
public boolean surfaceTouchEvent(MotionEvent event) {
  scene.droidTouchAgent().touchEvent(event);
  return true;
}
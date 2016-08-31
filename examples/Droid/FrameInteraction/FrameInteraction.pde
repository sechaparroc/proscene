/**
 * Frame Interaction.
 * by Victor Forero and Jean Pierre Charalambos.
 * 
 * Android version of the Frame.FrameInteraction example.
 * 
 * Press 'f' to display the interactive frame picking hint.
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

//import remixlab.bias.event.*;
import remixlab.proscene.*;
import android.view.MotionEvent;

Scene scene;
InteractiveFrame frame1, frame2, frame3, frame4;

//Choose one of P3D for a 3D scene, or P2D for a 2D scene
String renderer = P3D;

void setup() {
  //size(displayWidth, displayHeight, P3D);
  fullScreen(P3D, 1);
  scene = new Scene(this);
  scene.eyeFrame().setDamping(0);
  scene.setPickingVisualHint(true);

  //frame 1
  frame1 = new InteractiveFrame(scene);
  frame1.setPickingPrecision(InteractiveFrame.PickingPrecision.ADAPTIVE);
  frame1.setGrabsInputThreshold(scene.radius()/4);
  frame1.translate(50, 50);

  // frame 2
  PShape sphere = createShape(SPHERE, 40);
  sphere.setFill(color(255,255,0));
  frame2 = new InteractiveFrame(scene, sphere);
  //frame2.setMotionBinding(LEFT, "translate");
  //frame2.setMotionBinding(RIGHT, "scale");

  //frame 3
  frame3 = new InteractiveFrame(scene);
  frame3.setFrontShape("drawAxes");
  frame3.setPickingShape(this, "boxPicking");
  frame3.setHighlightingMode(InteractiveFrame.HighlightingMode.FRONT_PICKING_SHAPES);
  frame3.translate(-100, -50);
  //frame3.setMotionBinding(this, LEFT, "boxCustomMotion");
  //frame3.setClickBinding(this, LEFT, 1, "boxCustomClick");
  
  //frame 4
  //frame4 will behave as frame3 since the latter is passed as its
  //referenceFrame() in its constructor 
  frame4 = new InteractiveFrame(scene, frame3);
  frame4.setFrontShape(this, "boxDrawing");
  frame4.setPickingShape(this, "boxPicking");
  frame4.setHighlightingMode(InteractiveFrame.HighlightingMode.FRONT_PICKING_SHAPES);
  frame4.translate(0, 100);
}

void boxDrawing(PGraphics pg) {
  pg.fill(0,255,0);
  pg.strokeWeight(3);
  pg.box(30);
}

void boxPicking(PGraphics pg) {
  pg.noStroke();
  pg.fill(255,0,0,126);
  pg.sphere(30);
}

/*
void boxCustomMotion(InteractiveFrame frame, MotionEvent event) {
  frame.screenRotate(event);
}

void boxCustomClick(InteractiveFrame frame) {
  if(frame.scene().mouseAgent().pickingMode() == MouseAgent.PickingMode.MOVE)
    frame.center();
}
*/

void draw() {
  background(0);    
  // 1. Apply the frame transformation before your drawing

  // Save the current model view matrix
  pushMatrix();
  pushStyle();
  // Multiply matrix to get in the frame coordinate system.
  // applyMatrix(Scene.toPMatrix(iFrame.matrix())); //is possible but inefficient
  frame1.applyTransformation();//very efficient
  // Draw an axis using the Scene static function
  scene.drawAxes(20);

  if (frame1.grabsInput())
    fill(255, 0, 0);
  else 
    fill(0, 255, 255);
  scene.drawTorusSolenoid();

  popStyle();
  popMatrix();

  // 2. Draw frames for which visual representations have been set
  scene.drawFrames();
}

public boolean surfaceTouchEvent(MotionEvent event) {
  scene.droidTouchAgent().touchEvent(event);
  return true;
}

/*
void keyPressed() {
  if(key == ' ')
    if( scene.mouseAgent().pickingMode() == MouseAgent.PickingMode.CLICK ) {
      scene.mouseAgent().setPickingMode(MouseAgent.PickingMode.MOVE);
      scene.eyeFrame().setMotionBinding(LEFT, "rotate");
      scene.eyeFrame().removeMotionBinding(MouseAgent.NO_BUTTON);
    }
    else {
      scene.mouseAgent().setPickingMode(MouseAgent.PickingMode.CLICK);
      scene.eyeFrame().setMotionBinding(MouseAgent.NO_BUTTON, "rotate");
      scene.eyeFrame().removeMotionBinding(LEFT);
    }
}
*/
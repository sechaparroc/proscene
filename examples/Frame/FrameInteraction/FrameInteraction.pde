/**
 * Frame Interaction.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates how to deal with interactive frames: how to pick &
 * manipulate them and how to visually represent them.
 * 
 * Interactivity may be fine-tuned either from an InteractiveFrame instance (frame2) or
 * from some code within the sketch (frame3 and frame4). Note that frame1 has default
 * mouse and keyboard interactivity. Also note that the scene eye has a frame instance
 * (scene.eyeFrame()) which may be controlled in the same way.
 * 
 * Visual representations may be related to a frame in two different ways: 1. Applying
 * the frame transformation just before the graphics code happens in draw() (frame1);
 * or, 2. Setting a visual representation to the frame, either by calling
 * frame.setShape(myPShape) or frame.setShape(myProcedure) in setup() (frame2 and frame3,
 * resp.), and then calling scene.drawFrames() in draw() (frame2, frame3 and frame4).
 * Note that in frame4 different visual representations for the front and picking shapes
 * are set with setFrontShape() and setPickingShape() resp. Note that setShape() is
 * just a wrapper method that call both functions on the same shape paramenter.
 * 
 * Frame picking is achieved by tracking the pointer and checking whether or not it
 * lies within the frame 'selection area': a square around the frame's projected origin
 * (frame 1) or the projected frame visual representation (frame2, frame3 and frame4)
 * which requires drawing the frame picking-shape into an scene.pickingBuffer().
 *
 * Press 'i' (which is a shortcut defined below) to switch the interaction between the
 * camera frame and the interactive frame. You can also manipulate the interactive
 * frame by picking the blue torus passing the mouse next to its axes origin.
 * 
 * Press 'f' to display the interactive frame picking hint.
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.bias.event.*;
import remixlab.proscene.*;

Scene scene;
InteractiveFrame frame1, frame2, frame3, frame4;

//Choose one of P3D for a 3D scene, or P2D or JAVA2D for a 2D scene
String renderer = P3D;

void setup() {
  size(640, 360, renderer);    
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
  frame2.setMotionBinding(LEFT, "translate");
  frame2.setMotionBinding(RIGHT, "scale");

  //frame 3
  //also possible:
  frame3 = new InteractiveFrame(scene, this, "boxDrawing");
  //same as:
  //frame3 = new InteractiveFrame(scene);
  //frame3.setShape(this, "boxDrawing");
  frame3.translate(-100, -50);
  frame3.setMotionBinding(this, LEFT, "boxCustomMotion");
  frame3.setClickBinding(this, LEFT, 1, "boxCustomClick");
  
  //frame 4
  //frame4 will behave as frame3 since the latter is passed as its
  //referenceFrame() in the constructor 
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

void boxCustomMotion(InteractiveFrame frame, MotionEvent event) {
  frame.screenRotate(event);
}

void boxCustomClick(InteractiveFrame frame) {
  if(frame.scene().mouseAgent().pickingMode() == MouseAgent.PickingMode.MOVE)
    frame.center();
}

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
package frame;

import processing.core.PApplet;
import processing.event.Event;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.MouseAgent;
import remixlab.proscene.Scene;

/**
 * Created by pierre on 11/15/16.
 */
public class FrameInteraction2 extends PApplet {
 Scene scene;
 InteractiveFrame frame1, frame2;

 //Choose one of P3D for a 3D scene, or P2D or JAVA2D for a 2D scene
 String renderer = JAVA2D;

 public void settings() {
  size(1024, 768, renderer);
 }

 public void setup() {
  scene = new Scene(this);
  scene.eyeFrame().setDamping(0);
  scene.setPickingVisualHint(true);

  //frame 1
  frame1 = new InteractiveFrame(scene, "drawAxes");
  frame1.setPickingPrecision(InteractiveFrame.PickingPrecision.ADAPTIVE);
  frame1.setGrabsInputThreshold(scene.radius()/4);
  frame1.translate(50, 50);

  //frame 3
  frame2 = new InteractiveFrame(scene, frame1, "drawTorusSolenoid");
  frame2.translate(-100, -50);
  frame2.setKeyBinding(LEFT, "rotateYPos");
  frame2.setMotionBinding((Event.SHIFT | Event.CTRL), LEFT, "translate");
  frame2.setKeyBinding((Event.SHIFT | Event.CTRL), LEFT, "rotateZNeg");
 }

 public void draw() {
  background(0);
  scene.drawFrames();
 }

 public void keyPressed() {
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
  // set the default grabber at both the scene.motionAgent() and the scene.keyAgent()
  if(key == 'v') {
   scene.inputHandler().setDefaultGrabber(frame1);
   println(frame1.info());
  }
  if(key == 'x') {
   scene.inputHandler().setDefaultGrabber(frame2);
   println(frame2.info());
  }
 }

 public static void main(String args[]) {
  PApplet.main(new String[] { "frame.FrameInteraction2" });
 }
}
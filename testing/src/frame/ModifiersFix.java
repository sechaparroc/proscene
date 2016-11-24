package frame;

import processing.core.*;
import processing.event.*;
import remixlab.proscene.*;
// this one is need to make the p5Java2DModifiersFix in the doc work
import remixlab.bias.core.Shortcut;

/**
 * Idea is to test the modifiers key issue found in the JAVA2D renderer
 * (https://github.com/processing/processing/issues/3828).
 *
 * I found the use of modifier keys + the mouse pretty universal within
 * desktop VR apps. I think they thus must be supported in Proscene.
 *
 * @see remixlab.proscene.InteractiveFrame#p5Java2DModifiersFix(Shortcut)
 */
public class ModifiersFix extends PApplet {
  Scene scene;
  InteractiveFrame frame1;

  //Choose JAVA2D, FX2D or P2D for a 2D scene, or P3D for a 3D one
  String renderer = JAVA2D;

  public void settings() {
    size(700, 700, renderer);
  }

  public void setup() {
    scene = new Scene(this);
    scene.setPickingVisualHint(true);
    scene.setGridVisualHint(false);

    //frame 1
    frame1 = new InteractiveFrame(scene, "drawTorusSolenoid");
    frame1.setPickingPrecision(InteractiveFrame.PickingPrecision.ADAPTIVE);
    frame1.setGrabsInputThreshold(scene.radius() / 4);
    frame1.translate(50, 50);

    customBindings1();
  }

  public void customBindings1() {
    scene.mouseAgent().setPickingMode(MouseAgent.PickingMode.MOVE);
    // eyeFrame() and frame1
    for (InteractiveFrame iFrame : scene.frames()) {
      iFrame.removeBindings();
      iFrame.setMotionBinding(LEFT, "rotate");
      iFrame.setMotionBinding(RIGHT, "translate");
      iFrame.setMotionBinding(CENTER, "scale");
      iFrame.setMotionBinding((Event.SHIFT | Event.CTRL), RIGHT, "screenRotate");
      iFrame.setMotionBinding(MouseAgent.WHEEL_ID, "scale");
      iFrame.setClickBinding(LEFT, 1, "center");
      iFrame.setClickBinding(RIGHT, 1, "align");
      println(iFrame == scene.eyeFrame() ? "eyeFrame BINDINGS" : "frame1 BINDINGS");
      println(iFrame.info());
    }
  }

  public void customBindings2() {
    scene.mouseAgent().setPickingMode(MouseAgent.PickingMode.CLICK);
    // eyeFrame() and frame1
    for (InteractiveFrame iFrame : scene.frames()) {
      iFrame.removeBindings();
      iFrame.setMotionBinding(MouseAgent.NO_BUTTON, "rotate");
      iFrame.setMotionBinding(Event.SHIFT, MouseAgent.NO_BUTTON, "translate");
      iFrame.setMotionBinding(Event.CTRL, MouseAgent.NO_BUTTON, "scale");
      // This works but doesn't show the line visual hint as with customBindings1
      // (this is the expected behavior)
      iFrame.setMotionBinding((Event.SHIFT | Event.CTRL), MouseAgent.NO_BUTTON, "screenRotate");
      iFrame.setMotionBinding(Event.ALT, MouseAgent.WHEEL_ID, "scale");
      iFrame.setClickBinding(Event.SHIFT, LEFT, 1, "center");
      iFrame.setClickBinding(Event.CTRL, RIGHT, 1, "align");
      println(iFrame == scene.eyeFrame() ? "eyeFrame BINDINGS" : "frame1 BINDINGS");
      println(iFrame.info());
    }
  }

  public void draw() {
    background(0);
    scene.drawFrames();
  }

  public void keyPressed() {
    if (key == ' ')
      if (scene.mouseAgent().pickingMode() == MouseAgent.PickingMode.CLICK)
        customBindings1();
      else
        customBindings2();
    // set the default grabber at both the scene.motionAgent() and the scene.keyAgent()
    if (key == 'u')
      scene.inputHandler().setDefaultGrabber(frame1);
    if (key == 'v')
      scene.inputHandler().setDefaultGrabber(scene.eyeFrame());
  }

  public static void main(String args[]) {
    PApplet.main(new String[]{"frame.ModifiersFix"});
  }
}
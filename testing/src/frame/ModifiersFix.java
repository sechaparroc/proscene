package frame;

import processing.core.PApplet;
import processing.event.Event;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.MouseAgent;
import remixlab.proscene.Scene;

/**
 * Created by pierre on 11/15/16.
 */
public class ModifiersFix extends PApplet {
  Scene scene;
  InteractiveFrame frame1;

  //Choose one of P3D for a 3D scene, or P2D or JAVA2D for a 2D scene
  String renderer = P3D;

  public void settings() {
    size(700, 700, renderer);
  }

  public void setup() {
    scene = new Scene(this);
    scene.eyeFrame().setDamping(0);
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
    for (InteractiveFrame iFrame : scene.frames()) {
      iFrame.removeBindings();
      iFrame.setMotionBinding(LEFT, "rotate");
      iFrame.setMotionBinding(RIGHT, "translate");
      iFrame.setMotionBinding(CENTER, "scale");
      iFrame.setMotionBinding((Event.SHIFT | Event.CTRL), RIGHT, "screenRotate");
      iFrame.setMotionBinding(MouseAgent.WHEEL_ID, "scale");
      iFrame.setClickBinding(LEFT, 1, "center");
      iFrame.setClickBinding(RIGHT, 1, "align");
    }
  }

  public void customBindings2() {
    scene.mouseAgent().setPickingMode(MouseAgent.PickingMode.CLICK);
    for (InteractiveFrame iFrame : scene.frames()) {
      iFrame.removeBindings();
      iFrame.setMotionBinding(MouseAgent.NO_BUTTON, "rotate");
      iFrame.setMotionBinding(Event.SHIFT, MouseAgent.NO_BUTTON, "translate");
      iFrame.setMotionBinding(Event.CTRL, MouseAgent.NO_BUTTON, "scale");
      iFrame.setMotionBinding((Event.SHIFT | Event.CTRL), MouseAgent.NO_BUTTON, "screenRotate");
      iFrame.setMotionBinding(Event.ALT, MouseAgent.WHEEL_ID, "scale");
      iFrame.setClickBinding(Event.SHIFT, LEFT, 1, "center");
      iFrame.setClickBinding(Event.CTRL, RIGHT, 1, "align");
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
    if (key == 'v') {
      scene.inputHandler().setDefaultGrabber(frame1);
      println(frame1.info());
    }
    if (key == 'w') {
      scene.inputHandler().setDefaultGrabber(scene.eyeFrame());
      println(scene.eyeFrame().info());
    }
  }

  public static void main(String args[]) {
    PApplet.main(new String[]{"frame.ModifiersFix"});
  }
}
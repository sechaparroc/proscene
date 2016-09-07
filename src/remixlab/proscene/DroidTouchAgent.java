/**************************************************************************************
 * ProScene (version 3.0.0)
 * Copyright (c) 2014-2016 National University of Colombia, https://github.com/remixlab
 * @author Victor Manuel Forero, Jean Pierre Charalambos, http://otrolado.info/
 * 
 * All rights reserved. Library that eases the creation of interactive scenes
 * in Processing, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 **************************************************************************************/

package remixlab.proscene;

import processing.core.PApplet;
import remixlab.bias.core.*;
import remixlab.bias.event.*;
import remixlab.proscene.MouseAgent.PickingMode;
import remixlab.proscene.TouchProcessor.Gestures;

public class DroidTouchAgent extends Agent {
  Scene scene;
  boolean fired, flushed;
  protected DOF2Event d2Event, d2PrevEvent;
  protected DOF6Event d6Event, d6PrevEvent;
  protected TouchProcessor touchProcessor;
  public static int TAP_ID, DRAG_ONE_ID, DRAG_TWO_ID, DRAG_THREE_ID, TURN_TWO_ID, TURN_THREE_ID, PINCH_TWO_ID,
      PINCH_THREE_ID, OPPOSABLE_THREE_ID;

  public DroidTouchAgent(Scene scn) {
    super(scn.inputHandler());
    scene = scn;
    TAP_ID = 1;//scene().registerClickID(this);
    DRAG_ONE_ID = scene().registerMotionID(2);
    DRAG_TWO_ID = scene().registerMotionID(2);
    DRAG_THREE_ID = scene().registerMotionID(2);
    TURN_TWO_ID = scene().registerMotionID(6);
    TURN_THREE_ID = scene().registerMotionID(6);
    PINCH_TWO_ID = scene().registerMotionID(6);
    PINCH_THREE_ID = scene().registerMotionID(6);
    OPPOSABLE_THREE_ID = scene().registerMotionID(6);
    touchProcessor = new TouchProcessor();
    /*
    TAP_ID = scene().registerClickID(1, this);
    DRAG_ONE_ID = scene().registerMotionID(1, 6);
    DRAG_TWO_ID = scene().registerMotionID(2, 6);
    DRAG_THREE_ID = scene().registerMotionID(3, 6);
    TURN_TWO_ID = scene().registerMotionID(4, 6);
    TURN_THREE_ID = scene().registerMotionID(5, 6);
    PINCH_TWO_ID = scene().registerMotionID(6, 6);
    PINCH_THREE_ID = scene().registerMotionID(7, 6);
    OPPOSABLE_THREE_ID = scene().registerMotionID(8, 6);
    touchProcessor = new TouchProcessor();
    // */
    //debug
    System.out.println("TAP_ID " + TAP_ID);
    System.out.println("DRAG_ONE_ID " + DRAG_ONE_ID);
    System.out.println("DRAG_TWO_ID " + DRAG_TWO_ID);
    System.out.println("DRAG_THREE_ID " + DRAG_THREE_ID);
    System.out.println("TURN_TWO_ID " + TURN_TWO_ID);
    System.out.println("TURN_THREE_ID " + TURN_THREE_ID);
    System.out.println("PINCH_TWO_ID " + PINCH_TWO_ID);
    System.out.println("PINCH_THREE_ID " + PINCH_THREE_ID);
    System.out.println("OPPOSABLE_THREE_ID " + OPPOSABLE_THREE_ID);
  }

  protected void setDefaultBindings(InteractiveFrame frame) {
    frame.removeMotionBindings();
    frame.removeClickBindings();

    frame.setMotionBinding(DRAG_ONE_ID, "rotate");
    frame.setMotionBinding(TURN_TWO_ID, frame.isEyeFrame() ? "zoomOnRegion" : "screenRotate");
    frame.setMotionBinding(DRAG_TWO_ID, "translate");
    frame.setMotionBinding(PINCH_TWO_ID, scene().is3D() ? frame.isEyeFrame() ? "translateZ" : "scale" : "scale");
  }

  /**
   * Returns the scene this object belongs to.
   */
  public Scene scene() {
    return scene;
  }

  public void touchEvent(android.view.MotionEvent e) {
    int action = e.getAction();
    int code = action & android.view.MotionEvent.ACTION_MASK;
    int index = action >> android.view.MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    int turnOrientation;
    float x = e.getX(index);
    float y = e.getY(index);
    int id = e.getPointerId(index);
    Gestures gesture;
    PApplet.println("touch");
    PApplet.print(x + " " + y + " " + id);
    // pass the events to the TouchProcessor
    fired  = (code == android.view.MotionEvent.ACTION_DOWN || code == android.view.MotionEvent.ACTION_POINTER_DOWN);
    flushed = (code == android.view.MotionEvent.ACTION_UP || code == android.view.MotionEvent.ACTION_POINTER_UP);
    if (fired) {
      // touch(new DOF6Event(x, y, 0, 0, 0, 0));
      PApplet.print("down");

      touchProcessor.pointDown(x, y, id);
      touchProcessor.parse();
      /*
      d6Event = new DOF6Event(null, touchProcessor.getCx(), touchProcessor.getCy(), 0, 0, 0, 0, MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID);
      d6PrevEvent = d6Event.get();
      d6Event = new DOF6Event(d6PrevEvent, touchProcessor.getCx(), touchProcessor.getCy(), 0, 0, 0, 0,
          MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID);
      if (e.getPointerCount() == 1)
        updateTrackedGrabber(d6Event);
      */
      d2Event = new DOF2Event(null, touchProcessor.getCx(), touchProcessor.getCy(), MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID);
      d2PrevEvent = d2Event.get();
      d2Event = new DOF2Event(d2PrevEvent, touchProcessor.getCx(), touchProcessor.getCy(), MotionEvent.NO_MODIFIER_MASK, MotionEvent.NO_ID);
      if (e.getPointerCount() == 1)
        updateTrackedGrabber(d2Event);
    } else if (flushed) {
      PApplet.print("up");
      touchProcessor.pointUp(id);
      if (e.getPointerCount() == 1) {
        gesture = touchProcessor.parseTap();
        if (gesture == Gestures.TAP_ID) {
          handle(
              new ClickEvent(e.getX() - scene.originCorner().x(), e.getY() - scene.originCorner().y(), gesture.id()));
        }
        this.disableTracking();
        this.enableTracking();
      }
    } else if (code == android.view.MotionEvent.ACTION_MOVE) {
      PApplet.print("move");
      int numPointers = e.getPointerCount();
      for (int i = 0; i < numPointers; i++) {
        id = e.getPointerId(i);
        x = e.getX(i);
        y = e.getY(i);
        touchProcessor.pointMoved(x, y, id);
      }
      gesture = touchProcessor.parseGesture();
      if (gesture != null) {
        PApplet.print("Gesto " + gesture + ", id: " + gesture.id());
        /*
        if (d6PrevEvent.id() != gesture.id()) {
          d6PrevEvent = null;
        }
        //*/
        switch (gesture) {
        case DRAG_ONE_ID:
        case DRAG_TWO_ID:
        case DRAG_THREE_ID:// Drag
          //d6Event = new DOF6Event(d6PrevEvent, touchProcessor.getCx(), touchProcessor.getCy(), 0, 0, 0, 0, MotionEvent.NO_MODIFIER_MASK, gesture.id());
          d2Event = new DOF2Event(d2PrevEvent, touchProcessor.getCx(), touchProcessor.getCy(), MotionEvent.NO_MODIFIER_MASK, gesture.id());
          handle(fired ? d2Event.fire() : flushed ? d2Event.flush() : d2Event);
          d2PrevEvent = d2Event.get();
          
          //currentEvent = new DOF2Event(prevEvent, touchProcessor.getCx(), touchProcessor.getCy(), MotionEvent.NO_MODIFIER_MASK, gesture.id());
          PApplet.print("drag");
          break;
         /*
        case OPPOSABLE_THREE_ID:
          d6Event = new DOF6Event(d6PrevEvent, x, y, 0, 0, 0, 0, MotionEvent.NO_MODIFIER_MASK, gesture.id());
          PApplet.print("opposable");
          break;
        case PINCH_TWO_ID:
        case PINCH_THREE_ID: // Pinch
          d6Event = new DOF6Event(d6PrevEvent, touchProcessor.getZ(), 0, 0, 0, 0, 0, MotionEvent.NO_MODIFIER_MASK,
              gesture.id());
          PApplet.print("pinch");
          break;
        case TURN_TWO_ID:
        case TURN_THREE_ID: // Rotate
          turnOrientation = 1;
          // TODO needs testing
          if (inputGrabber() instanceof InteractiveFrame)
            turnOrientation = ((InteractiveFrame) inputGrabber()).isEyeFrame() ? -1 : 1;
          d6Event = new DOF6Event(d6PrevEvent, touchProcessor.getR() * turnOrientation, 0, 0, 0, 0, 0,
              MotionEvent.NO_MODIFIER_MASK, gesture.id());
          PApplet.print("rotate");
          break;
          //*/
        default:
          break;
        }
        /*
        if (gesture != null) {
          if (d6PrevEvent != null) {
            handle(d6Event);
          }
          d6PrevEvent = d6Event.get();
        }
        //*/
      }
    }
  }
}
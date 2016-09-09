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
import remixlab.proscene.TouchProcessor.Gestures;

/**
 * Proscene Android touch-agent. A Processing fully fledged touch
 * {@link remixlab.bias.core.Agent}.
 *
 * @see remixlab.bias.core.Agent
 * @see remixlab.proscene.KeyAgent
 * @see remixlab.proscene.DroidKeyAgent
 * @see remixlab.proscene.MouseAgent
 */
public class DroidTouchAgent extends Agent {
  Scene scene;
  //boolean fired, flushed;
  protected DOF2Event d2Event, d2PrevEvent;
  protected DOF1Event d1Event, d1PrevEvent;
  // protected DOF6Event d6Event, d6PrevEvent;
  protected TouchProcessor touchProcessor;
  public static int TAP_ID, DRAG_ONE_ID, DRAG_TWO_ID, DRAG_THREE_ID, TURN_TWO_ID, TURN_THREE_ID, PINCH_TWO_ID,
      PINCH_THREE_ID, OPPOSABLE_THREE_ID;

  public DroidTouchAgent(Scene scn) {
    super(scn.inputHandler());
    scene = scn;
    TAP_ID = 1;
    DRAG_ONE_ID = scene().registerMotionID(2);
    DRAG_TWO_ID = scene().registerMotionID(2);
    DRAG_THREE_ID = scene().registerMotionID(2);
    OPPOSABLE_THREE_ID = scene().registerMotionID(2);
    TURN_TWO_ID = scene().registerMotionID(1);
    TURN_THREE_ID = scene().registerMotionID(1);
    PINCH_TWO_ID = scene().registerMotionID(1);
    PINCH_THREE_ID = scene().registerMotionID(1);
    touchProcessor = new TouchProcessor();
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
    float x = e.getX(index);
    float y = e.getY(index);
    int id = e.getPointerId(index);
    Gestures gesture;
    PApplet.println("touch");
    PApplet.print(x + " " + y + " " + id);
    // pass the events to the TouchProcessor
    if (code == android.view.MotionEvent.ACTION_DOWN || code == android.view.MotionEvent.ACTION_POINTER_DOWN) {
      // touch(new DOF6Event(x, y, 0, 0, 0, 0));
      PApplet.print("down");
      touchProcessor.pointDown(x, y, id);
      touchProcessor.parse();
      d2Event = new DOF2Event(d2PrevEvent, touchProcessor.getCx(), touchProcessor.getCy(), MotionEvent.NO_MODIFIER_MASK,
          MotionEvent.NO_ID);
      if (e.getPointerCount() == 1)
        updateTrackedGrabber(d2Event);
      d2PrevEvent = d2Event.get();
    } else if (code == android.view.MotionEvent.ACTION_UP || code == android.view.MotionEvent.ACTION_POINTER_UP) {
      PApplet.print("up");
      touchProcessor.pointUp(id);
      if (e.getPointerCount() == 1) {
        gesture = touchProcessor.parseTap();
        if (gesture == Gestures.TAP_ID) {
          handle(
              new ClickEvent(e.getX() - scene.originCorner().x(), e.getY() - scene.originCorner().y(), gesture.id()));
        }
        this.resetTrackedGrabber();
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
        // TODO is this necessary?
        // if (d6PrevEvent.id() != gesture.id()) d6PrevEvent = null;
        // should be done by fire and flush events as it's done in dof2 events now
        // dof1 code moved below (uncer how to handle dof1 null events) and left commented
        // Note, however, that some actions such as translateZ (see my comments PINCH_TWO_ID)
        // should be re-implemented first to support fire and flush events, such as it's
        // done with zoomOnRegion (refer to the GenericFrame class).
        switch (gesture) {
        case DRAG_ONE_ID:
        case DRAG_TWO_ID:
        case DRAG_THREE_ID:// Drag
          // d6Event = new DOF6Event(d6PrevEvent, touchProcessor.getCx(),
          // touchProcessor.getCy(), 0, 0, 0, 0, MotionEvent.NO_MODIFIER_MASK,
          // gesture.id());
          if (d2PrevEvent != null)
            if (d2PrevEvent.id() != gesture.id())
              d2PrevEvent = null;
          d2Event = new DOF2Event(d2PrevEvent, touchProcessor.getCx(), touchProcessor.getCy(),
              MotionEvent.NO_MODIFIER_MASK, gesture.id());
          //handle(fired ? d2Event.fire() : flushed ? d2Event.flush() : d2Event);
          handle(d2Event);
          d2PrevEvent = d2Event.get();
          PApplet.print("drag");
          break;
        case OPPOSABLE_THREE_ID:
          if (d2PrevEvent != null)
            if (d2PrevEvent.id() != gesture.id())
              d2PrevEvent = null;
          // d6Event = new DOF6Event(d6PrevEvent, x, y, 0, 0, 0, 0,
          // MotionEvent.NO_MODIFIER_MASK, gesture.id());
          d2Event = new DOF2Event(d2PrevEvent, x, y, MotionEvent.NO_MODIFIER_MASK, gesture.id());
          //handle(fired ? d2Event.fire() : flushed ? d2Event.flush() : d2Event);
          handle(d2Event);
          d2PrevEvent = d2Event.get();
          PApplet.print("opposable");
          break;
        case PINCH_TWO_ID:
          // TODO
          // translateZ on the eyeFrame works only the first time the gesture is performed
          // after the second time it behaves weirdly.
          // Maybe it has to do with the handling of null events?
        case PINCH_THREE_ID: // Pinch
          // TODO how to handle dof1 null events
          if (d1PrevEvent != null)
            if (d1PrevEvent.id() != gesture.id())
              d1PrevEvent = null;
          d1Event = new DOF1Event(d1PrevEvent, touchProcessor.getZ(), MotionEvent.NO_MODIFIER_MASK, gesture.id());
          if (d1PrevEvent != null)
            handle(d1Event);
          d1PrevEvent = d1Event.get();
          PApplet.print("pinch");
          break;
        case TURN_TWO_ID:
        case TURN_THREE_ID: // Rotate
          // TODO how to handle dof1 null events
          if (d1PrevEvent != null)
            if (d1PrevEvent.id() != gesture.id())
              d1PrevEvent = null;
          // int turnOrientation = 1;
          // TODO ennumarate which actions need turn, as it should be handled at the
          // GenericFrame
          // and not here
          // if (inputGrabber() instanceof InteractiveFrame)
          // turnOrientation = ((InteractiveFrame) inputGrabber()).isEyeFrame() ? -1 : 1;
          // d1Event = new DOF1Event(d1PrevEvent, touchProcessor.getR() * turnOrientation,
          // MotionEvent.NO_MODIFIER_MASK, gesture.id());
          d1Event = new DOF1Event(d1PrevEvent, touchProcessor.getR(), MotionEvent.NO_MODIFIER_MASK, gesture.id());
          if (d1PrevEvent != null)
            handle(d1Event);
          d1PrevEvent = d1Event.get();
          PApplet.print("rotate");
          break;
        default:
          break;
        }
        // TODO is this necessary?
        // if (gesture != null) {
        // if (d6PrevEvent != null)
        // handle(d6Event);
        // d6PrevEvent = d6Event.get();
        // }
      }
    }
  }
}
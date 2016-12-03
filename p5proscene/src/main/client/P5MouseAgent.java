/**************************************************************************************
 * ProScene_JS (version 3.0.0)
 * Copyright (c) 2014-2016 National University of Colombia, https://github.com/remixlab
 * @author Cesar Colorado, https://github.com/orgs/remixlab/people/cacolorador
 *
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 **************************************************************************************/

package main.client;

import main.eventjs.JsMouseEvent;
import main.p5js.P5JS;

import com.gwtent.reflection.client.Reflectable;

import remixlab.bias.core.Agent;
import remixlab.bias.core.BogusEvent;
import remixlab.bias.event.ClickEvent;
import remixlab.bias.event.DOF1Event;
import remixlab.bias.event.DOF2Event;

@Reflectable
public class P5MouseAgent extends Agent {
  public static final int LEFT_ID = P5JS.LEFT, CENTER_ID = P5JS.CENTER, RIGHT_ID = P5JS.RIGHT, WHEEL_ID = P5JS.WHEEL, NO_BUTTON = BogusEvent.NO_ID;

  protected boolean click2Pick;
  protected DOF2Event currentEvent, prevEvent;
  protected boolean move, press, drag, release;

  P5Scene scene = null;

  public P5MouseAgent(P5Scene scn) {
    super(scn.inputHandler());
    scene = scn;
  }

  public void mouseEvent(JsMouseEvent e) {
    move = e.getAction() == JsMouseEvent.MOVE;
    press = e.getAction() == JsMouseEvent.PRESS;
    drag = e.getAction() == JsMouseEvent.DRAG;
    release = e.getAction() == JsMouseEvent.RELEASE;
    //better and more robust is to work without modifiers, which Processing don't report reliably
    if (move || press || drag || release) {
      currentEvent = new DOF2Event(prevEvent, e.getX() - scene.originCorner().x(), e.getY() - scene.originCorner().y(),
            /*e.getModifiers()*/BogusEvent.NO_MODIFIER_MASK, move ? BogusEvent.NO_ID : e.getButton());
      if (move && !click2Pick)
        updateTrackedGrabber(currentEvent);
      handle(press ? currentEvent.fire() : release ? currentEvent.flush() : currentEvent);
      prevEvent = currentEvent.get();
      return;
    }
    if (e.getAction() == JsMouseEvent.WHEEL) {// e.getAction() = MouseEvent.WHEEL = 8
      handle(new DOF1Event(e.getCount(), /*e.getModifiers()*/BogusEvent.NO_MODIFIER_MASK, WHEEL_ID));
      return;
    }
    if (e.getAction() == JsMouseEvent.CLICK) {
      ClickEvent bogusClickEvent = new ClickEvent(e.getX() - scene.originCorner().x(), e.getY() - scene.originCorner().y(),
	          /*e.getModifiers()*/BogusEvent.NO_MODIFIER_MASK, e.getButton(), e.getCount());
      if (click2Pick)
        updateTrackedGrabber(bogusClickEvent);
      handle(bogusClickEvent);
      return;
    }
  }
}


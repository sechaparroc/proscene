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

import main.eventjs.JsKeyEvent;
import main.p5js.P5JS;

import com.gwtent.reflection.client.Reflectable;

import remixlab.bias.core.Agent;
import remixlab.bias.event.KeyboardEvent;

@Reflectable
public class P5KeyAgent extends Agent {
  public static final int LEFT_KEY = P5JS.LEFT, RIGHT_KEY = P5JS.RIGHT, UP_KEY = P5JS.UP, DOWN_KEY = P5JS.DOWN;

  protected boolean press, release, type;
  protected KeyboardEvent currentEvent;

  public P5KeyAgent(P5Scene scene) {
    super(scene.inputHandler());
    addGrabber(scene);
  }

  public void keyEvent(JsKeyEvent e) {
    press = e.getAction() == JsKeyEvent.PRESS;
    release = e.getAction() == JsKeyEvent.RELEASE;
    type = e.getAction() == JsKeyEvent.TYPE;

    // P5JS.console( Integer.toString( e.getAction()));

    if (type)
      currentEvent = new KeyboardEvent(e.getKey());
    else if (press || release)
      currentEvent = new KeyboardEvent(e.getModifiers(), e.getKeyCode());
    if (type || press)
      updateTrackedGrabber(currentEvent);
    handle(release ? currentEvent.flush() : currentEvent.fire());
  }
}

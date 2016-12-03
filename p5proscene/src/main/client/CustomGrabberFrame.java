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

import remixlab.bias.event.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.core.GenericFrame;

public class CustomGrabberFrame extends GenericFrame {
  public CustomGrabberFrame(AbstractScene _scene) {
    super(_scene);
  }

  public CustomGrabberFrame(Eye _eye) {
    super(_eye);
  }

  protected CustomGrabberFrame(CustomGrabberFrame otherFrame) {
    super(otherFrame);
  }

  @Override
  public CustomGrabberFrame get() {
    return new CustomGrabberFrame(this);
  }

  @Override
  public void performInteraction(MotionEvent event) {


    switch (event.shortcut().id()) {
      //it's also possible to use Processing constants such as:
      //case LEFT:
      case P5MouseAgent.LEFT_ID:
        rotate(event);
        break;
      case P5MouseAgent.CENTER_ID:
        screenRotate(event);
        break;
      case P5MouseAgent.RIGHT_ID:
        translate(event);
        break;
      case P5MouseAgent.WHEEL_ID:
        if (scene().is3D() && isEyeFrame())
          translateZ(event);
        else
          scale(event);
        break;
    }
  }

  @Override
  public void performInteraction(ClickEvent event) {
    if (event.clickCount() == 2) {
      if (event.id() == P5MouseAgent.LEFT_ID)
        center();
      if (event.id() == P5MouseAgent.RIGHT_ID)
        align();
    }
  }

  @Override
  public void performInteraction(KeyboardEvent event) {
    if (bypassKey(event))
      return;
    if (event.isShiftDown()) {
      //also possible here is to use Processing keys: UP
      if (event.id() == P5KeyAgent.UP_KEY)
        translateY(true);
      if (event.id() == P5KeyAgent.DOWN_KEY)
        translateY(false);
      if (event.id() == P5KeyAgent.LEFT_KEY)
        translateX(false);
      if (event.id() == P5KeyAgent.RIGHT_KEY)
        translateX(true);
    } else {
      if (event.id() == P5KeyAgent.UP_KEY)
        if (gScene.is3D())
          rotateX(true);
      if (event.id() == P5KeyAgent.DOWN_KEY)
        if (gScene.is3D())
          rotateY(false);
      if (event.id() == P5KeyAgent.LEFT_KEY)
        rotateZ(false);
      if (event.id() == P5KeyAgent.RIGHT_KEY)
        rotateZ(true);
    }
  }
}
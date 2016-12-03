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

import java.util.HashMap;
import java.util.Map;

import main.p5js.P5JS;
import remixlab.bias.event.KeyboardEvent;
import remixlab.dandelion.core.AbstractScene;
import remixlab.dandelion.core.Camera;
import remixlab.dandelion.core.Eye;
import remixlab.dandelion.core.GenericFrame;
import remixlab.dandelion.core.KeyFrameInterpolator;
import remixlab.dandelion.core.Window;
import remixlab.dandelion.geom.Point;
import remixlab.dandelion.geom.Vec;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtent.reflection.client.Reflectable;

@Reflectable
public class P5Scene extends AbstractScene {
  P5JS parent;
  Map<String, JavaScriptObject> handlers = new HashMap<String, JavaScriptObject>();

  public void subscribe(String key, JavaScriptObject obj) {
    if (!handlers.containsKey(key))
      handlers.put(key, obj);
  }

  public JavaScriptObject getHandler(String key) {
    if (handlers.containsKey(key))
      return handlers.get(key);
    return null;
  }

  public P5Scene getIt() {
    return this;
  }

  public P5Scene() {
  }

  public P5Scene(P5JS papplet) {
    // 1. P5 objects
    parent = papplet;

    // 2. P5 connection
    setMatrixHelper(new P5MatrixHelper(this, parent));
    defMotionAgent = new P5MouseAgent(this);
    defKeyboardAgent = new P5KeyAgent(this);
    parent.registerMethod("mouseEvent", motionAgent());
    parent.registerMethod("keyEvent", keyboardAgent());

    parent.registerMethod("pre", this);
    parent.registerMethod("draw", this);

    // 3. Eye
    setLeftHanded();
    width = parent.width();
    height = parent.height();
    eye = is3D() ? new Camera(this) : new Window(this);
    eye.setFrame(new CustomGrabberFrame(eye));
    setEye(eye());// calls showAll();

    // 4. init
    init();
  }

//	  @Override
//	  protected void performInteraction(KeyboardEvent event) {
//		  
//	    	P5JS.console("KeyboardEvent_performInteraction->"+event.key()); 
//	    if(event.key() == 'S')
//	    {
//	      eye().interpolateToFitScene();
//	      eye().centerScene();
//	    }
//	    if( event.key() =='A')
//	    {
//	      eye().addKeyFrameToPath(1);
//	    }
//	    else if( event.key()=='R' )
//	    {
//	      eye().deletePath(1);
//	    }
//	    else if(event.key()=='1')
//	    {
//	      eye().playPath(1);
//	    }
//	  }
//	  
//	  @Override
//	  protected boolean checkIfGrabsInput(KeyboardEvent event) {
//		  P5JS.console("checkIfGrabsInput");
//	    return event.key() == 'S' || event.key() == 'A' || event.key() == 'R' || event.key() == '1';
//	  }	


  @Override
  protected native void performInteraction(KeyboardEvent event)  /*-{
      //console.log( "performInteraction JSNI ");

      var handler = this.@main.client.P5Scene::getHandler(Ljava/lang/String;)("performInteraction");

      if (handler)
          handler.call(this, event);
  }-*/;

  @Override
  protected native boolean checkIfGrabsInput(KeyboardEvent event) /*-{
      //  console.log( "checkIfGrabsInput JSNI ");

      var handler = this.@main.client.P5Scene::getHandler(Ljava/lang/String;)("checkIfGrabsInput");

      if (handler)
          return handler.call(this, event);
      else
          return false;

  }-*/;

  public void pre() {
    //	  P5JS.console("java reflection pre");
    if ((width != parent.width()) || (height != parent.height())) {
      width = parent.width();
      height = parent.height();
      eye().setScreenWidthAndHeight(width, height);
    }
    preDraw();
  }

  public void draw() {
//		  P5JS.console("java reflection draw"); 
    postDraw();
  }


  @Override
  public int width() {
    return parent.width();
  }

  @Override
  public int height() {
    return parent.height();
  }

  // DIM

  @Override
  public boolean is3D() {
    return true;
  }


  @Override
  protected void setPlatform() {
    // TODO Auto-generated method stub
  }

  @Override
  public String info() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void drawTorusSolenoid(int faces, int detail, float insideRadius, float outsideRadius) {
    // TODO Auto-generated method stub
  }


  @Override
  public void drawCylinder(float w, float h) {
    // TODO Auto-generated method stub
  }


  @Override
  public void drawHollowCylinder(int detail, float w, float h, Vec m, Vec n) {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawCone(int detail, float x, float y, float r, float h) {
    // TODO Auto-generated method stub
  }


  @Override
  public void drawCone(int detail, float x, float y, float r1, float r2, float h) {
    // TODO Auto-generated method stub
  }


  @Override
  public void drawAxes(float length) {
    // TODO Auto-generated method stub
  }


  @Override
  public void drawGrid(float size, int nbSubdivisions) {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawDottedGrid(float size, int nbSubdivisions) {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawPath(KeyFrameInterpolator kfi, int mask, int nbFrames, float scale) {
    // TODO Auto-generated method stub
  }


  public void drawEye(Eye eye, float scale) {
    // TODO Auto-generated method stub
  }


  @Override
  protected void drawKFIEye(float scale) {
    // TODO Auto-generated method stub
  }

  @Override
  protected void drawZoomWindowHint() {
    // TODO Auto-generated method stub
  }


  @Override
  protected void drawScreenRotateHint() {
    // TODO Auto-generated method stub
  }

  @Override
  protected void drawAnchorHint() {
    // TODO Auto-generated method stub
  }

  @Override
  protected void drawPointUnderPixelHint() {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawCross(float px, float py, float size) {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawFilledCircle(int subdivisions, Vec center, float radius) {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawFilledSquare(Vec center, float edge) {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawShooterTarget(Vec center, float length) {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawPickingTarget(GenericFrame gFrame) {
    // TODO Auto-generated method stub
  }

  @Override
  public float pixelDepth(Point pixel) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void disableDepthTest() {
    // TODO Auto-generated method stub
  }

  @Override
  public void enableDepthTest() {
    // TODO Auto-generated method stub
  }

  @Override
  public void drawEye(Eye eye) {
    // TODO Auto-generated method stub
  }
}
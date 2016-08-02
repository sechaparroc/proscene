/**************************************************************************************
 * ProScene (version 3.0.0)
 * Copyright (c) 2014-2016 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 * 
 * All rights reserved. Library that eases the creation of interactive scenes
 * in Processing, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 **************************************************************************************/

package remixlab.proscene;

import java.lang.reflect.Method;

import processing.core.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;
import remixlab.util.*;

class Shape {
  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(shp).append(obj).append(mth).append(shift).toHashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (object == null)
      return false;
    if (object == this)
      return true;
    if (object.getClass() != getClass())
      return false;
    Shape other = (Shape) object;
    return new EqualsBuilder().append(shp, other.shp).append(obj, other.obj).append(mth, other.mth)
        .append(shift, other.shift).isEquals();
  }

  protected InteractiveFrame iFrame;

  protected PShape shp;
  protected Object obj;
  protected Method mth;
  protected Vec shift;

  Shape(InteractiveFrame frame) {
    iFrame = frame;
  }

  public void shift(Vec s) {
    if (iFrame.isEyeFrame())
      AbstractScene.showOnlyEyeWarning("shift", true);
    shift = s;
  }

  protected void draw(PGraphics pg) {
    if (iFrame.isEyeFrame())
      return;
    if (shift != null)
      if (pg.is3D())
        pg.translate(shift.x(), shift.y(), shift.z());
      else
        pg.translate(shift.x(), shift.y());
    if (shp != null) {
      // don't do expensive matrix ops if invisible
      if (shp.isVisible() && !iFrame.isEyeFrame()) {
        pg.flush();
        if (pg.shapeMode == PApplet.CENTER) {
          pg.pushMatrix();
          iFrame.translate(-shp.getWidth() / 2, -shp.getHeight() / 2);
        }
        shp.draw(pg); // needs to handle recorder too
        if (pg.shapeMode == PApplet.CENTER) {
          pg.popMatrix();
        }
      }
    } else if (mth != null && obj != null) {
      try {
        mth.invoke(obj, new Object[] { pg });
      } catch (Exception e1) {
        try {
          mth.invoke(obj, new Object[] { iFrame, pg });
        } catch (Exception e2) {
          PApplet.println("Something went wrong when invoking your " + mth.getName() + " method");
          e1.printStackTrace();
          e2.printStackTrace();
        }
      }
    }
  }

  public void set(PShape ps) {
    if (!isReset()) {
      System.out.println("overwritting shape by set(PShape ps)");
      reset();
    }
    shp = ps;
  }

  public void set(Shape other) {
    if (!isReset()) {
      System.out.println("overwritting shape by set(Shape other)");
      reset();
    }
    shp = other.shp;
    obj = other.obj;
    mth = other.mth;
    shift = other.shift;
  }

  public void set(Object object, Method method) {
    if (!isReset()) {
      System.out.println("overwritting shape by set(Object object, Method method)");
      reset();
    }
    obj = object;
    mth = method;
  }

  public boolean set(Object object, String methodName) {
    return set(object, methodName, false);
  }

  public boolean set(Object object, String methodName, boolean print) {
    boolean success = false;
    if (isImmediate())
      if (obj == object && mth.getName().equals(methodName)) {
        System.out.println("Warning: shape already set. Nothing done in set(Object object, String methodName)");
        return false;
      }
    if (!isReset()) {
      System.out.println("Warning: overwritting shape by set(Object object, String methodName)");
      reset();
    }
    try {
      obj = object;
      mth = object.getClass().getMethod(methodName, new Class<?>[] { PGraphics.class });
      success = true;
    } catch (Exception e1) {
      try {
        if (iFrame.isEyeFrame()) {
          PApplet.println("Warning: no eyeFrame shape set. Either the " + methodName
              + " wasn't found, or perhaps it takes an extra InteractiveFrame param?");
          return false;
        }
        if (object == iFrame) {
          if (print)
            PApplet.println("Warning: no iFrame shape set. Use setShape(methodName) instead");
          return false;
        }
        obj = object;
        mth = object.getClass().getMethod(methodName, new Class<?>[] { InteractiveFrame.class, PGraphics.class });
        success = true;
      } catch (Exception e2) {
        if (print) {
          PApplet.println("Warning: no iFrame shape set with " + methodName + " method");
          e1.printStackTrace();
          e2.printStackTrace();
        }
      }
    }
    return success;
  }

  public boolean set(String methodName) {
    boolean result = set(iFrame, methodName, false);
    if (!result)
      result = set(iFrame.scene(), methodName, false);
    if (!result)
      PApplet.println("Warning: no iFrame shape set. No " + methodName + " method found in the iFrame or in the scene");
    return result;
  }

  public void reset() {
    shp = null;
    mth = null;
    obj = null;
    shift = null;
  }

  public boolean isReset() {
    return shp == null && mth == null;
  }

  public boolean isRetained() {
    return shp != null;
  }

  public boolean isImmediate() {
    return obj != null && mth != null;
  }
}
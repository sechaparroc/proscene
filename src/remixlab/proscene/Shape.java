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

/**
 * An interactive-frame shape may wrap either a PShape (Processing retained mode) or a
 * graphics procedure (Processing immediate mode), but not both.
 * <p>
 * This class allows to easily set an interactive-frame shape (see all the set() methods)
 * and is provided to ease the {@link remixlab.proscene.InteractiveFrame} class
 * implementation itself.
 */
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

  InteractiveFrame iFrame;
  PShape shp;
  Object obj;
  Method mth;
  Vec shift;

  Shape(InteractiveFrame frame) {
    iFrame = frame;
  }

  /**
   * Defines the shape shift, i.e., the translation respect to the frame origin used to
   * draw the shape.
   */
  void shift(Vec s) {
    if (iFrame.isEyeFrame())
      AbstractScene.showOnlyEyeWarning("shift", true);
    shift = s;
    iFrame.modified();
  }

  /**
   * Draw the shape into an arbitrary PGraphics.
   */
  void draw(PGraphics pg) {
    if (iFrame.isEyeFrame())
      return;
    if (shift != null)
      if (pg.is3D())
        pg.translate(shift.x(), shift.y(), shift.z());
      else
        pg.translate(shift.x(), shift.y());
    // The shape part took verbatim from Processing, see:
    // https://github.com/processing/processing/blob/master/core/src/processing/core/PGraphics.java
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

  /**
   * Retained mode.
   */
  void set(PShape ps) {
    if (shp == ps)
      return;
    if (!isReset()) {
      System.out.println("overwritting shape by set(PShape ps)");
      reset();
    }
    shp = ps;
    iFrame.modified();
  }

  /**
   * Sets shape from other.
   * <p>
   * Note that all fields are copied by reference.
   */
  void set(Shape other) {
    if (equals(other))
      return;
    if (!isReset()) {
      System.out.println("Overwriting shape by set(Shape other)");
      reset();
    }
    shp = other.shp;
    obj = other.obj;
    mth = other.mth;
    shift = other.shift;
    iFrame.modified();
  }

  /**
   * Immediate mode.
   * <p>
   * Low-level routine. Looks for a {@code void methodName(PGraphics)} function prototype
   * in {@code object}.
   */
  void singleParam(Object object, String methodName) throws NoSuchMethodException, SecurityException {
    mth = object.getClass().getMethod(methodName, new Class<?>[] { PGraphics.class });
    obj = object;
  }

  /**
   * Immediate mode.
   * <p>
   * Low-level routine. Looks for a {@code void methodName(InteractiveFrame, PGraphics)}
   * function prototype in {@code object}.
   */
  void doubleParam(Object object, String methodName) throws NoSuchMethodException, SecurityException {
    mth = object.getClass().getMethod(methodName, new Class<?>[] { InteractiveFrame.class, PGraphics.class });
    obj = object;
  }

  /**
   * Immediate mode.
   * <p>
   * High-level routine where the {@code object} declaring the graphics procedure is
   * explicitly given.
   * <p>
   * Looks for a {@link #singleParam(Object, String)} function prototype first. If nothing
   * is hit, then looks for a {@link #doubleParam(Object, String)} function prototype,
   * only if the {@link remixlab.proscene.InteractiveFrame} instance this shape is
   * attached to is not a {@link remixlab.proscene.InteractiveFrame#isEyeFrame()}.
   */
  boolean set(Object object, String methodName) {
    if (!isSetable(object, methodName))
      return false;
    boolean success = false;
    if (object == iFrame || object == iFrame.scene() || object == iFrame.scene().pApplet())
      PApplet.println("Warning: you can use the simpler setShape(methodName) method too");
    try {
      singleParam(object, methodName);
      success = true;
    } catch (Exception e1) {
      try {
        if (iFrame.isEyeFrame()) {
          PApplet.println("Warning: no eyeFrame shape set. Either the " + methodName
              + " wasn't found, or perhaps it takes an extra InteractiveFrame param?");
          return false;
        }
        doubleParam(object, methodName);
        success = true;
      } catch (Exception e2) {
        PApplet.println("Warning: no shape set with " + methodName + " method");
        e1.printStackTrace();
        e2.printStackTrace();
      }
    }
    if (success)
      iFrame.modified();
    return success;
  }

  /**
   * Immediate mode.
   * <p>
   * High-level routine where the object declaring the graphics procedure is not given and
   * hence need to be inferred. It could be either:
   * <ol>
   * <li>The {@link remixlab.proscene.Scene#pApplet()};</li>
   * <li>The {@link remixlab.proscene.InteractiveFrame} instance this shape is attached
   * to, or;</li>
   * <li>The {@link remixlab.proscene.InteractiveFrame#scene()} handling that frame
   * instance.
   * </ol>
   * The algorithm looks for a {@link #singleParam(Object, String)} function prototype
   * first. If nothing is hit, then looks for a {@link #doubleParam(Object, String)}
   * function prototype, within the objects in the above order.
   */
  boolean set(String methodName) {
    boolean success = false;
    if (!isSetable(iFrame.scene().pApplet(), methodName))
      return false;
    try {
      singleParam(iFrame.scene().pApplet(), methodName);
      success = true;
    } catch (Exception e1) {
      try {
        doubleParam(iFrame.scene().pApplet(), methodName);
        success = true;
      } catch (Exception e2) {
        if (!isSetable(iFrame, methodName))
          return false;
        try {
          singleParam(iFrame, methodName);
          success = true;
        } catch (Exception e4) {
          if (!isSetable(iFrame.scene(), methodName))
            return false;
          try {
            singleParam(iFrame.scene(), methodName);
            success = true;
          } catch (Exception e3) {
            try {
              doubleParam(iFrame.scene(), methodName);
              success = true;
            } catch (Exception e5) {
              PApplet.println("Warning: no shape set with " + methodName + " method");
              e1.printStackTrace();
              e2.printStackTrace();
              e3.printStackTrace();
              e4.printStackTrace();
              e5.printStackTrace();
            }
          }
        }
      }
    }
    if (success)
      iFrame.modified();
    return success;
  }

  /**
   * Sets all internal shape references to null.
   */
  void reset() {
    if (isReset())
      return;
    shp = null;
    mth = null;
    obj = null;
    shift = null;
    iFrame.modified();
  }

  boolean isSetable(Object object, String methodName) {
    if (isImmediate())
      if (obj == object && mth.getName().equals(methodName))
        return false;
    if (!isReset()) {
      System.out.println("Warning: overwritting shape by set(Object object, String methodName)");
      reset();
    }
    return true;
  }

  /**
   * Checks if internal references are null.
   */
  boolean isReset() {
    return shp == null && mth == null;
  }

  /**
   * Does the shape wraps a PShape Processing object?
   */
  boolean isRetained() {
    return shp != null;
  }

  /**
   * Does the shape wraps a graphics procedure Processing object?
   */
  boolean isImmediate() {
    return obj != null && mth != null;
  }
}
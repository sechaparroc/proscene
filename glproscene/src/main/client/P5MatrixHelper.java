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

import main.p5js.P5JS;
import main.p5js.PMatrix2D;
import main.p5js.PMatrix3D;
import main.p5js.PVector;
import remixlab.dandelion.core.Camera;
import remixlab.dandelion.core.Eye;
import remixlab.dandelion.core.MatrixHelper;
import remixlab.dandelion.core.Window;
import remixlab.dandelion.geom.Mat;
import remixlab.dandelion.geom.Rotation;
import remixlab.dandelion.geom.Vec;

public class P5MatrixHelper extends MatrixHelper {
  P5JS pg;

  public P5MatrixHelper(P5Scene scn, P5JS renderer) {
    super(scn);
    pg = renderer;
  }

  public P5JS pggl() {
    return pg;
  }

  @Override
  public void bind(boolean recompute) {
    Eye eye = this.gScene.eye();

    if (recompute) {
      eye.computeProjection();
      eye.computeView();
      cacheProjectionView();
    }

    if (gScene.is3D())
      bind3d((Camera) eye);
    else
      bind2d((Window) eye);
  }

  public void bind3d(Camera camera) {

    Vec aux = camera.position();


    // 1. set projection
    switch (camera.type()) {
      case PERSPECTIVE:
        pggl().perspective(camera.fieldOfView(), camera.aspectRatio(), camera.zNear(), camera.zFar());
        break;
      case ORTHOGRAPHIC:
        float[] wh = camera.getOrthoWidthHeight();//return halfWidth halfHeight
        pggl().ortho(-wh[0], wh[0], -wh[1], wh[1], camera.zNear(), camera.zFar());
        break;
    }
    if (this.gScene.isRightHanded())
      pggl().projection().m11 = -pggl().projection().m11;

    // 2. set modelview
    pggl().camera(camera.position().x(), camera.position().y(), camera.position().z(),
        camera.at().x(), camera.at().y(), camera.at().z(),
        camera.upVector().x(), camera.upVector().y(), camera.upVector().z());
  }

  public void bind2d(Window window) {
    Vec pos = window.position();
    Rotation o = window.frame().orientation();
    translate(gScene.width() / 2, gScene.height() / 2);
    if (gScene.isRightHanded())
      scale(1, -1);
    scale(1 / window.frame().magnitude(), 1 / window.frame().magnitude());
    rotate(-o.angle());
    translate(-pos.x(), -pos.y());
  }

  @Override
  public void beginScreenDrawing() {
    if (gScene.is3D())
      super.beginScreenDrawing();
    else {
      Vec pos = gScene.eye().position();
      Rotation o = gScene.eye().frame().orientation();
      pushModelView();
      translate(pos.x(), pos.y());
      rotate(o.angle());
      scale(gScene.window().frame().magnitude(), gScene.window().frame().magnitude());
      if (gScene.isRightHanded())
        scale(1, -1);
      translate(-gScene.width() / 2, -gScene.height() / 2);
    }
  }

  @Override
  public void endScreenDrawing() {
    if (gScene.is3D())
      super.endScreenDrawing();
    else
      popModelView();
  }

  @Override
  public void pushProjection() {
    if (gScene.is3D())
      pggl().pushProjection();
    else
      super.pushProjection();
  }

  @Override
  public void popProjection() {
    if (gScene.is3D())
      pggl().popProjection();
    else
      super.popProjection();
  }

  @Override
  public void resetProjection() {
    if (gScene.is3D())
      pggl().resetProjection();
    else
      super.resetProjection();
  }

  @Override
  public void printProjection() {
    pg.printProjection();
  }

  @Override
  public Mat projection() {
    //return gScene.is3D() ? toMat(pggl().projection.get()) : gScene.eye().getProjection();
    return gScene.eye().getProjection();
  }

  @Override
  public Mat getProjection(Mat target) {
    if (target == null)
      target = projection().get();
    else
      target.set(projection());
    return target;
  }

  @Override
  public void applyProjection(Mat source) {
    if (gScene.is3D())
      pggl().applyProjection(toPMatrix(source));
    else
      super.applyProjection(source);
  }

  @Override
  public void pushModelView() {
    pg.pushMatrix();
  }

  @Override
  public void popModelView() {
    pg.popMatrix();
  }

  @Override
  public void resetModelView() {
    pggl().resetMatrix();
  }

  @Override
  public Mat modelView() {
    //return gScene.is3D() ? toMat((PMatrix3D) pggl().getMatrix()) : gScene.eye().getView();
    return gScene.eye().getView();
  }

  @Override
  public Mat getModelView(Mat target) {
    if (target == null)
      target = modelView().get();
    else
      target.set(modelView());
    return target;
  }

  @Override
  public void printModelView() {
    pg.printMatrix();
  }

  @Override
  public void applyModelView(Mat source) {
    pg.applyMatrix(toPMatrix(source));
  }

  @Override
  public void translate(float tx, float ty) {
    pg.translate(tx, ty);
  }

  @Override
  public void translate(float tx, float ty, float tz) {
    pg.translate(tx, ty, tz);
  }

  @Override
  public void rotate(float angle) {
    pg.rotate(angle);
  }

  @Override
  public void rotateX(float angle) {
    pg.rotateX(angle);
  }

  @Override
  public void rotateY(float angle) {
    pg.rotateY(angle);
  }

  @Override
  public void rotateZ(float angle) {
    pg.rotateZ(angle);
  }

  @Override
  public void rotate(float angle, float vx, float vy, float vz) {
    pg.rotate(angle, vx, vy, vz);
  }

  @Override
  public void scale(float s) {
    pg.scale(s);
  }

  @Override
  public void scale(float sx, float sy) {
    pg.scale(sx, sy);
  }

  @Override
  public void scale(float x, float y, float z) {
    pg.scale(x, y, z);
  }

  @Override
  public void setProjection(Mat source) {
    if (gScene.is3D())
      pggl().setProjection(toPMatrix(source));
    else
      super.setProjection(source);
  }

  @Override
  public void setModelView(Mat source) {
    if (gScene.is3D())
      pggl().setMatrix(toPMatrix(source));// in P5 this caches projmodelview
    else {
      if (pg instanceof P5JS) {
        pggl().modelview().set(toPMatrix(source));
        pggl().projmodelview().set(Mat.multiply(gScene.eye().getProjection(false), gScene.eye().getView(false)).getTransposed(new float[16]));
      }
//	      @unal
//	      else
//	        pg.setMatrix(Scene.toPMatrix2D(source));
    }
  }

  // estos helpers deberian ir en el TargetScene, pero Processing no lo deja hacer

  public PVector toPVector(Vec v) {
    return new PVector(v.x(), v.y(), v.z());
  }

  public Vec toVec(PVector v) {
    return new Vec(v.x, v.y, v.z);
  }

  public PMatrix3D toPMatrix(Mat m) {
    float[] a = m.getTransposed(new float[16]);
    return new PMatrix3D(a[0], a[1], a[2], a[3],
        a[4], a[5], a[6], a[7],
        a[8], a[9], a[10], a[11],
        a[12], a[13], a[14], a[15]);
  }

  public Mat toMat(PMatrix3D m) {
    return new Mat(m.get(new float[16]), true);
  }

  public Mat toMat(PMatrix2D m) {
    return toMat(new PMatrix3D(m));
  }

  public PMatrix2D toPMatrix2D(Mat m) {
    float[] a = m.getTransposed(new float[16]);
    return new PMatrix2D(a[0], a[1], a[3],
        a[4], a[5], a[7]);
  }
}
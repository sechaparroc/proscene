package basics;

import processing.core.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;
import remixlab.proscene.*;

/**
 * Created by pierre on 12/3/16.
 */
public class Geom extends PApplet {
  Scene scene;
  GenericFrame frame1, frame2;
  Quat q = new Quat();
  Mat m1 = new Mat();
  Mat m2 = new Mat();

  public void settings() {
    size(640, 360, P2D);  // your size() command here
  }

  public void setup() {
    scene = new Scene(this);
    frame1 = new GenericFrame(scene);
    frame1.translate(10, 20);
    frame2 = new GenericFrame(scene, frame1);
    frame2.translate(15, -20);
    q.fromAxisAngle(1,-1,1, QUARTER_PI);
    q.print();
    frame2.rotate(q);
    m2 = frame2.worldMatrix();
    m1.translate(10,-5,-8);
    Mat.multiply(m1, m2).print();
    Mat.multiply(m2, m1).print();
    m2.translate(10,-5,-8);
    m2.print();
  }

  public void keyPressed() {
    if (key == 'u') scene.flip();
  }

  public void draw() {
    background(0);
  }

  public static void main(String args[]) {
    PApplet.main(new String[]{"basics.Geom"});
  }
}

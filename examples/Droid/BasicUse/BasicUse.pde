import remixlab.proscene.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

import android.view.MotionEvent;

Scene scene;
float x,y,z;
Box [] boxes;

void setup() {
  //size(displayWidth, displayHeight, P3D);
  fullScreen(P3D, 1);
  boxes = new Box[10];
  scene = new Scene(this);
  for (int i = 0; i < boxes.length; i++)
    boxes[i] = new Box(scene);
  frameRate(100);
}

void draw() {
  background(0);
  lights();
  println(Scene.platform());
  scene.beginScreenDrawing();  
  text(frameRate, 5, 17);
  scene.endScreenDrawing();
  for (int i = 0; i < boxes.length; i++)      
    boxes[i].draw(); 
}

public boolean surfaceTouchEvent(MotionEvent event) {
  scene.droidTouchAgent().touchEvent(event);
  return true;
}
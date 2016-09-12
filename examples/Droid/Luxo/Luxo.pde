/**
 * Luxo.
 * by Jean Pierre Charalambos.
 * 
 * Android version of the Frame.Luxo example.
 *
 * This example requires the Processing Android Mode and an Android device.
 */

import remixlab.proscene.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.constraint.*;

import android.view.MotionEvent;

Scene scene;
Lamp lamp;

public void setup() {
  fullScreen(P3D, 1);
  scene = new Scene(this);  
  scene.setRadius(100);
  scene.showAll();
  scene.setGridVisualHint(false);
  lamp = new Lamp(scene);
  scene.setPickingVisualHint(true);
  // If picking is not too slow in your Android, comment to enable it
  scene.disablePickingBuffer();
}

public void draw() {
  background(0);
  lights();
  
  //draw the lamp
  scene.drawFrames();

  //draw the ground
  noStroke();
  fill(120, 120, 120);
  float nbPatches = 100;
  normal(0.0f, 0.0f, 1.0f);
  for (int j=0; j<nbPatches; ++j) {
    beginShape(QUAD_STRIP);
    for (int i=0; i<=nbPatches; ++i) {
      vertex((200*(float)i/nbPatches-100), (200*j/nbPatches-100));
      vertex((200*(float)i/nbPatches-100), (200*(float)(j+1)/nbPatches-100));
    }
    endShape();
  }
}

// Processing currently doesn't support registering Android MotionEvent. 
// This method thus needs to be declared.
public boolean surfaceTouchEvent(MotionEvent event) {
  scene.droidTouchAgent().touchEvent(event);
  return true;
}
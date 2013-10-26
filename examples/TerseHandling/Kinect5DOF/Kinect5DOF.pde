/**
 * Low High
 * by Miguel Parra and Pierre Charalambos.
 *
 * Doc to come...
 */

import processing.opengl.*;

import SimpleOpenNI.*;

import remixlab.proscene.*;
import remixlab.proscene.Scene.ProsceneKeyboard;
import remixlab.proscene.Scene.ProsceneMouse;
import remixlab.tersehandling.core.*;
import remixlab.tersehandling.generic.event.*;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.agent.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.core.Constants.*;

Scene scene;
HIDAgent agent;
Kinect kinect;
PVector kinectPos, kinectRot;
Box [] boxes;
boolean cameraMode = true;

void setup() {
  size(800, 600, P3D);
  scene = new Scene(this);
  kinect=new Kinect(this);

  scene.camera().setPosition(new Vec(250, 250, 250));
  scene.camera().lookAt(new Vec(0, 0, 0));

  agent = new HIDAgent(scene, "Kinect") {
    GenericDOF6Event<Constants.DOF6Action> event, prevEvent;
    @Override
    public GenericDOF6Event<Constants.DOF6Action> feed() {
      if (cameraMode) { //-> event is absolute
        setDefaultGrabber(scene.viewport().frame()); //set it by default
        disableTracking();
        scene.setFrameSelectionHintIsDrawn(false);
        event=new GenericDOF6Event<Constants.DOF6Action>(kinectPos.x, kinectPos.y, kinectPos.z, 0, kinectRot.y, kinectRot.z); 
      }
      else { //frame mode -> event is relative
        setDefaultGrabber(null);
        enableTracking();
        scene.setFrameSelectionHintIsDrawn(true);
        event = new GenericDOF6Event<Constants.DOF6Action>(prevEvent, kinect.posit.x, kinect.posit.y, 0,0,0,0);
        prevEvent = event.get();
        if(trackedGrabber() == null)
          updateGrabber(event); 
      }
      return event;
    }
  };  
  agent.setSensitivities(0.03, 0.03, 0.03, 0.00005, 0.00005, 0.00005);

  boxes = new Box[30];
  for (int i = 0; i < boxes.length; i++) {
    boxes[i] = new Box(scene);
    agent.addInPool(boxes[i].iFrame);
  }
}

void draw() {
  background(0);

  for (int i = 0; i < boxes.length; i++)      
    boxes[i].draw();

  //Update the Kinect data
  kinect.update();

  kinect.draw();

  //Get the translation and rotation vectors from Kinect
  kinectPos=kinect.deltaPositionVector();
  kinectRot=kinect.rotationVector();
}

void keyPressed() {
  cameraMode = !cameraMode;
}

void onNewUser(SimpleOpenNI curContext, int userId) {
  kinect.onNewUser(curContext, userId);
}


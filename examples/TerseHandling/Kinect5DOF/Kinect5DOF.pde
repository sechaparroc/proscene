/**
 * Space Navigator
 * by Miguel Alejandro Parra and Jean Pierre Charalambos.
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
HIDAgent hidAgent;
Kinect kinect;
PVector kinectPos, kinectRot;

void setup() {
  size(800, 600, P3D);
  scene = new Scene(this);
  kinect=new Kinect(this);

  scene.camera().setPosition(new Vec(250, 250, 250));
  scene.camera().lookAt(new Vec(0, 0, 0));

  hidAgent = new HIDAgent(scene, "Kinect") {
    @Override
    public GenericDOF6Event<Constants.DOF6Action> feed() {
      return new GenericDOF6Event<Constants.DOF6Action>(kinectPos.x, kinectPos.y, kinectPos.z/100, 
                                                        kinectRot.x, kinectRot.y, kinectRot.z, 0, 0);
    }
  };  
  hidAgent.setSensitivities(0.2, 0.2, 1.8, 0.0002, 0.0002, 0.0002);
}

void draw() {
  background(0);
  //Update the Kinect data
  kinect.update();

  kinect.draw();

  //Get the translation and rotation vectors from Kinect
  kinectPos=kinect.deltaPositionVector();
  kinectRot=kinect.rotationVector();
}

// SimpleOpenNI events
void onNewUser(SimpleOpenNI curContext, int userId) {
  kinect.onNewUser(curContext, userId);
}


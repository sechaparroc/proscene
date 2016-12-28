/**
 * Camera Interpolation.
 * by Jean Pierre Charalambos.
 *
 * This example (together with Frame Interpolation) illustrates the
 * KeyFrameInterpolator functionality.
 *
 * KeyFrameInterpolator smoothly interpolate its attached Camera Frames over time
 * on a path defined by Frames. The interpolation can be started/stopped/reset,
 * played in loop, played at a different speed, etc...
 *
 * In this example, a Camera path is defined by four InteractiveFrames
 * which are attached to the first Camera path (the Camera holds five such paths).
 * The Frames can be moved with the mouse, making the path editable. The Camera
 * interpolating path is played with the shortcut '1'.
 *
 * Press CONTROL + '1' to add (more) key frames to the Camera path.
 *
 * Press ALT + '1' to delete the Camera path.
 *
 * The displayed info are interactive. Click on them to see what happens.
 *
 * The Camera holds 3 KeyFrameInterpolators, binded to [1..3] keys. Pressing
 * CONTROL + [1..3] adds key frames to the specific path. Pressing ALT + [1..3]
 * deletes the specific path. Press 'r' to display all the key frame camera paths
 * (if any). The displayed paths are editable.
 *
 * Press 'h' to display the key shortcuts and mouse bindings in the console.
 */

import remixlab.proscene.*;

Scene scene;
Info toggleInfo;
ArrayList<Info> info;
PFont font;

void setup() {
  size(800, 800, P3D);
  scene = new Scene(this);
  unregisterMethod("dispose", scene);
  //create a camera path and add some key frames:
  //key frames can be added at runtime with keys [j..n]
  scene.loadConfig();
  font = loadFont("FreeSans-24.vlw");
  toggleInfo = new Info(new PVector(10, 7), font);
  info = new ArrayList<Info>();
  for (int i=0; i<3; ++i)
    info.add(new Info(new PVector(10, toggleInfo.height*(i+1) + 7*(i+2)), font, String.valueOf(i+1)));
}

void draw() {
  background(0);
  fill(204, 102, 0, 150);
  info();
  scene.drawTorusSolenoid();
}

void info() {
  toggleInfo.setText("Camera paths edition "
                     + ( scene.pathsVisualHint() ? "ON" : "OFF" )
                     + " (press 'r' to toggle it)");
  toggleInfo.display();
  for (int i = 0; i < info.size(); i++)
    // Check if CameraPathPlayer is still valid
    if (scene.eye().keyFrameInterpolator(i+1) != null) {
      info.get(i).setText("Path " + String.valueOf(i+1) + "( press " + String.valueOf(i+1) + " to" +
                           (scene.eye().keyFrameInterpolator(i+1).numberOfKeyFrames() > 1 ?
                           scene.eye().keyFrameInterpolator(i+1).interpolationStarted() ?
                           " stop it)" : " play it)"
                           : " restore init position)"));
      info.get(i).display();
    }
}
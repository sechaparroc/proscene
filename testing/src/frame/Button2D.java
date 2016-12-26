package frame;

import processing.core.*;
import remixlab.bias.event.*;
import remixlab.proscene.*;

/**
 * Created by pierre on 12/26/16.
 */
public class Button2D {
  Scene scene;
  InteractiveFrame iFrame;
  PVector position;
  String myText =  new String();
  PFont myFont;
  float myWidth;
  float myHeight;

  public Button2D(Scene scn, PVector p, PFont font, String t) {
    scene = scn;
    iFrame = new InteractiveFrame(scene);
    iFrame.removeBindings();
    iFrame.disablePickingHint();
    iFrame.setFrontShape(this, "display");
    iFrame.setPickingShape(this, "highlight");
    iFrame.setHighlightingMode(InteractiveFrame.HighlightingMode.FRONT_PICKING_SHAPES);
    setPosition(p);
    setFont(font);
    setText(t);
  }

  public void setPosition(PVector pos) {
    position = pos;
  }

  public void setFont(PFont font) {
    myFont = font;
    update();
  }

  public void setText(String text) {
    myText = text;
    update();
  }

  protected void update() {
    scene.pg().textAlign(PApplet.LEFT);
    scene.pg().textFont(myFont);
    myWidth = scene.pg().textWidth(myText);
    myHeight = scene.pg().textAscent() + scene.pg().textDescent();
  }

  public void display(PGraphics pg) {
    pg.pushStyle();
    pg.fill(255);
    scene.beginScreenDrawing(pg);
    scene.pg().textFont(myFont);
    pg.text(myText, position.x, position.y, myWidth+1, myHeight);
    scene.endScreenDrawing(pg);
    pg.popStyle();
  }

  public void highlight(PGraphics pg) {
    pg.noStroke();
    pg.fill(255, 0, 0, 126);
    scene.beginScreenDrawing(pg);
    pg.rect(position.x, position.y, myWidth, myHeight);
    scene.endScreenDrawing(pg);
  }
}

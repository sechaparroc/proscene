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

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;


import remixlab.bias.event.KeyboardEvent;
import remixlab.dandelion.core.AbstractScene;
import remixlab.dandelion.core.Camera;
import remixlab.dandelion.core.Eye;

import com.google.gwt.core.client.JavaScriptObject;

public class ExporterFacade {

  /*
	@Export("TargetScene")
	@ExportPackage("remixlab")
	 public abstract class GrabberSceneFacade implements ExportOverlay<TargetScene> {
		    // always stub static methods     
		public abstract void setPlatform();
	}	
  */

	/*
	
	@Export("BasicUse")
	@ExportPackage("")
	 public abstract static class BasicUseFacade implements ExportOverlay<BasicUse> {
		    // always stub static methods     
		public abstract void setup();
		public abstract void draw();

		@ExportConstructor
		public static BasicUse constructor(JavaScriptObject ctx)
		{	
			return new 	BasicUse(ctx);		
		}
	}	
	*/

  @Export("P5Scene")
  @ExportPackage("")
  @ExportClosure
  public abstract static class P5SceneFacade implements ExportOverlay<P5Scene> {
    // always stub static methods
    // public abstract void setup();
    public abstract void draw();

    public abstract void postDraw();

    public abstract void performInteraction(KeyboardEvent event);

    public abstract void subscribe(String key, JavaScriptObject obj);

    public abstract Camera eye();

    @ExportConstructor
    public static P5Scene constructor(JavaScriptObject ctx) {
      return new P5Scene(new P5JS(ctx));
    }
  }

  @Export("KeyboardEvent")
  @ExportPackage("")
  @ExportClosure
  public abstract static class KeyboardEventFacade implements ExportOverlay<KeyboardEvent> {
    // always stub static methods
    //public abstract void setup();
    //	public abstract int width();
    public abstract char key();
  }

  @Export("Camera")
  @ExportPackage("")
  @ExportClosure
  public abstract static class CameraFacade implements ExportOverlay<Camera> {
    // always stub static methods
    // public abstract void setup();
    //	public abstract int width();
    public abstract void interpolateToFitScene();

    public abstract void centerScene();

    public abstract void addKeyFrameToPath(int key);

    public abstract void deletePath(int key);

    public abstract void playPath(int key);
  }
}
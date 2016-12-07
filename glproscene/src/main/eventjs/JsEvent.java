/**************************************************************************************
 * ProScene_JS (version 3.0.0)
 * Copyright (c) 2014-2016 National University of Colombia, https://github.com/remixlab
 * @author Cesar Colorado, https://github.com/orgs/remixlab/people/cacolorador
 *
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 **************************************************************************************/

package main.eventjs;

import remixlab.util.Copyable;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The Class JsEvent.
 * Encapsulates a native javascript event,
 * is the javascript version of the class
 * remixlab.bias.event.TerseEvent
 */
public abstract class JsEvent implements Copyable {

  /**
   * The canvas.
   */
  protected JavaScriptObject canvas;

  /**
   * The event.
   */
  protected JavaScriptObject event;

  /**
   * The action.
   */
  private int action;

  /**
   * The modifiers.
   */
  protected int modifiers;

  /**
   * The action.
   */
  private short which;

  /**
   * Instantiates a new js event.
   *
   * @param event the event
   */

//	public JsEvent(JavaScriptObject event) {
//		this.event = event;
//
//	}
  public JsEvent(JavaScriptObject event, short which) {
    this.event = event;
    this.which = which;
    //PApplet.log(Integer.toString(this.which ));
  }

  /**
   * Process action.
   */
  protected abstract void processAction();

  /**
   * Gets the action.
   *
   * @return the action
   */
  public int getAction() {

    return action;
  }

  /**
   * Sets the action.
   *
   * @param action the new action
   */
  protected void setAction(int action) {
    this.action = action;

  }

  /**
   * Gets the modifiers.
   *
   * @return the modifiers
   */
  public int getModifiers() {
    return modifiers;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public native String getType()
  /*-{

      var event = this.@main.eventjs.JsEvent::event;

      return event.type;

  }-*/;

  /**
   * Gets the button.
   *
   * @return the button
   */

  public short getButton() {
    short retValue = 0;
    switch (this.which) {
      case 1:
        retValue = 37;
        break;
      case 2:
        retValue = 3;
        break;
      case 3:
        retValue = 39;
        break;
    }
    return retValue;
  }
//	public native short getButton()
//	/*-{
//									
//		var retValue;
//		
//		var e = this.@processing.eventjs.JsEvent::event;
//					
//		switch (e.which) {
//		case 1:
//		retValue = 37;
//		break;
//		case 2:
//		retValue = 3;
//		break;
//		case 3:
//		retValue = 39;
//		break;
//		}
//		
//		return retValue;
//	
//	}-*/;

  /**
   * Checks if is control down.
   *
   * @return true, if is control down
   */
  public native boolean isControlDown()
	/*-{
      var event = this.@main.eventjs.JsEvent::event;
      return event.ctrlKey;

  }-*/;

  /**
   * Checks if is shift down.
   *
   * @return true, if is shift down
   */
  public native boolean isShiftDown()
	/*-{
      var event = this.@main.eventjs.JsEvent::event;
      return event.shiftKey;

  }-*/;

  /**
   * Checks if is alt down.
   *
   * @return true, if is alt down
   */
  public native boolean isAltDown() /*-{
      var event = this.@main.eventjs.JsEvent::event;
      return event.altKey;

  }-*/;

  /**
   * Checks if is meta down.
   *
   * @return true, if is meta down
   */
  public native boolean isMetaDown() /*-{
      var event = this.@main.eventjs.JsEvent::event;
      return event.metaKey;

  }-*/;

  protected native JavaScriptObject getJsEvent()/*-{
      return this.@main.eventjs.JsEvent::event;
  }-*/;


  public native short WheelDelta() /*-{
      var event = this.@main.eventjs.JsEvent::event;
      return -1 * (Math.max(-1, Math.min(1, (event.wheelDelta || -event.detail))));
  }-*/;


  private native String getWhichfromCharCode() /*-{

//		var event = this.@processing.eventjs.JsEvent::getJsEvent();
//
//		console.log("event.type->"+String(event.type));			
//		
//		
//		  if (event.which == null) {			  				  				  	
//		    return String.fromCharCode(event.keyCode) // IE
//		  } else if (event.which!=0 && event.charCode!=0) {
//		    return String.fromCharCode(event.which)   // the rest
//		  } else {
//		    return null // special key
//		  }

      var which = this.@main.eventjs.JsEvent::which;

      //console.log(String.fromCharCode(which) );

      return String.fromCharCode(which);

  }-*/;


//		private native String getWhich() /*-{
//		
//			var event = this.@processing.eventjs.JsEvent::event;
//	 		//console.log(String.fromCharCode(event.which) );
//			
//			  if (event.which == null) {
//			    return String.fromCharCode(event.keyCode) // IE
//			  }
//			   else if (event.which!=0
//			   // && event.charCode!=0
//			   ) 
//			  {	
//			 
//			    return String.fromCharCode(event.which)   // the rest
//			  } else {
//			    return null // special key
//			  }
//			
//
//
//		}-*/;		

  public char getKey() {
    String retValue = this.getWhichfromCharCode();
    //PApplet.log(retValue);
    if (retValue != null && retValue.length() > 0)

      return retValue.charAt(0);

    else
      return ' ';
  }


  public native int getCharCode() /*-{
      var event = this.@main.eventjs.JsEvent::event;
      return event.charCode;

  }-*/;


  public native int getKeyCode() /*-{
      var event = this.@main.eventjs.JsEvent::event;

//		  if (event.which == null) {
//		    return event.keyCode // IE
//		  } else if (event.which!=0 
//		  //&& event.charCode!=0
//		  ) {
//		    return event.which   // the rest
//		  } else {
//		    return -1 // special key
//		  }

      return event.keyCode;

  }-*/;


  public boolean isRightMouseButton() {
    if (this.which == 3)
      return true;
    else
      return false;

  }


//	public native boolean isRightMouseButton()
//	/*-{
//											
//		var e = this.@processing.eventjs.JsEvent::event;
//					
//		
//		if(e.which == 39)
//			return true;
//		else			
//			return false;
//	
//	}-*/;

}

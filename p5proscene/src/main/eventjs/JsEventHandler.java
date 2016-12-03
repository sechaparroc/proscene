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

import com.google.gwt.core.client.JavaScriptObject;

public class JsEventHandler {

  public JsEventHandler() {
    init();
  }

  public native void init() /*-{
      $wnd.isMousePressed = false;

      $doc.body.onmousedown = function (e) {
          $wnd.isMousePressed = true;

          $wnd.mouseWhich = e.which;

      }
      $doc.body.onmouseup = function (e) {
          $wnd.isMousePressed = false;
          $wnd.mouseWhich = 0;
      }


//			$doc.body.onkeypress = function(e) { 			   
//			    $wnd.keyWhich = e.which;
//			}

  }-*/;


  public native Boolean isMousePressed() /*-{

      if (typeof $wnd.isMousePressed != 'undefined')
          return $wnd.isMousePressed;
      else
          return false;


  }-*/;


  public native void addMouseAgent(JavaScriptObject canvas, Object agent, String methodName)/*-{

      var eventHandler = function (event) {

          var type = event.type;


          var isMousePressed = false;

          if (typeof $wnd.isMousePressed != 'undefined')
              isMousePressed = $wnd.isMousePressed;


          var mouseWhich = $wnd.mouseWhich;

          //console.log("type:"+event.type);
          //console.log("event which:"+event.which);
          //console.log("win which:"+$wnd.which);
          //event.which = $wnd.which;
          //console.log("new event which:"+event.which);


          // new JsMouseEvent with the native event
          var JsMouseEvent =

              @main.eventjs.JsMouseEvent::new(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;ZS)
              (event, canvas, isMousePressed, mouseWhich);

          //@processing.eventjs.JsMouseEvent::new(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Z)
          //(event,canvas, isMousePressed);


          //pass to the agent the JsMouseEvent to run
          //	@main.eventjs.Reflect::ExecuteEvent(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)
          //(agent,methodName, JsMouseEvent);
          @main.eventjs.Reflect::ExecuteEvent(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)
          (agent, methodName, JsMouseEvent);
          //

          //event.stopPropagation();
          //event.preventDefault();

      }


      //listeners
      canvas.addEventListener('mousemove', eventHandler, false);
      canvas.addEventListener('click', eventHandler, false);
      canvas.addEventListener('ondblclick', eventHandler, false);
      canvas.addEventListener('mousedown', eventHandler, false);
      canvas.addEventListener('mouseup', eventHandler, false);
      canvas.addEventListener('mouseout', eventHandler, false);
      canvas.addEventListener('mouseover', eventHandler, false);
      // IE9, Chrome, Safari, Opera
      canvas.addEventListener("mousewheel", eventHandler, false);
      // Firefox
      canvas.addEventListener("DOMMouseScroll", eventHandler, false);


  }-*/;

  public native void addKeyAgent(JavaScriptObject canvas, Object agent, String methodName)/*-{


      var eventHandler = function (event) {

          var type = event.type;


          var keyWhich = event.which;
          //	console.log("event which:"+event.which);

          // new JsMouseEvent with the native event
          var JsKeyEvent =
              @main.eventjs.JsKeyEvent::new(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;S)
              (event, canvas, keyWhich);
          //@processing.eventjs.JsKeyEvent::new(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)
          //(event,canvas);


          @main.eventjs.Reflect::ExecuteEvent(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)
          (agent, methodName, JsKeyEvent);


      }

      //listeners
      //canvas.addEventListener('keydown',eventHandler,false);
      //canvas.addEventListener('keypress',eventHandler,false);
      //canvas.addEventListener('keyup',eventHandler,false);

      $wnd.addEventListener('keydown', eventHandler, false);
      $wnd.addEventListener('keypress', eventHandler, false);
      $wnd.addEventListener('keyup', eventHandler, false);

      //console.log(canvas);

  }-*/;
}

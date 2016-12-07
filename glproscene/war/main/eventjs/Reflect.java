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

import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.TypeOracle;

public class Reflect {
  /**
   * calls the method by reflection.
   *
   * @param agt        the anonymous agent to call the event with reflection
   * @param methodName the method name
   * @param param      the JsMouseEvent
   */
  public static void ExecuteEvent(Object agt, String methodName, Object param) {

    ClassType<? extends Object> classType = TypeOracle.Instance.getClassType(agt.getClass());
    classType.invoke(agt, methodName, param);
  }

  /**
   * calls the method by reflection.
   *
   * @param agt        the anonymous agent to call the event with reflection
   * @param methodName the method name
   */
  public static void ExecuteEvent(Object agt, String methodName) {

    ClassType<? extends Object> classType = TypeOracle.Instance.getClassType(agt.getClass());
    classType.invoke(agt, methodName);
  }
}

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

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class P5Proscene implements EntryPoint {
  @Override
  public void onModuleLoad() {
    ExporterUtil.exportAll();
    onLoadImpl();
  }

  /**
   * On load impl in html.
   */
  private native void onLoadImpl() /*-{
      if ($wnd.gwtOnLoad && typeof $wnd.gwtOnLoad == 'function') $wnd.gwtOnLoad();
  }-*/;
}

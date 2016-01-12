/**************************************************************************************
 * ProScene (version 3.0.0)
 * Copyright (c) 2014-2016 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 * 
 * All rights reserved. Library that eases the creation of interactive scenes
 * in Processing, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 **************************************************************************************/

package remixlab.proscene;

import remixlab.bias.core.*;
import remixlab.bias.event.*;
import remixlab.bias.ext.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.core.AbstractScene.Platform;
import remixlab.dandelion.geom.Frame;
import remixlab.util.*;

class GenericP5Frame extends GenericFrame {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
				appendSuper(super.hashCode()).
				append(profile).
				toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		GenericP5Frame other = (GenericP5Frame) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(profile, other.profile)
				.isEquals();
	}
	
	@Override
	public Scene scene() {
		return (Scene)gScene;
	}
	
	protected Profile profile;
	
	public GenericP5Frame(Scene scn) {
		super(scn);
		setProfile(new Profile(this));
		//TODO
		if(Scene.platform() == Platform.PROCESSING_DESKTOP)
			setDefaultMouseBindings();
		// else
		    // setDefaultTouchBindings();
		setDefaultKeyBindings();
	}
	
	public GenericP5Frame(Scene scn, Frame referenceFrame) {
		super(scn, referenceFrame);
		setProfile(new Profile(this));
		if(referenceFrame instanceof GenericP5Frame)
			this.profile.from(((GenericP5Frame)referenceFrame).profile);
		else {
			//TODO
			if(Scene.platform() == Platform.PROCESSING_DESKTOP)
				setDefaultMouseBindings();
			// else
			    // setDefaultTouchBindings();
			setDefaultKeyBindings();
		}
	}
	
	public GenericP5Frame(Eye eye) {
		super(eye);
		setProfile(new Profile(this));
		//TODO
		if(Scene.platform() == Platform.PROCESSING_DESKTOP)
			setDefaultMouseBindings();
		// else
		    // setDefaultTouchBindings();
		setDefaultKeyBindings();
	}
	
	protected GenericP5Frame(GenericP5Frame otherFrame) {
		super(otherFrame);
		setProfile(new Profile(this));
		this.profile.from(otherFrame.profile);
	}
	
	@Override
	public GenericP5Frame get() {
		return new GenericP5Frame(this);
	}
	
	@Override
	protected boolean checkIfGrabsInput(KeyboardEvent event) {
		return profile.hasBinding(event.shortcut());
	}
	
	@Override
	public void performInteraction(BogusEvent event) {
		if (!bypassKey(event))
			profile.handle(event);
	}
	
	public void removeBindings() {
		profile.removeBindings();
	}
	
	public String action(Shortcut key) {
		return profile.action(key);
	}
	
	public boolean isActionBound(String action) {
		return profile.isActionBound(action);
	}
	
	public void setDefaultMouseBindings() {
		scene().mouseAgent().setDefaultBindings(this);
	}
	
	//TODO restore me
	/*
	public void setDefaultTouchBindings() {
		scene().touchAgent().setDefaultBindings(this);
	}
	*/
	
	public void setDefaultKeyBindings() {
		removeKeyBindings();
		setKeyBinding('n', "align");
		setKeyBinding('c', "center");
		setKeyBinding(KeyAgent.LEFT_KEY, "translateXNeg");
		setKeyBinding(KeyAgent.RIGHT_KEY, "translateXPos");
		setKeyBinding(KeyAgent.DOWN_KEY, "translateYNeg");
		setKeyBinding(KeyAgent.UP_KEY, "translateYPos");
		setKeyBinding(BogusEvent.SHIFT, KeyAgent.LEFT_KEY, "rotateXNeg");
		setKeyBinding(BogusEvent.SHIFT, KeyAgent.RIGHT_KEY, "rotateXPos");
		setKeyBinding(BogusEvent.SHIFT, KeyAgent.DOWN_KEY, "rotateYNeg");
		setKeyBinding(BogusEvent.SHIFT, KeyAgent.UP_KEY, "rotateYPos");	
		setKeyBinding('z', "rotateZNeg");
		setKeyBinding('Z', "rotateZPos");
	}

	// good for all dofs :P
	
	public void setMotionBinding(int id, String action) {
		profile.setBinding(new MotionShortcut(id), action);
	}
	
	public void setMotionBinding(Object object, int id, String action) {
		profile.setBinding(object, new MotionShortcut(id), action);
	}
	
	public void removeMotionBindings() {
		profile.removeBindings(MotionShortcut.class);
	}
	
	public boolean hasMotionBinding(int id) {
		return profile.hasBinding(new MotionShortcut(id));
	}
	
	public void removeMotionBinding(int id) {
		profile.removeBinding(new MotionShortcut(id));
	}
	
	public void removeMotionBindings(int [] ids) {
		for(int i=0; i< ids.length; i++)
			removeMotionBinding(ids[i]);
	}
	
	// Key
	
	public void setKeyBinding(int vkey, String action) {
		profile.setBinding(new KeyboardShortcut(vkey), action);
	}
	
	public void setKeyBinding(char key, String action) {
		profile.setBinding(new KeyboardShortcut(key), action);
	}
	
	public void setKeyBinding(Object object, int vkey, String action) {
		profile.setBinding(object, new KeyboardShortcut(vkey), action);
	}
	
	public void setKeyBinding(Object object, char key, String action) {
		profile.setBinding(object, new KeyboardShortcut(key), action);
	}
	
	public boolean hasKeyBinding(int vkey) {
		return profile.hasBinding(new KeyboardShortcut(vkey));
	}
	
	public boolean hasKeyBinding(char key) {
		return profile.hasBinding(new KeyboardShortcut(key));
	}
	
	public void removeKeyBinding(int vkey) {
		profile.removeBinding(new KeyboardShortcut(vkey));
	}
	
	public void removeKeyBinding(char key) {
		profile.removeBinding(new KeyboardShortcut(key));
	}
	
	public void setKeyBinding(int mask, int vkey, String action) {
		profile.setBinding(new KeyboardShortcut(mask, vkey), action);
	}
	
	public void setKeyBinding(Object object, int mask, int vkey, String action) {
		profile.setBinding(object, new KeyboardShortcut(mask, vkey), action);
	}
	
	public boolean hasKeyBinding(int mask, int vkey) {
		return profile.hasBinding(new KeyboardShortcut(mask, vkey));
	}
	
	public void removeKeyBinding(int mask, int vkey) {
		profile.removeBinding(new KeyboardShortcut(mask, vkey));
	}
	
	public void setKeyBinding(int mask, char key, String action) {
		setKeyBinding(mask, KeyAgent.keyCode(key), action);
	}
	
	public void setKeyBinding(Object object, int mask, char key, String action) {
		setKeyBinding(object, mask, KeyAgent.keyCode(key), action);
	}
	
	public boolean hasKeyBinding(int mask, char key) {
		return hasKeyBinding(mask, KeyAgent.keyCode(key));
	}
	
	public void removeKeyBinding(int mask, char key) {
		removeKeyBinding(mask, KeyAgent.keyCode(key));
	}
	
	public void removeKeyBindings() {
		profile.removeBindings(KeyboardShortcut.class);
	}
	
	// click
	
	public void setClickBinding(int id, int count, String action) {
		if(count > 0 && count < 4)
			profile.setBinding(new ClickShortcut(id, count), action);
		else
			System.out.println("Warning no click binding set! Count should be between 1 and 3");
	}
	
	public void setClickBinding(Object object, int id, int count, String action) {
		if(count > 0 && count < 4)
			profile.setBinding(object, new ClickShortcut(id, count), action);
		else
			System.out.println("Warning no click binding set! Count should be between 1 and 3");
	}
	
	public boolean hasClickBinding(int id, int count) {
		return profile.hasBinding(new ClickShortcut(id, count));
	}
	
	public void removeClickBinding(int id, int count) {
		profile.removeBinding(new ClickShortcut(id, count));
	}
	
	public void removeClickBindings() {
		profile.removeBindings(ClickShortcut.class);
	}
	
	public void removeClickBindings(int [] ids, int count) {
		for(int i=0; i<ids.length; i++)
			removeClickBinding(ids[i], count);
	}
	
	public Profile profile() {
		return profile;
	}
	
	public void setProfile(Profile p) {
		if( p.grabber() == this )
			profile = p;
		else
			System.out.println("Nothing done, profile grabber is different than this grabber");
	}
	
	public void setBindings(GenericP5Frame otherFrame) {
		profile.from(otherFrame.profile());
	}
	
	public String info() {
		String result = new String();
		String info = profile().info(KeyboardShortcut.class);
		if(!info.isEmpty()) {
			result = "Key bindings:\n";
			result += Scene.parseKeyInfo(info);
		}
		info = profile().info(MotionShortcut.class);
		if(!info.isEmpty()) {
			result += "Motion bindings:\n";
			result += Scene.parseInfo(info);
		}
		info = profile().info(ClickShortcut.class);
		if(!info.isEmpty()) {
			result += "Click bindings:\n";
			result += Scene.parseInfo(info);
		}
		return result;
	}
	
	// dandelion <-> Processing
	
	
}
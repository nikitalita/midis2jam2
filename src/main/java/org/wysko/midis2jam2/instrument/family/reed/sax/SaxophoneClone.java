/*
 * Copyright (C) 2021 Jacob Wysko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
 */

package org.wysko.midis2jam2.instrument.family.reed.sax;

import com.jme3.math.Quaternion;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.instrument.clone.UpAndDownKeyClone;

import static org.wysko.midis2jam2.Midis2jam2.rad;

/**
 * Shared code for sax clones.
 */
public abstract class SaxophoneClone extends UpAndDownKeyClone {
	
	/**
	 * The number of keys on a saxophone.
	 */
	private final static int NUMBER_OF_KEYS = 20;
	
	/**
	 * The amount to rotate the sax by when playing.
	 */
	private final static float ROTATION_FACTOR = 0.1f;
	
	/**
	 * Instantiates a new Saxophone clone.
	 *
	 * @param parent        the parent
	 * @param stretchFactor the stretch factor
	 */
	public SaxophoneClone(Saxophone parent, float stretchFactor) {
		super(NUMBER_OF_KEYS, parent, ROTATION_FACTOR, stretchFactor);
		
		for (int i = 0; i < keyCount; i++) {
			keysUp[i] = parent.context.loadModel(String.format("AltoSaxKeyUp%d.obj", i),
					"HornSkinGrey.bmp", Midis2jam2.MatType.REFLECTIVE, 0.9f);
			
			keysDown[i] = parent.context.loadModel(String.format("AltoSaxKeyDown%d.obj", i),
					"HornSkinGrey.bmp", Midis2jam2.MatType.REFLECTIVE, 0.9f);
		}
		
		attachKeys();
	}
	
	@Override
	protected void moveForPolyphony() {
		offsetNode.setLocalRotation(new Quaternion().fromAngles(0, rad(25 * indexForMoving()), 0));
	}
}

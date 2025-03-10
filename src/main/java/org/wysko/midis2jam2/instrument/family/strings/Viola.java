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

package org.wysko.midis2jam2.instrument.family.strings;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.midi.MidiChannelSpecificEvent;

import java.util.List;

import static org.wysko.midis2jam2.Midis2jam2.rad;

/**
 * The Viola.
 */
public class Viola extends StringFamilyInstrument {
	
	/**
	 * Instantiates a new Viola.
	 *
	 * @param context the context
	 * @param events  the events
	 */
	public Viola(Midis2jam2 context, List<MidiChannelSpecificEvent> events) {
		super(context,
				events,
				true,
				180,
				new Vector3f(1, 1, 1),
				new int[] {48, 55, 62, 69},
				48,
				105,
				context.loadModel("Violin.obj", "ViolaSkin.bmp")
		);
		
		highestLevel.setLocalTranslation(-2, 27, -15);
		highestLevel.attachChild(instrumentNode);
		
		instrumentNode.setLocalScale(1f);
		instrumentNode.setLocalRotation(new Quaternion().fromAngles(rad(-130), rad(-174), rad(-28.1)));
	}
	
	@Override
	protected void moveForMultiChannel() {
		offsetNode.setLocalTranslation(20 * indexForMoving(), 0, 0);
	}
}

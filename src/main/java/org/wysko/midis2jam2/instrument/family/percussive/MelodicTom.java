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

package org.wysko.midis2jam2.instrument.family.percussive;

import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.jetbrains.annotations.NotNull;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.midi.MidiChannelSpecificEvent;

import java.util.List;

import static org.wysko.midis2jam2.Midis2jam2.rad;

/**
 * The Melodic tom.
 */
public class MelodicTom extends OneDrumOctave {
	
	/**
	 * @param context   the context to the main class
	 * @param eventList the event list
	 */
	public MelodicTom(@NotNull Midis2jam2 context,
	                  @NotNull List<MidiChannelSpecificEvent> eventList) {
		super(context, eventList);
		Spatial drum = context.loadModel("MelodicTom.obj", "DrumShell_MelodicTom.bmp");
		
		for (int i = 0; i < 12; i++) {
			malletNodes[i] = new Node();
			Spatial mallet = context.loadModel("DrumSet_Stick.obj", "StickSkin.bmp");
			malletNodes[i].attachChild(mallet);
			malletNodes[i].setLocalTranslation(1.8f * (i - 5.5f), 0, 15);
			mallet.setLocalTranslation(0, 0, -5);
			animNode.attachChild(malletNodes[i]);
		}
		
		drum.setLocalRotation(new Quaternion().fromAngles(rad(126 - 90), 0, 0));
		
		animNode.attachChild(drum);
		instrumentNode.attachChild(animNode);
		instrumentNode.setLocalTranslation(0, 61.1f, -133.8f);
	}
	
	@Override
	protected void moveForMultiChannel() {
		highestLevel.setLocalRotation(new Quaternion().fromAngles(0, rad(-26.3 + indexForMoving() * -15), 0));
	}
}

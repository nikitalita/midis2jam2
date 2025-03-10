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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.jetbrains.annotations.NotNull;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.instrument.family.percussion.drumset.PercussionInstrument;
import org.wysko.midis2jam2.midi.MidiChannelSpecificEvent;

import java.util.List;
import java.util.stream.IntStream;

import static com.jme3.math.FastMath.HALF_PI;
import static org.wysko.midis2jam2.Midis2jam2.rad;

/**
 * The Woodblocks.
 */
public class Woodblocks extends TwelveDrumOctave {
	
	/**
	 * The Wood block nodes.
	 */
	final Node[] woodBlockNodes = new Node[12];
	
	/**
	 * @param context   the context to the main class
	 * @param eventList the event list
	 */
	public Woodblocks(@NotNull Midis2jam2 context,
	                  @NotNull List<MidiChannelSpecificEvent> eventList) {
		super(context, eventList);
		
		IntStream.range(0, 12).forEach(i -> woodBlockNodes[i] = new Node());
		
		
		for (int i = 0; i < 12; i++) {
			malletNodes[i] = new Node();
			Spatial child = context.loadModel("DrumSet_Stick.obj", "StickSkin.bmp");
			child.setLocalTranslation(0, 0, -5);
			malletNodes[i].setLocalTranslation(0, 0, 18);
			malletNodes[i].attachChild(child);
			Node oneBlock = new Node();
			oneBlock.attachChild(malletNodes[i]);
			Woodblock woodblock = new Woodblock(i);
			twelfths[i] = woodblock;
			oneBlock.attachChild(woodblock.highestLevel);
			woodBlockNodes[i].attachChild(oneBlock);
			oneBlock.setLocalTranslation(0, 0, 20);
			woodBlockNodes[i].setLocalRotation(new Quaternion().fromAngles(0, rad(7.5 * i), 0));
			woodBlockNodes[i].setLocalTranslation(0, 0.3f * i, 0);
			instrumentNode.attachChild(woodBlockNodes[i]);
		}
		
		instrumentNode.setLocalTranslation(75, 0, -35);
		
	}
	
	@Override
	public void tick(double time, float delta) {
		super.tick(time, delta);
		for (TwelfthOfOctaveDecayed woodblock : twelfths) {
			woodblock.tick(time, delta);
		}
	}
	
	@Override
	protected void moveForMultiChannel() {
		offsetNode.setLocalTranslation(0, 15 + 3.6f * indexForMoving(), 0);
		instrumentNode.setLocalRotation(new Quaternion().fromAngles(0, -HALF_PI + HALF_PI * indexForMoving(), 0));
	}
	
	/**
	 * A single Woodblock.
	 */
	public class Woodblock extends TwelveDrumOctave.TwelfthOfOctaveDecayed {
		
		/**
		 * Instantiates a new Woodblock.
		 *
		 * @param i the index of this woodblock
		 */
		public Woodblock(int i) {
			Spatial mesh = context.loadModel("WoodBlockSingle.obj", "SimpleWood.bmp");
			mesh.setLocalScale(1 - 0.036363636f * i);
			animNode.attachChild(mesh);
		}
		
		@Override
		public void tick(double time, float delta) {
			Vector3f localTranslation = highestLevel.getLocalTranslation();
			if (localTranslation.y < -0.0001) {
				highestLevel.setLocalTranslation(0, Math.min(0, localTranslation.y + (PercussionInstrument.DRUM_RECOIL_COMEBACK * delta)), 0);
			} else {
				highestLevel.setLocalTranslation(0, 0, 0);
			}
		}
	}
}

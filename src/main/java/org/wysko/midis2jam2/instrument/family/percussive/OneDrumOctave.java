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

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.instrument.DecayedInstrument;
import org.wysko.midis2jam2.instrument.family.percussion.drumset.PercussionInstrument;
import org.wysko.midis2jam2.midi.MidiChannelSpecificEvent;
import org.wysko.midis2jam2.midi.MidiNoteOnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A drum that is hit at different spots to represent the notes in an octave.
 */
public abstract class OneDrumOctave extends DecayedInstrument {
	
	/**
	 * The Anim node.
	 */
	protected final Node animNode = new Node();
	
	/**
	 * The Mallet nodes.
	 */
	@NotNull
	protected final Node[] malletNodes;
	
	/**
	 * The Mallet strikes.
	 */
	protected final ArrayList<MidiNoteOnEvent>[] malletStrikes;
	
	/**
	 * @param context   the context to the main class
	 * @param eventList the event list
	 */
	@SuppressWarnings("unchecked")
	protected OneDrumOctave(@NotNull Midis2jam2 context,
	                        @NotNull List<MidiChannelSpecificEvent> eventList) {
		super(context, eventList);
		malletNodes = new Node[12];
		malletStrikes = new ArrayList[12];
		for (int i = 0; i < 12; i++) {
			malletStrikes[i] = new ArrayList<>();
		}
		List<MidiNoteOnEvent> collect =
				eventList.stream().filter(e -> e instanceof MidiNoteOnEvent).map(e -> ((MidiNoteOnEvent) e)).collect(Collectors.toList());
		for (MidiNoteOnEvent noteOn : collect) {
			malletStrikes[(noteOn.note + 3) % 12].add(noteOn);
		}
	}
	
	@Override
	public void tick(double time, float delta) {
		super.tick(time, delta);
		for (int i = 0; i < 12; i++) {
			Stick.StickStatus stickStatus = Stick.handleStick(context, malletNodes[i], time, delta, malletStrikes[i], 3, 50);
			if (stickStatus.justStruck()) {
				animNode.setLocalTranslation(0, -3, 0);
			}
		}
		Vector3f localTranslation = animNode.getLocalTranslation();
		if (localTranslation.y < -0.0001) {
			animNode.setLocalTranslation(0, Math.min(0,
					localTranslation.y + (PercussionInstrument.DRUM_RECOIL_COMEBACK * delta)), 0);
		} else {
			animNode.setLocalTranslation(0, 0, 0);
		}
	}
}

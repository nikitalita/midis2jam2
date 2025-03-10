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

package org.wysko.midis2jam2.instrument.family.percussion.drumset;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.midi.MidiNoteOnEvent;

import java.util.List;

/**
 * A drum that is hit with a stick.
 */
public abstract class StickDrum extends SingleStickInstrument {
	
	/**
	 * How far the drum should travel when hit.
	 */
	public static final float RECOIL_DISTANCE = -2f;
	
	/**
	 * Attach {@link #drum} and {@link #stick} to this and move this for recoil.
	 */
	protected final Node recoilNode = new Node();
	
	/**
	 * The Drum.
	 */
	protected Spatial drum;
	
	protected StickDrum(Midis2jam2 context, List<MidiNoteOnEvent> hits) {
		super(context, hits);
	}
	
	/**
	 * Handles animation and note handling for the drum recoil.
	 *
	 * @param time  the current time
	 * @param delta the amount of time since the last frame update
	 */
	protected void drumRecoil(double time, float delta) {
		MidiNoteOnEvent recoil = null;
		while (!hits.isEmpty() && context.file.eventInSeconds(hits.get(0)) <= time) {
			recoil = hits.remove(0);
		}
		PercussionInstrument.recoilDrum(drum, recoil != null, recoil != null ? recoil.velocity : 0, delta);
	}
	
}

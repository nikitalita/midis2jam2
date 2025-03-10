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

package org.wysko.midis2jam2.instrument.family.piano;

import com.jme3.scene.Spatial;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.instrument.Instrument;
import org.wysko.midis2jam2.instrument.SustainedInstrument;
import org.wysko.midis2jam2.midi.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Any instrument that visualizes notes by rotating piano keys.
 */
public abstract class KeyedInstrument extends Instrument {
	
	/**
	 * The lowest note this instrument can play.
	 */
	protected final int rangeLow;
	
	/**
	 * The highest note this instrument can play.
	 */
	protected final int rangeHigh;
	
	/**
	 * The events associated with this instrument.
	 */
	@NotNull
	final protected List<MidiNoteEvent> events;
	
	/**
	 * The keys of this instrument.
	 */
	protected final Key[] keys;
	
	/**
	 * The Note periods to play.
	 */
	@Unmodifiable
	@NotNull
	private final List<NotePeriod> notePeriods;
	
	
	/**
	 * Instantiates a new Keyed instrument.
	 *
	 * @param context   the context
	 * @param eventList the event list
	 * @param rangeLow  the lowest note this instrument can play
	 * @param rangeHigh the highest note this instrument can play
	 */
	protected KeyedInstrument(@NotNull Midis2jam2 context,
	                          @NotNull List<MidiChannelSpecificEvent> eventList,
	                          int rangeLow,
	                          int rangeHigh) {
		super(context);
		this.events = SustainedInstrument.scrapeMidiNoteEvents(eventList);
		this.rangeLow = rangeLow;
		this.rangeHigh = rangeHigh;
		this.keys = new Key[keyCount()];
		this.notePeriods = Collections.unmodifiableList(calculateNotePeriods(this.events));
	}
	
	/**
	 * Calculates if a MIDI note value is a black or white key on a standard piano.
	 *
	 * @param x the MIDI note value
	 * @return {@link KeyColor#WHITE} or {@link KeyColor#BLACK}
	 */
	@Contract(pure = true)
	@NotNull
	public static KeyColor midiValueToColor(int x) {
		int note = x % 12;
		return note == 1 || note == 3 || note == 6 || note == 8 || note == 10 ? KeyColor.BLACK : KeyColor.WHITE;
	}
	
	@Override
	public void tick(double time, float delta) {
		setIdleVisibilityByNoteOnAndOff(time);
		moveForMultiChannel();
		List<MidiNoteEvent> eventsToPerform = new ArrayList<>();
		if (!events.isEmpty()) {
			if (!(events.get(0) instanceof MidiNoteOnEvent) && !(events.get(0) instanceof MidiNoteOffEvent)) {
				events.remove(0);
			}
			while (!events.isEmpty() && ((events.get(0) instanceof MidiNoteOnEvent && context.file.eventInSeconds(events.get(0)) <= time) ||
					(events.get(0) instanceof MidiNoteOffEvent && context.file.eventInSeconds(events.get(0)) - time <= 0.05))) {
				eventsToPerform.add(events.remove(0));
			}
		}
		
		for (MidiNoteEvent event : eventsToPerform) {
			Key key = keyByMidiNote(event.note);
			if (key == null) continue;
			if (event instanceof MidiNoteOnEvent) {
				key.setBeingPressed(true);
			} else if (event instanceof MidiNoteOffEvent) {
				key.setBeingPressed(false);
			}
		}
		
		for (Key key : keys) {
			key.tick(delta);
		}
	}
	
	/**
	 * Returns the number of keys on this instrument.
	 *
	 * @return the number of keys on this instrument
	 */
	@Contract(pure = true)
	public int keyCount() {
		return (rangeHigh - rangeLow) + 1;
	}
	
	/**
	 * Returns the key associated with the MIDI note.
	 *
	 * @param midiNote the midi note
	 * @return the key
	 */
	@Nullable
	protected abstract Key keyByMidiNote(int midiNote);
	
	
	/**
	 * Sets idle visibility by note on and off events.
	 *
	 * @param time the current time
	 */
	protected void setIdleVisibilityByNoteOnAndOff(double time) {
		boolean b = SustainedInstrument.calcVisibility(time, notePeriods);
		visible = b;
		instrumentNode.setCullHint(b ? Spatial.CullHint.Dynamic : Spatial.CullHint.Always);
	}
	
	/**
	 * Keyboards have two different colored keys: white and black.
	 */
	public enum KeyColor {
		
		/**
		 * White key color.
		 */
		WHITE,
		
		/**
		 * Black key color.
		 */
		BLACK
	}
	
}

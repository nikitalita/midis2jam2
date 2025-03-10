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

package org.wysko.midis2jam2.instrument.family.guitar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a position on the fretboard.
 */
public class FretboardPosition {
	
	/**
	 * The string of the position.
	 */
	public final int string;
	
	/**
	 * The fret of the position
	 */
	public final int fret;
	
	/**
	 * Constructs a FretboardPosition.
	 *
	 * @param string the string of the position
	 * @param fret   the fret of the position
	 */
	public FretboardPosition(int string, int fret) {
		this.string = string;
		this.fret = fret;
	}
	
	/**
	 * Calculates the distance from this position to another, but ignores variable spacing. Good ol' distance
	 * formula.
	 *
	 * @param o the other fret to find the distance
	 * @return the distance
	 */
	@Contract(pure = true)
	double distance(@NotNull FretboardPosition o) {
		return Math.sqrt(Math.pow(string - o.string, 2) + Math.pow(fret - o.fret, 2));
	}
}

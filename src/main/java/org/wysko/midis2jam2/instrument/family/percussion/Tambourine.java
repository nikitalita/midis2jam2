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

package org.wysko.midis2jam2.instrument.family.percussion;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.wysko.midis2jam2.Midis2jam2;
import org.wysko.midis2jam2.instrument.family.percussion.drumset.NonDrumSetPercussion;
import org.wysko.midis2jam2.instrument.family.percussive.Stick;
import org.wysko.midis2jam2.midi.MidiNoteOnEvent;

import java.util.List;

import static com.jme3.math.FastMath.PI;
import static com.jme3.scene.Spatial.CullHint.Dynamic;
import static org.wysko.midis2jam2.Midis2jam2.rad;

/**
 * The tambourine.
 */
public class Tambourine extends NonDrumSetPercussion {
	
	/**
	 * The list of hits.
	 */
	private final List<MidiNoteOnEvent> hits;
	
	/**
	 * Contains the hand with the tambourine.
	 */
	private final Node tambourineHandNode = new Node();
	
	/**
	 * Contains the empty hand.
	 */
	private final Node emptyHandNode = new Node();
	
	/**
	 * Instantiates a new tambourine.
	 *
	 * @param context the context
	 * @param hits    the hits
	 */
	public Tambourine(Midis2jam2 context,
	                  List<MidiNoteOnEvent> hits) {
		super(context, hits);
		this.hits = hits;
		
		Spatial tambourineHand = context.loadModel("hand_tambourine.fbx", "hands.bmp");
		
		/* Set tambourine materials */
		Material tambourineWoodMat = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		tambourineWoodMat.setTexture("ColorMap", context.getAssetManager().loadTexture("Assets/TambourineWood.bmp"));
		((Node) tambourineHand).getChild(2).setMaterial(tambourineWoodMat);
		
		Material metalTexture = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		metalTexture.setTexture("ColorMap", context.getAssetManager().loadTexture("Assets/MetalTexture.bmp"));
		((Node) tambourineHand).getChild(1).setMaterial(metalTexture);
		
		tambourineHand.setLocalTranslation(0, 0, -2);
		tambourineHandNode.attachChild(tambourineHand);
		
		Spatial hand = context.loadModel("hand_right.obj", "hands.bmp");
		hand.setLocalTranslation(0, 0, -2);
		hand.setLocalRotation(new Quaternion().fromAngles(0, 0, PI));
		emptyHandNode.attachChild(hand);
		
		instrumentNode.setLocalTranslation(12, 42.3f, -48.4f);
		instrumentNode.setLocalRotation(new Quaternion().fromAngles(rad(90), rad(-70), 0));
		
		instrumentNode.attachChild(tambourineHandNode);
		instrumentNode.attachChild(emptyHandNode);
	}
	
	@Override
	public void tick(double time, float delta) {
		super.tick(time, delta);
		
		Stick.StickStatus status = Stick.handleStick(context, tambourineHandNode, time, delta, hits,
				2, 30);
		tambourineHandNode.setCullHint(Dynamic);
		emptyHandNode.setLocalRotation(new Quaternion().fromAngles(-status.getRotationAngle(), 0, 0));
	}
}

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.io.IOException;

public class SeqType {

	public static int count;
	public static SeqType[] instances;

	public static void unpack(FileArchive archive) throws IOException {
		Buffer buffer = new Buffer(archive.read("seq.dat"));
		count = buffer.read16U();
		if (instances == null) {
			instances = new SeqType[count];
		}

		for (int i = 0; i < count; i++) {
			if (instances[i] == null) {
				instances[i] = new SeqType();
			}
			instances[i].load(buffer);
		}
	}

	/**
	 * The amount of frames in this {@link SeqType}.
	 */
	public int frameCount;

	/**
	 * The list of {@link SeqTransform} indices indexed by Frame ID.
	 * @see SeqTransform
	 */
	public int[] transformIDs;

	/**
	 * Auxiliary transform indices appear to only be used by a {@link IfType} of type <code>6</code> as seen in {@link Game#drawParentInterface(IfType, int, int, int)}.
	 */
	public int[] auxiliaryTransformIDs;

	/**
	 * A list of durations indexed by Frame ID.
	 */
	public int[] frameDuration;

	/**
	 * The number of frames from the end of this {@link SeqType} used for looping.
	 */
	public int loopFrameCount = -1;

	/**
	 * Used to determine which transform bases a primary frame is allowed to use, and secondary not.
	 * @see Model#applyTransforms(int, int, int[])
	 */
	public int[] mask;

	/**
	 * Adds additional space to the render bounds.
	 * @see Scene#addTemporary(Entity, int, int, int, int, int, int, boolean, int)
	 */
	public boolean forwardRenderPadding = false;

	/**
	 * The priority.
	 */
	public int priority = 5;

	/**
	 * Allows this {@link SeqType} to override the right hand of a {@link PlayerEntity}.
	 * @see PlayerEntity#getSequencedModel()
	 */
	public int rightHandOverride = -1;

	/**
	 * Allows this {@link SeqType} to override the left hand of a {@link PlayerEntity}.
	 * @see PlayerEntity#getSequencedModel()
	 */
	public int leftHandOverride = -1;

	/**
	 * How many times this seq is allowed to loop before stopping.
	 */
	public int loopCount = 99;

	/**
	 * If 0, causes faster movement, allows looking at target
	 * If 1, pause while moving
	 * If 2, does not look at target, continues playing during movement
	 * @see Game#updateMovement(PathingEntity)
	 */
	public int moveStyle = -1;
	
	/**
	 * If 0, allows looking at a target
	 * If 1, stops playing on move
	 * If 2, does not look at target, continues playing during movement
	 * @see Game#updateMovement(PathingEntity)
	 */
	public int idleStyle = -1;

	/**
	 * If 0, restarts the sequence if already playing
	 * If 1, does not restart if already playing
	 * @see Game#readNPCUpdates()
	 */
	public int replayStyle = 1;

	public int unusedInt;

	public SeqType() {
	}

	public int getFrameDuration(int frame) {
		int duration = frameDuration[frame];

		if (duration == 0) {
			SeqTransform transform = SeqTransform.get(transformIDs[frame]);
			if (transform != null) {
				duration = frameDuration[frame] = transform.delay;
			}
		}

		if (duration == 0) {
			duration = 1;
		}

		return duration;
	}

	public void load(Buffer buffer) {
		do {
			int opcode = buffer.read8U();
			if (opcode == 0) {
				break;
			} else if (opcode == 1) {
				frameCount = buffer.read8U();
				transformIDs = new int[frameCount];
				auxiliaryTransformIDs = new int[frameCount];
				frameDuration = new int[frameCount];
				for (int f = 0; f < frameCount; f++) {
					transformIDs[f] = buffer.read16U();
					auxiliaryTransformIDs[f] = buffer.read16U();
					if (auxiliaryTransformIDs[f] == 65535) {
						auxiliaryTransformIDs[f] = -1;
					}
					frameDuration[f] = buffer.read16U();
				}
			} else if (opcode == 2) {
				loopFrameCount = buffer.read16U();
			} else if (opcode == 3) {
				int count = buffer.read8U();
				mask = new int[count + 1];
				for (int l = 0; l < count; l++) {
					mask[l] = buffer.read8U();
				}
				mask[count] = 9999999;
			} else if (opcode == 4) {
				forwardRenderPadding = true;
			} else if (opcode == 5) {
				priority = buffer.read8U();
			} else if (opcode == 6) {
				rightHandOverride = buffer.read16U();
			} else if (opcode == 7) {
				leftHandOverride = buffer.read16U();
			} else if (opcode == 8) {
				loopCount = buffer.read8U();
			} else if (opcode == 9) {
				moveStyle = buffer.read8U();
			} else if (opcode == 10) {
				idleStyle = buffer.read8U();
			} else if (opcode == 11) {
				replayStyle = buffer.read8U();
			} else if (opcode == 12) {
				unusedInt = buffer.read32();
			} else {
				System.out.println("Error unrecognised seq config code: " + opcode);
			}
		} while (true);

		if (frameCount == 0) {
			frameCount = 1;
			transformIDs = new int[1];
			transformIDs[0] = -1;
			auxiliaryTransformIDs = new int[1];
			auxiliaryTransformIDs[0] = -1;
			frameDuration = new int[1];
			frameDuration[0] = -1;
		}

		if (moveStyle == -1) {
			if (mask != null) {
				moveStyle = 2;
			} else {
				moveStyle = 0;
			}
		}

		if (idleStyle == -1) {
			if (mask != null) {
				idleStyle = 2;
				return;
			}
			idleStyle = 0;
		}
	}

}

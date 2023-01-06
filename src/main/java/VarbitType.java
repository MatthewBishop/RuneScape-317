// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.io.IOException;

public class VarbitType {

	public static VarbitType[] instances;

	public static void unpack(FileArchive archive) throws IOException {
		Buffer buffer = new Buffer(archive.read("varbit.dat"));
		int count = buffer.read16U();

		if (instances == null) {
			instances = new VarbitType[count];
		}

		for (int j = 0; j < count; j++) {
			if (instances[j] == null) {
				instances[j] = new VarbitType();
			}
			instances[j].read(buffer);
			if (instances[j].unusedBool) {
				VarpType.instances[instances[j].varp].unusedBool3 = true;
			}
		}
		if (buffer.position != buffer.data.length) {
			System.out.println("varbit load mismatch");
		}
	}

	public String unusedString;
	public int varp;
	/**
	 * The least significant bit.
	 */
	public int lsb;
	/**
	 * The most significant bit.
	 */
	public int msb;
	public boolean unusedBool = false;
	public int unusedInt0 = -1;
	public int unusedInt1;

	public VarbitType() {
	}

	public void read(Buffer buffer) {
		do {
			int opcode = buffer.read8U();
			if (opcode == 0) {
				return;
			} else if (opcode == 1) {
				varp = buffer.read16U();
				lsb = buffer.read8U();
				msb = buffer.read8U();
			} else if (opcode == 10) {
				unusedString = buffer.readString();
			} else if (opcode == 2) {
				unusedBool = true;
			} else if (opcode == 3) {
				unusedInt0 = buffer.read32();
			} else if (opcode == 4) {
				unusedInt1 = buffer.read32();
			} else {
				System.out.println("Error unrecognised config code: " + opcode);
			}
		} while (true);
	}

}

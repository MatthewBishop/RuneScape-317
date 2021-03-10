// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class SoundTrack {

	public static final SoundTrack[] A_TRACK_ARRAY_325 = new SoundTrack[5000];
	public static final int[] anIntArray326 = new int[5000];
	public static byte[] aByteArray327;
	public static Packet aPacket_328;
	public final SoundTone[] aToneArray329 = new SoundTone[10];
	public int anInt330;
	public int anInt331;

	public SoundTrack() {
	}

	public static void method240(Packet packet) {
		aByteArray327 = new byte[0x6baa8];
		aPacket_328 = new Packet(aByteArray327);
		SoundTone.method166();
		do {
			int j = packet.get2U();
			if (j == 65535) {
				return;
			}
			A_TRACK_ARRAY_325[j] = new SoundTrack();
			A_TRACK_ARRAY_325[j].method242(packet);
			anIntArray326[j] = A_TRACK_ARRAY_325[j].method243();
		} while (true);
	}

	public static Packet method241(int i, int j) {
		if (A_TRACK_ARRAY_325[j] != null) {
			SoundTrack track = A_TRACK_ARRAY_325[j];
			return track.method244(i);
		} else {
			return null;
		}
	}

	public void method242(Packet packet) {
		for (int i = 0; i < 10; i++) {
			int j = packet.get1U();
			if (j != 0) {
				packet.position--;
				aToneArray329[i] = new SoundTone();
				aToneArray329[i].method169(packet);
			}
		}
		anInt330 = packet.get2U();
		anInt331 = packet.get2U();
	}

	public int method243() {
		int j = 0x98967f;
		for (int k = 0; k < 10; k++) {
			if ((aToneArray329[k] != null) && ((aToneArray329[k].anInt114 / 20) < j)) {
				j = aToneArray329[k].anInt114 / 20;
			}
		}
		if ((anInt330 < anInt331) && ((anInt330 / 20) < j)) {
			j = anInt330 / 20;
		}
		if ((j == 0x98967f) || (j == 0)) {
			return 0;
		}
		for (int l = 0; l < 10; l++) {
			if (aToneArray329[l] != null) {
				aToneArray329[l].anInt114 -= j * 20;
			}
		}
		if (anInt330 < anInt331) {
			anInt330 -= j * 20;
			anInt331 -= j * 20;
		}
		return j;
	}

	public Packet method244(int i) {
		int k = method245(i);
		aPacket_328.position = 0;
		aPacket_328.put4(0x52494646);
		aPacket_328.put4LE(36 + k);
		aPacket_328.put4(0x57415645);
		aPacket_328.put4(0x666d7420);
		aPacket_328.put4LE(16);
		aPacket_328.put2LE(1);
		aPacket_328.put2LE(1);
		aPacket_328.put4LE(22050);
		aPacket_328.put4LE(22050);
		aPacket_328.put2LE(1);
		aPacket_328.put2LE(8);
		aPacket_328.put4(0x64617461);
		aPacket_328.put4LE(k);
		aPacket_328.position += k;
		return aPacket_328;
	}

	public int method245(int i) {
		int j = 0;
		for (int k = 0; k < 10; k++) {
			if ((aToneArray329[k] != null) && ((aToneArray329[k].anInt113 + aToneArray329[k].anInt114) > j)) {
				j = aToneArray329[k].anInt113 + aToneArray329[k].anInt114;
			}
		}
		if (j == 0) {
			return 0;
		}
		int l = (22050 * j) / 1000;
		int i1 = (22050 * anInt330) / 1000;
		int j1 = (22050 * anInt331) / 1000;
		if ((i1 < 0) || (j1 < 0) || (j1 > l) || (i1 >= j1)) {
			i = 0;
		}
		int k1 = l + ((j1 - i1) * (i - 1));
		for (int l1 = 44; l1 < (k1 + 44); l1++) {
			aByteArray327[l1] = -128;
		}
		for (int i2 = 0; i2 < 10; i2++) {
			if (aToneArray329[i2] != null) {
				int j2 = (aToneArray329[i2].anInt113 * 22050) / 1000;
				int i3 = (aToneArray329[i2].anInt114 * 22050) / 1000;
				int[] ai = aToneArray329[i2].method167(j2, aToneArray329[i2].anInt113);
				for (int l3 = 0; l3 < j2; l3++) {
					aByteArray327[l3 + i3 + 44] += (byte) (ai[l3] >> 8);
				}
			}
		}
		if (i > 1) {
			i1 += 44;
			j1 += 44;
			l += 44;
			int k2 = (k1 += 44) - l;
			for (int j3 = l - 1; j3 >= j1; j3--) {
				aByteArray327[j3 + k2] = aByteArray327[j3];
			}
			for (int k3 = 1; k3 < i; k3++) {
				int l2 = (j1 - i1) * k3;
				for (int i4 = i1; i4 < j1; i4++) {
					aByteArray327[i4 + l2] = aByteArray327[i4];
				}
			}
			k1 -= 44;
		}
		return k1;
	}

}

package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.Serializable;

import net.minecraft.src.ChunkCoordIntPair;

public class CoordPair implements Serializable {

	public int x;
	public int z;
	
	public CoordPair(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public CoordPair(ChunkCoordIntPair coords) {
		this.x = coords.chunkXPos;
		this.z = coords.chunkZPosition;
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + z + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		CoordPair test = (CoordPair) obj;
		return test.x == x && test.z == z;
	}
}

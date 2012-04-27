package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.src.AnvilChunkLoader;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.forge.DimensionManager;

public class SaveManager {

	private static final String FILE_PREFIX = "ap";

	private static HashMap<Integer, SaveManager> managers = new HashMap<Integer, SaveManager>();
	private int dimension;

	public static SaveManager getManager(int dimension) {

		if (!managers.containsKey(dimension)) {
			managers.put(dimension, new SaveManager(dimension));
		}

		return managers.get(dimension);
	}

	private SaveManager(int dimension) {

		this.dimension = dimension;
	}

	public void save(String key, Object obj) {

		File saveFile = new File(getDataDir(), FILE_PREFIX + key + ".dat");

		GZIPOutputStream gzipOutputStream;
		ObjectOutputStream objOut;
		try {

			gzipOutputStream = new GZIPOutputStream(new FileOutputStream(saveFile));
			objOut = new ObjectOutputStream(gzipOutputStream);

			objOut.writeObject(obj);
			objOut.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void load(String key, Object obj) {

		File saveFile = new File(getDataDir(), FILE_PREFIX + key + ".dat");

		GZIPInputStream gzipInputStream;
		ObjectInputStream objIn;
		try {

			gzipInputStream = new GZIPInputStream(new FileInputStream(saveFile));
			objIn = new ObjectInputStream(gzipInputStream);

			obj = objIn.readObject();
			objIn.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getDataDir() {

		World world = DimensionManager.getWorld(dimension);

		try {
			ISaveHandler worldsaver = world.getSaveHandler();
			IChunkLoader loader = worldsaver.getChunkLoader(world.worldProvider);
			if (loader instanceof AnvilChunkLoader) {
				return (String) ModLoader.getPrivateValue(AnvilChunkLoader.class, loader, 3);
			}
			return null;
		} catch (Exception e) {
			ModLoader.throwException("Additional Pipes Save Manager", e);
			return null;
		}

	}
}

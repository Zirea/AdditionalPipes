package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

	private static SaveManager manager;
	private int dimension;

	public static SaveManager getManager() {

		if (manager == null) {
			manager = new SaveManager();
		}

		return manager;
	}

	private SaveManager() {
	}

	public void save(String key, Serializable obj) {

		File saveDir = new File(getDataDir());
		
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		
		File saveFile = new File(saveDir, FILE_PREFIX + key + ".dat");

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

	public Object load(String key, Serializable obj) {

		File saveFile = new File(getDataDir(), FILE_PREFIX + key + ".dat");

		if (!saveFile.exists()) {
			return obj;
		}
		
		GZIPInputStream gzipInputStream;
		ObjectInputStream objIn;
		Object objFromFile = null;
		try {

			gzipInputStream = new GZIPInputStream(new FileInputStream(saveFile));
			objIn = new ObjectInputStream(gzipInputStream);

			objFromFile = objIn.readObject();
			objIn.close();
			
			return objFromFile;

		} catch (Exception e) {
			ModLoader.getLogger().warning("Additional Pipes Save Manager: " +
					"Unable to load saved data! (" + key + ")");
		}
		
		if (objFromFile != null) {
			return objFromFile;
		}
		
		return obj;
	}
	
	public Object load(String key) {
		return load(key, null);
	}

	public String getDataDir() {

		World world = DimensionManager.getProvider(dimension).worldObj;

		try {
			ISaveHandler worldsaver = world.getSaveHandler();
			IChunkLoader loader = worldsaver.getChunkLoader(world.worldProvider);
			if (loader instanceof AnvilChunkLoader) {
				return ((File)ModLoader.getPrivateValue(AnvilChunkLoader.class, loader, 3)).getPath() + File.separator + "ap";
			}
			return null;
		} catch (Exception e) {
			ModLoader.throwException("Additional Pipes Save Manager: Unable to get data dir!", e);
			return null;
		}

	}
}

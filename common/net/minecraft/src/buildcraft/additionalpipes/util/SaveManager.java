package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.src.AnvilChunkLoader;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.forge.DimensionManager;

public class SaveManager {

	private static final String FILE_PREFIX = "ap";

	private static SaveManager manager;

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

		} catch (Exception e) {
			ModLoader.getLogger().warning("Additional Pipes Save Manager: " +
					"Unable to save data! (" + key + ")");
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
		
		for (int id : DimensionManager.getIDs()) {
			
			World world = DimensionManager.getProvider(id).worldObj;
			if (world != null) {
				
				try {
					
					IChunkLoader chunkLoader = world.getSaveHandler().getChunkLoader(world.worldProvider);
					File worldDir = ((File)ModLoader.getPrivateValue(AnvilChunkLoader.class, chunkLoader, 3));
					
					return getBaseWorldDir(worldDir) + File.separator + "ap";
					
				} catch (Exception e) {
					ModLoader.throwException("Additional Pipes Save Manager: Unable to get data dir!", e);
					e.printStackTrace();
					return null;
				}
			}
		}
		
		return null;
	}
	
	private String getBaseWorldDir(File dir) {
		
		if (dir.getPath().contains("DIM")) {
			return dir.getParent();
		}
		
		return dir.getPath();
	}
}

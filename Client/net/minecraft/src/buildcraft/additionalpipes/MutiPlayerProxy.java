package net.minecraft.src.buildcraft.additionalpipes;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public class MutiPlayerProxy {
    public static boolean NeedsLoad = true;
    public static File WorldDir;
    public static boolean isServer = false;
    public static boolean HDSet = false;
    public static boolean HDFound = false;
    public static boolean OFFound = false;

    public static Minecraft mc = ModLoader.getMinecraftInstance();

    public static boolean isOnServer() {
        return mc.theWorld.isRemote;
    }

}

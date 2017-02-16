package net.machinemuse.numina.basemod.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.machinemuse.numina.event.FOVUpdateEventHandler;
import net.machinemuse.numina.event.KeybindKeyHandler;
import net.machinemuse.numina.mouse.MouseEventHandler;
import net.machinemuse.numina.render.RenderGameOverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register((Object)new MouseEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new RenderGameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register((Object)new KeybindKeyHandler());
    }
}
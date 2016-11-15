package net.machinemuse.numina.death;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.machinemuse.numina.basemod.Numina;
import net.machinemuse.numina.general.MuseLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:31 PM, 10/15/13
 *
 * Ported to java by lehjr on 10/10/16.
 */
public class DeathEventHandler {
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent e) {
        EntityPlayer player = (EntityPlayer) e.entityLiving;
        e.setCanceled(true);
        player.openGui(Numina.getInstance(), 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        MuseLogger.logDebug("Death");
//        player.setHealth(10f)
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent e) {
        if (e.gui instanceof GuiGameOver) {
            e.setCanceled(true);
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            player.openGui(Numina.getInstance(), 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
    }
}

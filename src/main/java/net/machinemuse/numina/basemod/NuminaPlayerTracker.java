package net.machinemuse.numina.basemod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.machinemuse.numina.network.MusePacketRecipeUpdate;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.recipe.JSONRecipe;
import net.machinemuse.numina.recipe.JSONRecipeList;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 *
 * Ported to Java by lehjr on 10/26/16.
 */
public final class NuminaPlayerTracker
{
    @SubscribeEvent
    public void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
            for (JSONRecipe recipe : JSONRecipeList.getJSONRecipesList()) {
                JSONRecipe[] recipeArray = new JSONRecipe[]{recipe};
                String recipeAsString= JSONRecipeList.gson.toJson(recipeArray);
                PacketSender.sendTo(new MusePacketRecipeUpdate(event.player, recipeAsString), (EntityPlayerMP)event.player);
            }
        }
    }
}





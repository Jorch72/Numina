package net.machinemuse.numina.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.numina.item.IModeChangingItem;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.DataInputStream;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketModeChangeRequest extends MusePacket {
    String mode;
    int slot;

    public MusePacketModeChangeRequest(EntityPlayer player, String mode, int slot) {
        this.slot = slot;
        this.mode = mode;
    }

    @Override
    public IMusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(slot);
        writeString(mode);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClient(EntityClientPlayerMP player) {
        if (slot > -1 && slot < 9) {
            ItemStack stack = player.inventory.mainInventory[slot];
            if (stack != null) {
                Item item = stack.getItem();
                if (item instanceof IModeChangingItem) {
                    List<String> modes = ((IModeChangingItem) item).getValidModes(stack, player);
                    if (modes.contains(mode)) {
                        ((IModeChangingItem) item).setActiveMode(stack, mode);
                    }
                }
            }
        }
    }

    private static MusePacketModeChangeRequestPackager PACKAGERINSTANCE;
    public static MusePacketModeChangeRequestPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketModeChangeRequestPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketModeChangeRequestPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int slot = readInt(datain);
            String mode = readString(datain);
            return new MusePacketModeChangeRequest(player, mode, slot);
        }
    }
}

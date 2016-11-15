package net.machinemuse.numina.death;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

/**
 * Ported to Java by lehjr on 10/10/16.
 */
@SideOnly(Side.CLIENT)
public class GuiGameOverPlus extends GuiScreen {
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();
        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            if (this.mc.isIntegratedServerRunning()) {
                addButton(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld")));
            } else {
                addButton(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer")));
            }
        } else {
            addButton(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
            addButton(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));
            addButton(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 120, "Revive"));
            if (this.mc.getSession() == null) {
                ((GuiButton)this.buttonList.get(1)).enabled = false;
            }
        }
        Iterator<GuiButton> iterator = this.buttonList.iterator();
        while (iterator.hasNext()) {
            iterator.next().enabled = false;
        }
    }

    public void addButton(GuiButton b) {
        this.buttonList.add(b);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        switch(par1GuiButton.id) {
            case 1:
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen(null);
            case 2:
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                this.mc.displayGuiScreen(new GuiMainMenu());
            case 3:
                this.mc.thePlayer.setHealth(10f);
                this.mc.thePlayer.isDead = false;
                this.mc.displayGuiScreen(null);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        Boolean flag = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        this.drawCenteredString(this.fontRendererObj, flag ? I18n.format("deathScreen.title.hardcore", new Object[0]) : I18n.format("deathScreen.title", new Object[0]), this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();
        if (flag) {
            this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
        }
        this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.cooldownTimer += 1;
        if (this.cooldownTimer == 20) {
            Iterator<GuiButton> iterator = this.buttonList.iterator();
            while (iterator.hasNext()) {
                iterator.next().enabled = false;
            }
        }
    }

    /**
     * The cooldown timer for the buttons, increases every tick and enables all buttons when reaching 20.
     */
    private int cooldownTimer = 0;
}

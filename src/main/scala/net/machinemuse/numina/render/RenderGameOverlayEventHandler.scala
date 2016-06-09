package net.machinemuse.numina.render

import net.machinemuse.numina.item.ModeChangingItem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
//import net.minecraft.util.IIcon
import net.machinemuse.numina.geometry.Colour
import net.machinemuse.numina.scala.OptionCast
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
  * Author: MachineMuse (Claire Semple)
  * Created: 2:17 PM, 9/6/13
  */
object RenderGameOverlayEventHandler {
  @SubscribeEvent def onPreRenderGameOverlayEvent(e: RenderGameOverlayEvent.Pre) {
    e.`getType` match {
      //      case ElementType.ALL =>
      //      case ElementType.HELMET =>
      //      case ElementType.PORTAL =>
      //      case ElementType.CROSSHAIRS =>
      //      case ElementType.BOSSHEALTH =>
      //      case ElementType.ARMOR =>
      //      case ElementType.HEALTH =>
      //      case ElementType.FOOD =>
      //      case ElementType.AIR =>
      //      case ElementType.HOTBAR =>
      //      case ElementType.EXPERIENCE =>
      //      case ElementType.TEXT =>
      //      case ElementType.HEALTHMOUNT =>
      //      case ElementType.JUMPBAR =>
      case _ =>
    }
  }

  @SubscribeEvent def onPostRenderGameOverlayEvent(e: RenderGameOverlayEvent.Post) {
    e.`getType` match {
      //      case ElementType.ALL =>
      //      case ElementType.HELMET =>
      //      case ElementType.PORTAL =>
      //      case ElementType.CROSSHAIRS =>
      //      case ElementType.BOSSHEALTH =>
      //      case ElementType.ARMOR =>
      //      case ElementType.HEALTH =>
      //      case ElementType.FOOD =>
      //      case ElementType.AIR =>
      case ElementType.HOTBAR => drawModeChangeIcons()
      //      case ElementType.EXPERIENCE =>
      //      case ElementType.TEXT =>
      //      case ElementType.HEALTHMOUNT =>
      case ElementType.JUMPBAR =>
      case _ =>
    }
  }

  val SWAPTIME: Int = 200
  var lastSwapTime: Long = 0
  var lastSwapDirection: Int = 0

  def drawModeChangeIcons() {
    for {
      mc <- Option(Minecraft.getMinecraft)
      player <- Option(mc.thePlayer)
      i <- Option(player.inventory.currentItem)
      stack <- Option(player.inventory.getCurrentItem)
      item <- OptionCast[ModeChangingItem](stack.getItem)
    } {
      val screen = new ScaledResolution(mc)

      MuseTextureUtils.pushTexture(MuseTextureUtils.TEXTURE_QUILT)
      RenderState.blendingOn()
      val swapTime = Math.min(System.currentTimeMillis - lastSwapTime, SWAPTIME)
      val currentMode: Option[TextureAtlasSprite] = item.getModeIcon(item.getActiveMode(stack, player), stack, player)
      val nextMode: Option[TextureAtlasSprite] = item.getModeIcon(item.nextMode(stack, player), stack, player)
      val prevMode: Option[TextureAtlasSprite] = item.getModeIcon(item.prevMode(stack, player), stack, player)
      var prevX: Double = .0
      var prevY: Double = .0
      var currX: Double = .0
      var currY: Double = .0
      var nextX: Double = .0
      var nextY: Double = .0
      val sw: Int = screen.getScaledWidth
      val sh: Int = screen.getScaledHeight
      var baroffset: Int = 22
      if (!player.capabilities.isCreativeMode) {
        baroffset += 16
        if (ForgeHooks.getTotalArmorValue(player) > 0) {
          baroffset += 8
        }
      }
      RenderState.scissorsOn(0, 0, sw, sh - baroffset)
      baroffset = screen.getScaledHeight - baroffset
      prevX = sw / 2.0 - 105.0 + 20.0 * i
      prevY = baroffset - 8
      currX = sw / 2.0 - 89.0 + 20.0 * i
      currY = baroffset - 18
      nextX = sw / 2.0 - 73.0 + 20.0 * i
      nextY = baroffset - 8
      if (swapTime == SWAPTIME || lastSwapDirection == 0) {
        drawIcon(prevX, prevY, prevMode, 0.4)
        drawIcon(currX, currY, currentMode, 0.8)
        drawIcon(nextX, nextY, nextMode, 0.4)
      }
      else {
        val r1: Double = 1 - swapTime / SWAPTIME.asInstanceOf[Double]
        val r2: Double = swapTime / SWAPTIME.asInstanceOf[Double]
        if (lastSwapDirection == -1) {
          nextX = currX * r1 + nextX * r2
          nextY = currY * r1 + nextY * r2
          currX = prevX * r1 + currX * r2
          currY = prevY * r1 + currY * r2
          drawIcon(currX, currY, currentMode, 0.8)
          drawIcon(nextX, nextY, nextMode, 0.8)
        }
        else {
          prevX = currX * r1 + prevX * r2
          prevY = currY * r1 + prevY * r2
          currX = nextX * r1 + currX * r2
          currY = nextY * r1 + currY * r2
          drawIcon(prevX, prevY, prevMode, 0.8)
          drawIcon(currX, currY, currentMode, 0.8)
        }
      }
      RenderState.scissorsOff()
      RenderState.blendingOff()
      MuseTextureUtils.popTexture()
      Colour.WHITE.doGL()

    }
  }

  private def drawIcon(x: Double, y: Double, icon: Option[TextureAtlasSprite], alpha: Double) {
    icon.map(i => MuseIconUtils.drawIconAt(x, y, i, Colour.WHITE.withAlpha(alpha)))
  }

  def updateSwap(dModeSig: Int) {
    lastSwapTime = System.currentTimeMillis
    lastSwapDirection = dModeSig
  }
}

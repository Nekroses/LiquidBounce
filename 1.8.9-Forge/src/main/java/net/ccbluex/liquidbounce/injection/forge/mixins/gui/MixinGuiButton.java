package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
@Mixin(GuiButton.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiButton {

    @Shadow
    public boolean visible;

    @Shadow
    public int xPosition;

    @Shadow
    public int yPosition;

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Shadow
    protected boolean hovered;

    @Shadow
    public boolean enabled;

    @Shadow
    protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

    @Shadow
    public String displayString;

    private float cut;
    private float alpha;

    /**
     * @author CCBlueX
     */
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if(visible) {
            final FontRenderer fontRenderer = mc.getLanguageManager().isCurrentLocaleUnicode() ? mc.fontRendererObj : Fonts.font35;
            hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);

            final int delta = RenderUtils.deltaTime;

            if(enabled && hovered) {
                cut += 0.05F * delta;

                if(cut >= 4) cut = 4;

                alpha += 0.3F * delta;

                if(alpha >= 210) alpha = 210;
            }else{
                cut -= 0.05F * delta;

                if(cut <= 0) cut = 0;

                alpha -= 0.3F * delta;

                if(alpha <= 120) alpha = 120;
            }

            Gui.drawRect(this.xPosition + (int) this.cut, this.yPosition,
                    this.xPosition + this.width - (int) this.cut, this.yPosition + this.height,
                    this.enabled ? new Color(0F, 0F, 0F, this.alpha / 255F).getRGB() :
                            new Color(0.5F, 0.5F, 0.5F, 0.5F).getRGB());

            mouseDragged(mc, mouseX, mouseY);

            fontRenderer.drawStringWithShadow(displayString, (float) ((this.xPosition + this.width / 2) - fontRenderer.getStringWidth(displayString) / 2), this.yPosition + (this.height - 5) / 2, 14737632);
            GlStateManager.resetColor();
        }
    }
}
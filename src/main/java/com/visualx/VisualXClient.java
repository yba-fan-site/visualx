package com.visualx;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * VisualX is an original, lightweight PvP HUD inspired by the clean layout
 * commonly seen in competitive Minecraft clients. It does not include or
 * redistribute Lunar Client assets or code.
 */
public final class VisualXClient implements ClientModInitializer {
    private static final int PANEL = 0xB9161924;
    private static final int ACCENT = 0xFFB58CFF;
    private static final int WHITE = 0xFFF2F2F7;
    private static final int MUTED = 0xFFB6B8C5;
    private static final int ACTIVE = 0xFFE6D7FF;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((graphics, tickCounter) -> renderHud(graphics));
    }

    private static void renderHud(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        int x = 10;
        int y = 10;
        int width = 112;
        int height = 78;
        g.fill(x, y, x + width, y + height, PANEL);
        g.fill(x, y, x + 2, y + height, ACCENT);
        g.drawString(mc.font, Component.literal("VISUALX"), x + 9, y + 7, ACCENT, false);
        g.drawString(mc.font, Component.literal("LUNAR STYLE"), x + 9, y + 19, MUTED, false);

        int fps = mc.getFps();
        g.drawString(mc.font, Component.literal("FPS  " + fps), x + 9, y + 34, WHITE, false);

        if (mc.player != null) {
            int hp = (int) Math.ceil(mc.player.getHealth());
            int armor = mc.player.getArmorValue();
            g.drawString(mc.font, Component.literal("HP   " + hp + "     ARMOR  " + armor), x + 9, y + 47, WHITE, false);

            int px = (int) Math.floor(mc.player.getX());
            int py = (int) Math.floor(mc.player.getY());
            int pz = (int) Math.floor(mc.player.getZ());
            g.drawString(mc.font, Component.literal("XYZ  " + px + " " + py + " " + pz), x + 9, y + 60, MUTED, false);
        }

        renderKeystrokes(g, mc, 10, 96);
    }

    private static void renderKeystrokes(GuiGraphics g, Minecraft mc, int x, int y) {
        int box = 18;
        int gap = 3;
        keyBox(g, mc, x + box + gap, y, box, "W", mc.options.keyUp.isDown());
        keyBox(g, mc, x, y + box + gap, box, "A", mc.options.keyLeft.isDown());
        keyBox(g, mc, x + box + gap, y + box + gap, box, "S", mc.options.keyDown.isDown());
        keyBox(g, mc, x + (box + gap) * 2, y + box + gap, box, "D", mc.options.keyRight.isDown());
        keyBox(g, mc, x, y + (box + gap) * 2, box * 2 + gap, "JUMP", mc.options.keyJump.isDown());
        keyBox(g, mc, x + (box + gap) * 2, y + (box + gap) * 2, box, "SPR", mc.options.keySprint.isDown());
    }

    private static void keyBox(GuiGraphics g, Minecraft mc, int x, int y, int w, String label, boolean pressed) {
        int bg = pressed ? 0xD04D376E : PANEL;
        int fg = pressed ? ACTIVE : WHITE;
        g.fill(x, y, x + w, y + 18, bg);
        g.fill(x, y + 17, x + w, y + 18, pressed ? ACCENT : 0x665C6070);
        int textWidth = mc.font.width(label);
        g.drawString(mc.font, Component.literal(label), x + (w - textWidth) / 2, y + 5, fg, false);
    }
}

package com.visualx;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * VisualX: an original, lightweight PvP HUD with a simple click settings menu.
 * No Lunar Client code or assets are included.
 */
public final class VisualXClient implements ClientModInitializer {
    private static final int PANEL = 0xE9161924;
    private static final int ACCENT = 0xFFB58CFF;
    private static final int WHITE = 0xFFF2F2F7;
    private static final int MUTED = 0xFFB6B8C5;
    private static final int ACTIVE = 0xFFE6D7FF;

    private static boolean showBrand = true;
    private static boolean showFps = true;
    private static boolean showVitals = true;
    private static boolean showCoordinates = true;
    private static boolean showKeystrokes = true;

    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("visualx", "main")
        );
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.visualx.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.screen instanceof VisualXScreen) {
                    client.setScreen(null);
                } else if (client.screen == null) {
                    client.setScreen(new VisualXScreen());
                }
            }
        });

        HudRenderCallback.EVENT.register((graphics, tickCounter) -> renderHud(graphics));
    }

    private static void renderHud(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        int x = 10;
        int y = 10;
        int width = 154;
        int rows = (showBrand ? 2 : 0) + (showFps ? 1 : 0)
                + (showVitals ? 1 : 0) + (showCoordinates ? 1 : 0);
        if (rows > 0) {
            int height = 10 + rows * 13;
            g.fill(x, y, x + width, y + height, PANEL);
            g.fill(x, y, x + 2, y + height, ACCENT);
            int textY = y + 7;
            if (showBrand) {
                g.drawString(mc.font, Component.literal("VISUALX"), x + 9, textY, ACCENT, false);
                textY += 13;
                g.drawString(mc.font, Component.literal("LUNAR STYLE"), x + 9, textY, MUTED, false);
                textY += 13;
            }
            if (showFps) {
                g.drawString(mc.font, Component.literal("FPS  " + mc.getFps()), x + 9, textY, WHITE, false);
                textY += 13;
            }
            if (showVitals) {
                int hp = (int) Math.ceil(mc.player.getHealth());
                int armor = mc.player.getArmorValue();
                g.drawString(mc.font, Component.literal("HP  " + hp + "     ARMOR  " + armor), x + 9, textY, WHITE, false);
                textY += 13;
            }
            if (showCoordinates) {
                int px = (int) Math.floor(mc.player.getX());
                int py = (int) Math.floor(mc.player.getY());
                int pz = (int) Math.floor(mc.player.getZ());
                g.drawString(mc.font, Component.literal("XYZ  " + px + " " + py + " " + pz), x + 9, textY, MUTED, false);
            }
        }
        if (showKeystrokes) renderKeystrokes(g, mc, 10, 96);
    }

    private static void renderKeystrokes(GuiGraphics g, Minecraft mc, int x, int y) {
        int box = 18, gap = 3;
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

    private static final class VisualXScreen extends Screen {
        private static final int WIDTH = 230;
        private static final int ROW_HEIGHT = 27;
        private static final String[] NAMES = {
                "Branding", "FPS Counter", "Health & Armor", "Coordinates", "Keystrokes"
        };

        protected VisualXScreen() {
            super(Component.literal("VisualX Settings"));
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
            // Avoid Screen.renderBackground(): in 1.21.11 it requests a blur pass,
            // and this can conflict with another blur pass in the same frame.
            // A translucent overlay gives the menu a dimmed backdrop without blur.
            g.fill(0, 0, this.width, this.height, 0x99000000);
            int left = (this.width - WIDTH) / 2;
            int top = (this.height - 220) / 2;
            g.fill(left, top, left + WIDTH, top + 220, 0xF0181A26);
            g.fill(left, top, left + WIDTH, top + 3, ACCENT);
            g.drawCenteredString(this.font, Component.literal("VISUALX  /  SETTINGS"),
                    this.width / 2, top + 13, ACCENT);
            g.drawCenteredString(this.font, Component.literal("Click a module to toggle it"),
                    this.width / 2, top + 31, MUTED);

            boolean[] values = {showBrand, showFps, showVitals, showCoordinates, showKeystrokes};
            for (int i = 0; i < NAMES.length; i++) {
                int rowY = top + 55 + i * ROW_HEIGHT;
                boolean hover = mouseX >= left + 12 && mouseX <= left + WIDTH - 12
                        && mouseY >= rowY && mouseY <= rowY + 22;
                g.fill(left + 12, rowY, left + WIDTH - 12, rowY + 22,
                        hover ? 0xFF343044 : 0xFF252735);
                g.drawString(this.font, Component.literal(NAMES[i]), left + 21, rowY + 7, WHITE);
                String state = values[i] ? "ON" : "OFF";
                int color = values[i] ? 0xFF9FE0B0 : 0xFFFF9292;
                g.drawString(this.font, Component.literal(state), left + WIDTH - 48, rowY + 7, color);
            }
            g.drawCenteredString(this.font, Component.literal("Right Shift: close menu"),
                    this.width / 2, top + 198, MUTED);
            super.render(g, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            double mouseX = event.x();
            double mouseY = event.y();
            int button = event.button();
            if (button == 0) {
                int left = (this.width - WIDTH) / 2;
                int top = (this.height - 220) / 2;
                int index = (int) ((mouseY - (top + 55)) / ROW_HEIGHT);
                if (mouseX >= left + 12 && mouseX <= left + WIDTH - 12
                        && index >= 0 && index < NAMES.length
                        && mouseY >= top + 55 + index * ROW_HEIGHT
                        && mouseY <= top + 77 + index * ROW_HEIGHT) {
                    switch (index) {
                        case 0 -> showBrand = !showBrand;
                        case 1 -> showFps = !showFps;
                        case 2 -> showVitals = !showVitals;
                        case 3 -> showCoordinates = !showCoordinates;
                        case 4 -> showKeystrokes = !showKeystrokes;
                    }
                    return true;
                }
            }
            return super.mouseClicked(event, doubleClick);
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}

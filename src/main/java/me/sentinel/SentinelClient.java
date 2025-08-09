package me.sentinel;

import me.sentinel.gui.GuiOverlay;
import me.sentinel.gui.ShaderManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class SentinelClient implements ClientModInitializer {
    public static KeyBinding toggleGuiKey;

    @Override
    public void onInitializeClient() {
        toggleGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sentinel.toggle_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DELETE,
                "category.sentinel"
        ));

        try {
            ShaderManager.init();
            System.out.println("[Sentinel] ShaderManager.init() called");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            GuiOverlay.init();
            System.out.println("[Sentinel] GuiOverlay.init() called");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null) return;

            while (toggleGuiKey.wasPressed()) {
                GuiOverlay.VISIBLE = !GuiOverlay.VISIBLE;
                System.out.println("[Sentinel] toggle key pressed. VISIBLE = " + GuiOverlay.VISIBLE);

                if (client.player != null) {
                    final boolean visible = GuiOverlay.VISIBLE;
                    client.execute(() -> client.player.sendMessage(Text.literal("Sentinel GUI: " + (visible ? "visible" : "hidden")), false));
                }
            }
        });
    }
}

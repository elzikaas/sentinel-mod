package me.sentinel.gui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class GuiOverlay {
    public static boolean VISIBLE = false;

    private static final int SQUARE_SIZE = 400;
    private static final int RADIUS_PIXELS = 60;
    private static final int COLOR_ARGB = 0xFFBFBFBF;
    private static final int FALLBACK_COLOR_ARGB = 0xFF777777;

    public static void init() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!VISIBLE) return;

            try {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client == null) return;

                int screenW = client.getWindow().getScaledWidth();
                int screenH = client.getWindow().getScaledHeight();

                int x = (screenW - SQUARE_SIZE) / 2;
                int y = (screenH - SQUARE_SIZE) / 2;

                drawContext.fill(x, y, x + SQUARE_SIZE, y + SQUARE_SIZE, FALLBACK_COLOR_ARGB);
                drawRoundedRect(drawContext, x, y, SQUARE_SIZE, SQUARE_SIZE, RADIUS_PIXELS, COLOR_ARGB);

            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int r, int color) {
        if (r <= 0) {
            ctx.fill(x, y, x + w, y + h, color);
            return;
        }
        if (r * 2 > w) r = w / 2;
        if (r * 2 > h) r = h / 2;

        int left = x;
        int top = y;
        int right = x + w;
        int bottom = y + h;

        ctx.fill(left + r, top + r, right - r, bottom - r, color);

        ctx.fill(left + r, top, right - r, top + r, color);
        ctx.fill(left + r, bottom - r, right - r, bottom, color);

        ctx.fill(left, top + r, left + r, bottom - r, color);
        ctx.fill(right - r, top + r, right, bottom - r, color);

        for (int dx = 0; dx < r; dx++) {
            double fx = dx + 0.5;
            double yy = Math.sqrt((double) r * r - fx * fx);
            int ySpan = (int) Math.floor(yy);
            int columnLeft = left + dx;
            int columnRight = columnLeft + 1;

            int topYStart = top + r - ySpan;
            int topYEnd = top + r;
            if (topYEnd > topYStart) ctx.fill(columnLeft, topYStart, columnRight, topYEnd, color);

            int bottomCenter = bottom - r;
            int bottomYStart = bottomCenter;
            int bottomYEnd = bottomCenter + ySpan;
            if (bottomYEnd > bottomYStart) ctx.fill(columnLeft, bottomYStart, columnRight, bottomYEnd, color);

            int colRightX = right - dx - 1;
            int colRightX2 = colRightX + 1;

            if (topYEnd > topYStart) ctx.fill(colRightX, topYStart, colRightX2, topYEnd, color);

            if (bottomYEnd > bottomYStart) ctx.fill(colRightX, bottomYStart, colRightX2, bottomYEnd, color);
        }
    }
}

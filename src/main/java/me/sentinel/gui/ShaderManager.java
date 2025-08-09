package me.sentinel.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ShaderManager {
    private static final Logger LOGGER = LogManager.getLogger("Sentinel|Shader");

    public static ShaderProgram roundedShader;
    public static Uniform rectPosUniform;
    public static Uniform rectSizeUniform;
    public static Uniform radiusUniform;
    public static RenderLayer ROUNDED_LAYER;

    public static void init() {
        LOGGER.info("Registering CoreShaderRegistrationCallback for sentinel_rounded");
        CoreShaderRegistrationCallback.EVENT.register(context -> {
            try {
                Identifier id = Identifier.of("sentinel", "sentinel_rounded");
                LOGGER.info("CoreShaderRegistrationCallback invoked, attempting to register shader: {}", id.toString());

                context.register(id, VertexFormats.POSITION_TEXTURE_COLOR, program -> {
                    try {
                        if (program == null) {
                            LOGGER.warn("Received null ShaderProgram for {}", id);
                            return;
                        }

                        roundedShader = program;
                        LOGGER.info("ShaderProgram object received for {}: {}", id, program);

                        try {
                            rectPosUniform = program.getUniform("RectPos");
                            rectSizeUniform = program.getUniform("RectSize");
                            radiusUniform = program.getUniform("Radius");
                        } catch (Throwable t) {
                            LOGGER.warn("Exception while getting uniforms: {}", t.toString());
                        }

                        LOGGER.info("Uniforms: RectPos={}, RectSize={}, Radius={}", rectPosUniform != null, rectSizeUniform != null, radiusUniform != null);

                        try {
                            ROUNDED_LAYER = RenderLayer.of("sentinel_rounded",
                                    VertexFormats.POSITION_TEXTURE_COLOR,
                                    VertexFormat.DrawMode.QUADS,
                                    256,
                                    false,
                                    true,
                                    RenderLayer.MultiPhaseParameters.builder()
                                            .program(new RenderPhase.ShaderProgram(() -> roundedShader))
                                            .transparency(new RenderPhase.Transparency("sentinel_transparency", () -> {
                                                RenderSystem.enableBlend();
                                                RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                                            }, RenderSystem::disableBlend))
                                            .build(false)
                            );

                            LOGGER.info("RenderLayer 'sentinel_rounded' created successfully.");
                        } catch (Throwable t) {
                            LOGGER.error("Failed to create RenderLayer for sentinel_rounded: ", t);
                        }
                    } catch (Throwable inner) {
                        LOGGER.error("Error in shader program consumer for sentinel_rounded: ", inner);
                    }
                });

                LOGGER.info("Registration call completed for {}", id);
            } catch (Throwable ex) {
                LOGGER.error("Error while registering shader sentinel_rounded: ", ex);
            }
        });
    }
}

package andrews.table_top_craft.tile_entities.render;

import andrews.table_top_craft.events.DrawScreenEvent;
import andrews.table_top_craft.objects.blocks.ChessPieceFigureBlock;
import andrews.table_top_craft.tile_entities.ChessPieceFigureBlockEntity;
import andrews.table_top_craft.tile_entities.model.piece_figure.ChessPieceFigureStandModel;
import andrews.table_top_craft.util.Reference;
import andrews.table_top_craft.util.TTCRenderTypes;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class ChessPieceFigureTileEntityRenderer implements BlockEntityRenderer<ChessPieceFigureBlockEntity>
{
    // Dynamic Texture
    private static final NativeImage image = new NativeImage(NativeImage.Format.RGBA, 1, 1, true);
    private static final DynamicTexture texture = new DynamicTexture(image);
    private static ResourceLocation resourceLocation = null;
    // Chess Piece Stand texture and model
    public static final ResourceLocation EYE_TEXTURE = new ResourceLocation(Reference.MODID, "textures/tile/chess_piece_figure/chess_piece_figure.png");
    private static ChessPieceFigureStandModel chessPieceFigureStandModel;

    static
    {
        // We create the dummy texture
        image.setPixelRGBA(0, 0, 16777215);
        texture.upload();
        resourceLocation = Minecraft.getInstance().getTextureManager().register("table_top_craft_dummy", texture);
    }

    public ChessPieceFigureTileEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        chessPieceFigureStandModel = new ChessPieceFigureStandModel(context.bakeLayer(ChessPieceFigureStandModel.CHESS_PIECE_FIGURE_LAYER));
    }

    @Override
    public void render(ChessPieceFigureBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        renderChessPieceFigure(blockEntity, poseStack, bufferSource, packedLight, packedOverlay);
    }

    public static void renderChessPieceFigure(ChessPieceFigureBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        // Renders the Stand for the "Chess Piece Figure" block
        poseStack.pushPose();
            poseStack.translate(0.5D, 1.5D, 0.5D);
            poseStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(EYE_TEXTURE));
            chessPieceFigureStandModel.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        int rotation = 0;
        if(blockEntity.hasLevel())
        {
            BlockState blockstate = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
            if(blockstate.getBlock() instanceof ChessPieceFigureBlock)
            {
                rotation = blockstate.getValue(ChessPieceFigureBlock.ROTATION);
            }
        }
        int lightU = LightTexture.block(packedLight);
        int lightV = LightTexture.sky(packedLight);

        poseStack.pushPose();
            poseStack.translate(8 * 0.0625F, 2 * 0.0625F, 8 * 0.0625F);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(rotation * 22.5F));
            // We invert the model because Minecraft renders shit inside out.
            poseStack.scale(3.0F, -3.0F, -3.0F);
            poseStack.pushPose();
                RenderType type = TTCRenderTypes.getChessPieceSolid(resourceLocation);
                type.setupRenderState();
                BufferHelpers.setupRender(RenderSystem.getShader(), lightU, lightV);
                ShaderInstance shaderinstance = RenderSystem.getShader();
                RenderSystem.setShaderColor(210 / 255F, 188 / 255F, 161 / 255F, 1.0F);
                BufferHelpers.updateColor(shaderinstance);
                poseStack.pushPose();
                    if (shaderinstance.MODEL_VIEW_MATRIX != null) shaderinstance.MODEL_VIEW_MATRIX.set(poseStack.last().pose());
                    if (shaderinstance.PROJECTION_MATRIX != null) shaderinstance.PROJECTION_MATRIX.set(RenderSystem.getProjectionMatrix());
                    VertexBuffer pawnBuffer = DrawScreenEvent.pawnBuffer;
                    BufferHelpers.draw(pawnBuffer, shaderinstance);
                poseStack.popPose();
                type.clearRenderState();
            poseStack.popPose();
        poseStack.popPose();
    }
}
package dhyces.dinnerplate.client.render.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemModel {

    private static final float DEPTH_0 = 0.46875F;
    private static final float DEPTH_1 = 0.53125F;
    private static final float ONE_PIXEL = 0.0625F;

    public static List<BakedQuad> genStandardItem(NativeImage img, TextureAtlasSprite sprite, int tint) {
        List<BakedQuad> quads = new ArrayList<>();
        for (int v = 0; v < img.getHeight(); v++) {
            for (int u = 0; u < img.getWidth(); u++) {
                Function<Integer, Integer> alphaGetter = c -> (c >> 24) & 0xFF;
                int alpha = alphaGetter.apply(img.getPixelRGBA(u, v));
                int up = v > 0 ? alphaGetter.apply(img.getPixelRGBA(u, v - 1)) : 0;
                int right = u < img.getWidth() - 1 ? alphaGetter.apply(img.getPixelRGBA(u + 1, v)) : 0;
                //         if alpha - up is <0, that means above is opaque and we are translucent, so gen down
                //                                                             if alpha - right <0, right is opaque and we are translucent, gen west
                // 1 = east, 2 = west, 4 = up, 8 = down
                var horizontal = (alpha != right ? alpha - right < 0 ? 0b0010 : 0b0001 : 0);
                var vertical = (alpha != up ? alpha - up < 0 ? 0b1000 : 0b0100 : 0);
                int total = vertical | horizontal;
                if ((total & 1) == 1)
                    quads.add(genSideQuad(sprite, Direction.EAST, u, v));
                if ((total & 2) == 2)
                    quads.add(genSideQuad(sprite, Direction.WEST, u, v));
                if ((total & 4) == 4)
                    quads.add(genSideQuad(sprite, Direction.UP, u, v));
                if ((total & 8) == 8)
                    quads.add(genSideQuad(sprite, Direction.DOWN, u, v));
            }
        }
        return quads;
    }

    /**
     * side really only determines where the pixel color is derived from.
     * down means its generating for the affected pixel's bottom face, and the same goes for all of them
     */
    private static BakedQuad genSideQuad(TextureAtlasSprite sprite, Direction side, float u, float v) {
        BakedQuadBuilder builder = builder(sprite, side, 0, false);
        int width = sprite.getWidth(), height = sprite.getHeight();
        // the origin coordinate is in the top-left of a pixel and uv starts from the bottom left
        float x0 = u / width, x1 = x0;
        float y0 = (height - v) / height, y1 = y0;
        float z0 = DEPTH_0, z1 = DEPTH_1;
        float aU = u, aV = v;
        switch (side) {
            case DOWN:
                aV -= 1;
            case UP:
                x1 += ONE_PIXEL;
                break;
            case WEST:
                aU += 1;
            case EAST:
                y0 -= ONE_PIXEL;
                x0 += ONE_PIXEL;
                x1 += ONE_PIXEL;
                break;
            default:
                break;
        }

        putVertex(builder, side, x0, y0, z0, sprite.getU(aU), sprite.getV(aV), 0, 0);
        putVertex(builder, side, x1, y1, z0, sprite.getU(aU + 1), sprite.getV(aV + 1), 0, 0);
        putVertex(builder, side, x1, y1, z1, sprite.getU(aU + 1), sprite.getV(aV + 1), 0, 0);
        putVertex(builder, side, x0, y0, z1, sprite.getU(aU), sprite.getV(aV), 0, 0);

        return builder.build();
    }

    private static BakedQuadBuilder builder(TextureAtlasSprite sprite, Direction side, int tint, boolean diffuse) {
        var builder = new BakedQuadBuilder(sprite);
        builder.setQuadTint(tint);
        builder.setQuadOrientation(side);
        builder.setApplyDiffuseLighting(diffuse);
        return builder;
    }

    private static BakedQuad genQuad(TextureAtlasSprite sprite, Direction side, int tint, boolean diffuse,
                                     float x0, float y0, float z0, float u0, float v0,
                                     float x1, float y1, float z1, float u1, float v1,
                                     float x2, float y2, float z2, float u2, float v2,
                                     float x3, float y3, float z3, float u3, float v3) {
        var builder = builder(sprite, side, tint, diffuse);
        putVertex(builder, side, x0, y0, z0, sprite.getU(u0), sprite.getV(v0), 0, 0);
        putVertex(builder, side, x1, y1, z1, sprite.getU(u1), sprite.getV(v1), 0, 0);
        putVertex(builder, side, x2, y2, z2, sprite.getU(u2), sprite.getV(v2), 0, 0);
        putVertex(builder, side, x3, y3, z3, sprite.getU(u3), sprite.getV(v3), 0, 0);
        return builder.build();
    }

    private static void putVertex(IVertexConsumer consumer, Direction side, float x, float y, float z, float u, float v, int uLight, int vLight) {
        var format = consumer.getVertexFormat();
        for (int e = 0; e < format.getElements().size(); e++) {
            var element = format.getElements().get(e);
            switch (element.getUsage()) {
                case POSITION:
                    consumer.put(e, x, y, z, 1.0f);
                    break;
                case COLOR:
                    consumer.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case NORMAL:
                    consumer.put(e, side.getStepX(), side.getStepY(), side.getStepZ(), 0.0f);
                    break;
                case UV:
                    if (element.getIndex() == 0) {
                        consumer.put(e, u, v, 0f, 1f);
                    } else if (element.getIndex() == 2) {
                        consumer.put(e, (uLight << 4) / 32768.0f, (vLight << 4) / 32768.0f, 0, 1);
                    }
                    break;
                default:
                    consumer.put(e);
                    break;
            }
        }
    }
}

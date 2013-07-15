package organicchem.core.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

public class BlockTextureStitched extends TextureAtlasSprite
{

    public BlockTextureStitched(String name, int subIndex)
    {
        super(name);
        this.subIndex = subIndex;
    }

    public void copyFrom(TextureAtlasSprite textureStitched)
    {
        if(textureStitched.getIconName().equals("missingno") && mappedTexture != null)
            super.copyFrom(mappedTexture);
        else
            super.copyFrom(textureStitched);
    }

    public void updateAnimation()
    {
    }

    public boolean load(ResourceManager manager, ResourceLocation location)
        throws IOException
    {
        String name = location.func_110623_a();
        int index = name.indexOf(':');
        if(index != -1)
        {
            int extStart = name.lastIndexOf('.');
            location = new ResourceLocation(location.func_110624_b(), (new StringBuilder()).append(name.substring(0, index)).append(name.substring(extStart)).toString());
        }
        return loadSubImage(manager.func_110536_a(location));
    }

    public boolean loadSubImage(Resource res)
        throws IOException
    {
        String name = getIconName();
        BufferedImage bufferedImage = (BufferedImage)cachedImages.get(name);
        if(bufferedImage == null)
        {
            bufferedImage = ImageIO.read(res.func_110527_b());
            cachedImages.put(name, bufferedImage);
        }
        int size = bufferedImage.getHeight();
        int count = bufferedImage.getWidth() / size;
        int index = subIndex;
        if(count == 1 || count == 6 || count == 12)
            index %= count;
        else
        if(count == 2)
        {
            index /= 6;
        } else
        {
            //IC2.log.warning((new StringBuilder()).append("texture ").append(name).append(" is not properly sized").toString());
            throw new IOException();
        }
        field_130223_c = size;
        field_130224_d = size;
        comparisonImage = bufferedImage.getSubimage(index * size, 0, size, size);
        int rgbaData[] = new int[size * size];
        comparisonImage.getRGB(0, 0, size, size, rgbaData, 0, size);
        int hash = Arrays.hashCode(rgbaData);
        java.util.List matchingTextures = (java.util.List)existingTextures.get(Integer.valueOf(hash));
        if(matchingTextures != null)
        {
            int rgbaData2[] = new int[size * size];
            for(Iterator i$ = matchingTextures.iterator(); i$.hasNext();)
            {
                BlockTextureStitched matchingTexture = (BlockTextureStitched)i$.next();
                if(matchingTexture.comparisonImage.getWidth() == size)
                {
                    matchingTexture.comparisonImage.getRGB(0, 0, size, size, rgbaData2, 0, size);
                    if(Arrays.equals(rgbaData, rgbaData2))
                    {
                        mappedTexture = matchingTexture;
                        return false;
                    }
                }
            }

            matchingTextures.add(this);
        } else
        {
            matchingTextures = new ArrayList();
            matchingTextures.add(this);
            existingTextures.put(Integer.valueOf(hash), matchingTextures);
        }
        field_110976_a.add(rgbaData);
        return true;
    }

    public Icon getRealTexture()
    {
        return ((Icon) (mappedTexture != null ? mappedTexture : this));
    }

    public static void onPostStitch()
    {
        for(Iterator i$ = existingTextures.values().iterator(); i$.hasNext();)
        {
            java.util.List textures = (java.util.List)i$.next();
            Iterator j = textures.iterator();
            while(j.hasNext()) 
            {
                BlockTextureStitched texture = (BlockTextureStitched)j.next();
                texture.comparisonImage = null;
            }
        }

        cachedImages.clear();
        existingTextures.clear();
    }

    private BufferedImage comparisonImage;
    private TextureAtlasSprite mappedTexture;
    private final int subIndex;
    private static Map cachedImages = new HashMap();
    private static Map existingTextures = new HashMap();

}

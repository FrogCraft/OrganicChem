package organicchem.core.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.client.texturepacks.ITexturePack;

public class BlockTextureStitched extends TextureStitched {

    public BlockTextureStitched(String name)
    {
        super(name);
    }

    @Override
    public void copyFrom(TextureStitched textureStitched)
    {
        if(textureStitched.getIconName().equals("missingno") && mappedTexture != null)
            super.copyFrom(mappedTexture);
        else
            super.copyFrom(textureStitched);
    }

    @Override
    public boolean loadTexture(TextureManager manager, ITexturePack texturepack, String name, String fileName, java.awt.image.BufferedImage image, ArrayList textures)
    {
        int extStart = fileName.lastIndexOf('.');
        int indexStart = fileName.lastIndexOf('.', extStart - 1);
        String textureFile = (new StringBuilder()).append("/").append(fileName.substring(0, indexStart)).append(".png").toString();
        int index = Integer.parseInt(fileName.substring(indexStart + 1, extStart));
        java.awt.image.BufferedImage bufferedImage;
        try
        {
            bufferedImage = ImageIO.read(texturepack.getResourceAsStream(textureFile));
        }
        catch(IOException e)
        {
            //IC2.log.warning((new StringBuilder()).append("can't read texture ").append(textureFile).toString());
            return false;
        }
        int size = bufferedImage.getHeight();
        int count = bufferedImage.getWidth() / size;
        if(count == 1 || count == 6 || count == 12)
            index %= count;
        else
        if(count == 2)
        {
            index /= 6;
        } else
        {
            //IC2.log.warning((new StringBuilder()).append("texture ").append(textureFile).append(" is not properly sized").toString());
            return false;
        }
        bufferedImage = bufferedImage.getSubimage(index * size, 0, size, size);
        int rgbaData[] = new int[size * size];
        bufferedImage.getRGB(0, 0, size, size, rgbaData, 0, size);
        int hash = Arrays.hashCode(rgbaData);
        List matchingTextures = (List)existingTextures.get(Integer.valueOf(hash));
        if(matchingTextures != null)
        {
            for(Iterator i$ = matchingTextures.iterator(); i$.hasNext();)
            {
                BlockTextureStitched matchingTexture = (BlockTextureStitched)i$.next();
                if(Arrays.equals(rgbaData, matchingTexture.comparisonImage))
                {
                    mappedTexture = matchingTexture;
                    return true;
                }
            }

            matchingTextures.add(this);
        } else
        {
            matchingTextures = new ArrayList();
            matchingTextures.add(this);
            existingTextures.put(Integer.valueOf(hash), matchingTextures);
        }
        comparisonImage = rgbaData;
        Texture texture = new Texture(name, 2, size, size, 10496, 6408, 9728, 9728, bufferedImage);
        textures.add(texture);
        return true;
    }

    //TODO what does this method do?
    public static void onPostStitch()
    {
        for(Iterator i$ = existingTextures.values().iterator(); i$.hasNext();)
        {
            List textures = (List)i$.next();
            Iterator ii$ = textures.iterator();
            while(ii$.hasNext()) 
            {
                BlockTextureStitched texture = (BlockTextureStitched)ii$.next();
                texture.comparisonImage = null;
            }
        }

        existingTextures.clear();
    }

    private int comparisonImage[];
    private TextureStitched mappedTexture;
    private static Map existingTextures = new HashMap();


}

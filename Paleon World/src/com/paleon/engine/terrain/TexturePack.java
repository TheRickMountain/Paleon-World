package com.paleon.engine.terrain;

import com.paleon.engine.graph.Texture;

/**
 * Created by Rick on 08.10.2016.
 */
public class TexturePack {

    public final Texture blendMap;
    public final Texture redTexture;
    public final Texture greenTexture;
    public final Texture blueTexture;
    public final Texture alphaTexture;

    public TexturePack(Texture blendMap, Texture redTexture, Texture greenTexture, Texture blueTexture, Texture alphaTexture) {
        this.blendMap = blendMap;
        this.redTexture = redTexture;
        this.greenTexture = greenTexture;
        this.blueTexture = blueTexture;
        this.alphaTexture = alphaTexture;
    }
}

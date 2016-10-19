package com.paleon.engine.world;

import com.paleon.engine.components.Image;
import com.paleon.engine.graph.Texture;
import com.paleon.engine.graph.transform.Transform2D;
import com.paleon.engine.scenegraph.Entity3D;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.utils.Color;

/**
 * Created by Rick on 17.10.2016.
 */
public class Button extends Entity3D {

    private ButtonBh buttonBh;

    public Button(World world, String name, Texture texture) {
        super(world, name);

        addComponent(new Image(texture, Color.WHITE));
        setTransform(new Transform2D());
        buttonBh = new ButtonBh();
        addBehaviour(buttonBh);
    }

    public boolean isOverMouse() {
        return buttonBh.isOverMouse();
    }

    public boolean isPressedDown(int button) {
        return buttonBh.isPressedDown(button);
    }

    public boolean isPressedUp(int button) {
        return buttonBh.isPressedUp(button);
    }

    public boolean isPressed(int button) {
        return buttonBh.isPressed(button);
    }

}

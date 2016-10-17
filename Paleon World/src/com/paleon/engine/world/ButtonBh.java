package com.paleon.engine.world;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.input.Mouse;

/**
 * Created by Rick on 17.10.2016.
 */
public class ButtonBh extends Behaviour {


    @Override
    public void start() {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void onGUI() {

    }

    public boolean isOverMouse() {
        float x = parent.position.x;
        float y = parent.position.y;

        return (Mouse.getX() >= x && Mouse.getX() <= x + parent.scale.x &&
                Mouse.getY() >= y && Mouse.getY() <= y + parent.scale.y);
    }

    public boolean isPressedDown(int button) {
        if(isOverMouse()) {
            if(Mouse.isButtonDown(button)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPressedUp(int button) {
        if(isOverMouse()) {
            if(Mouse.isButtonUp(button)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPressed(int button) {
        if(isOverMouse()) {
            if(Mouse.isButton(button)) {
                return true;
            }
        }

        return false;
    }

}

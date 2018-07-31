package com.example.wave.receiver.ModelClasses;

/**
 * Created by wave on 3/16/2018.
 */

public class Wifimodel {
    CharSequence name;
    public String capabilities;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int level;

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }
}

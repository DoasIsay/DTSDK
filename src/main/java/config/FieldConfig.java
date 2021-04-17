package config;

import util.Checkable;

public class FieldConfig implements Checkable {
    public String name;
    public String type = "String";
    public int length  = 0;

    public void check() {
        if (name == null)
            throw new RuntimeException("FieldConfig name must not null");
    }
}
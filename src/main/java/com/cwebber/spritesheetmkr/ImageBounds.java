/**
 * 
 */
package com.cwebber.spritesheetmkr;

/**
 * @author Chris Webber
 *
 */
public class ImageBounds
{
    private String name; 
    private int x;
    private int y;
    private int width;
    private int height;
    
    public ImageBounds()
    {}
    
    public ImageBounds(String name, int x, int y, int width, int height)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public String toString()
    {
        return new String("ImageBounds[Name: " + name + " X: " + x + " Y: " + y + " Width: " + width + " Height: " + height + "]");
    }
}
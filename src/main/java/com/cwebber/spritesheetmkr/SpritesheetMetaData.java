/**
 * 
 */
package com.cwebber.spritesheetmkr;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chris Webber
 *
 */
public class SpritesheetMetaData
{
    private String name;
    private int width;
    private int height;
    private List<ImageBounds> imageBounds;
    
    public SpritesheetMetaData()
    {
        imageBounds = new ArrayList<ImageBounds>();
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public List<ImageBounds> getImageBounds()
    {
        return imageBounds;
    }

    public void setImageBounds(List<ImageBounds> imageBounds)
    {
        this.imageBounds = imageBounds;
    }

    public String toString()
    {
        return new String("SpritesheetMetaData[Width: " + width + " Height: " + height + "]");
    }
}

package com.cwebber.spritesheetmkr;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;


/**
 * @author Chris Webber
 *
 */
public class SpritePack
{
    public static void main(String[] args)
    {
        if (args.length < 3)
        {
            System.out.println("ERROR: Missing arguments.");
            showHelp();
            
            System.exit(0);
        }
        
        int rowLength = 0;
        try
        {
            rowLength = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e)
        {
            System.out.println("ERROR: Row length must be a number greater than 0.");
            showHelp();
            
            System.exit(0);
        }
        
        if (rowLength <= 0)
        {
            System.out.println("ERROR: Row length must be a number greater than 0.");
            showHelp();
            
            System.exit(0);
        }
        
        //TODO: check if is a valid system name.
        String imgOutputPath = args[1];
        
        String outputFormat = "png";
        
        String[] imgPaths = Arrays.stream(args, 2, args.length).toArray(String[]::new);
        
        try
        {
            SpritesheetMetaData metaData = calculateMetaData(rowLength, imgPaths);
            
//            System.out.println(metaData);
            
            BufferedImage spritesheet = createSpritesheet(rowLength, metaData, imgPaths);
            
            File imgOutputFile = new File(imgOutputPath);
            
            metaData.setName(imgOutputFile.getName());
            
            writeSpriteSheet(spritesheet, outputFormat, imgOutputFile);
            
            File metaDataOutFile = getMetaDataOutputFile(imgOutputPath);
            
            writeMetaData(metaData, metaDataOutFile);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            
            System.exit(0);
        }
    }
    
    public static BufferedImage createSpritesheet(int rowLength, SpritesheetMetaData metaData, String[] imgPaths)
    {
        if (rowLength <= 0)
        {
            throw new IllegalArgumentException("rowLength must be greater than 0.");
        }
        
        if (metaData == null)
        {
            throw new IllegalArgumentException("metaData must not be null.");
        }
        
        if (imgPaths == null || imgPaths.length == 0)
        {
            throw new IllegalArgumentException("imgPaths must be a non-empty list of file paths.");
        }
        
        BufferedImage spritesheet = new BufferedImage(metaData.getWidth(), metaData.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) spritesheet.getGraphics();
        
        List<ImageBounds> imageBounds = new ArrayList<ImageBounds>();
        
        int rowImgCount = 0;
        int rowMaxHeight = 0;
        int currX = 0;
        int currY = 0;
        for (String imgPath : imgPaths)
        {
            try
            {
                File imgFile = new File(imgPath);
                BufferedImage img = ImageIO.read(imgFile);
                if (img != null)
                {
                    g2d.drawImage(img, currX, currY, null);
                    
                    ImageBounds bounds = new ImageBounds(imgFile.getName(), currX, currY, img.getWidth(),
                        img.getHeight());
                    imageBounds.add(bounds);
                    
                    currX += img.getWidth();
                    
                    int imgHeight = img.getHeight();
                    if (rowMaxHeight < imgHeight)
                    {
                        rowMaxHeight = imgHeight;
                    }
                    
                    ++rowImgCount;
                    if (rowImgCount == rowLength)
                    {
                        currX = 0;
                        currY += rowMaxHeight;
                        
                        rowImgCount = 0;
                        rowMaxHeight = 0;
                    }
                }
                else
                {
                    throw new IllegalStateException("Could not read image " + imgPath);
                }
            }
            catch (NullPointerException e)
            {
                throw new IllegalStateException("Image path cannot be null");
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalStateException("Image file cannot be null");
            }
            catch (IOException e)
            {
                throw new IllegalStateException("Could not read image " + imgPath);
            }
        }
        
        metaData.setImageBounds(imageBounds);
        
        return spritesheet;
    }
    
    public static void writeSpriteSheet(BufferedImage spritesheet, String outputFormat,
        File outputFile) throws Exception
    {
        try
        {
            ImageIO.write(spritesheet, outputFormat, outputFile);
            System.out.println("Writing file to " + outputFile.getPath());
        }
        catch (Exception e)
        {
            System.out.println("ERROR: Unable to write file to " + outputFile.getPath());
            
            throw e;
        }
    }
    
    public static SpritesheetMetaData calculateMetaData(int rowLength, String[] imgPaths)
    {
        if (rowLength <= 0)
        {
            throw new IllegalArgumentException("rowLength must be greater than 0.");
        }
        
        if (imgPaths == null || imgPaths.length == 0)
        {
            throw new IllegalArgumentException("imgPaths must be a non-empty list of file paths.");
        }
        
        SpritesheetMetaData metaData = new SpritesheetMetaData();
        
        int totalHeight = 0;
        int rowWidth = 0;
        int maxRowWidth = 0;
        int rowMaxHeight = 0;
        int rowImgCount = 0;
        for (String imgPath : imgPaths)
        {
            try
            {
                File imgFile = new File(imgPath);
                BufferedImage img = ImageIO.read(imgFile);
                if (img != null)
                {
                    int imgWidth = img.getWidth();
                    int imgHeight = img.getHeight();
                    if (rowMaxHeight < imgHeight)
                    {
                        rowMaxHeight = imgHeight;
                    }
                    rowWidth += imgWidth;
                    
                    if (rowImgCount == rowLength - 1)
                    {
                        totalHeight += rowMaxHeight;
                        
                        if (maxRowWidth < rowWidth)
                        {
                            maxRowWidth = rowWidth;
                        }
                        
                        rowImgCount = 0;
                        rowMaxHeight = 0;
                        rowWidth = 0;
                    }
                    else
                    {
                        ++rowImgCount;
                    }
                    
                }
                else
                {
                    throw new Exception("Could not read image " + imgPath);
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        
        if (rowImgCount != 0)
        {
            totalHeight += rowMaxHeight;
            
            if (maxRowWidth < rowWidth)
            {
                maxRowWidth = rowWidth;
            }
        }
        
        metaData.setHeight(totalHeight);
        metaData.setWidth(maxRowWidth);
        
        return metaData;
    }
    
    public static File getMetaDataOutputFile(String imgOutputPath)
    {
        File file = new File(imgOutputPath);
        
        String parentPath = file.getParent();
        
        String name = file.getName();
        String[] nameParts = name.split("\\.");
        
        String outputName = "";
        if (nameParts.length == 1)
        {
            outputName = String.join(".", Arrays.copyOfRange(nameParts, 0, 1)) + "-metadata.txt";
        }
        else
        {
            outputName = String.join(".", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1)) + "-metadata.txt";
        }
        
        File outputFile = new File(parentPath, outputName);
        
        return outputFile;
    }
    
    public static void writeMetaData(SpritesheetMetaData metaData, File metaDataOutFile) throws IOException
    {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(metaDataOutFile)));)
        {
            System.out.println("Writing file to " + metaDataOutFile.getPath());
            
            writer.print("name: " + metaData.getName() + "\n");
            writer.print("width: " + metaData.getWidth() + "\n");
            writer.print("height: " + metaData.getHeight() + "\n");
            writer.print("\n");
            writer.print("images\n");
            for (ImageBounds bnd : metaData.getImageBounds())
            {
                String line = String.join("\t",
                    "name: " + bnd.getName(),
                    "x: " + bnd.getX(),
                    "y: " + bnd.getY(),
                    "width: " + bnd.getWidth(),
                    "height: " + bnd.getHeight());
                line += "\n";
                writer.print(line);
                
            }
        }
        catch (IOException e)
        {
            System.out.println("ERROR: Unable to write file to " + metaDataOutFile.getPath());
            
            throw e;
        }
    }
    
    public static void showHelp()
    {
        System.out.println("Usage:");
        System.out.println("\tsimgcombine rowLength outputFileName inputImage [inputImage...]");
        System.out.println();
        System.out.println("\trowLength:\tThe number of images to place on one row.");
        System.out.println("\toutputFileName:\tThe name of the image file to be created.");
        System.out.println("\tinputImage:\tAn image to include in the final image.");
        System.out.println();
    }
}

package com.cwebber.spritesheetmkr;

import static org.junit.Assert.assertEquals;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Chris Webber
 *
 */
public class SpritePackTest
{
    private static String[] imgFileNames = {"blue.png", "green.png", "orange.png", "red.png"};
    private static int[][] dims = {
        {200, 50},
        {75, 300},
        {400, 500},
        {100, 100}
    };
    
    private static String[] imgPaths;
    
    
    @BeforeClass
    public static void setup() throws URISyntaxException
    {
        imgPaths = new String[imgFileNames.length];
            
        for (int ii = 0; ii < imgFileNames.length; ++ii)
        {
            String fileName = imgFileNames[ii];
            
            URL url = SpritePackTest.class.getClassLoader().getResource(fileName);
            String path = Paths.get(url.toURI()).toFile().getAbsolutePath();
            imgPaths[ii] = path;
        }
    }
    
    @Test
    public void test_SingleRow()
    {
        SpritesheetMetaData md = SpritePack.calculateMetaData(imgPaths.length, imgPaths);
        
        int expectedWidth = Arrays.stream(dims).map(p -> p[0]).reduce(Integer::sum).orElse(-1);
        int expectedHeight = Arrays.stream(dims).mapToInt(p -> p[1]).max().orElse(-1);
        
        assertEquals("Incorrect width.", expectedWidth, md.getWidth());
        assertEquals("Incorrect height.", expectedHeight, md.getHeight());
        
        testCreateSpritesheet_SingleRow(md);
    }

    @Test
    public void test_SingleColumn()
    {
        SpritesheetMetaData md = SpritePack.calculateMetaData(1, imgPaths);
        
        int expectedWidth = Arrays.stream(dims).mapToInt(p -> p[0]).max().orElse(-1);;
        int expectedHeight = Arrays.stream(dims).map(p -> p[1]).reduce(Integer::sum).orElse(-1);
        
        assertEquals("Incorrect width.", expectedWidth, md.getWidth());
        assertEquals("Incorrect height.", expectedHeight, md.getHeight());
        
        testCreateSpritesheet_SingleColumn(md);
    }
    
    @Test
    public void test_Mixed()
    {
        SpritesheetMetaData md = SpritePack.calculateMetaData(2, imgPaths);
        
        int expectedWidth = Integer.max(dims[0][0] + dims[1][0], dims[2][0] + dims[3][0]);
        int expectedHeight = Integer.max(dims[0][1], dims[1][1]) + Integer.max(dims[2][1], dims[3][1]);
        
        assertEquals("Incorrect width.", expectedWidth, md.getWidth());
        assertEquals("Incorrect height.", expectedHeight, md.getHeight());
        
        testCreateSpritesheet_Mixed(md);
    }
    
    public void testCreateSpritesheet_SingleRow(SpritesheetMetaData md)
    {
        BufferedImage img = SpritePack.createSpritesheet(imgPaths.length, md, imgPaths);
        
        assertEquals("Incorrect width.", md.getWidth(), img.getWidth());
        assertEquals("Incorrect height.", md.getHeight(), img.getHeight());
        
        List<ImageBounds> ibs = md.getImageBounds();
        
        assertEquals("ImageBounds is not the same size as the number of images processed.", imgPaths.length, ibs.size());
        
        int currX = 0;
        int currY = 0;
        for (int ii = 0; ii < ibs.size(); ++ii)
        {
            ImageBounds ib = ibs.get(ii);
            int[] dim = dims[ii];
            
            assertEquals("ImageBounds contains incorrect width.", dim[0], ib.getWidth());
            assertEquals("ImageBounds contains incorrect height.", dim[1], ib.getHeight());
            assertEquals("ImageBounds contains incorrect X.", currX, ib.getX());
            assertEquals("ImageBounds contains incorrect Y.", currY, ib.getY());
            
            currX += ib.getWidth();
//            currY += ib.getHeight();
        }
    }
    
    public void testCreateSpritesheet_SingleColumn(SpritesheetMetaData md)
    {
        BufferedImage img = SpritePack.createSpritesheet(1, md, imgPaths);
        
        assertEquals("Incorrect width.", md.getWidth(), img.getWidth());
        assertEquals("Incorrect height.", md.getHeight(), img.getHeight());
        
        List<ImageBounds> ibs = md.getImageBounds();
        
        assertEquals("ImageBounds is not the same size as the number of images processed.", imgPaths.length, ibs.size());
        
        int currX = 0;
        int currY = 0;
        for (int ii = 0; ii < ibs.size(); ++ii)
        {
            ImageBounds ib = ibs.get(ii);
            int[] dim = dims[ii];
            
            assertEquals("ImageBounds contains incorrect width.", dim[0], ib.getWidth());
            assertEquals("ImageBounds contains incorrect height.", dim[1], ib.getHeight());
            assertEquals("ImageBounds contains incorrect X.", currX, ib.getX());
            assertEquals("ImageBounds contains incorrect Y.", currY, ib.getY());
            
//            currX += ib.getWidth();
            currY += ib.getHeight();
        }
    }
    
    public void testCreateSpritesheet_Mixed(SpritesheetMetaData md)
    {
        BufferedImage img = SpritePack.createSpritesheet(2, md, imgPaths);
        
        assertEquals("Incorrect width.", md.getWidth(), img.getWidth());
        assertEquals("Incorrect height.", md.getHeight(), img.getHeight());
        
        List<ImageBounds> ibs = md.getImageBounds();
        
        assertEquals("ImageBounds is not the same size as the number of images processed.", imgPaths.length, ibs.size());
        
        int currX = 0;
        int currY = 0;
        int maxHeight = 0;
        for (int ii = 0; ii < ibs.size(); ++ii)
        {
            ImageBounds ib = ibs.get(ii);
            int[] dim = dims[ii];
            
            assertEquals("ImageBounds contains incorrect width.", dim[0], ib.getWidth());
            assertEquals("ImageBounds contains incorrect height.", dim[1], ib.getHeight());
            assertEquals("ImageBounds contains incorrect X.", currX, ib.getX());
            assertEquals("ImageBounds contains incorrect Y.", currY, ib.getY());
            
            if (ib.getHeight() > maxHeight)
            {
                maxHeight = ib.getHeight();
            }

            if (ii == 1)
            {
                currX = 0;
                currY += ib.getHeight();
            }
            else
            {
                currX += ib.getWidth();
            }
        }
    }
    
    @Test
    public void testGetMetaDataOutputFile_NormalPath()
    {
        String testPath = "C:\\test\\file.txt";
        String expectedPath = "C:\\test\\file-metadata.txt";
        
        File f = SpritePack.getMetaDataOutputFile(testPath);
        
        assertEquals("Incorrect metadata output file path.", expectedPath, f.getAbsolutePath());
    }
    
    @Test
    public void testGetMetaDataOutputFile_ExtraPeriods()
    {
        String testPath = "C:\\test\\this.my.file.txt";
        String expectedPath = "C:\\test\\this.my.file-metadata.txt";
        
        File f = SpritePack.getMetaDataOutputFile(testPath);
        
        assertEquals("Incorrect metadata output file path.", expectedPath, f.getAbsolutePath());
    }
    
    @Test
    public void testGetMetaDataOutputFile_NoPeriods()
    {
        String testPath = "C:\\test\\file";
        String expectedPath = "C:\\test\\file-metadata.txt";
        
        File f = SpritePack.getMetaDataOutputFile(testPath);
        
        assertEquals("Incorrect metadata output file path.", expectedPath, f.getAbsolutePath());
    }
}

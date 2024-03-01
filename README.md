## simgcombine

### Usage
<pre>
  java -jar simgcombine.jar rowLength outputFileName inputImage [inputImage...]

  rowLength       The number of images to place on one row.
  outputFileName  The name of the image file to be created.
  inputImage      An image to include in the output image.
</pre>

This program combines multiple images into one image. It does this by 
taking each specified input image and copying it onto the new image, moving 
from left to right and top to bottom according to the specified row length.

This script does not try to pack images together to maximize space, but only
processes each image in order. It is left to the user to determine the preferred 
order.

In addition, a metadata file will be output to accompany the new image. This file
lists the name, width, and height of the produced image, as well as tab-separated 
lines containing the x,y position and width and height of each included image. 
This will hopefully make operations like clipping and such easier.

Currently, only PNG image types are produced.


### Code
This program was originally created in Eclipse, but only the source code and the
Maven POM file are included here in order to be environment neutral.

Testing has only been done on Windows, not Linux, though ostensibly things should
still work on Linux since handling of files and paths is done through standard 
classes of the Java platform.

The POM lists Java 17 as the required version, but I don't think the code uses
anything past Java 8. 
package lk.rgd.prs.web;

import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.tools.UnitConv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *  Generates and serves a badcode image of a person registered in PRS
 *  @author: Ashoka Ekanayaka
 */
public class ImageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            // Hard-coded for a GIF image.
            response.setContentType("image/gif");
            //response.setContentLength(length);

            // Get the output stream from our response object, so we
            // can write our image data to the client:
            ServletOutputStream out = response.getOutputStream();

            // Now, loop through buffer reads of our content, and send it to the client:
    //        byte [] buffer = new byte[1024];
    //        int len;
    //        while ((len = iStream.read(buffer)) != -1) {
    //            oStream.write(buffer, 0, len);
    //        }

            // Now that we've sent the image data to the client, close down all the resources:

            try {
                //Create the barcode bean
                DataMatrixBean bean = new DataMatrixBean();

                final int dpi = 150;

                //Configure the barcode generator
                bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
                                                                 //width exactly one pixel
                //bean.setWideFactor(3);
                bean.setHeight(1.0);
                bean.setModuleWidth(1.0);
                bean.doQuietZone(false);

                //Set up the canvas provider for monochrome JPEG output
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                        out, "image/gif", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

                //Generate the barcode
                bean.generateBarcode(canvas, "123456" + "name");

                //Signal end of generation
                canvas.finish();
            } finally {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(
        HttpServletRequest request, HttpServletResponse response
        ) throws ServletException, IOException {

        // Pass through to the doPost method:
        doGet(request, response);
    }
}

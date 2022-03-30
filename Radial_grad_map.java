/*
 * Author @Diptatanu_Das
 */
package radiality;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import ij.gui.GenericDialog;

public class Radial_grad_map {

    public ImagePlus imp;
    ImageStack ims;
    ImageCanvas impcan;
    ImageWindow impwin;
    int imsize;
    int oW;
    int oH;
    int f;
    int p;
    int b;
    
    public void loadImage() {

        MouseListener impcanDisplayImageMouseUsed = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {

                Roi roi = imp.getRoi();
                Roi roi2;
                Overlay impov;

                // Find out if ROI is valid
                if (true) { //TODO
                    roi.setStrokeColor(Color.green);
                    imp.setRoi(roi);

                } else {
                    IJ.log("invalid ROI selction, setting default roi");
                    //set the ROI in the image
                    if (imp.getOverlay() != null) {
                        imp.getOverlay().clear();
                        imp.setOverlay(imp.getOverlay());
                    }
                }

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

        };

        imp = IJ.openImage();
        imp.show();
        ims = imp.getImageStack();
        
        impwin = imp.getWindow();
        impcan = imp.getCanvas();

        imsize = imp.getStackSize();
        oW = imp.getWidth();
        oH = imp.getHeight();

        impcan.addMouseListener(impcanDisplayImageMouseUsed);
        
        IJ.log("you have imagestack of W: " + oW + ", H: " + oH + ", Z: " + imsize);
    }
    public void initialize() {
        int[] parameters = new Radial_grad_map().parameter_dialogbox(); //stores the user input
        int[] nback = new int[1];
        f = parameters[0];
        p = parameters[1];
        if (p == 1){
            nback = new Radial_grad_map().background_dialogbox(); //stores the number of frames for average background calculation
            b = nback[0];
        }
        else
            p = 1;
        IJ.log("Parameters set as : ");
        IJ.log("Number of sub-pixels each pixel is to be splitted into : " + String.valueOf(f));
        IJ.log("Number of frames over which average background will be calculated : " + String.valueOf(b));
    }
    public void run() {
            int w = oW;
            int w0 = oW;
            int m = imsize;
            int isBack = b;
            ArrayList<ArrayList<ArrayList<Double>>> Master_data = new ArrayList<ArrayList<ArrayList<Double>>>(m);
            ArrayList<ArrayList<ArrayList<Double>>> Master_data_center = new ArrayList<ArrayList<ArrayList<Double>>>(m);
            int n = (int) Math.floor(m/p);   
            int fac = (int)Math.sqrt(f);
            w = w * fac;
            float[] pixels;
            float [][] im0 = new float[w][w];
            ImagePlus imgRadGrad = IJ.createImage("Radial Gradient Magnitude", "GRAY32", w-1, w-1, m);
            float [][] imbb = new float [w][w];
            for (int i = 0; i < n; i++)  {
                float[][] imb = new float [w0][w0];
                if (isBack==1){
                    imb = new Radial_grad_map().average_background(ims, i, p, w0);
                }
                
                if (fac>1){
                // putting pixel values of average background of the original image stack in a bigger 2D matrix so as to achieve pixel splitting
                    int c1 = 0, c2 = 0;
                    for (int j = 0; j < (w/fac); j++){
                        c2 = 0;
                        for (int k = 0; k < (w/fac); k++){
                            imbb [c1][c2] = imb[j][k];
                            imbb [c1+1][c2] = imb[j][k];
                            imbb [c1][c2+1] = imb[j][k];
                            imbb [c1+1][c2+1] = imb[j][k];
                            c2+=fac;
                        }
                    c1+=fac;
                    }
                }
                for (int j = 0; j < p; j++)  {
                    IJ.showProgress(i*p+j, n*p);
                    pixels = (float[])ims.getPixels(i*p+j+1);
                if (fac>1){
                    int c1 = 0, c2 = 0;
            // putting pixel values in a bigger 2D matrix so as to achieve pixel splitting
                    for (int k = 0; k < (w/fac); k++) {
                        c2 = 0;
                        for (int l = 0; l < (w/fac); l++) {
                            im0[c1][c2] = pixels[k + w0 * l];
                            im0[c1+1][c2] = pixels[k + w0 * l];
                            im0[c1][c2+1] = pixels[k + w0 * l];
                            im0[c1+1][c2+1] = pixels[k + w0 * l];
                            c2+=fac;
                        }
                        c1+=fac;
                    }
                    for (int k = 0; k < w; k++) {
                        for (int l = 0; l < w; l++) {
                            im0[k][l] = im0[k][l] - imbb [k][l]; // substracting average background of the image stack
                        }
                    }
                }
                else {
                    for (int k = 0; k < w; k++) {
                        for (int l = 0; l < w; l++) {
                            im0[k][l] = pixels[k + w0 * l];
                        }
                    }
                    for (int k = 0; k < w; k++) {
                        for (int l = 0; l < w; l++) {
                            im0[k][l] = im0[k][l] - imb [k][l]; // substracting average background of the image stack
                        }
                    }
                }
                    ArrayList<ArrayList<Double>> data = new Radial_grad_map().radial_gradient(im0);
                    for (int k = 0; k < m; k++){
                        Master_data.add(new ArrayList());
                        for (int l = 0; l < 2; l++){
                            Master_data.get(k).add(new ArrayList());
                        }
                    }
                    for (int k = 0; k < m; k++){
                        Master_data_center.add(new ArrayList());
                        for (int l = 0; l < 2; l++){
                            Master_data_center.get(k).add(new ArrayList());
                        }
                    }
                    Master_data.set(i*p+j, data);
                    ArrayList<ArrayList<Double>> center = new Radial_grad_map().calc_Particle_Center(w, data);
                    Master_data_center.set(i*p+j, center);
                }
            }
            IJ.log("Radial gradient data contents:");
            for (int i = 0; i < m; i++){
                IJ.log(String.valueOf(Master_data.get(i)));
            }
            for (int i = 0; i < m; i++){
                if (fac == 1){
                    IJ.log("Xc:" + String.valueOf(Master_data_center.get(i).get(0).get(0)));
                    IJ.log("Yc:" + String.valueOf(Master_data_center.get(i).get(1).get(0)));
                }
                else{
                    IJ.log("Uncorrected Xc:" + String.valueOf(Master_data_center.get(i).get(0).get(0)));
                    IJ.log("Uncorrected Yc:" + String.valueOf(Master_data_center.get(i).get(1).get(0)));
                    IJ.log("Xc:" + String.valueOf(Master_data_center.get(i).get(0).get(0) / fac));
                    IJ.log("Yc:" + String.valueOf(Master_data_center.get(i).get(1).get(0) / fac));
                }
            }
            for (int i = 0; i < m; i++)  {
                ImageProcessor imgRadGradMap = imgRadGrad.getStack().getProcessor(i+1);
                for (int j = 0; j < w - 1; j++)  {
                    for (int k = 0; k < w - 1; k++)  {//
                        imgRadGradMap.putPixelValue( j, k, Master_data.get(i).get(0).get((j*(w-1)+k)));
                    }
                }
            }
            imgRadGrad.show();
        }
        
        public ArrayList<ArrayList<Double>> calc_Particle_Center(int w, ArrayList<ArrayList<Double>> data){
            ArrayList<ArrayList<Double>> centroid_map = new ArrayList<ArrayList<Double>>(2);
            for (int i = 0; i < 2; i++){
                centroid_map.add(new ArrayList());
            }
            double [][] mag = new double[w-1][w-1];
            double [][] slope = new double[w-1][w-1];
            double [][] b = new double[w-1][w-1];
            double [][] wt = new double[w-1][w-1];
            double sdI2, xcentroid, ycentroid;
            sdI2 = xcentroid = ycentroid = 0.0;
            for (int i = 0; i < w-1; i++){
                for (int j = 0; j < w-1; j++){
                    mag[i][j] = data.get(0).get(i*(w-1)+j);
                    slope[i][j] = data.get(1).get(i*(w-1)+j);
                }
            }
            for (int i = 0; i < w-1; i++){
                for (int j = 0; j < w-1; j++){
                    sdI2 += mag[i][j];
                }
            }
            for (int i = 0; i < w-1; i++){
                for (int j = 0; j < w-1; j++){
                    b[i][j] = (double)(j+1) - slope[i][j] * (i+1);
                    xcentroid += ((mag[i][j] * (i+0.5))/sdI2);
                    ycentroid += ((mag[i][j] * (j+0.5))/sdI2);
                    wt[i][j]  = mag[i][j]/Math.sqrt(((i+0.5)-xcentroid)*((i+0.5)-xcentroid)+((j+0.5)-ycentroid)*((j+0.5)-ycentroid)); //weighting based on pixel intensity
                }
            }
            double[] center_data = new Radial_grad_map().radial_center(slope, b, wt, w);
            IJ.log("Xk:" + String.valueOf(xcentroid));
            IJ.log("Yk:" + String.valueOf(ycentroid));
            centroid_map.get(0).add(center_data[0]);
            centroid_map.get(1).add(center_data[1]);
            return centroid_map;
        }
    
        ArrayList<ArrayList<Double>> radial_gradient(float img[][]){
        int n = img.length;
        float pix_a, pix_b, pix_c, pix_d;
        double m, df_d1, df_d2, mag;
        ArrayList<ArrayList<Double>> grad_map = new ArrayList<ArrayList<Double>>(2);
        grad_map.add(new ArrayList());
        grad_map.add(new ArrayList());
        for (int i = 0; i < n-1; i++){
            for (int j = 0; j < n-1; j++){
                pix_a = img[i][j];
                pix_b = img[i][j+1];
                pix_c = img[i+1][j];
                pix_d = img[i+1][j+1];
                df_d1 = (double)(pix_a - pix_d)/Math.sqrt(2);
                df_d2 = (double)(pix_b - pix_c)/(float)Math.sqrt(2);
                mag = (df_d1 * df_d1) + (df_d2 * df_d2); // Finding diagonal differences
                if (df_d2 - df_d1 != 0.0) // Check to prevent infinite/undefined as slope value
                    m = (df_d2 + df_d1)/(df_d2 - df_d1);
                else
                    m = (df_d2 + df_d1) / Math.pow(9.0, 9);
                grad_map.get(0).add(mag);
                grad_map.get(1).add(m);
            }
        }
        return grad_map;
    }
    public float [][] average_background(ImageStack stack, int n, int p, int w){        
        short[] pix1 = new short [w*w]; 
        short[] pix2;
        float [][] imb = new float[w][w];
        
        pix2 = (short[]) stack.getPixels(n*p+1);
        System.arraycopy(pix2, 0, pix1, 0, w*w);    

        for (int j = 1; j < p; j++) {
            pix2 = (short[]) stack.getPixels(n*p+j+1);                  
            for (int k = 0; k < w*w; k++) { 
                if (pix2[k] < pix1[k]){ pix1[k] = pix2[k]; } 
            }
        }
        for (int j = 0; j < w; j++){
            for (int i = 0; i < w; i++){
                imb[i][j] = (float) (pix1[i+w*j]);
            }   
        }
        return imb;
        }
    
    public double[] radial_center(double[][] m, double[][] b, double[][] wt, int w) {
        double[] particle_center = new double[2];
        double[][] wm2p = new double[w-1][w-1];
        double sw, sm2w, smw, smbw, sbw;
        sw = sm2w = smw = smbw = sbw = 0.0;
        for (int i = 0; i < w-1; i++){
            for (int j = 0; j < w-1; j++){
                wm2p[i][j] = wt[i][j]/(m[i][j] * m[i][j] + 1);
                sw += wm2p[i][j];
                sm2w += (m[i][j] * m[i][j] * wm2p[i][j]);
                smw += (m[i][j] * wm2p[i][j]);
                smbw += (m[i][j] * b[i][j] * wm2p[i][j]);
                sbw += (b[i][j] * wm2p[i][j]);
            }
        }
        double det = smw*smw - sm2w*sw;
        IJ.log("sw: " + sw + " smw: " + smw + " sm2w: " + sm2w + " smbw: " + smbw + " sbw: " + sbw + " det: " + det);
        particle_center[0] = ((smbw*sw) - (smw*sbw))/det;
        particle_center[1] = ((smbw*smw) - (sm2w*sbw))/det;
        particle_center[0] = particle_center[0] - 0.5; //as the results were shifted by half a pixel
        particle_center[1] = particle_center[1] - 0.5; //as the results were shifted by half a pixel
        ImagePlus imgSlope = IJ.createImage("Slope", "GRAY32", w-1, w-1, 1);
        ImageProcessor imgSlopeMap = imgSlope.getStack().getProcessor(1);
        for (int j = 0; j < w - 1; j++)  {
            for (int k = 0; k < w - 1; k++)  {//
                imgSlopeMap.putPixelValue( j, k, Math.atan(m[j][k])); // Optional output for debugging
            }
        }
        imgSlope.show();
        ImagePlus imgWt = IJ.createImage("Weight", "GRAY32", w-1, w-1, 1);
        ImageProcessor imgWtMap = imgWt.getStack().getProcessor(1);
        for (int j = 0; j < w - 1; j++)  {
            for (int k = 0; k < w - 1; k++)  {//
                imgWtMap.putPixelValue( j, k, wt[j][k]);  // Optional output for debugging
            }
        }
        imgWt.show();
        ImagePlus imgInt = IJ.createImage("Intercept", "GRAY32", w-1, w-1, 1);
        ImageProcessor imgIntMap = imgInt.getStack().getProcessor(1);
        for (int j = 0; j < w - 1; j++)  {
            for (int k = 0; k < w - 1; k++)  {
                imgIntMap.putPixelValue( j, k, b[j][k]); // Optional output for debugging
            }
        }
        imgInt.show();
        ImagePlus imgWm2p = IJ.createImage("wm2p", "GRAY32", w-1, w-1, 1);
        ImageProcessor imgWm2p1Map = imgWm2p.getStack().getProcessor(1);
        for (int j = 0; j < w - 1; j++)  {
            for (int k = 0; k < w - 1; k++)  {//
                imgWm2p1Map.putPixelValue( j, k, wm2p[j][k]); // Optional output for debugging
            }
        }
        imgWm2p.show();
        return particle_center;
    }
    
    public int[] parameter_dialogbox() {
        boolean add = true;
	int val[] = new int[2];
	// a generic dialog is created here ...
	GenericDialog gd = new GenericDialog("Enter the parameters : ");
        gd.addNumericField("Number of sub-pixels each pixel is to be splitted into (please enter a square value) : ", val[0], 0);
	gd.addNumericField("Calculate Average background (if yes press 1, if no press 0 )? ", val[1], 0);
//	we don't want to allow cancel so we hide the cancel button; later try also with cancel button
	gd.hideCancelButton();
	// and finally we show the dialog
	gd.showDialog();
	// once an action happened get the values ...
        val[0] = (int)gd.getNextNumber();
        val[1] = (int)gd.getNextNumber();
        return val;
    }
    public int[] background_dialogbox() {
        boolean add = true;
	int val1[] = new int[1];
	// a generic dialog is created here ...
	GenericDialog gd = new GenericDialog("Enter the number of frames to calculate average background : ");
	gd.addNumericField("Number of frames should divide the total number of frames equally ", val1[0], 0);
//	we don't want to allow cancel so we hide the cancel button; later try also with cancel button
	gd.hideCancelButton();
	// and finally we show the dialog
	gd.showDialog();
	// once an action happened get the values ...
        val1[0] = (int)gd.getNextNumber();
        return val1;
    }
}
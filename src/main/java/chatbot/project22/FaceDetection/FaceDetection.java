package chatbot.project22.FaceDetection;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;


public class FaceDetection extends JFrame {

    //camera screen --> Camera will be shown in the JLabel component
    private JLabel cameraScreen;


    //VideoCapture class object to start the camera
    private VideoCapture videoCapture;

    //Mat class object to store images as matrices
    private Mat frameMat = new Mat();


    public FaceDetection(){
        //design ui
        setLayout(null);

        cameraScreen=new JLabel();
        cameraScreen.setBounds(0,0,640,480);
        add(cameraScreen);


        //creating a window
        setSize(640,560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //creating the camera
    public void startCamera(){
        videoCapture=new VideoCapture(0);
        frameMat=new Mat();
        byte[] imageData;
        ImageIcon icon;
        while (true){

            //read image (video frames) from the video stream
            // stores the read to matrix
            videoCapture.read(frameMat);

            //using pre-trained face detection classifier
            CascadeClassifier cascadeFaceClassifier=new CascadeClassifier("xmls/lbpcascade_frontalface.xml");

            //using pre-trained eyes detection classifier
            CascadeClassifier cascadeEyesClassifier=new CascadeClassifier("xmls/haarcascade_eye_tree_eyeglasses.xml");

            // Convert the frame to grayscale
            //the Viola-Jones algorithm for face detection works better on grayscale images.
            Mat grayMat = new Mat();
            Imgproc.cvtColor(frameMat, grayMat, Imgproc.COLOR_BGR2GRAY);

            //detect faces in the frame
            MatOfRect faceDetections= new MatOfRect(); //faces that were detected
            cascadeFaceClassifier.detectMultiScale(grayMat,faceDetections);

            System.out.println(String.format("Detected faces: %d",faceDetections.toArray().length));
            for (Rect rect: faceDetections.toArray()){
                //painting rectangles on the detected faces
                Imgproc.rectangle(frameMat,new org.opencv.core.Point(rect.x, rect.y),new Point(rect.x+ rect.width, rect.y+rect.height),new Scalar(0,0,255),3);

                //detecting eyes for each face
                //the eye detection is applied to the region of interest (each detected face)
                //using the submat we crop the roi for each face
                Mat faceROI = grayMat.submat(rect);
                MatOfRect eyeDetections = new MatOfRect();
                cascadeEyesClassifier.detectMultiScale(faceROI,eyeDetections);

                for (Rect eye : eyeDetections.toArray()){
                    Point center= new Point(rect.x+eye.width/2, rect.y+eye.height/2);
                    int radius =(int)Math.round((eye.width+eye.height)*0.25);
                    //Imgproc.circle(frameMat,center,radius,new Scalar(0, 255, 0),3);
                }
                for (Rect eyeRect: eyeDetections.toArray()){
                    //painting rectangles on the detected eyes
                    Imgproc.rectangle(frameMat, new Point(rect.x+eyeRect.x, rect.y+eyeRect.y),
                            new Point(rect.x+eyeRect.x+eyeRect.width, rect.y+eyeRect.y+eyeRect.height),
                            new Scalar(0,255,0),3);
                }
            }




            //convert matrix to byte
            final MatOfByte buffer=new MatOfByte();
            Imgcodecs.imencode(".jpg",frameMat,buffer);
            imageData=buffer.toArray();
            //add to JLabel
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);
            //capture and save to file
            // Wait for 30 milliseconds before capturing the next frame
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        //loading opencv libraries
        nu.pattern.OpenCV.loadShared();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FaceDetection faceDetection=new FaceDetection();
                //start camera in thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        faceDetection.startCamera();
                    }
                }).start();

            }
        });


    }
}

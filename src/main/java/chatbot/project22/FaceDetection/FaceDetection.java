package chatbot.project22.FaceDetection;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;



public class FaceDetection extends JFrame {


    //camera screen --> Camera will be shown in the JLabel component
    final private JLabel cameraScreen;


    //VideoCapture class object to start the camera
    private VideoCapture videoCapture;

    //Mat class object to store images as matrices
    private Mat frameMat = new Mat();

    final private Timer timer;
    public boolean faceDetected= false;

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

        // Create a timer with a 2-second delay
        timer = new Timer(2000, e -> {
            // not showing the window
            setVisible(false);
//            //closing the frame
//            dispose();
            //closing the face detection
            faceDetected = true;
            //closing the camera
            videoCapture.release();
        });
    }


    //creating the camera
    public static void startCamera(){
        FaceDetection faceDetection=new FaceDetection();
        faceDetection.videoCapture=new VideoCapture(0);
        faceDetection.frameMat=new Mat();
        byte[] imageData;
        ImageIcon icon;
        while (!faceDetection.faceDetected){

            //read image (video frames) from the video stream
            // stores the read to matrix
            faceDetection.videoCapture.read(faceDetection.frameMat);

            //using pre-trained face detection classifier
            CascadeClassifier cascadeFaceClassifier=new CascadeClassifier("xmls/lbpcascade_frontalface.xml");

            //using pre-trained eyes detection classifier
            CascadeClassifier cascadeEyesClassifier=new CascadeClassifier("xmls/haarcascade_eye_tree_eyeglasses.xml");

            // Convert the frame to grayscale
            //the Viola-Jones algorithm for face detection works better on grayscale images.
            Mat grayMat = new Mat();
            Imgproc.cvtColor(faceDetection.frameMat, grayMat, Imgproc.COLOR_BGR2GRAY);

            //detect faces in the frame
            MatOfRect faceDetections= new MatOfRect(); //faces that were detected
            cascadeFaceClassifier.detectMultiScale(grayMat,faceDetections);

            System.out.printf("Detected faces: %d%n",faceDetections.toArray().length);

            //when a face is detected
            if (faceDetections.toArray().length>0){
                // Start the timer to delay the close operation
                faceDetection.timer.start();

            }
            for (Rect rect: faceDetections.toArray()){
                //painting rectangles on the detected faces
                Imgproc.rectangle(faceDetection.frameMat,new org.opencv.core.Point(rect.x, rect.y),new Point(rect.x+ rect.width, rect.y+rect.height),new Scalar(0,0,255),3);

                //detecting eyes for each face
                //the eye detection is applied to the region of interest (each detected face)
                //using the submat we crop the roi for each face
                Mat faceROI = grayMat.submat(rect);
                MatOfRect eyeDetections = new MatOfRect();
                cascadeEyesClassifier.detectMultiScale(faceROI,eyeDetections);

                for (Rect eyeRect: eyeDetections.toArray()){
                    //painting rectangles on the detected eyes
                    Imgproc.rectangle(faceDetection.frameMat, new Point(rect.x+eyeRect.x, rect.y+eyeRect.y),
                            new Point(rect.x+eyeRect.x+eyeRect.width, rect.y+eyeRect.y+eyeRect.height),
                            new Scalar(0,255,0),3);
                }
            }




            //convert matrix to byte
            final MatOfByte buffer=new MatOfByte();
            Imgcodecs.imencode(".jpg",faceDetection.frameMat,buffer);
            imageData=buffer.toArray();
            //add to JLabel
            icon = new ImageIcon(imageData);
            faceDetection.cameraScreen.setIcon(icon);
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
        //EventQueue.invokeLater(FaceDetection::run);
        startCamera();

    }
}

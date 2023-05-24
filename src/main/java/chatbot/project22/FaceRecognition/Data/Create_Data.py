import time
import numpy as np
import imutils
import cv2
from sklearn.decomposition import PCA
from sklearn.neural_network import MLPClassifier
from imutils import paths
import numpy as np
import cv2
import os
import sys

# Get the absolute path of the script's directory
base_path = os.path.dirname(os.path.abspath(__file__))

print("[INFO] loading face detector model...")
configPath = os.path.join(base_path, '..', 'Face_Detection', 'deploy.prototxt')
modelPath = os.path.join(base_path, '..', 'Face_Detection', 'res10_300x300_ssd_iter_140000.caffemodel')

# load the face detection model
neural_network = cv2.dnn.readNet(configPath, modelPath)
input_path = os.path.join(base_path, '..', 'Data', 'users_faces')


# print(f"[INFO] Trained model loaded from {model_file}")
min_confidence = 0.5
min_samples = 15
n_components = 20
visualize = 1




def detect_faces(neural_network, image, min_confidence=0.5):
    """
    Detects faces in an input image using a pre-trained deep learning model.

    Arguments:
    neural_net -- the pre-trained deep learning model
    image -- the input image to be processed
    min_confidence -- a threshold for the minimum detection confidence level for a positive detection

    Returns:
    face_frames -- a list of bounding frames representing the detected faces in the image
    """

    # find the image's dimension
    (height, width) = image.shape[:2]
    # pre-processing the input image, so it can be fed into a deep learning model
    # In this case, the input image is resized to a fixed size of (300, 300) pixels
    # and normalized using mean subtraction.
    # The (104.0, 177.0, 123.0) tuple  represents the mean value of each channel of all images in the training set of
    # the pre-trained face detection model used
    # By subtracting this mean value from the input image, the model becomes less sensitive to lighting
    # variations across different images, which helps to improve its accuracy.
    blob = cv2.dnn.blobFromImage(image, 1.0, (300, 300), (104.0, 177.0, 123.0))

    # pass forward the pre-processed image through the network to obtain the face detections,
    neural_network.setInput(blob)
    detections = neural_network.forward()
    # initialize a list to store the predicted face boundaries
    face_frames = []

    # iterate over each detection in the "detections" array
    for i in range(0, detections.shape[2]):
        # Retrieve the confidence value, which indicates the probability of the detection
        # being right (true positive- a detected face is a face)
        confidence = detections[0, 0, i, 2]

        # If the confidence level for a detection is greater than the minimum threshold
        # specified by the "min_confidence" parameter, the detection is considered valid
        # and the code proceeds to the next step.
        # This way we can focus only on the element in detections that have high probability of
        # being a face
        if confidence > min_confidence:
            # compute the (x, y)-coordinates of the bounding box for
            # the object
            frame = detections[0, 0, i, 3:7] * np.array([width, height, width, height])
            (x_min, y_min, x_max, y_max) = frame.astype("int")

            # add the face to our face list
            face_frames.append((x_min, y_min, x_max, y_max))

    # return the face detection bounding boxes
    return face_frames


def preprocess_face_dataset(input_path, neural_network, min_confidence=0.5, min_samples=15):

    """
    The function iterates through the dataset of images and analyzes each image of a
    person who has enough images in the dataset. It detects the face in the image,
    creates a cropped version of the image that contains only the face, and adds it
    to a list of processed faces. Additionally, it creates a list of names that
    correspond to each face.

    Arguments:
    input_path -- path to the Faces dataset
    neural_network -- the pre-trained deep learning model
    image -- the input image to be processed
    min_confidence -- a threshold for the minimum detection confidence level
    min_samples -- minimum number of example images required for each face.

    Returns:
    faces --  a list of faces images
    labels -- a list of names corresponding to each face
    """
    # grab the paths to all images in our input directory, extract
    # the name of the person (i.e., class label) from the directory
    # structure, and count the number of example images we have per
    # face
    #  create a list of paths to each individual's images in the dataset.
    image_paths = list(paths.list_images(input_path))
    #  Each image's path contains the name of the person
    #  We can retrieve the name by parsing each path  and getting the second to last element
    #  in the resulting list of path segments
    #  We save all the names extracted from the directory structure for each image.
    names = [p.split(os.path.sep)[-2] for p in image_paths]
    # count repetitions of each unique name in the names array to get the number of example images we have per
    # person
    (names, counts) = np.unique(names, return_counts=True)
    names = names.tolist()
    # if os.path.exists(faces_file) and os.path.exists(labels_file):
    #     names, labels = load_names_and_labels(faces_file, labels_file)
    # else:
    faces = []
    labels = []
    # iterate over each image paths
    for image_path in image_paths:
        image = cv2.imread(image_path)  # load the image
        name = image_path.split(os.path.sep)[-2]  # retrieve the name from the path
        # make sure there are enough images of this person
        if counts[names.index(name)] < min_samples:
            # os.remove(image_path)
            continue
        # for each image we extract the borders of the face
        face_frames = detect_faces(neural_network, image, min_confidence)
        # iterate over each face_frames
        for (x_min, y_min, x_max, y_max) in face_frames:
            # for each image extract the region of interest --> the face detected
            face = image[y_min:y_max, x_min:x_max]
            face = cv2.resize(face, (47, 62))
            face = cv2.cvtColor(face, cv2.COLOR_BGR2GRAY)

            faces.append(face)
            labels.append(name)

    faces = np.array(faces)
    labels = np.array(labels)
    # save_names_and_labels(faces, labels, faces_file, labels_file)
    return faces, labels

def create_data(user_name):
    """
   The function is called when a new user is added. It opens a new file with the user names
   and fills it with 30 images of the new user using the computer camera.

   Arguments:
   user_name -- the input of the user name which be used to label the gathered images
   """
    print("[INFO] starting video stream...")
    camera = cv2.VideoCapture(0)
    time.sleep(1)
    count = 1
    faces = []
    labels = []

    # Create a new folder inside Data/users_faces with the given name
    folder_path = os.path.join(input_path, user_name)
    # print(os.path.exists(folder_path))
    os.makedirs(folder_path, exist_ok=True)
    # print(os.path.exists(folder_path))
    # print(os.path.abspath(folder_path))
    while count <= 30:
        ret, frame = camera.read()
        cv2.imshow("Frame", frame)
        count_str = str(count).zfill(4)  # Zero-padding the count
        # file_name = os.path.join(folder_path, f"{user_name}_{count_str}.jpg")
        file_name = os.path.join(folder_path, "{}_{}.jpg".format(user_name, count_str))
        cv2.imwrite(file_name, frame)
        faces.append(frame)
        labels.append(user_name)
        count += 1
        # time.sleep(1)
        if cv2.waitKey(1) == ord('q'):  # Press 'q' to quit
            break
    camera.release()
    cv2.destroyAllWindows()
    print("[INFO] " + user_name + " was added to the data")

def search():
    """
   The function checks if the person infront of the camera is recognized, meaning s/he exists
   in the users database

   Returns:
   best_prediction--the predictet user_name with the highest probability of being true
   -- if the prediction with the highest probability is not higher then a specified cretiria,
   then the algorithem will not recognize the person infront of the camera and return not_recognized
   """
    # Compute a PCA
    print("[INFO] processing the newly added faces")
    faces, labels = preprocess_face_dataset(input_path, neural_network, 0.5, 20)
    flat_faces = np.array([f.flatten() for f in faces])
    pca = PCA(n_components=n_components, whiten=True).fit(flat_faces)

    # apply PCA transformation
    pca_faces = pca.transform(flat_faces)

    model = MLPClassifier(hidden_layer_sizes=(1024,), batch_size=256, verbose=True, early_stopping=True).fit(pca_faces,
                                                                                                             labels)
    camera = cv2.VideoCapture(0)
    time.sleep(1)
    ret, frame = camera.read()
    cv2.imshow("Frame", frame)
    predictions = []
    count = 0
    while count <= 100:
        ret, frame = camera.read()
        face_frames = detect_faces(neural_network, frame, min_confidence=0.5)
        if len(face_frames) > 0:  # one face was detected
            print("[INFO] preprocessing the face...")
            (x_min, y_min, x_max, y_max) = face_frames[0]
            face = frame[y_min:y_max, x_min:x_max]
            face = cv2.resize(face, (47, 62))
            face = cv2.cvtColor(face, cv2.COLOR_BGR2GRAY)

            pca_face = pca.transform(face.reshape(1, -1))


            probability = model.predict_proba(pca_face).max()  # Get the maximum probability score

            # if probability < 0.6:
            #     predicted_name = "not_recognized"
            # # Predict the label of the face using the trained model
            # else:
            predicted_name = model.predict(pca_face)
            # Convert the predicted label back to the corresponding name
            predictions.append((predicted_name, probability))
            print("Recognized face: {}, Probability: {:.2f}".format(predicted_name, probability))

            # face = np.dstack([origTest[i]] * 3)
            face = imutils.resize(face, width=250)

            # Draw a rectangle around the detected face
            cv2.rectangle(frame, (x_min, y_min), (x_max, y_max), (0, 255, 0), 2)

            text = "{} ({:.2f}%)".format(predicted_name, probability * 100)
            # text = "{}".format(predicted_name)
            cv2.putText(frame, text, (x_min, y_min - 10), cv2.FONT_HERSHEY_COMPLEX, 0.9, (0, 255, 0), 2)



            # Display the frame with the prediction
            cv2.imshow("Frame", frame)


            count += 1
            if cv2.waitKey(1) == ord('q'):  # Press 'q' to quit
                break

    # Release the video stream
    camera.release()
    cv2.destroyAllWindows()

    # Find the prediction with the highest probability
    best_prediction, best_probability = max(predictions, key=lambda x: x[1])

    return best_prediction




def main():
    """
    check that the user provided the method he wants to use
    the way we call this method from the java file is giving at least the class path and the method name which
    means that wehn we call it we need to have at least to arguments.
    If we are calling the create_data we also give a user_name, so we need at least three arguments.
    """

    if len(sys.argv) < 2:
        print("Invalid command")
        return

    function_name = sys.argv[1]

    if function_name == "create_data":
        #if the method that was called is create data we check that a user name was given
        if len(sys.argv) < 3:
            print("Invalid command")
            return
        user_name = sys.argv[2]
        create_data(user_name)
    elif function_name == "search":
        search()
    else:
        print("Invalid command")

if __name__ == "__main__":
    main()


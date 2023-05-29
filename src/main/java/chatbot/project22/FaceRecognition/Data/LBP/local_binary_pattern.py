import time
from sklearn.neighbors import KNeighborsClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from imutils import paths
import cv2
import numpy as np
import os
import joblib

def get_weight(square_number):
    # EYES:
    if square_number == 15 or square_number == 16 or square_number == 18 or square_number == 19:
        return 4
    # EARS
    elif square_number == 14 or square_number == 21 or square_number == 20 or square_number == 27 or square_number == 38:
        return 2
    # nose and outer cheek
    elif square_number == 17 or square_number == 24 or square_number == 28 or square_number == 35 or square_number == 42 or square_number == 48 or square_number == 41 or square_number == 34:
        return 0
    else:
        return 1

def create_array(img_list):
    hisList = []

    end_hist = []

    for i in range(0, len(img_list)):
        img = img_list[i]
        adjusted = img.flatten()
        dst = np.histogram(adjusted, bins = 255, density= True)
        weigth = get_weight(i)
        if (weigth != 0):
            array = dst[0] * weigth
            end_hist = np.concatenate((end_hist, array))
        hisList.append(dst)

    return end_hist

def cut_image(image, grid):
    h,w = image.shape

    w_step = int(w/grid)
    h_step = int(h/grid)

    imageList = []

    for y in range (0,grid):
        for x in range (0,grid):
            imageList.append(image[h_step * y: h_step * (y + 1) , w_step * x : w_step * (x+1)])

    return imageList

def find_LBP_value(list, treshold):
    a = 0

    for i in range(0,8):
        x = list[i]
        if x >= treshold:
            a += 2**(7-i)

    return a

def LBP_image(img):
    h, w = img.shape

    array = np.zeros((h, w), np.uint16)

    for i in range (0,h):
        for j in range (0,w):

            # toprow
            if i == 0:
                # topleft corner
                if j == 0:
                    binary_list = [img[i+1, j+1], img[i+1, j], img[i+1, j+1], img[i, j+1],  img[i+1, j+1], img[i+1, j], img[i+1, j+1], img[i, j+1]]
                # topright corner
                elif j == w - 1:
                    binary_list = [img[i+1,j-1], img[i+1, j] , img[i+1,j-1],img[i, j-1] , img[i+1,j-1], img[i+1, j], img[i+1,j-1], img[i, j-1]]
                # middle of the row
                else:
                    binary_list = [img[i, j-1], img[i+1,j-1], img[i+1, j], img[i, j+1], img[i+1, j+1], img[i+1, j], img[i+1,j-1], img[i, j-1]]


            # bottom row
            elif i == h-1:
                # bottom left corner
                if j == 0:
                    binary_list= [img[i-1, j + 1], img[i-1, j], img[i-1, j + 1],img[i, j+1] , img[i-1, j + 1], img[i-1, j], img[i-1, j + 1], img[i, j+1]]
                # bottom right corner
                elif j == w - 1:
                    binary_list = [img[i-1, j - 1], img[i-1, j], img[i-1, j - 1], img[i, j-1], img[i-1, j - 1], img[i-1, j], img[i-1, j - 1], img[i, j-1]]
                # middle of the row
                else:
                    binary_list = [img[i-1, j - 1], img[i-1, j], img[i-1, j + 1], img[i, j+1], img[i-1, j + 1], img[i-1, j],img[i-1, j - 1],img[i, j-1] ]

            # middle of the left collom
            elif j == 0:
                binary_list = [img[i-1, j + 1], img[i-1, j], img[i-1, j + 1], img[i, j+1], img[i+1, j+1], img[i+1, j], img[i+1, j+1], img[i, j+1]]

            # middle of the right collom
            elif j == w - 1:
                binary_list = [img[i-1, j - 1], img[i-1, j], img[i-1, j - 1], img[i, j-1],img[i+1,j-1],img[i+1, j], img[i+1,j-1], img[i, j-1] ]

            # middle of the image
            else:
                binary_list = [img[i-1, j - 1], img[i-1, j], img[i-1, j + 1], img[i, j+1], img[i+1, j+1], img[i+1, j], img[i+1,j-1], img[i, j-1]]

            treshold = img[i,j]
            array[i,j] = find_LBP_value(binary_list,treshold)

    return array

def find_face(image):
    face_cascade = cv2.CascadeClassifier('src/main/java/chatbot/project22/FaceRecognition/Data/LBP/haarcascade_frontalface_default.xml')

    face = face_cascade.detectMultiScale(image, 1.3, 5)

    cutted = image

    for (x,y,w,h) in face:

        cutted = image  [y:y+h, x:x+w]

    return cutted

# steps to convert image to a array
def load_image(image):
    try:
        image = cv2.imread(image)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        image = find_face(image)
        return  convert_image(image)
    except Exception as e:
        print(e)
        return np.array([1])

def convert_image(image):
    image = LBP_image(image)
    image_list = cut_image(image, 7)
    array = create_array(image_list)
    return array


# this method is called to train the knn data
def train_data():

    X = []
    Y = []

    dir_name = "src/main/java/chatbot/project22/FaceRecognition/Data/users_faces"

    for name in os.listdir(dir_name):

        path = os.path.join(dir_name, name)

        all_images = list(paths.list_images(path))
        print(name)

        for image in all_images:
            c = load_image(image)
            if c.all() == [1]:
                pass
            else:
                X.append(c)
                Y.append(name)

    X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=0.2, random_state=20)
    # Create a kNN classifier
    k = 1
    knn = KNeighborsClassifier(n_neighbors=k)

    # Train the classifier
    knn.fit(X_train, y_train)

    name = "src/main/java/chatbot/project22/FaceRecognition/Data/LBP/knn.joblib"
    joblib.dump(knn, name)

    # Predict labels for the test data
    y_pred = knn.predict(X_test)

    # Evaluate the accuracy of the classifier
    accuracy = accuracy_score(y_test, y_pred)
    print(accuracy)
    return accuracy

def search():
    # loads the knn classifier
    name = "src/main/java/chatbot/project22/FaceRecognition/Data/LBP/knn.joblib"
    knn = joblib.load(name)

    video_capture = cv2.VideoCapture(0)
    time.sleep(1)

    face_cascade = cv2.CascadeClassifier('src/main/java/chatbot/project22/FaceRecognition/Data/LBP/haarcascade_frontalface_default.xml')

    predictions = []
    count = 0

    while count < 100:


        ret, frame = video_capture.read()
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = face_cascade.detectMultiScale(gray, 1.3, 5)

        for (x,y,w,h) in faces:

            cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)

            face_roi = gray[y:y+h, x:x+w]

            X = [convert_image(face_roi)]

            distances, indices = knn.kneighbors(X)
            confidence = 1.0 / distances.mean() * 100

            label = knn.predict(X)
            predictions.append((label, confidence))
            label = ("{}, {:.2f} %".format(label, confidence))

            cv2.putText(frame, str(label), (x, y-10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)
            count += 1



        cv2.imshow('Face Recognition', frame)

        k = cv2.waitKey(30) & 0xff
        if k == 27:
            break

    best_predict, best_prob = max(predictions, key = lambda x : x[1])
    print(best_prob)
    print(best_predict)

    video_capture.release()
    cv2.destroyAllWindows()

search()

import os
import cv2
import time
import joblib
import numpy as np
import pandas as pd
import mediapipe as mp
from sklearn.decomposition import PCA
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split
from sklearn.neural_network import MLPClassifier
import sys
from imutils import paths
import tensorflow as tf
import time
import imutils



class LandmarksHelpers():
    print("[info] landmarks helpers")
    @staticmethod
    def get_landmarks(img):
        # print("[info] get landmarks")
        # The function takes an image as input and uses the MediaPipe FaceMesh model to detect and
        # extract landmarks from the image.

        # creating the face_mesh model
        mp_face_mesh = mp.solutions.face_mesh
        face_mesh = mp_face_mesh.FaceMesh()

        # detects faces and estimates the face landmarks
        results = face_mesh.process(img)
        if results.multi_face_landmarks is not None:
            # returns the landmarks of the first face detected.
            return results.multi_face_landmarks[0]
        else:
            return None


    # it will return the position of the landmark specified with the index
    @staticmethod
    def landmark_to_coordinate(index, landmarks, image):
        # print("[info] landmarks to coord")
        s = landmarks.landmark[index]
        return (int(image.shape[1] * s.x), int(image.shape[0] * s.y))


    # will return the routes to form the oval shape around the face
    @staticmethod
    def find_routes_of_oval(image_landmarks, image):
        # print("[info] find oruts of oval")
        mp_face_mesh = mp.solutions.face_mesh
        face_oval = mp_face_mesh.FACEMESH_FACE_OVAL

        # convert the face_oval variable into a list of tuples.
        # Each tuple in the list represents a pair of consecutive landmarks that will be connected later with a line
        df = pd.DataFrame(list(face_oval), columns=["start_point", "end_point"])

        routes_idx = []

        start_point = df.iloc[0]["start_point"]
        end_point = df.iloc[0]["end_point"]

        # extract the routes between consecutive landmarks
        for i in range(0, df.shape[0]):
            # the variable obj is assigned the subset of rows in the DataFrame where the "start_point" column is equal
            # to the current value of end_point.
            # This step is used to find the next pair of landmarks that should be connected.
            obj = df[df["start_point"] == end_point]
            start_point = obj["start_point"].values[0]
            end_point = obj["end_point"].values[0]

            route_idx = []
            route_idx.append(start_point)
            route_idx.append(end_point)
            routes_idx.append(route_idx)

        routes = []

        for source_idx, target_idx in routes_idx:
            routes.append(LandmarksHelpers.landmark_to_coordinate(source_idx, image_landmarks, image))
            routes.append(LandmarksHelpers.landmark_to_coordinate(target_idx, image_landmarks, image))

        return routes


    # shows any route between landmarks on an image
    @staticmethod
    def show_routes(img, routes):
        # print("[info] show routes")
        for i in range(0, len(routes) - 2, 2):
            cv2.line(img, routes[i], routes[i + 1], (255, 0, 0), 2)
        cv2.imshow("Image", img)
        cv2.waitKey()


    @staticmethod
    def rotate_coordinate(r_angle, coords, center):
        # print("[info] routes to coord")
        rot_matrix = cv2.getRotationMatrix2D(center, r_angle, 1.0)
        point = np.array([[coords[0], coords[1]]], dtype=np.float32)
        rotated = cv2.transform(point.reshape(1, -1, 2), rot_matrix)
        r_x = int(rotated[0, 0, 0])
        r_y = int(rotated[0, 0, 1])
        return [r_x, r_y]


    @staticmethod
    def coordinates_array(landmarks, angle, image):
        # print("coord array")
        h, w, _ = image.shape
        center = (w // 2, h // 2)

        output_array = []
        for i in range(0, len(landmarks.landmark)):
            coords = LandmarksHelpers.rotate_coordinate(angle, LandmarksHelpers.landmark_to_coordinate(i, landmarks, image), center)
            output_array.extend(coords)
        return output_array

    @staticmethod
    def extract_face(img, routes):
        # print("[info] extract face")
        # Create an empty mask with the same dimensions as the input image
        mask = np.zeros((img.shape[0], img.shape[1]))
        # filling parts of the mask that include the inside of the oval with ones
        #  creates a binary mask where the face region is marked as white (pixel value of one)
        #  and the background is black (pixel value of zero)
        mask = cv2.fillConvexPoly(mask, np.array(routes), 1)
        mask = mask.astype(bool)

        extracted_face = np.zeros_like(img)
        # Copy the pixels from the input image to the output image only where the mask is True
        extracted_face[mask] = img[mask]

        return extracted_face

    @staticmethod
    def calculate_rotation_angle(landmarks,image):
        # print("[info] calculate rotation angle")
        top = LandmarksHelpers.landmark_to_coordinate(10, landmarks,image)
        bottom = LandmarksHelpers.landmark_to_coordinate(152, landmarks,image)
        dx = top[0] - bottom[0]
        dy = top[1] - bottom[1]
        angle = np.degrees(np.arctan2(dy, dx)) - 270
        return angle

    # Function to align faces based on oval landmarks
    @staticmethod
    def align_faces(img, landmarks):
        # print("[info] align faces")
        rotation_angle = LandmarksHelpers.calculate_rotation_angle(landmarks, img)

        rows, cols = img.shape[:2]
        rotation_matrix = cv2.getRotationMatrix2D((cols // 2, rows // 2), rotation_angle, 1)
        aligned_img = cv2.warpAffine(img, rotation_matrix, (cols, rows))

        return aligned_img


    @staticmethod
    def image_to_aligned_oval(image):
        # print("[info] image to align faces")
        # print("image to aligned oval")
        landmarks = LandmarksHelpers.get_landmarks(image)

        routes = LandmarksHelpers.find_routes_of_oval(landmarks, image)
        # print("oval")
        oval = LandmarksHelpers.extract_face(image, routes)
        # print("align")
        aligned_oval = LandmarksHelpers.align_faces(oval, landmarks)

        c10 = LandmarksHelpers.landmark_to_coordinate(10, landmarks, image)
        c454 = LandmarksHelpers.landmark_to_coordinate(454, landmarks, image)
        c152 = LandmarksHelpers.landmark_to_coordinate(152, landmarks, image)
        c234 = LandmarksHelpers.landmark_to_coordinate(234, landmarks, image)

        a = LandmarksHelpers.calculate_rotation_angle(landmarks, image)

        h,w,_ = image.shape
        center = (w//2, h//2)

        r10 = LandmarksHelpers.rotate_coordinate(a, c10, center)
        r454 = LandmarksHelpers.rotate_coordinate(a, c454, center)
        r152 = LandmarksHelpers.rotate_coordinate(a, c152, center)
        r234 = LandmarksHelpers.rotate_coordinate(a, c234, center)


        aligned_oval = aligned_oval[r10[1]:r152[1], r234[0]:r454[0]]

        # aligned_oval = cv2.cvtColor(aligned_oval, cv2.COLOR_BGR2RGB)
        return aligned_oval


class Lbp():

    @staticmethod
    def process_image(img):
        oval = LandmarksHelpers.image_to_aligned_oval(img)
        gray_oval = cv2.cvtColor(oval, cv2.COLOR_BGR2GRAY)
        cropped_lbp = Lbp.lbp_image(gray_oval)
        image_list = Lbp.cut_image(cropped_lbp, 7)
        array = Lbp.create_array(image_list)
        return array


    @staticmethod
    def lbp_image(img):
        h, w = img.shape

        array = np.zeros((h, w), np.uint16)

        for i in range(0, h):
            for j in range(0, w):

                # toprow
                if i == 0:
                    # topleft corner
                    if j == 0:
                        binary_list = [img[i + 1, j + 1], img[i + 1, j], img[i + 1, j + 1], img[i, j + 1],
                                       img[i + 1, j + 1], img[i + 1, j], img[i + 1, j + 1], img[i, j + 1]]
                    # topright corner
                    elif j == w - 1:
                        binary_list = [img[i + 1, j - 1], img[i + 1, j], img[i + 1, j - 1], img[i, j - 1],
                                       img[i + 1, j - 1], img[i + 1, j], img[i + 1, j - 1], img[i, j - 1]]
                    # middle of the row
                    else:
                        binary_list = [img[i, j - 1], img[i + 1, j - 1], img[i + 1, j], img[i, j + 1], img[i + 1, j + 1],
                                       img[i + 1, j], img[i + 1, j - 1], img[i, j - 1]]


                # bottom row
                elif i == h - 1:
                    # bottom left corner
                    if j == 0:
                        binary_list = [img[i - 1, j + 1], img[i - 1, j], img[i - 1, j + 1], img[i, j + 1],
                                       img[i - 1, j + 1], img[i - 1, j], img[i - 1, j + 1], img[i, j + 1]]
                    # bottom right corner
                    elif j == w - 1:
                        binary_list = [img[i - 1, j - 1], img[i - 1, j], img[i - 1, j - 1], img[i, j - 1],
                                       img[i - 1, j - 1], img[i - 1, j], img[i - 1, j - 1], img[i, j - 1]]
                    # middle of the row
                    else:
                        binary_list = [img[i - 1, j - 1], img[i - 1, j], img[i - 1, j + 1], img[i, j + 1],
                                       img[i - 1, j + 1], img[i - 1, j], img[i - 1, j - 1], img[i, j - 1]]

                # middle of the left collom
                elif j == 0:
                    binary_list = [img[i - 1, j + 1], img[i - 1, j], img[i - 1, j + 1], img[i, j + 1], img[i + 1, j + 1],
                                   img[i + 1, j], img[i + 1, j + 1], img[i, j + 1]]

                # middle of the right collom
                elif j == w - 1:
                    binary_list = [img[i - 1, j - 1], img[i - 1, j], img[i - 1, j - 1], img[i, j - 1], img[i + 1, j - 1],
                                   img[i + 1, j], img[i + 1, j - 1], img[i, j - 1]]

                # middle of the image
                else:
                    binary_list = [img[i - 1, j - 1], img[i - 1, j], img[i - 1, j + 1], img[i, j + 1], img[i + 1, j + 1],
                                   img[i + 1, j], img[i + 1, j - 1], img[i, j - 1]]

                treshold = img[i, j]
                array[i, j] = Lbp.find_lbp_value(binary_list, treshold)

        return array


    @staticmethod
    def find_lbp_value(list, treshold):
        a = 0

        for i in range(0, 8):
            x = list[i]
            if x >= treshold:
                a += 2 ** (7 - i)

        return a


    @staticmethod
    def cut_image(image, grid):
        h, w = image.shape

        w_step = int(w / grid)
        h_step = int(h / grid)

        image_list = []

        for y in range(0, grid):
            for x in range(0, grid):
                image_list.append(image[h_step * y: h_step * (y + 1), w_step * x: w_step * (x + 1)])

        return image_list


    @staticmethod
    def create_array(img_list):
        his_list = []

        end_hist = []

        for i in range(0, len(img_list)):
            img = img_list[i]
            adjusted = img.flatten()
            dst = np.histogram(adjusted, bins=255, density=True)
            weight = Lbp.get_weight(i)
            if (weight != 0):
                array = dst[0] * weight
                end_hist = np.concatenate((end_hist, array))
            his_list.append(dst)

        return end_hist


    @staticmethod
    def get_weight(square_number):
        # EYES:
        if square_number == 8 or square_number == 9 or square_number == 11 or square_number == 12:
            return 4
        # MOUTH
        elif square_number == 38 or square_number == 37 or square_number == 39:
            return 2
        # nose and outer cheek
        elif square_number == 17 or square_number == 24 or square_number == 28 or square_number == 35 or square_number == 42 or square_number == 48 or square_number == 41 or square_number == 34 or square_number == 36 or square_number == 47 or square_number == 0 or square_number == 6 or square_number == 1 or square_number == 2:
            return 0
        else:
            return 1

    @staticmethod
    def lbp_search():
        lbp_name = "lbp_knn.joblib"

        video_capture = cv2.VideoCapture(0)
        time.sleep(1)

        lbp_knn = joblib.load(lbp_name)

        predictions = []
        count = 0

        while count < 50:

            ret, frame = video_capture.read()

            frame_copy = frame.copy()

            frame_landmarks = LandmarksHelpers.get_landmarks(frame)

            if frame_landmarks is not None:
                c10 = LandmarksHelpers.landmark_to_coordinate(10, frame_landmarks, frame)
                c454 = LandmarksHelpers.landmark_to_coordinate(454, frame_landmarks, frame)
                c152 = LandmarksHelpers.landmark_to_coordinate(152, frame_landmarks, frame)
                c234 = LandmarksHelpers.landmark_to_coordinate(234, frame_landmarks, frame)

                top_left = [c234[0], c10[1]]
                bottom_right = [c454[0], c152[1]]

                cv2.rectangle(frame, top_left, bottom_right, (0, 255, 0), 2)

                lbp_array_data = Lbp.process_image(frame_copy)

                final_pred = lbp_knn.predict([lbp_array_data])

                distances, indices = lbp_knn.kneighbors([lbp_array_data])
                confidence = 1.0 / distances.mean()

                predictions.append((final_pred, confidence))

                label = ("{}, {:.2f} %".format(final_pred, confidence))

                cv2.putText(frame, str(label), top_left , cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)
                count += 1

            cv2.imshow('Face Recognition', frame)

            k = cv2.waitKey(30) & 0xff
            if k == 27:
                break

        best_predict, best_prob = max(predictions, key=lambda x: x[1])
        print(best_predict)

        video_capture.release()
        cv2.destroyAllWindows()
        return best_predict

class Mesh():

    @staticmethod
    def triangle_processing(img):
        landmarks = LandmarksHelpers.get_landmarks(img)
        triangles = [
            [127,  34, 139],
            [ 11,   0,  37],
            [232, 231, 120],
            [ 72,  37,  39],
            [128, 121,  47],
            [232, 121, 128],
            [104,  69,  67],
            [175, 171, 148],
            [118,  50, 101],
            [ 73,  39,  40],
            [  9, 151, 108],
            [ 48, 115, 131],
            [194, 204, 211],
            [ 74,  40, 185],
            [ 80,  42, 183],
            [ 40,  92, 186],
            [230, 229, 118],
            [202, 212, 214],
            [ 83,  18,  17],
            [ 76,  61, 146],
            [160,  29,  30],
            [ 56, 157, 173],
            [106, 204, 194],
            [135, 214, 192],
            [203, 165,  98],
            [ 21,  71,  68],
            [ 51,  45,   4],
            [144,  24,  23],
            [ 77, 146,  91],
            [205,  50, 187],
            [201, 200,  18],
            [ 91, 106, 182],
            [ 90,  91, 181],
            [ 85,  84,  17],
            [206, 203,  36],
            [148, 171, 140],
            [ 92,  40,  39],
            [193, 189, 244],
            [159, 158,  28],
            [247, 246, 161],
            [236,   3, 196],
            [ 54,  68, 104],
            [193, 168,   8],
            [117, 228,  31],
            [189, 193,  55],
            [ 98,  97,  99],
            [126,  47, 100],
            [166,  79, 218],
            [155, 154,  26],
            [209,  49, 131],
            [135, 136, 150],
            [ 47, 126, 217],
            [223,  52,  53],
            [ 45,  51, 134],
            [211, 170, 140],
            [ 67,  69, 108],
            [ 43, 106,  91],
            [230, 119, 120],
            [226, 130, 247],
            [ 63,  53,  52],
            [238,  20, 242],
            [ 46,  70, 156],
            [ 78,  62,  96],
            [ 46,  53,  63],
            [143,  34, 227],
            [123, 117, 111],
            [ 44, 125,  19],
            [236, 134,  51],
            [216, 206, 205],
            [154, 153,  22],
            [ 39,  37, 167],
            [200, 201, 208],
            [ 36, 142, 100],
            [ 57, 212, 202],
            [ 20,  60,  99],
            [ 28, 158, 157],
            [ 35, 226, 113],
            [160, 159,  27],
            [204, 202, 210],
            [113, 225,  46],
            [ 43, 202, 204],
            [ 62,  76,  77],
            [137, 123, 116],
            [ 41,  38,  72],
            [203, 129, 142],
            [ 64,  98, 240],
            [ 49, 102,  64],
            [ 41,  73,  74],
            [212, 216, 207],
            [ 42,  74, 184],
            [169, 170, 211],
            [170, 149, 176],
            [105,  66,  69],
            [122,   6, 168],
            [123, 147, 187],
            [ 96,  77,  90],
            [ 65,  55, 107],
            [ 89,  90, 180],
            [101, 100, 120],
            [ 63, 105, 104],
            [ 93, 137, 227],
            [ 15,  86,  85],
            [129, 102,  49],
            [ 14,  87,  86],
            [ 55,   8,   9],
            [100,  47, 121],
            [145,  23,  22],
            [ 88,  89, 179],
            [  6, 122, 196],
            [ 88,  95,  96],
            [138, 172, 136],
            [215,  58, 172],
            [115,  48, 219],
            [ 42,  80,  81],
            [195,   3,  51],
            [ 43, 146,  61],
            [171, 175, 199],
            [ 81,  82,  38],
            [ 53,  46, 225],
            [144, 163, 110],
            [ 52,  65,  66],
            [229, 228, 117],
            [ 34, 127, 234],
            [107, 108,  69],
            [109, 108, 151],
            [ 48,  64, 235],
            [ 62,  78, 191],
            [129, 209, 126],
            [111,  35, 143],
            [117, 123,  50],
            [222,  65,  52],
            [ 19, 125, 141],
            [221,  55,  65],
            [  3, 195, 197],
            [ 25,   7,  33],
            [220, 237,  44],
            [ 70,  71, 139],
            [122, 193, 245],
            [247, 130,  33],
            [ 71,  21, 162],
            [170, 169, 150],
            [188, 174, 196],
            [216, 186,  92],
            [  2,  97, 167],
            [141, 125, 241],
            [164, 167,  37],
            [ 72,  38,  12],
            [ 38,  82,  13],
            [ 63,  68,  71],
            [226,  35, 111],
            [101,  50, 205],
            [206,  92, 165],
            [209, 198, 217],
            [165, 167,  97],
            [220, 115, 218],
            [133, 112, 243],
            [239, 238, 241],
            [214, 135, 169],
            [190, 173, 133],
            [171, 208,  32],
            [125,  44, 237],
            [ 86,  87, 178],
            [ 85,  86, 179],
            [ 84,  85, 180],
            [ 83,  84, 181],
            [201,  83, 182],
            [137,  93, 132],
            [ 76,  62, 183],
            [ 61,  76, 184],
            [ 57,  61, 185],
            [212,  57, 186],
            [214, 207, 187],
            [ 34, 143, 156],
            [ 79, 239, 237],
            [123, 137, 177],
            [ 44,   1,   4],
            [201, 194,  32],
            [ 64, 102, 129],
            [213, 215, 138],
            [ 59, 166, 219],
            [242,  99,  97],
            [  2,  94, 141],
            [ 75,  59, 235],
            [ 24, 110, 228],
            [ 25, 130, 226],
            [ 23,  24, 229],
            [ 22,  23, 230],
            [ 26,  22, 231],
            [112,  26, 232],
            [189, 190, 243],
            [221,  56, 190],
            [ 28,  56, 221],
            [ 27,  28, 222],
            [ 29,  27, 223],
            [ 30,  29, 224],
            [247,  30, 225],
            [238,  79,  20],
            [166,  59,  75],
            [ 60,  75, 240],
            [147, 177, 215],
            [ 20,  79, 166],
            [187, 147, 213],
            [112, 233, 244],
            [233, 128, 245],
            [128, 114, 188],
            [114, 217, 174],
            [131, 115, 220],
            [217, 198, 236],
            [198, 131, 134],
            [177, 132,  58],
            [143,  35, 124],
            [110, 163,   7],
            [228, 110,  25],
            [356, 389, 368],
            [ 11, 302, 267],
            [452, 350, 349],
            [302, 303, 269],
            [357, 343, 277],
            [452, 453, 357],
            [333, 332, 297],
            [175, 152, 377],
            [347, 348, 330],
            [303, 304, 270],
            [  9, 336, 337],
            [278, 279, 360],
            [418, 262, 431],
            [304, 408, 409],
            [310, 415, 407],
            [270, 409, 410],
            [450, 348, 347],
            [422, 430, 434],
            [313, 314,  17],
            [306, 307, 375],
            [387, 388, 260],
            [286, 414, 398],
            [335, 406, 418],
            [364, 367, 416],
            [423, 358, 327],
            [251, 284, 298],
            [281,   5,   4],
            [373, 374, 253],
            [307, 320, 321],
            [425, 427, 411],
            [421, 313,  18],
            [321, 405, 406],
            [320, 404, 405],
            [315,  16,  17],
            [426, 425, 266],
            [377, 400, 369],
            [322, 391, 269],
            [417, 465, 464],
            [386, 257, 258],
            [466, 260, 388],
            [456, 399, 419],
            [284, 332, 333],
            [417, 285,   8],
            [346, 340, 261],
            [413, 441, 285],
            [327, 460, 328],
            [355, 371, 329],
            [392, 439, 438],
            [382, 341, 256],
            [429, 420, 360],
            [364, 394, 379],
            [277, 343, 437],
            [443, 444, 283],
            [275, 440, 363],
            [431, 262, 369],
            [297, 338, 337],
            [273, 375, 321],
            [450, 451, 349],
            [446, 342, 467],
            [293, 334, 282],
            [458, 461, 462],
            [276, 353, 383],
            [308, 324, 325],
            [276, 300, 293],
            [372, 345, 447],
            [352, 345, 340],
            [274,   1,  19],
            [456, 248, 281],
            [436, 427, 425],
            [381, 256, 252],
            [269, 391, 393],
            [200, 199, 428],
            [266, 330, 329],
            [287, 273, 422],
            [250, 462, 328],
            [258, 286, 384],
            [265, 353, 342],
            [387, 259, 257],
            [424, 431, 430],
            [342, 353, 276],
            [273, 335, 424],
            [292, 325, 307],
            [366, 447, 345],
            [271, 303, 302],
            [423, 266, 371],
            [294, 455, 460],
            [279, 278, 294],
            [271, 272, 304],
            [432, 434, 427],
            [272, 407, 408],
            [394, 430, 431],
            [395, 369, 400],
            [334, 333, 299],
            [351, 417, 168],
            [352, 280, 411],
            [325, 319, 320],
            [295, 296, 336],
            [319, 403, 404],
            [330, 348, 349],
            [293, 298, 333],
            [323, 454, 447],
            [ 15,  16, 315],
            [358, 429, 279],
            [ 14,  15, 316],
            [285, 336,   9],
            [329, 349, 350],
            [374, 380, 252],
            [318, 402, 403],
            [  6, 197, 419],
            [318, 319, 325],
            [367, 364, 365],
            [435, 367, 397],
            [344, 438, 439],
            [272, 271, 311],
            [195,   5, 281],
            [273, 287, 291],
            [396, 428, 199],
            [311, 271, 268],
            [283, 444, 445],
            [373, 254, 339],
            [282, 334, 296],
            [449, 347, 346],
            [264, 447, 454],
            [336, 296, 299],
            [338,  10, 151],
            [278, 439, 455],
            [292, 407, 415],
            [358, 371, 355],
            [340, 345, 372],
            [346, 347, 280],
            [442, 443, 282],
            [ 19,  94, 370],
            [441, 442, 295],
            [248, 419, 197],
            [263, 255, 359],
            [440, 275, 274],
            [300, 383, 368],
            [351, 412, 465],
            [263, 467, 466],
            [301, 368, 389],
            [395, 378, 379],
            [412, 351, 419],
            [436, 426, 322],
            [  2, 164, 393],
            [370, 462, 461],
            [164,   0, 267],
            [302,  11,  12],
            [268,  12,  13],
            [293, 300, 301],
            [446, 261, 340],
            [330, 266, 425],
            [426, 423, 391],
            [429, 355, 437],
            [391, 327, 326],
            [440, 457, 438],
            [341, 382, 362],
            [459, 457, 461],
            [434, 430, 394],
            [414, 463, 362],
            [396, 369, 262],
            [354, 461, 457],
            [316, 403, 402],
            [315, 404, 403],
            [314, 405, 404],
            [313, 406, 405],
            [421, 418, 406],
            [366, 401, 361],
            [306, 408, 407],
            [291, 409, 408],
            [287, 410, 409],
            [432, 436, 410],
            [434, 416, 411],
            [264, 368, 383],
            [309, 438, 457],
            [352, 376, 401],
            [274, 275,   4],
            [421, 428, 262],
            [294, 327, 358],
            [433, 416, 367],
            [289, 455, 439],
            [462, 370, 326],
            [  2, 326, 370],
            [305, 460, 455],
            [254, 449, 448],
            [255, 261, 446],
            [253, 450, 449],
            [252, 451, 450],
            [256, 452, 451],
            [341, 453, 452],
            [413, 464, 463],
            [441, 413, 414],
            [258, 442, 441],
            [257, 443, 442],
            [259, 444, 443],
            [260, 445, 444],
            [467, 342, 445],
            [459, 458, 250],
            [289, 392, 290],
            [290, 328, 460],
            [376, 433, 435],
            [250, 290, 392],
            [411, 416, 433],
            [341, 463, 464],
            [453, 464, 465],
            [357, 465, 412],
            [343, 412, 399],
            [360, 363, 440],
            [437, 399, 456],
            [420, 456, 363],
            [401, 435, 288],
            [372, 383, 353],
            [339, 255, 249],
            [448, 261, 255],
            [133, 243, 190],
            [133, 155, 112],
            [ 33, 246, 247],
            [ 33, 130,  25],
            [398, 384, 286],
            [362, 398, 414],
            [362, 463, 341],
            [263, 359, 467],
            [263, 249, 255],
            [466, 467, 260],
            [ 75,  60, 166],
            [238, 239,  79],
            [162, 127, 139],
            [ 72,  11,  37],
            [121, 232, 120],
            [ 73,  72,  39],
            [114, 128,  47],
            [233, 232, 128],
            [103, 104,  67],
            [152, 175, 148],
            [119, 118, 101],
            [ 74,  73,  40],
            [107,   9, 108],
            [ 49,  48, 131],
            [ 32, 194, 211],
            [184,  74, 185],
            [191,  80, 183],
            [185,  40, 186],
            [119, 230, 118],
            [210, 202, 214],
            [ 84,  83,  17],
            [ 77,  76, 146],
            [161, 160,  30],
            [190,  56, 173],
            [182, 106, 194],
            [138, 135, 192],
            [129, 203,  98],
            [ 54,  21,  68],
            [  5,  51,   4],
            [145, 144,  23],
            [ 90,  77,  91],
            [207, 205, 187],
            [ 83, 201,  18],
            [181,  91, 182],
            [180,  90, 181],
            [ 16,  85,  17],
            [205, 206,  36],
            [176, 148, 140],
            [165,  92,  39],
            [245, 193, 244],
            [ 27, 159,  28],
            [ 30, 247, 161],
            [174, 236, 196],
            [103,  54, 104],
            [ 55, 193,   8],
            [111, 117,  31],
            [221, 189,  55],
            [240,  98,  99],
            [142, 126, 100],
            [219, 166, 218],
            [112, 155,  26],
            [198, 209, 131],
            [169, 135, 150],
            [114,  47, 217],
            [224, 223,  53],
            [220,  45, 134],
            [ 32, 211, 140],
            [109,  67, 108],
            [146,  43,  91],
            [231, 230, 120],
            [113, 226, 247],
            [105,  63,  52],
            [241, 238, 242],
            [124,  46, 156],
            [ 95,  78,  96],
            [ 70,  46,  63],
            [116, 143, 227],
            [116, 123, 111],
            [  1,  44,  19],
            [  3, 236,  51],
            [207, 216, 205],
            [ 26, 154,  22],
            [165,  39, 167],
            [199, 200, 208],
            [101,  36, 100],
            [ 43,  57, 202],
            [242,  20,  99],
            [ 56,  28, 157],
            [124,  35, 113],
            [ 29, 160,  27],
            [211, 204, 210],
            [124, 113,  46],
            [106,  43, 204],
            [ 96,  62,  77],
            [227, 137, 116],
            [ 73,  41,  72],
            [ 36, 203, 142],
            [235,  64, 240],
            [ 48,  49,  64],
            [ 42,  41,  74],
            [214, 212, 207],
            [183,  42, 184],
            [210, 169, 211],
            [140, 170, 176],
            [104, 105,  69],
            [193, 122, 168],
            [ 50, 123, 187],
            [ 89,  96,  90],
            [ 66,  65, 107],
            [179,  89, 180],
            [119, 101, 120],
            [ 68,  63, 104],
            [234,  93, 227],
            [ 16,  15,  85],
            [209, 129,  49],
            [ 15,  14,  86],
            [107,  55,   9],
            [120, 100, 121],
            [153, 145,  22],
            [178,  88, 179],
            [197,   6, 196],
            [ 89,  88,  96],
            [135, 138, 136],
            [138, 215, 172],
            [218, 115, 219],
            [ 41,  42,  81],
            [  5, 195,  51],
            [ 57,  43,  61],
            [208, 171, 199],
            [ 41,  81,  38],
            [224,  53, 225],
            [ 24, 144, 110],
            [105,  52,  66],
            [118, 229, 117],
            [227,  34, 234],
            [ 66, 107,  69],
            [ 10, 109, 151],
            [219,  48, 235],
            [183,  62, 191],
            [142, 129, 126],
            [116, 111, 143],
            [118, 117,  50],
            [223, 222,  52],
            [ 94,  19, 141],
            [222, 221,  65],
            [196,   3, 197],
            [ 45, 220,  44],
            [156,  70, 139],
            [188, 122, 245],
            [139,  71, 162],
            [149, 170, 150],
            [122, 188, 196],
            [206, 216,  92],
            [164,   2, 167],
            [242, 141, 241],
            [  0, 164,  37],
            [ 11,  72,  12],
            [ 12,  38,  13],
            [ 70,  63,  71],
            [ 31, 226, 111],
            [ 36, 101, 205],
            [203, 206, 165],
            [126, 209, 217],
            [ 98, 165,  97],
            [237, 220, 218],
            [237, 239, 241],
            [210, 214, 169],
            [140, 171,  32],
            [241, 125, 237],
            [179,  86, 178],
            [180,  85, 179],
            [181,  84, 180],
            [182,  83, 181],
            [194, 201, 182],
            [177, 137, 132],
            [184,  76, 183],
            [185,  61, 184],
            [186,  57, 185],
            [216, 212, 186],
            [192, 214, 187],
            [139,  34, 156],
            [218,  79, 237],
            [147, 123, 177],
            [ 45,  44,   4],
            [208, 201,  32],
            [ 98,  64, 129],
            [192, 213, 138],
            [235,  59, 219],
            [141, 242,  97],
            [ 97,   2, 141],
            [240,  75, 235],
            [229,  24, 228],
            [ 31,  25, 226],
            [230,  23, 229],
            [231,  22, 230],
            [232,  26, 231],
            [233, 112, 232],
            [244, 189, 243],
            [189, 221, 190],
            [222,  28, 221],
            [223,  27, 222],
            [224,  29, 223],
            [225,  30, 224],
            [113, 247, 225],
            [ 99,  60, 240],
            [213, 147, 215],
            [ 60,  20, 166],
            [192, 187, 213],
            [243, 112, 244],
            [244, 233, 245],
            [245, 128, 188],
            [188, 114, 174],
            [134, 131, 220],
            [174, 217, 236],
            [236, 198, 134],
            [215, 177,  58],
            [156, 143, 124],
            [ 25, 110,   7],
            [ 31, 228,  25],
            [264, 356, 368],
            [  0,  11, 267],
            [451, 452, 349],
            [267, 302, 269],
            [350, 357, 277],
            [350, 452, 357],
            [299, 333, 297],
            [396, 175, 377],
            [280, 347, 330],
            [269, 303, 270],
            [151,   9, 337],
            [344, 278, 360],
            [424, 418, 431],
            [270, 304, 409],
            [272, 310, 407],
            [322, 270, 410],
            [449, 450, 347],
            [432, 422, 434],
            [ 18, 313,  17],
            [291, 306, 375],
            [259, 387, 260],
            [424, 335, 418],
            [434, 364, 416],
            [391, 423, 327],
            [301, 251, 298],
            [275, 281,   4],
            [254, 373, 253],
            [375, 307, 321],
            [280, 425, 411],
            [200, 421,  18],
            [335, 321, 406],
            [321, 320, 405],
            [314, 315,  17],
            [423, 426, 266],
            [396, 377, 369],
            [270, 322, 269],
            [413, 417, 464],
            [385, 386, 258],
            [248, 456, 419],
            [298, 284, 333],
            [168, 417,   8],
            [448, 346, 261],
            [417, 413, 285],
            [326, 327, 328],
            [277, 355, 329],
            [309, 392, 438],
            [381, 382, 256],
            [279, 429, 360],
            [365, 364, 379],
            [355, 277, 437],
            [282, 443, 283],
            [281, 275, 363],
            [395, 431, 369],
            [299, 297, 337],
            [335, 273, 321],
            [348, 450, 349],
            [359, 446, 467],
            [283, 293, 282],
            [250, 458, 462],
            [300, 276, 383],
            [292, 308, 325],
            [283, 276, 293],
            [264, 372, 447],
            [346, 352, 340],
            [354, 274,  19],
            [363, 456, 281],
            [426, 436, 425],
            [380, 381, 252],
            [267, 269, 393],
            [421, 200, 428],
            [371, 266, 329],
            [432, 287, 422],
            [290, 250, 328],
            [385, 258, 384],
            [446, 265, 342],
            [386, 387, 257],
            [422, 424, 430],
            [445, 342, 276],
            [422, 273, 424],
            [306, 292, 307],
            [352, 366, 345],
            [268, 271, 302],
            [358, 423, 371],
            [327, 294, 460],
            [331, 279, 294],
            [303, 271, 304],
            [436, 432, 427],
            [304, 272, 408],
            [395, 394, 431],
            [378, 395, 400],
            [296, 334, 299],
            [  6, 351, 168],
            [376, 352, 411],
            [307, 325, 320],
            [285, 295, 336],
            [320, 319, 404],
            [329, 330, 349],
            [334, 293, 333],
            [366, 323, 447],
            [316,  15, 315],
            [331, 358, 279],
            [317,  14, 316],
            [  8, 285,   9],
            [277, 329, 350],
            [253, 374, 252],
            [319, 318, 403],
            [351,   6, 419],
            [324, 318, 325],
            [397, 367, 365],
            [288, 435, 397],
            [278, 344, 439],
            [310, 272, 311],
            [248, 195, 281],
            [375, 273, 291],
            [175, 396, 199],
            [312, 311, 268],
            [276, 283, 445],
            [390, 373, 339],
            [295, 282, 296],
            [448, 449, 346],
            [356, 264, 454],
            [337, 336, 299],
            [337, 338, 151],
            [294, 278, 455],
            [308, 292, 415],
            [429, 358, 355],
            [265, 340, 372],
            [352, 346, 280],
            [295, 442, 282],
            [354,  19, 370],
            [285, 441, 295],
            [195, 248, 197],
            [457, 440, 274],
            [301, 300, 368],
            [417, 351, 465],
            [251, 301, 389],
            [394, 395, 379],
            [399, 412, 419],
            [410, 436, 322],
            [326,   2, 393],
            [354, 370, 461],
            [393, 164, 267],
            [268, 302,  12],
            [312, 268,  13],
            [298, 293, 301],
            [265, 446, 340],
            [280, 330, 425],
            [322, 426, 391],
            [420, 429, 437],
            [393, 391, 326],
            [344, 440, 438],
            [458, 459, 461],
            [364, 434, 394],
            [428, 396, 262],
            [274, 354, 457],
            [317, 316, 402],
            [316, 315, 403],
            [315, 314, 404],
            [314, 313, 405],
            [313, 421, 406],
            [323, 366, 361],
            [292, 306, 407],
            [306, 291, 408],
            [291, 287, 409],
            [287, 432, 410],
            [427, 434, 411],
            [372, 264, 383],
            [459, 309, 457],
            [366, 352, 401],
            [  1, 274,   4],
            [418, 421, 262],
            [331, 294, 358],
            [435, 433, 367],
            [392, 289, 439],
            [328, 462, 326],
            [ 94,   2, 370],
            [289, 305, 455],
            [339, 254, 448],
            [359, 255, 446],
            [254, 253, 449],
            [253, 252, 450],
            [252, 256, 451],
            [256, 341, 452],
            [414, 413, 463],
            [286, 441, 414],
            [286, 258, 441],
            [258, 257, 442],
            [257, 259, 443],
            [259, 260, 444],
            [260, 467, 445],
            [309, 459, 250],
            [305, 289, 290],
            [305, 290, 460],
            [401, 376, 435],
            [309, 250, 392],
            [376, 411, 433],
            [453, 341, 464],
            [357, 453, 465],
            [343, 357, 412],
            [437, 343, 399],
            [344, 360, 440],
            [420, 437, 456],
            [360, 420, 363],
            [361, 401, 288],
            [265, 372, 353],
            [390, 339, 249],
            [339, 448, 255],
            [255, 339, 448]
        ]
        return Mesh.angles(landmarks, triangles)

    @staticmethod
    def angle_calculation(vector1, vector2):
        dot_product = np.dot(vector1, vector2)
        norm1 = np.linalg.norm(vector1)
        norm2 = np.linalg.norm(vector2)
        cos_theta = dot_product / (norm1 * norm2)
        angle_in_radians = np.arccos(cos_theta)
        angle_in_degrees = np.degrees(angle_in_radians)
        return angle_in_degrees

    @staticmethod
    def angles(landmarks, triangles):
        output_array = []
        for triangle in triangles:
            point_a = triangle[0]
            point_b = triangle[1]
            point_c = triangle[2]

            coords_a = np.array((landmarks.landmark[point_a].x, landmarks.landmark[point_a].y, landmarks.landmark[point_a].z))
            coords_b = np.array((landmarks.landmark[point_b].x, landmarks.landmark[point_b].y, landmarks.landmark[point_b].z))
            coords_c = np.array((landmarks.landmark[point_c].x, landmarks.landmark[point_c].y, landmarks.landmark[point_c].z))

            #   A
            vector_ab = np.subtract(coords_a, coords_b)
            vector_ac = np.subtract(coords_a, coords_c)
            vector_angle = Mesh.angle_calculation(vector_ab, vector_ac)
            output_array.append(vector_angle)

            #   B
            vector_ba = np.subtract(coords_b, coords_a)
            vector_bc = np.subtract(coords_b, coords_c)
            vector_angle = Mesh.angle_calculation(vector_ba, vector_bc)
            output_array.append(vector_angle)

            #   C
            vector_ca = np.subtract(coords_c, coords_a)
            vector_cb = np.subtract(coords_c, coords_b)
            vector_angle = Mesh.angle_calculation(vector_ca, vector_cb)
            output_array.append(vector_angle)

        return output_array

    @staticmethod
    def angle_search():
        lm_name = "angles_knn.joblib"

        video_capture = cv2.VideoCapture(0)
        time.sleep(1)

        lm_knn = joblib.load(lm_name)

        predictions = []
        count = 0

        while count < 50:

            ret, frame = video_capture.read()

            frame_landmarks = LandmarksHelpers.get_landmarks(frame)

            if frame_landmarks is not None:
                c10 = LandmarksHelpers.landmark_to_coordinate(10, frame_landmarks, frame)
                c454 = LandmarksHelpers.landmark_to_coordinate(454, frame_landmarks, frame)
                c152 = LandmarksHelpers.landmark_to_coordinate(152, frame_landmarks, frame)
                c234 = LandmarksHelpers.landmark_to_coordinate(234, frame_landmarks, frame)

                top_left = [c234[0], c10[1]]
                bottom_right = [c454[0], c152[1]]

                cv2.rectangle(frame, top_left, bottom_right, (0, 255, 0), 2)

                landmark_angle_data = Mesh.triangle_processing(frame_landmarks)

                y_pred_lm = lm_knn.predict([landmark_angle_data])

                final_pred = y_pred_lm

                distances, indices = lm_knn.kneighbors([landmark_angle_data])
                confidence = 1.0 / distances.mean() * 100

                predictions.append((final_pred, confidence))

                label = ("{}, {:.2f} %".format(final_pred, confidence))

                cv2.putText(frame, str(label), top_left , cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)
                count += 1

            cv2.imshow('Face Recognition', frame)

            k = cv2.waitKey(30) & 0xff
            if k == 27:
                break

        best_predict, best_prob = max(predictions, key=lambda x: x[1])
        print(best_predict)

        video_capture.release()
        cv2.destroyAllWindows()

        return best_predict


class mesh_lbp():

    @staticmethod
    def search():
        lbp_name = "lbp_knn.joblib"
        lm_name = "angles_knn.joblib"

        video_capture = cv2.VideoCapture(0)
        time.sleep(1)

        lbp_knn = joblib.load(lbp_name)
        lm_knn = joblib.load(lm_name)

        predictions = []
        count = 0

        while count < 50:

            ret, frame = video_capture.read()

            frame_copy = frame.copy()

            frame_landmarks = LandmarksHelpers.get_landmarks(frame)


            if frame_landmarks is not None:
                c10 = LandmarksHelpers.landmark_to_coordinate(10, frame_landmarks, frame)
                c454 = LandmarksHelpers.landmark_to_coordinate(454, frame_landmarks, frame)
                c152 = LandmarksHelpers.landmark_to_coordinate(152, frame_landmarks, frame)
                c234 = LandmarksHelpers.landmark_to_coordinate(234, frame_landmarks, frame)

                top_left = [c234[0], c10[1]]
                bottom_right = [c454[0], c152[1]]

                cv2.rectangle(frame, top_left, bottom_right, (0, 255, 0), 2)

                landmark_angle_data = Mesh.triangle_processing(frame_landmarks)
                lbp_array_data = Lbp.process_image(frame_copy)

                y_pred_lbp = lbp_knn.predict([lbp_array_data])
                y_pred_lm = lm_knn.predict([landmark_angle_data])

                confidence = 0
                final_pred = ""

                prob1 = lbp_knn.predict_proba([lbp_array_data])
                c1 = np.max(prob1) * 1
                prob2 = lm_knn.predict_proba([landmark_angle_data])
                c2 = np.max(prob2) * 1
                if c1 >= c2:
                    final_pred = y_pred_lbp
                    distances, indices = lbp_knn.kneighbors([lbp_array_data])
                    confidence = 1.0 / distances.mean()
                else:
                    final_pred = y_pred_lm
                    distances, indices = lm_knn.kneighbors([landmark_angle_data])
                    confidence = 1.0 / distances.mean() * 100

                predictions.append((final_pred, confidence))

                label = ("{}, {:.2f} %".format(final_pred, confidence))

                cv2.putText(frame, str(label), top_left , cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)
                count += 1

            cv2.imshow('Face Recognition', frame)

            k = cv2.waitKey(30) & 0xff
            if k == 27:
                break

        best_predict, best_prob = max(predictions, key=lambda x: x[1])
        print(best_predict)

        video_capture.release()
        cv2.destroyAllWindows()

        return best_predict


class AnnMarks():
    print("[info] ann marks")
    base_path = os.path.dirname(os.path.abspath(__file__))  # Get the absolute path of the script's directory
    dir_name = os.path.join(base_path,'Data' ,'caletch')
    configPath = os.path.join(base_path, '..', 'Face_Detection', 'deploy.prototxt')
    modelPath = os.path.join(base_path, '..', 'Face_Detection', 'res10_300x300_ssd_iter_140000.caffemodel')
    # load the face detection model
    # neural_network = cv2.dnn.readNet(configPath, modelPath)

    #load the preprocessed landmarks and labels
    landmarks_file = os.path.join(base_path, '..', 'Data', 'landmarks_file.npy')
    marks_labels_file = os.path.join(base_path, '..', 'Data', 'marks_labels_file.txt')


    @staticmethod
    def load_image(image_path):
        # print("[info] load image")
        try:
            image = cv2.imread(image_path)
            if image is None:
                return None
            # Convert grayscale images to RGB
            if len(image.shape) == 2:
                image = cv2.cvtColor(image, cv2.COLOR_GRAY2RGB)
            # Convert single-channel images to three channels
            elif image.shape[2] == 1:
                image = cv2.cvtColor(image, cv2.COLOR_GRAY2RGB)
            return AnnMarks.process_image(image)
        except Exception as e:
            print(e)
            return None

    @staticmethod
    def process_image(img):
        # print("[info] process image")
        face = LandmarksHelpers.image_to_aligned_oval(img)
        face = cv2.resize(face, (215, 215))
        face = cv2.cvtColor(face, cv2.COLOR_BGR2RGB)
        return face

    @staticmethod
    def process_data(dir_name):
        print("[info] process data")
        faces = []
        labels = []
        landmarks = []
        angles = []
        valid_faces = []
        valid_labels = []
        coord_data = []
        for name in os.listdir(dir_name):

            path = os.path.join(dir_name, name)

            all_images = list(paths.list_images(path))
            print(name)

            for image in all_images:
                c = AnnMarks.load_image(image)
                # if c.all() == [1]:
                #     pass
                # else:
                if c is None:  # Skip images that couldn't be loaded
                    continue
                faces.append(c)
                labels.append(name)
            for face, label in zip(faces, labels):
                face_landmarks = LandmarksHelpers.get_landmarks(face)
                if face_landmarks is None:
                    # ignore this face and label
                    continue
                face_angles = LandmarksHelpers.calculate_rotation_angle(face_landmarks, face)
                landmarks.append(face_landmarks)
                angles.append(face_angles)
                valid_faces.append(face)
                valid_labels.append(label)
                coord_array = LandmarksHelpers.coordinates_array(face_landmarks, face_angles, face)
                coord_data.append(coord_array)
        AnnMarks.save_landmarks_and_labels(coord_data, valid_labels,AnnMarks.landmarks_file, AnnMarks.marks_labels_file)
        return faces,labels


    @staticmethod
    def save_landmarks_and_labels(landmarks, labels, landmarks_file, marks_labels_file):
        print("[info] save namlandmarks and labels")
        """
        a function to keep the landmarks and labels that already have been processed
        Arguments:
                 faces: processed faced to add to the list
                 labels: labels to add to the list
                 landmarks_file: file to which we will add the processed faces
                 labels_file: file to which we will add the labels
        """
        try:
            existing_landmarks = np.load(landmarks_file)
            landmarks = np.concatenate((existing_landmarks, landmarks))
        except FileNotFoundError:
            # Create the directory if it doesn't exist
            os.makedirs(os.path.dirname(landmarks_file), exist_ok=True)

        np.save(landmarks_file, landmarks, allow_pickle=False)

        try:
            existing_labels = np.loadtxt(marks_labels_file, dtype=str)
            labels = np.concatenate((existing_labels, labels))
        except FileNotFoundError:
            # Create the directory if it doesn't exist
            os.makedirs(os.path.dirname(marks_labels_file), exist_ok=True)

        np.savetxt(marks_labels_file, labels, fmt='%s')

    @staticmethod
    def load_landmarks_and_labels(landmarks_file, marks_labels_file):
        print("[info] load landmarks and labels")
        """
        a function to keep the names and labels that already have been processed
        Arguments:
                 faces_file: file from which we read the processed faces
                 labels_file: file to which we read the labels
        Returns:
                faces: processed faces
                labels: labels of the faces
        """
        landmarks = np.load(landmarks_file)
        labels = np.loadtxt(marks_labels_file, dtype=str)
        return landmarks, labels


    @staticmethod
    def train_data():
        print("[info] training data")
        faces = []
        labels = []
        landmarks = []
        angles = []
        valid_faces = []
        valid_labels = []
        coord_data = []

        for name in os.listdir(AnnMarks.dir_name):

            path = os.path.join(AnnMarks.dir_name, name)

            all_images = list(paths.list_images(path))
            print(name)

            for image in all_images:
                c = AnnMarks.load_image(image)
                # if c.all() == [1]:
                #     pass
                # else:
                if c is None:  # Skip images that couldn't be loaded
                    continue
                faces.append(c)
                labels.append(name)

        for face, label in zip(faces, labels):
            face_landmarks = LandmarksHelpers.get_landmarks(face)
            if face_landmarks is None:
                # ignore this face and label
                continue
            face_angles = LandmarksHelpers.calculate_rotation_angle(face_landmarks, face)
            landmarks.append(face_landmarks)
            angles.append(face_angles)
            valid_faces.append(face)
            valid_labels.append(label)
            coord_array = LandmarksHelpers.coordinates_array(face_landmarks, face_angles, face)
            coord_data.append(coord_array)
        print("[info] separating to test and train")
        try:
            # Convert landmarks to numerical features using PCA
            # pca = PCA(n_components=128)  # Choose the number of components you want to keep
            # features = pca.fit_transform(coord_data)
            AnnMarks.save_landmarks_and_labels(coord_data, valid_labels, AnnMarks.landmarks_file, AnnMarks.marks_labels_file)

        except ValueError as e:
            print("Error:", str(e))

    @staticmethod
    def search():
        print("[info] training data")
        if os.path.exists(AnnMarks.landmarks_file) and os.path.exists(AnnMarks.marks_labels_file):
            print("[INFO] loading the preprocessed data")
            landmarks_data, labels = AnnMarks.load_landmarks_and_labels(AnnMarks.landmarks_file, AnnMarks.marks_labels_file)
        else:
            AnnMarks.train_data()
            landmarks_data, labels = AnnMarks.load_landmarks_and_labels(AnnMarks.landmarks_file, AnnMarks.marks_labels_file)
            print("[INFO] no preprocessed data")


        try:
            print("[info] Convert landmarks to numerical features using PCA")
            # pca = PCA(n_components=128)  # Choose the number of components you want to keep
            # features = pca.fit_transform(landmarks_data)

            pca = PCA(n_components=128, whiten=True).fit(landmarks_data)
            features=pca.transform(landmarks_data)
            print("[info] training ann")
            model = MLPClassifier(hidden_layer_sizes=(1024,), batch_size=100, verbose=True, early_stopping=True).fit(features,labels)

            camera = cv2.VideoCapture(0)
            time.sleep(1)
            ret, frame = camera.read()
            cv2.imshow("Frame", frame)
            predictions = []
            count = 0
            while count <= 100:
                ret, frame = camera.read()
                print("processing image")
                face=AnnMarks.process_image(frame)
                print("finding landmarks")
                face_landmarks = LandmarksHelpers.get_landmarks(face)
                face_angles = LandmarksHelpers.calculate_rotation_angle(face_landmarks, face)

                if face_landmarks is None:
                    # ignore this face and label
                    print("face_landmarks is None")
                    continue
                print("pca landmark")
                coord_list = LandmarksHelpers.coordinates_array(face_landmarks, face_angles, face)
                # coord_list = coord_list[:128]  # Truncate to 128 coordinates
                coord_array=np.array(coord_list)

                pca_landmarks = pca.transform(coord_array.reshape(1,-1))

                print("prob")
                probability = model.predict_proba(pca_landmarks).max()  # Get the maximum probability score

                predicted_name = model.predict(pca_landmarks)
                # Convert the predicted label back to the corresponding name
                predictions.append((predicted_name, probability))
                print("Recognized face: {}, Probability: {:.2f}".format(predicted_name, probability))
                face = imutils.resize(face, width=250)

                # Draw a rectangle around the detected face
                # cv2.rectangle(frame, (x_min, y_min), (x_max, y_max), (0, 255, 0), 2)

                text = "{} ({:.2f}%)".format(predicted_name, probability * 100)

                # text = "{}".format(predicted_name)
                # cv2.putText(frame, text, cv2.FONT_HERSHEY_COMPLEX, 0.9, (0, 255, 0), 2)
                text_position = (100,100)  # Adjust the y-coordinate to position the text above the rectangle
                cv2.putText(frame, text, text_position, cv2.FONT_HERSHEY_COMPLEX, 0.9, (0, 255, 0), 2)
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
            print(best_prediction)
            return best_prediction
        except Exception as e:
            print(f"An error occurred during training: {str(e)}")

def main():
    print("[info] main")
    """
    check that the user provided the method he wants to use
    the way we call this method from the java file is giving at least the class path and the method name which
    means that when we call it we need to have at least to arguments.
    If we are calling the create_data we also give a user_name, so we need at least three arguments.
    """
    AnnMarks.search()


if __name__ == "__main__":

    try:
        main()
    except Exception as e:
        print(f"An error occurred: {str(e)}")





# Project_2.2_02

Multi-modal Digital Assistant

Python setup:

1. open terminal from project directory
2. create a Python virtual environment:`python3 -m venv virtualPy`. Make sure the python is the one instaled on your machine.
3. Activate the newly created virtual environment:
   1. On Unix or MacOS: `source virtualPy/bin/activate`
   2. On Windows: `virtualPy\Scripts\activate`
4. Install the necessary Python packages within your virtual environment: `pip install imutils opencv-python scikit-learn cv2`

After these steps, navigate to `FaceRecognitionSystem` class and in command blocks, eg:

```
String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/FaceRecognition/Data/Create_Data.py",
                "create_data",
                user_name
        };
```

change the first line to `"virtualPy/Scripts/python"` if you work on Windows and `"virtualPy/bin/python"` for Mac.


To run the program first build the project and set up Python virtual enviroment, after please run `WelcomeScreen` class.

As a new user you will first have to `SignUp`, a set of picturtes of you will be taken. Next time you can `LogIn` ussing our face recognition system.

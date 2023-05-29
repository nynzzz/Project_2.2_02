# Project_2.2_02

## Multi-modal Digital Assistant

#### Python setup:

1. open terminal from project directory
2. create a Python virtual environment:`python3 -m venv virtualPy`. Make sure the python is the one instaled on your machine (python3 in this case).
3. Activate the newly created virtual environment:
   1. On Unix or MacOS: `source virtualPy/bin/activate`
   2. On Windows: `virtualPy\Scripts\activate`
4. Install the necessary Python packages within your virtual environment: `pip install imutils opencv-python scikit-learn cv2`
   If an error occurs when installing packages, try to install them one by one.

After these steps, navigate to `FaceRecognitionSystem` class and in command blocks, eg:

```
String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/FaceRecognition/Data/Create_Data.py",
                "create_data",
                user_name
        };
```

change the first line to `"virtualPy/Scripts/python"` if you work on Windows and `"virtualPy/bin/python"` for Unix or MacOS.


#### Run the Application

To run the program first build the project and set up Python virtual enviroment as described above, after you can run the main method of `WelcomeScreen` class.

As a new user you will first have to `SignUp`, a set of picturtes of you will be taken. Next time you can `LogIn` ussing our face recognition system.

After you can select a bot from the listed ones and enjoy the experience. You can also manage or add new skills to the bots within the GUI by selecting `Skill Editor` button.

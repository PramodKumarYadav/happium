# happium 😊
Goal of this project is to provide new Appium users with a reference framework that they can use to create production grade,
cross platform, mobile app tests.

End result should be a clean, readable and maintainable framework; the core of which you should be able to port to your actual project as is, and start writing tests from there. 

## Working principles
There are two key principles you will see throughout being followed in the project. 
- **For tests**: I will follow the principle of `seperating test intentions from test implementation`. Meaning, you will only see test intentions in the tests without any code implementation details in tests. 
> This results in highly readable `tests`, with low/zero code duplication. 
- **For code**: I will follow `seperation of concerns` principles. Broadly this means keeping code, config and data seperate. More importantly you will see this principle followed heavily within code to create classes and functions that follow clean code principles and are thus highly readable and easily maintainable.
> This results in highly readable `code` with no code duplication.

## Working approach
When I learn a new tool or technology, my working approach is to `first make it work` and `then make it better`. Thus refactoring is a `way of life` in all my projects. Following the scout and guides principle that if you touch a class, you have to `leave code cleaner than you found it`. Eventually this approach would result in the code that looks like what I mentioned before in the working principles.   

## Installation (dependencies and appium)
- For windows setup: Go to install folder in root directory. Now refer powershell script named `Install-AppiumOnWindows.psm1`
- To do a full setup on Windows; execute below commands.

```
- cd to this project. Say (cd D:\happium\)
- Import-Module .\install\Install-AppiumOnWindows.psm1 -Force
- Install-AppiumDependencies
- close this terminal. Check that you have node properly installed and available using npm -v and node -v commands. If you see versions, then run:
- Install-Appium 

P.S: To install any dependency that may have not properly installed in previous step. Close the current terminal. Open a new one as administrator and now run the function name. 
For example to only install appium-doctor, run on terminal.
- Install-AppiumDoctor
```
#Setup
-[X] Clone the sample apps repository and get some apps for testing.
    - Run git clone https://github.com/appium/sample-apps.git (to get 3 apk apps). Use "ContactManager.apk" from this list. Put it in apps folder.
    - npm install sample-apps (to install all the apps)
-[] Now we have the app. Let's create a driver factory that will give us a emulator for any avd that we ask for. 

## Reference
-[Sample apps to use for testing](https://github.com/appium/sample-apps)
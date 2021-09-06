# happium 😊
Goal of this project is to help new Appium users have a reference code repo that they can use in their project to write clean and maintainable test and code.

End result should be a clean, production grade framework, the core of which you should be able to port to your actual project and start writing tests from there. 

## Working principles
There are two key principles you will see throughout being followed in the project. 
- **For tests**: I will follow the principle of `seperating test intentions from test implementation`. Meaning, you will only see test intentions in the tests without any code implementation details in tests. 
> This results in highly readable `tests`, with low/zero code duplication. 
- **For code**: I will follow `seperation of concerns` principles. Broadly this means keeping code, config and data seperate. More importantly you will see this principle followed heavily within code to create classes and functions that follow clean code principles and are thus highly readable and easily maintainable.
> This results in highly readable `code` with no code duplication.

## Working approach
When I learn a new tool or technology, my working approach is to `first make it work` and `then make it better`. Thus refactoring is a `way of life` in all my projects. Following the scout and guides principle that if you touch a class, you have to `leave code cleaner than you found it`. Eventually this approach would result in the code that looks like what I mentioned before in the working principles.   

## Full Appium Setup
- For windows setup: Go to install folder in root directory. Now refer powershell script named `Install-FullAppiumSetupOnWindows.psm1`

- To do a full setup on windows; execute below three commands.

```
- cd to this project. Say (cd D:\happium\)
- Import-Module .\install\Install-FullAppiumSetupOnWindows.psm1 -Force
- Install-FullAppiumSetupOnWindows

For installing any particular item and not the full setup, after step 2 (Import-Module step), run that particular function. For example to only install appium-doctor, run on terminal.
- Install-AppiumDoctor.
```
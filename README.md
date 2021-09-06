# happium
I am creating this project with the idea to make working with Appium a pleasure and easy way for new appium learners. 

End result would be a clean, production grade framework, the core of which you should be able to port to your actual project and start writing tests from there. 

There are two key principles you will see throughout being followed in the project. 
- **For tests**: I will follow the principle of `seperating test intentions from test implementation`. Meaning, you will only see test intentions in the tests without any code implementation details in tests. 
> This results in highly readable `code`, with low/zero code duplication. 
- **For code**: I will follow `seperation of concerns` principles. Broadly this means keeping code, config and data seperate. More importantly you will see this principle followed heavily within code to create classes and functions that follow clean code principles and are thus highly readable and easily maintainable.
> This results in highly readable `tests` with no code duplication.

## Setup
- For windows setup: refer powershell script in the root of this folder named `Install-AppiumOnWindows.psm1`


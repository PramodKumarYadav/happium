#Requires -Version 5
#Requires -RunAsAdministrator

<# To use this script, open a powershell terminal with elevated access (as administrator).
# cd to this project. Say;
-------------------------------------------------
cd D:\happium
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope LocalMachine
Import-Module .\install\Start-Appium.psm1 -Force
Start-AppiumServerAndEmulator
-------------------------------------------------
# To see help or examples for any given function below, run:
Get-Help Start-AppiumServerAndEmulator  (to see the synopsis and description, for this chosen function)
Run Get-Help Start-AppiumServerAndEmulator -Examples (to see the examples usage, for this chosen function)
---------------------------------------------------------------------
#>

# Run this function to start both appium server and an emulator
function Start-AppiumServerAndEmulator {
    <#
    .Synopsis
    To start both appium server and a default emulator (or a specific emulator)

    .Description
    -> Running this function will launch a terminal window for appium server. If appium is already open, will not open another instance.
    -> emualtor will be launched in the current open terminal window.
    Note:
    -> The avd image should already be downloaded and available in this location: C:\Users\your-user-name\.android\avd
    -> Once you are done, you can close the terminals using ctrl+c combination.

    .Example
    Start-AppiumServerAndEmulator [to run using default emulator value "Pixel_5_API_31"]
    Start-AppiumServerAndEmulator -avdName Pixel_XL_API_31  [to run using a specific emulator value "Pixel_XL_API_31"]
    #>

    [CmdletBinding()]
    param(
        [String]$avdName = "Pixel_5_API_31"
    )

    Start-AppiumServer
    Start-Emulator -avdName Pixel_XL_API_31
}

function Start-AppiumServer {
    <#
    .Synopsis
    To start appium server

    .Description
    -> Will launch appium in another powershell window.
    -> If appium is already open, will not open another instance.
    Note:
    -> Once you are done, you can close the terminal's using ctrl+c combination.

    .Example
    Start-AppiumServer
    #>

    start powershell appium
}

# Start emulator with provided avd name (android virtual device name).
function Start-Emulator {
    <#
    .Synopsis
    To start emulator with provided avd name (android virtual device name).

    .Description
    Note: The avd image should already be downloaded in this location: C:\Users\your-user-name\.android\avd

    .Example
    Start-Emulator  [to run using default emulator value "Pixel_5_API_31"]
    Start-Emulator -avdName Pixel_XL_API_31  [to run using a specific emulator value "Pixel_XL_API_31"]
    #>

    [CmdletBinding()]
    param(
        [String]$avdName = "Pixel_5_API_31"
    )

    emulator -avd $avdName
}

# Verify if appium is running or not
function Test-AppiumServer {
    curl http://127.0.0.1:4723/wd/hub/status
}

# To show list of devices attached (i.e. all running emulators)
function Show-AttachedEmulatorDevices {
    adb devices
}
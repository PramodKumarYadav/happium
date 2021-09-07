# To use this script, open a powershell terminal with elevated access (as administrator).

# Then execute below commands.
# Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope LocalMachine

# ---------------------------------------------------------------------
# cd to this project. Say (cd D:\happium\)
# Import-Module .\install\Start-AppiumServer.psm1 -Force
# [To run in the same terminal]
    # Start-AppiumServer
# [To run as a background Job]
    # Start-AppiumServer -asBackgroundJob
# ---------------------------------------------------------------------

# To run server as background. Run below command. By default, it runs the server in the current terminal.
function Start-AppiumServer {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory=$false)]
        [Switch]$asBackgroundJob
    )

    # Remove any previously open appium background instances that may prevent from starting a new instance
    Stop-AppiumServer

    # start appium
    If ($asBackgroundJob.IsPresent) {
        Write-Host "starting appium server as a background job. Do `Stop-Job -Name Job1` to stop the background job."
        $appiumJob = Start-Job -ScriptBlock {appium}

        # Verify if appium is running or not (since in background mode, you dont see it running)
        $appiumJob
        Test-AppiumServer
    }else {
        Write-Host "starting appium server in the current terminal. Do ctrl +c to close the server."
        appium
    }
 }

 function Stop-AppiumServer {
    # Get all running jobs
    Get-Job

    # Stop all running background jobs (dont worry, there are no background jobs except the one we created)
    Get-Job | Stop-Job

    # Remove all running background jobs
    Get-Job | Remove-Job

    # Verify that the server is stopped and all background jobs are removed.
    Get-Job
 }

 function Test-AppiumServer {
    # Verify if appium is running or not
    curl http://127.0.0.1:4723/wd/hub/status
 }

# example usage(s):
# Add-AndroidVirtualDeviceImage -api 28 -deviceName Pixel_XL [ for a specific API version]
# or: Add-AndroidVirtualDeviceImage  [using default api values "31" and "Pixel_5"]
# Note: The SDK that you provide in the command should already be downloaded, else this command will fail.
function Add-AndroidVirtualDeviceImage {
    [CmdletBinding()]
    param(
        [String]$api = "31",
        [String]$deviceName = "Pixel_5"
    )
    $avdName = "$deviceName" + "_API_$api"
    avdmanager create avd -n "$avdName" -k "system-images;android-$api;google_apis;x86_64"
}

# example usage(s):
# Start-DeviceEmulator -avdName Pixel_XL_API_31
# or: Start-DeviceEmulator   [using default emulator value "Pixel_5_API_31"]
function Start-DeviceEmulator {
    [CmdletBinding()]
    param(
        [String]$avdName = "Pixel_5_API_31"
    )

    # Start emulator with provided avd name (android virtual device name).
    # Note: The avd image should already be downloaded in this location: C:\Users\your-user-name\.android\avd
    emulator -avd $avdName
}

function Show-AttachedEmulatorDevices {
    # List of devices attached (i.e. all running emulators)
    adb devices
}
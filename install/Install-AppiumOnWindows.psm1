#Requires -Version 5
#Requires -RunAsAdministrator

<# To use this script, open a powershell terminal with elevated access (as administrator).
# cd to this project. Say;
-------------------------------------------------
cd D:\happium
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope LocalMachine
Import-Module .\install\Install-AppiumOnWindows.psm1 -Force


STEP01: Install appium dependencies (everything needed to run tests and install appium)
Install-AppiumDependencies
-------------------------------------------------
After this script finishes, close this terminal and open another powershell terminal as administrator.
This is because, nodejs is not available in same terminal directly after install. And we need nodejs to install
appium client, server and appium doctor. To install all these run.
STEP01: Install appium (server, client, doctor and desktop)
Install-Appium
-------------------------------------------------
#>

<#
# NOTE: The Set-ExecutionPolicy command is to allow powershell to import and run this module. Else you may get this error:
# Import-Module: File D:\happium\install\Install-AppiumOnWindows.psm1 cannot be loaded.
# The file D:\happium\install\Install-AppiumOnWindows.psm1 is not digitally signed.
# You cannot run this script on the current system.
# For more information about running scripts and setting execution policy, see about_Execution_Policies
# at https://go.microsoft.com/fwlink/?LinkID=135170.

# Note: After full setup is complete, open a new powershell terminal as administrator to see all changed env variables and installed softwares.
 #>

function Install-AppiumDependencies {
<#
    .Synopsis
    This scripts installs all tools and dependencies needed to run appium tests on a windows machine.
    It also sets all env variables, so that you do not have to do them manually.
    The only thing you need to run this script is powershell, which comes pre-installed with windows latest versions.

    .Description
    This scripts installs:
    -> Chocolatey (windows package manager)
    -> JDK8
    -> nodejs and npm (needed to install appium)
    -> appium desktop
    -> android studio
    -> android sdk for latest api at the time of writing this script (api 31) for a 64 bit architecture
    -> a android virtual device image for pixel_5

    .Example
    Install-AppiumDependencies
#>
    [CmdletBinding()]
    param(
        [Alias('os')]
        [String] $operatingSystem = 'windows'
    )
    # NOTE: If any of the below softwares are alrady installed, this script will NOT reinstall them.
    # However, if the current version is behind latest version, it will be upgraded.

    # Install chocolatey (the awesome - windows package manager - if already installed/upgraded; skips)
    Install-Chocolatey

    # Install open JDK8 (if already installed/upgraded; skips). 
    Install-OpenJDK8

    # Install or upgrade nodejs (if already installed/upgraded; skips). 
    Install-NodeJS

    # Install android studio (if already installed/upgraded; skips). 
    Install-AndroidStudio

    # Install a android SDK to start with (you can add more later)
    Install-AndroidSDKForAPILevel -api 31 -cpu "x86_64"

    # Install a android virtual device image to start with (you can add more later)
    Install-AndroidVirtualDeviceImage -api 31 -deviceName "Pixel_5"
}

# Tested okay [when node is installed or uninstalled].
function Install-Chocolatey {
    # https://chocolatey.org/install
    # If already installed, below step does not install it again and skips. 
    Get-ExecutionPolicy
    Set-ExecutionPolicy AllSigned
    Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
    
    # upgrade chocolatey if already installed
    choco upgrade chocolatey
    choco -?
 }

# If already installed or is at latest version, than it will not install/upgrade again.
function Install-OpenJDK8 {
    Write-Host "$("*-" * 32)`nInstalling OpenJDK8!"
    choco install openjdk8
    choco upgrade openjdk8
 }

# Tested okay [when node is installed or uninstalled].
function Install-NodeJS {
    Write-Host "$("*-" * 32)`nInstalling nodejs!"
    choco install nodejs.install
    choco upgrade nodejs.install
 }

# Tested okay [when androidstudio is installed or uninstalled].
# Install android studio (if you want to run some virtual emulators for testing and not just real devices)
# This will also install sdk at the right location: C:\Users\your-user-name\AppData\Local\Android\Sdk\
# Choco also sets env varibles and saves you the hassle of setting up env variables
# if you only want to run using real devices than you dont need android studio but sdk will suffice
# however in that case, you would need to setup the env varaibles though - so below setup is better.
function Install-AndroidStudio {
    Write-Host "$("*-" * 32)`nInstalling androidstudio!"
    choco install androidstudio
    choco upgrade androidstudio

    # Set below properties at machine level; since eventually we want the sdk tools present in these folders to create emualtors from command line scripts (to automate the process) and not via android studio (manually)
    # Note: Running this command multiple times will NOT add the same path multiple times. So its completly safe to run this install script multiple times without having to worry about adding duplicate paths.
    [Environment]::SetEnvironmentVariable("Path", $env:Path + ";%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\tools;%ANDROID_HOME%\tools\bin;%ANDROID_HOME%\emulator", "Machine")
    refreshenv
}

# example usage(s):
# Install-AndroidSDKForAPILevel -api 29 -cpu "x86" [ for a specific API and for a 32 bit machine - use "x86_64" if you have a 64 bit machine]
# or: Install-AndroidSDKForAPILevel  [using default api value "31" and for a 64 bit cpu "x86_64"]
function Install-AndroidSDKForAPILevel {
    [CmdletBinding()]
    param(
        [String]$api = "31",
        [String]$cpu = "x86_64"
    )
    # Install android sdk for this API version for 64 bit machine
    Write-Host "$("*-" * 32)`nInstalling android sdk for API version $api for $cpu bit machine"
    sdkmanager "platform-tools" "platforms;android-$api"
    sdkmanager "system-images;android-$api;google_apis;$cpu"
    sdkmanager --licenses
}

# example usage(s):
# Install-AndroidVirtualDeviceImage -api 28 -deviceName Pixel_XL [for a specific API version]
# or: Install-AndroidVirtualDeviceImage  [using default api values "31" and "Pixel_5"]
# Note: The SDK that you provide in the command should already be downloaded (by above function); else this command will fail.
# Also (with little exploration that I did so far), you cannot run a 32 bit device on a 64 bit machine
# (so beaware of the images that you create and run). If this assumption is incorrect, please do let me know and we can remove this line.
# Note: read below to add hardware profile while creating device (else the emulation will not be proper)
# https://stuff.mit.edu/afs/sipb/project/android/docs/tools/devices/managing-avds-cmdline.html
# https://stuff.mit.edu/afs/sipb/project/android/docs/tools/devices/managing-avds-cmdline.html#hardwareopts
function Install-AndroidVirtualDeviceImage {
    [CmdletBinding()]
    param(
        [String]$api = "31",
        [String]$deviceName = "Pixel_5"
    )
    $avdName = "$deviceName" + "_API_$api"
    Write-Host "$("*-" * 32)`Creating a virtual device image named: $avdName"
    avdmanager create avd -n "$avdName" -k "system-images;android-$api;google_apis;x86_64" --force

    # Above process of creating device does not provice all hardware profile info (in config.ini file) to the virtual device being created.
    # So we copy a config.ini template that we got by creating a device directly from android studio. 
    # This help launch a proper emulator device.
    $src = "./config/android-virtual-devices/hardware-profiles/$avdName.ini"
    $dest = "$HOME\.android\avd\$avdName.avd\config.ini"
    Get-Content -Path $src | set-content -Path $dest
}

# Tested Okay: [when appium is already installed or uninstalled]
# Tested Okay: Script does not install again if already installed and tries to upgrade.
function Install-Appium {
    <#
    .Synopsis
    Installing appium server, client and doctor needs nodejs (from install dependencies step).
    Installing appium desktop needs chocolatey installed (from install dependencies step)

    .Description
    Installing appium server, client and doctor needs nodejs. Nodejs after installation needs to be opened via a
    seperate terminal than from which it was installed. Thus we have to do this installation seperately.
    -> appium client and server
    -> appium doctor
    -> appium desktop

    .Example
    Install-Appium
    #>

    [CmdletBinding()]
    param(
        [Alias('os')]
        [String] $operatingSystem = 'windows'
    )
    # Install appium server and client (if already installed; skips)
    Install-AppiumServerAndClient

    # Install appium doctor (if already installed; skips)
    Install-AppiumDoctor

    # Install appium desktop (if already installed; skips)
    Install-AppiumDesktop

    # Show versions after setup
    Test-AppiumDoctor
}

# If already installed or is at latest version, than it will not install/upgrade again.
function Install-AppiumDesktop {
    Write-Host "$("*-" * 32)`nInstalling appium desktop!. Takes some time to install this. Be patient..."
    choco install appium-desktop
    choco upgrade appium-desktop
}

# If already installed or is at latest version, than it will not install/upgrade again.
function Install-AppiumServerAndClient {
    Write-Host "$("*-" * 32)`nInstalling appium server!"
    npm install -g appium

    Write-Host "$("*-" * 32)`nInstalling appium client!"
    npm install wd
}

# If already installed or is at latest version, than it will not install/upgrade again.
function Install-AppiumDoctor {
    Write-Host "$("*-" * 32)`nInstalling appium doctor!"
    npm install -g appium-doctor
}

# Verify the installation
function Test-AppiumDoctor {
    Write-Host "$("*-" * 32)`nTest appium setup!"
    appium-doctor
}

# Tested Okay: [when appium is installed or uninstalled].
# Tested Okay: Script does not crash if already installed.
function Uninstall-Appium {
    Write-Host "$("*-" * 32)`nUninstalling appium-doctor"
    npm uninstall -g appium-doctor

    Write-Host "$("*-" * 32)`nUninstalling appium client"
    npm uninstall wd

    Write-Host "$("*-" * 32)`nUninstalling appium server"
    npm uninstall -g appium

    Write-Host "$("*-" * 32)`nUninstalling appium-desktop"
    choco uninstall appium-desktop
}

# Uninstall in the reverse order of installation (so first installed item is the last to be uninstalled)
function Uninstall-AppiumDependencies {
    # Last time I tested, this did not work. Had to do this manually. Test again.
    Write-Host "$("*-" * 32)`nUninstalling androidstudio"
    choco uninstall androidstudio

    Write-Host "$("*-" * 32)`nUninstalling nodejs; takes a few mins."
    Write-Host "`nBe patient when you see this line. Running auto uninstaller..."
    choco uninstall nodejs.install

    Write-Host "$("*-" * 32)`nUninstalling openjdk8"
    choco uninstall openjdk8

    Write-Host "$("*-" * 32)`nClose this terminal and open a new terminal and run Show-Versions to see that all dependencies are indeed removed."

    # In the end, if for some reasons,you want to uninstall chocolatey as well,
    # follow these instructions. I would recommend not to install chocolatey though.
    # https://docs.chocolatey.org/en-us/choco/uninstallation
}

function Show-Versions {
    Write-Host "Choco version:"
    choco -v

    Write-Host "`nJDK version:"
    java -version

    Write-Host "`nNode and NPM version:"
    node -v
    npm -v

    Write-Host "`nAppium version:"
    appium --version

    Write-Host "`nAndroid Studio version:"
    Get-Content $ENV:ANDROID_HOME\platform-tools\source.properties | Select-String Pkg.Revision
}
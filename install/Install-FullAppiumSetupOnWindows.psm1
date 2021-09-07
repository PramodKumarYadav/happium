# To use this script, open a powershell terminal with elevated access (as administrator).

# Then execute below commands.
# Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope LocalMachine

# ---------------------------------------------------------------------
# NOTE: This above first command is to allow powershell to import and run this module. Else you may get this error
# Import-Module: File D:\happium\install\Install-FullAppiumSetupOnWindows.psm1 cannot be loaded.
# The file D:\happium\install\Install-FullAppiumSetupOnWindows.psm1 is not digitally signed.
# You cannot run this script on the current system.
# For more information about running scripts and setting execution policy, see about_Execution_Policies
# at https://go.microsoft.com/fwlink/?LinkID=135170.
# ---------------------------------------------------------------------

# cd to this project. Say (cd D:\happium\)
# Import-Module .\install\Install-FullAppiumSetupOnWindows.psm1 -Force
# Install-FullAppiumSetupOnWindows

function Install-FullAppiumSetupOnWindows {
    [CmdletBinding()]
    param(
        [Alias('os')]
        [String] $operatingSystem = 'windows'
    )
    # Install chocolatey (the awesome - windows package manager)
    Install-Chocolatey

    # Install open JDK8 (if already installed/upgraded; skips). 
    Install-OpenJDK8

    # Install or upgrade nodejs (if already installed/upgraded; skips). 
    Install-NodeJS

    # Install appium server and client (if already installed; skips) 
    Install-AppiumServerAndClient 

    # Install appium desktop (if already installed; skips) 
    Install-AppiumDesktop

    # Install appium doctor (if already installed; skips) 
    Install-AppiumDoctor

    # Install android studio (if already installed/upgraded; skips). 
    Install-AndroidStudio
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

# Tested okay [when node is installed or uninstalled].
function Install-OpenJDK8 {
    # If already installed or is at latest version, than it will not install/upgrade again. 
    choco install openjdk8
    choco upgrade openjdk8
 }

# Tested okay [when node is installed or uninstalled].
function Install-NodeJS {
    # If already installed or is at latest version, than it will not install/upgrade again. 
    choco install nodejs
    choco upgrade nodejs
 }

# Tested okay [when appium is installed or uninstalled].
function Install-AppiumServerAndClient {
    # Check if appium server is already installed. If not, install. Else skip. 
    $appiumVersion = appium --version | Select-String '.'
    If ($appiumVersion) {
        Write-Host "appium is already installed. Version: $appiumVersion"
    }else {
        # Install appium server 
        npm install -g appium
    }

    # Install appium client (webdriverio) - always- until I find a way to check version and skip if already installed.
    npm install wd
 }

 function Install-AppiumDesktop {
    # If already installed or is at latest version, than it will not install/upgrade again. 
    choco install appium-desktop
    choco upgrade appium-desktop
 }

 # Tested okay [when appium-doctor is installed or uninstalled].
function Install-AppiumDoctor {
    # Check if appium doctor is already installed. If not, install. Else skip. 
    $appiumDoctorVersion = appium-doctor --version | Select-String '.'
    If ($appiumDoctorVersion) {
        Write-Host "appium doctor is already installed. Version: $appiumDoctorVersion"
    }else {
        # Install appium-doctor
        npm install -g appium-doctor
    }
 }

 # Tested okay [when androidstudio is installed or uninstalled].
 function Install-AndroidStudio {
    # Install android studio (if you want to run some virtual emulators for testing and not just real devices)
    # This will also install sdk at the right location: C:\Users\your-user-name\AppData\Local\Android\Sdk\
    # Choco also sets env varibles and saves you the hassle of setting up env variables
    # if you only want to run using real devices than you dont need android studio but sdk will suffice
    # however in that case, you would need to setup the env varaibles though - so below setup is better.
    choco install androidstudio
    choco upgrade androidstudio
 }

 function Test-AppiumSetUp {
    # Verify the installation
    appium-doctor

    # todo: Add checks to take self healing actions if setup is not correct. 
}

# To run server as background. Run below command. By default, it runs the server in the current terminal.
#  Start-AppiumServer -asBackgroundJob
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

# Uninstall in the reverse order of installation (so first installed item is the last to be uninstalled)
 function Uninstall-FullAppiumSetupFromWindows {
     # Uninstall android studio
     choco uninstall androidstudio

     # remove appium-doctor
     npm uninstall -g appium-doctor

     # Uninstall appium desktop
     choco uninstall appium-desktop

    # Uninstall appium server and client
     npm uninstall wd
    npm uninstall -g appium

    # nodejs (npm) work is done. Can uninstall now.
    choco uninstall nodejs

    # uninstall openjdk8
    choco uninstall openjdk8

    # In the end, if for some reasons,you want to uninstall chocolatey as well, 
    # follow these instructions. I would recommend not to install chocolatey though.
    # https://docs.chocolatey.org/en-us/choco/uninstallation
}
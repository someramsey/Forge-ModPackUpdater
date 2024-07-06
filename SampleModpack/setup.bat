@echo off

@REM %1 is the process ID of minecraft
@REM %2 is the path to the game directory


set modsDir="%~2\mods"
set pid=%~1

echo Mods Directory: %modsDir%
echo Waiting for process to end...

@REM Wait for minecraft to close before moving the files because of the file lock

:loop
tasklist /fi "PID eq %pid%" | findstr "%pid%" > nul

if errorlevel 1 ( goto end ) else (
    timeout /t 2 > nul
    goto loop
)

:end

@REM Delete the old files and copy the new installed files to the mods directory

echo Process has ended
timeout /t 2 > nul

echo Deleting old files...

rmdir /s /q %modsDir% 
mkdir %modsDir%

echo Copying new files...

xcopy "data\*" %modsDir% /e /y

echo Installation completed
pause
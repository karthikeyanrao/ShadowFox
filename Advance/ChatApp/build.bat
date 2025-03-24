@echo off
echo Creating bin directory...
mkdir bin 2>nul

echo Compiling source files...
javac -d bin src/common/Message.java
javac -d bin -cp bin src/server/ChatServer.java
javac -d bin -cp bin src/client/ChatClient.java

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo To start the server, run: java -cp bin server.ChatServer
echo To start a client, run: java -cp bin client.ChatClient
echo.
pause 
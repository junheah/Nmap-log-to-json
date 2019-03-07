@echo off

set /p filePath=file path:

echo var data = >> data.js
type %filePath% >> data.js

@echo off
set removeDirectory=%1%
if exist "%removeDirectory%" rmdir /S /Q "%removeDirectory%"
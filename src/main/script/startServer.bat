@echo off
rem ---------------------------------------------------------------------------
rem Start script for Listener
rem ---------------------------------------------------------------------------

set port="%1"
set vide=""
set argC=0
for %%x in (%*) do Set /A argC+=1

IF %argC% GTR 1 goto usage

:java
java -jar lib/subscriber-2.0.jar  %1

:usage
echo + +  usage : %0 [port]
echo +


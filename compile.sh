#!/bin/bash

echo "Compiling CPU Scheduler project..."
cd src
javac scheduler/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run GUI application:"
    echo "  java scheduler.CPUSchedulerGUI"
    echo ""
    echo "To run console application:"
    echo "  java scheduler.CPUSchedulerConsole"
    echo ""
    echo "To run demo:"
    echo "  java scheduler.SchedulerDemo"
else
    echo "Compilation failed!"
fi

cd ..


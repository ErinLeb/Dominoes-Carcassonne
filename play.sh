
compile (){

    echo "Compiling..."

    find -name "*.java" > sources.txt
    javac -d bin @sources.txt
    rm sources.txt

    echo "Done!"

}

run (){

    echo "Launching..."

    if [ "$1" = "terminal" ]; then
        java -cp bin/ TerminalLauncher
    else
        java -cp bin/ GraphicalLauncher
    fi

}

compile && run $1
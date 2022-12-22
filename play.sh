
compile (){

    echo "Compiling..."

    find -name "*.java" > sources.txt
    javac -d bin @sources.txt
    rm sources.txt

    echo "Done!"

}

run (){

    echo "Launching..."

    java -cp bin/ App

}

compile && run
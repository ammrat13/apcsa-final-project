# apcsa-final-project
**FINISHED:** An interactive plotter for complex-valued functions of complex numbers

# Description
This program allows you to plot complex functions. Complex numbers can be colored, and this program leverages this fact. The plot area will, when a function is put in, color each point in the plot area (whose bounds are set by the text boxes around the plot area) the color of its corresponding output. For example, if the function is *f(z) = 2z + 3*, the point corresponding to *1+i* will be colored the color corresponding to *5+2i*. You can also click on the plot area and drag your mouse to see how the output, marked by a green dot, changes with input, marked by a red dot where you clicked. Details about the input and output are displayed on the left hand side.

# To Compile and Run
Compile:
1. `git clone https://github.com/ammrat13/apcsa-final-project`
2. `cd apcsa-final-project/src`
3. `javac -cp . cs/ratnani/Main.java`
4. `mv ../res/ ./res/`
5. `jar -cvfm ../../APCSAFinalProject.jar META-INF/MANIFEST.MF ./*`
6. `cd ../..`
7. `chmod 777 APCSAFinalProject.jar`
8. `rm -rf apcsa-final-project`

Run:
Double-click `APCSAFinalProject.jar` after either compiling it or downloading it from this repository.

# To Use
Type in the function you wish to plot in postfix notation in the text box at the top of the screen. Then, set the area of the plot with the text boxes around the plot area on the right. Then hit "Plot...". The plot will then load and you will see it displayed on the right panel. You may click and drag your mouse over the plot. A red circle will appear at your cursor, representing an input to the function, and a green circle will appear the the point on the plot corresponding to the function's value at the point you clicked. Details about the input and output, such as real part, imaginary part, complex modulus (absolute value), and complex argument (angle) can be seen on the left hand panel.

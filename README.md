# apcsa-final-project
An interactive plotter for complex-valued functions of complex numbers

# Description
This program allows you to plot complex functions. Complex numbers can be colored, and this program leverages this fact. The plot area will, when a function is put in, color each point in the plot area (whose bounds are set by the text boxes around the plot area) the color of its corresponding output. For example, if the function is *f(z) = 2z + 3*, the point corresponding to *1+i* will be colored the color corresponding to *5+2i*. You can also click on the plot area and drag your mouse to see how the output, marked by a green dot, changes with input, marked by a red dot where you clicked. Details about the input and output are displayed on the left hand side.

# To Compile and Run
Run:
1. `git clone https://github.com/ammrat13/apcsa-final-project`
2. `cd apcsa-final-project`
3. `javac -cp src cs/ratnani/Main.java`
4. `mv res/ src/res/`
5. `cd src`
6. `java -cp . cs.ratnani.Main`

# To Use
Type in the function you wish to plot in postfix notation in the text box at the top of the screen. Then, set the area of the plot with the text boxes around the plot area on the right. Then hit "Plot...". The plot will then load and you will see it displayed on the right panel. You may click and drag your mouse over the plot. A red circle will appear at your cursor, respresenting an input to the function, and a green circle will appear the the point on the plot corresponding to the function's value at the point you clicked. Details about the input and output, such as real part, imaginary part, complex modulus (absoulte value), and complex argument (angle) can be seen on the left hand panel.

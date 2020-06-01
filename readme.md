# COVID-19 Risk Calculator
This was made as a proof of concept for Nasa's Space App COVID-19 Challenge.
We created an algorithm based on nighttime light emissions to estimate a risk index. We use the assumption that greater nighttime radiance means greater population as one aspect. We also look at the difference between light activity now compared to average light activity in 2019. This data implies how well a state is curbing social activity. Using both of these numbers, we generate a risk index that is displayed on the UI. Unfortunaly, due to limitations in the GIBS API, we cannot pick locations from coordinates, but we have chosen a couple of regions in the UI to be analyzed, both high and low risk.  

This was built in Java utilizing various libraries and APIs.

### How to Run:
- Clone this repository. 
- Compile the `src/` folder with `javac`, taking care to include all resources and libaries.
- Run RiskCalculator

Alternatively, open this project in an IDE and run through there. The libraries might have to be added to the project in whatever IDE you use.

### Libraries and Services Used:
- ACM Graphics: https://cs.stanford.edu/people/eroberts/jtf/javadoc/student/. Used in creating the UI.
- ImgScalar:  https://github.com/rkalla/imgscalr. Used for processing the images returned by GIBS.
- GIBS: https://wiki.earthdata.nasa.gov/display/GIBS/. Global Imagery Browse Services used to get nightime pictures of the earth.
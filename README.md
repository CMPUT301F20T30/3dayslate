# 3dayslate
CMPUT 301 Group Project

## Installing the app
An apk file can be found under app/build/outputs/apk/debug when you clone the repository to build the app.
Minimum sdk to download: 16

## What the app does
This app is a book sharing service where users can lend and request books from other users. To get started, users must create an account using an email and a unique username. Users can either add a book via scanning or by inputting an ISBN. Books in the app can then be viewed and requested by other users. The app will also handle the process of borrowing and returning books between owners and borrowers.

For more information regarding how the app works, please refer to this video linked here:

Link:

Documentation of development can be found in both the Projects tab and the Wiki tab in the repository.

JavaDocs can be found and downloaded in the /doc folder

## Restrictions

The app can currently only host 1 copy of a book throughout the entire system. This means that two people cannot own the same book in the app at the same time. 

Instrumented Testing in GitHub Actions will have some failures in automated testing due to how the CI testing was set up so it is recommended to try some of the failing tests and run them manually.

Testing was not done for the scanner portion of the app since it was hard set up the camera and testing with CI thus extensive manual testing was done for portions of the app that used this feature.

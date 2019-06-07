-------------------------------------------------------------------------------
PAYSAFE LIBRARY FOR ANDROID
-------------------------------------------------------------------------------


Introduction - Android - Version 2.1.2 - June 2016
--------------------------------------------------

Paysafe Android SDK provides development for Single Use Token API.


Table of Contents
-----------------
1. Installation Guide
2. Pre-requisites
3. Setup configuration
4. Setting up Paysafe Library
5. Content
6. Folder Structure
7. Known Issues
8. Contact Information


1. Installation Guide
---------------------

Please refer to below link to understand and follow steps for Android Studio 
Installation:

http://developer.android.com/sdk/index.html


2. Pre-requisites
-----------------

These are minimum configurations required before getting started with Library.
Following tools should be available before getting started with Paysafe 
Library:
a. Android Studio
b. JDK version 1.7.0
c. Gradle version 1.2.3
d. Android SDK with API Level 15(version 4.0.3) and above.

3. Setup configuration
----------------------
Configure your merchant api key and merchant api password before using paysafe library.
a. Open file 'app\src\main\assets\config.properties'
b. Add your configurations in this file.
c. Save file and proceed to next step.

4. Setting up Paysafe Library
-----------------------------

Getting Started with Paysafe Library on Android Studio.

Following are the steps to guide you to import Paysafe Library on 
Android Studio:
a. Click Android Studio icon to open the framework.
b. Choose "Open an Existing Android Studio Project" and provide the path to 
   Paysafe Library: "\Android SDK\paysafe-lib\android-studio"
c. Steps to run your library.
   Following are steps to setup configuration to build your library:
   a. Go to Run -> Edit Configurations
   b. Choose "Add New Configuration" and select "Gradle"
   c. Name the configuration (for e.g. Name: app)
   d. Select Gradle Project as: android-studio
   e. Under "Before launch" add "Run Gradle Task".
		a. Select Gradle Project as: android-studio:app
		b. Select Tasks and add "build"
		c. Click OK
	f. Click Apply and OK.
	g. Goto Run and select "Run app"
	h. After successful gradle build this library project will generate archive
	   file at the following path:
	   app\build\outputs\aar\
	   

5. Content
----------

Main Components:

a. Customer Vault Service
   - This will contain API service call to Single Use Token.
   
b. Paysafe Api Client
   - Main functionality of this component is to process request to the server
     which contains:
		- This contains connection to server
		- Get Authentication Credentials to process request
		- Function to serialize data
		- Function to de-serialize data
		
c. Environment
   - This will set Environment for the server connection.
   - There are two Environments provided to connect to server:
		a. LIVE (Production URL)
		b. TEST (Beta URL)
	
d. Other Components:
   - Other components include builder classes to build objects and send data to 
     the Paysafe server.
	 
	 
6. Folder Structure
-------------------

a. 'app'     - SDK classes and Application Test classes
b. java-doc  - SDK related documentation


7. Known Issues
---------------

None.


8. Contact Information
----------------------

https://developer.paysafe.com/en/resources-and-support/contact-us/
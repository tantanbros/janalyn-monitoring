# Patient Monitoring App

Once the repository is downloaded follow these steps to install and verify the app is running as expected.

# A) Installation Options

> Try installing using the Release version first and following the other steps below

## Release

- Download this file and send to your phone \$APP_DIRECTORY/app/release/app-release.apk

- Install the apk

- Run the app

### Note: \$APP_DIRECTORY is the base directory of the downloaded repository

## Debug

> Use this method if you want to view the code and run from Android Studio, or try making changes to the app for yourself

- Make sure you have Android Studio Installed

- Open the the project from Android Studio

- Run from android studio

# B) Troubleshooting for Show Monitoring

Once the app is installed, try to create a patient user and click show monitoring. If there is data displayed, then the app is working correctly. Proceed to Section C

> If NOT, Try out these steps below

- Wait around 10-20 seconds, if an error message appears saying

```
  There was a problem retrieving the data
  Please recheck your options and connection
```

- Make sure you are connected to the internet

- From the show monitoring screen, click options, and copy the url in the options or use this [link](https://72ea3862-f049-4cfe-b4fc-02d93e59efaf.mock.pstmn.io/json)

- Open a browser (chrome, or etc.) or RestClient then paste the url and see the results. You should see the following

```
{"data":"0.00,0.00,2.84,181,185,135"}
```

- If you dont see this output, there could be a problem with your internet connection, try changing your dns or reconnecting your connection.

- If you see this output, there could be a problem with the app, try closing the app, and opening it again. Then check show monitoring again

- If re-opening the app doesn't work, try re-installing the app.

- If it still doesn't work, contact us.

# C) Integrating with your device

1. From the show monitoring page, click options

2. Replace the url with the ip address and route of your device

```
ex. http://192.168.0.32/reqData
```

3. Save the options

4. Wait a while to check the results

5. If it doesn't work, verify that your device and the phone is connected on the same network and recheck the ip address.

## Other options

- Try looking at the response of your arduino from RestClient. Using the previously discussed method from the chat. Make sure it gives out a response that is similar to the format

```
{"data":"0.00,0.00,2.84,181,185,135"}
```

- Try Section B

- Contact us

# Thank you

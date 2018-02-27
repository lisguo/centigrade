# CentiGrade

## Getting Started

These instructions will guide you on how to setup the development environment on your local machine.

### Prerequisites

#### Install Gradle

Go to https://gradle.org/install/ and follow the steps for your machine.

#### Clone Repository
```git clone [Repo link.git]```

### Set-Up

#### IntelliJ

##### Import Project

1. Click 'Import Project', select the 'CentiGrade' folder and click OK

2. Select 'Import project from external model' and 'Gradle' and hit Next

3. Check 'Use auto-import', and under Gradle home, select your Gradle source folder. (Ex: C:/gradle-4.5.1)

4. Under Gradle JVM: Use Project JDK and click Finish

##### Build and Run Project

1. Go to Run -> Edit Configurations

2. Click on the green '+' symbol on the top left corner of the window and select 'Gradle'

3. Name this configuration anything you want. (Ex: CentiGrade)

4. For Gradle project: click on the folder icon and click on CentiGrade.

5. For Tasks: type in 'bootRun'

6. Click 'OK'

7. On the top right corner of IntelliJ, you will see your run configuration toolbar. Make sure the drop down menu is on your newly created run configuration and click the green play icon to run the project.

8. Go to http://localhost:8080/

## Created With

- Spring

- Gradle

- MySQL
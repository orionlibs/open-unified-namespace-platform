# UNS shell-based client application
This app requires a credentials file to exist in the home directory of this user,
for example "C:\Users\Jimmy\.uns\credentials" or "/home/Jimmy/.uns/credentials".
The contents of the credentials file will look like:<br>
uns_api_key=ABCD<br>
uns_api_secret=EFGH

To build the shell app run
```shell
$ cd workspaces/my-project/uns-cli
$ ./gradlew clean build
$ java -jar build/libs/uns-cli-0.0.1.jar say-hello Jimmy
```

# fifteen-android-sdk-ble-tech-test

## Requirements

- min Android SDK 21

## Setup

- Create a github.properties file within the project's root
- Add properties gpr.usr=GITHUB_USERID and gpr.key=PERSONAL_ACCESS_TOKEN
- Replace GITHUB_USERID with personal / organisation Github User ID and PERSONAL_ACCESS_TOKEN with a Personal Access Token / password

The personal access token can be generated in Github>Settings>Developer Settings>Personal access token.
The token must have repo and read:packages rights.

Alternatively you can also add the GPR_USER and GPR_API_KEY values to your environment variables on you local machine or build server to avoid creating a github properties file.

## Download

In your module gradle file, setup the github package repository :

```
def githubProperties = new Properties()
def githubPropertiesFiles = rootProject.file("github.properties")

if (githubPropertiesFiles.exists())
    githubProperties.load(new FileInputStream(githubPropertiesFiles))

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/birotaio/fifteen-android-sdk-ble-tech-test")
        credentials {
            username = githubProperties['gpr.usr'] ?: System.getenv("GPR_USER")
            password = githubProperties['gpr.key'] ?: System.getenv("GPR_API_KEY")
        }
    }
}
```

Add the dependency :

```
implementation 'io.birota.zoov.sdkble:sdkble:1.0.0'
```

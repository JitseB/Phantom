<div align="center">
  <img src="https://i.imgur.com/7EgWl6V.png" width="300px" height="300px">
</div>

Phantom – The ultimate Minecraft core.<br>
[![Release](https://jitpack.io/v/JitseB/phantom.svg)](https://github.com/JitseB/phantom/releases) 
[![Build Status](https://travis-ci.org/JitseB/phantom.svg?branch=master)](https://travis-ci.org/JitseB/phantom)
[![Downloads](https://img.shields.io/github/downloads/JitseB/redis-core/total.svg)](https://github.com/JitseB/phantom/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-gray.svg)](https://opensource.org/licenses/Apache-2.0)
=
Phantom is a high performance Minecraft core that allows servers (and networks) to create a manageable, stable and lag-free
gaming experience for their players. It also provides seamlessly integrated administrative tools so that it is easier to 
manage your server as it grows. It contains all features you will ever need in a core, these include features for both Spigot
and BungeeCord. A detailed list of features can be found under the [Features](#features) section.

## Support
### Star the repository
If you do not wish to donate any amount of money, it does not mean that you cannot still suppost Phantom.
There is a star button on this repository, click it. It means a lot to me! :wink:

### Donations
You can donate by clicking on the PayPal link below. 
You can add a message that will be displayed next to your name below.
If you do not want your name to be added, or want it to be removed. 
Either put it in the donation message or send me an [email](mailto:Jitse@fastmail.com).<br><br>
[![paypal](https://cdn.rawgit.com/twolfson/paypal-github-button/1.0.0/dist/button.svg)](https://paypal.me/JitseB)

#### History of donations
*No donations available*

### Servers using Phantom
*No servers available*

If you would like to be added to (or removed from) this list, please do not hesitate to send me an [email](mailto:Jitse@fastmail.com)!

## <a name="features"></a>Features (and roadmap)
:white_check_mark: -> Feature is finished and implemented.<br>
:warning: -> Feature is being implemented and is still experimental.<br>
:x: -> Feature has yet to be implemented.<br>

`To be written later...`

If you have any features you would to see added to Phantom, (you might have guessed it) send me an [email](mailto:Jitse@fastmail.com)! :smile:

## Developers
### Dependency Management
This project supports Maven and Gradle. Other dependency managers may hook into the Maven repository as well.

#### Maven
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
  	<dependency>
	    <groupId>com.github.JitseB</groupId>
	    <artifactId>phantom</artifactId>
	    <version>LATEST_RELEASE</version>
	</dependency>
```
#### Gradle
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  	dependencies {
		compile 'com.github.JitseB:phantom:LATEST_RELEASE'
	}
```
Replace "LATEST_RELEASE" with the actual latest release: [![Release](https://jitpack.io/v/JitseB/phantom.svg)](https://jitpack.io/#JitseB/phantom)<br>
*If you do not use Maven or Gradle, you may download the latest [release](https://github.com/JitseB/phantom/releases) and add it to your build path.*

### API Example
`To be written later...`

## Acknowledgements
We thank all those who have [contributed](https://github.com/JitseB/phantom/graphs/contributors) to the creation of what Phantom is today.

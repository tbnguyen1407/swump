build:
	gradlew clean build
check-update:
	gradlew dependencyUpdates -q
clean:
	gradlew clean
install:
	gradlew clean installDist

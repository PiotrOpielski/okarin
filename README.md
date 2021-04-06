# Okarin

Okarin is a very simple voice-recognition assistant. Or rather it will be.

# How to run it
After cloning this repo download Vosk model of your choice from [here][models] and unpack it in the main folder.
Then simply run 
`gradlew run`

# Configuring
You can configure your own commands in `data/configuration.json`, it works on a basic logic operands so it's pretty straightforward. Then you have to register your function in Register.java. 

[models]: https://alphacephei.com/vosk/models
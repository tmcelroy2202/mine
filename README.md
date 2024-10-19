# mine

Mine Stands for:
MinE
Is
Not
an Emulator

mIne allows you to interface with an externally hosted large language model within Minecraft.

It includes the following features:
* an /aigenerate command which allows you to generate an ai response to any query
* an aigenerate function which can be passed a prompt and used easily to generate ai responses to queries for use by other aspects of the mod 
* the amazing ruby code and filestructure of jakob august's tutorialmod. 
* whatever the rest of my groupmates write which will be later included in this readme


To set up mine, you are going to need to speak to tommy. 

First you need to clone down this repository 

```bash
git clone https://github.com/tmcelroy2202/mine
```

Then you will need to cd into it 

```bash
cd mine
```

Then you will need to create a .env file in the root of that directory 

```bash 
touch .env
```

Now, edit that file with your preferred editor, so that it looks like this (but with the username and password and url you get from talking to me):

```bash
API_USERNAME=theusernametommygivesyou
API_KEY=thepasswordtommygivesyou
URL=http://thesubdomain.mydomain.tld/api/generate
```

Now, to run mINe, do 

```bash
./gradlew runClient
```

in that same directory.
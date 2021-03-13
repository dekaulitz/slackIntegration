# Slack Integration 
This sample application is build on [BOLT framework](https://api.slack.com/tools/bolt) that developed by slack team 
Bolt provided api slack that we can interaction with slack. Before you running the application you need setup some permission for running this application
you need enable some features they are: 
* Incoming webhooks
* Slash command
* Interactive components
* Bot
* Event subscriptions
* Permissions

### Permissions
For permissions you should added some scopes for some features they are:
* app_mention:read => View messages that directly mention @testapps in conversations that the app is in
* channels:history => View messages and other content in public channels that TestApps has been added to
* chat:write => Send messages as the application name
* commands => Add shortcuts and/or slash commands that people can use
* groups:history => View messages and other content in private channels that TestApps has been added to
* im:history => View messages and other content in direct messages that TestApps has been added to
* incoming-webhook => Post messages to specific channels in Slack
* mpim:history => View messages and other content in group direct messages that TestApps has been added to
* users.profile:read => View profile details about people in a workspace

You need Bot User OAuth Token and Signing Secret that available on app credential.

## How its works
Just follow the instruction from slack for creating the application then installing the application on the channel that you want and install it.
After you enabling permission for incoming webhook you can try this curl for testing.
```$xslt
curl --location --request POST '__YOURE_WEBHOOK_URL__' \
--header 'Content-type: application/json' \
--data-raw '{
	"blocks": [
		{
			"type": "section",
			"text": {
				"type": "mrkdwn",
				"text": "You have a new task:\n*Dekaulitz Device Replacement Request*"
			}
		},
		{
			"type": "section",
			"fields": [
				{
					"type": "mrkdwn",
					"text": "*Type:*\nComputer or Laptop."
				},
				{
					"type": "mrkdwn",
					"text": "*Product Name:*\nMacbook pro 2021 13inch."
				},
				{
					"type": "mrkdwn",
					"text": "*Reason:*\nBroken."
				}
			]
		},
		{
			"type": "actions",
			"elements": [
				{
					"type": "button",
					"text": {
						"type": "plain_text",
						"text": "Approve",
						"emoji": true
					},
					"value": "approved",
                     "style":"primary",
					"action_id": "approve-action"
				},
				{
					"type": "button",
					"text": {
						"type": "plain_text",
						"text": "Deny",
						"emoji": true
					},
                   
					"value": "deny",
                     "style":"danger",
					"action_id": "deny-action"
				}
			]
		}
	]
}'
```
For templating the message you can use [Block Kit Template](https://app.slack.com/block-kit-builder/T01R2CPMDUL#%7B%22blocks%22:%5B%5D%7D), block kit is a standard templating for slack that we should use it.
You can use [ngrok](https://ngrok.com/) as your'e development environment that tunneling your localhost application so you can make you local application live as long as your local application running. After that registering you're ngrok domain like 
http://610277c23c39.ngrok.io/slack/events.
You can use the curl above to do some testing.

Have fun and keep learning !!



  


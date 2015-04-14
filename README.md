#CiOne SMS Verification  

This application should be work as SMS Portal that receive notification from CiOne API and automatically sent an SMS message to phone in which need to verify.  

#How does this work
When user login into www.cione.vn/class, they need to fill out phone number and our system will generate a code and then sent it to SMS Portal (this app). SMS Portal's responsibility to sent an SMS to designed phone number.

#How to setup on API
##Data Contract
The data format that API should sent to SMS Portal MUST HAVE basic fields:
```js
{
 code: ["code to verify"],
 phone: ["user's phone number"]
}
```
##GCM Setup
SMS Portal using GCM to receive intend from Google Cloud Messaging (GCM). You have to setup your device registration_id to receive the notification.  

At startup time. SMS Portal need to connect to GCM and generate an Registration_ID for the device. So, make sure you have internet connection to reach the goal.

#Contributors
phongcn (aloola.phong(at)gmail(dot)com)
sangcn (sangcn(at)gmail(dot)com)



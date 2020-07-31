# Social.ly

Team members: Aw Joey(s10196528), Yukie Ang(s10195775), Terris Ng(s10197829), Tan Ming Zhe(S10193694)

## Description
This application is created to allow its users to make new friends.This application is similar to dating applications but the purpose of this application is for users to bring people of similar interest together and join activites created by other users.


## Roles and contribution:
Ming Zhe - Lead Developer </br>
Aw Joey - Lead Developer </br>
Terris - Lead Designer </br>
Yukie - Lead Developer 

### Class file
Yukie: ExploreAdapter.java, ExploreFragment.java, DisplayActivities, DisplayActivitiesAdapter, DisplaySelectedActivity, Activity

MingZhe: Chat, Conversation, CreateActivity, GridFriendAdapter, Message, MessageAdapter, NewChat, UserAdapter, <b>UserChatViewModel, APIService, Client, Data, DirectReplyReceiver, MyFirebaseIdService, MyResponse, OreoNotification, Sender, Token, MyFirebaseMessaging, SplashScreen</b>

Aw Joey: User, FriendsAdapter, FriendsFragment, <b>FriendsInterestAdapter, FriendsInterestViewHolder, NotificationsFriend, NotificationsFragment, NotificationsAdapter, NotificationsFriendsFragment, NotificationsFriendsAdapter, NotificationsFriendsViewHolder</b>

Terris: Register.java, Register2.Java, Login.Java, Start.Java

### Layout file

Yukie: activity_display_selected.xml, create_activity.xml, display_activities.xml, fragment_explore.xml, activityitem.xml, interestitem.xml

MingZhe: Activity_conversation.xml, Activity_message, Activity_NewChat.xml, chat_left.xml, chat_right.xml,gridfrienditem.xml, useritem.xml

Aw Joey: fragment_home.xml, fragment_home_addfriend.xml, fragment_home_alert.xml, <b>fragment_home_addfriend_interest.xml, fragment_notifications.xml, fragment_notifications_activity.xml, fragment_notifications_friends.xml, fragment_notifications_friends_list_item.xml, activity_splash_screen</b>

Terris: activity_login.xml, activity_register.xml, activity_register2.xml, toolbar.xml,topmenu.xml

## Concepts Implemented
1. Responsive layout
2. Firebase Database
3. Sign Up
4. Sign in
5. System Permissions
6. Camera
7. Multimedia
8. Activity, Fragment
9. Intent
10. Toast
11. Dialog
12. Recyclerview
13. TabLayout/ViewPager2
14. Phone Notifications Service
15. Broadcast Receiver
16. HTTP Post

## Relevant appendices

### Splash Screen
This activity shows a loading page with our app logo.</br>
<img src="/Images/SplashScreen" width=40% height= 40%/>
### Welcome Screen
This activity welcomes our users and shows them a welcome message along with a short description of what our app is about. This activity allows the user to register or login.

![Welcome Activity](/Images/Start.JPG)

### Register (1/2)
This activity is the first out of two steps to registering in this app. This screen lets users to enter their name, email, password and birth date. The user is not allowed to proceed to the next step without putting in all the information

![Register Activity](/Images/Register.JPG)

### Register (2/2)
This activity is the second out of two steps to registering in this app. This screen allows users to personalize their profile by uploading their profile image (either taking a picture or uploading an image from the gallery), their nickname, interest, and short description. Upon pressing register, the application uploads the information from the first and second screen to firebase and creates the profile and automatically logs them in.

![Register2 Screen](/Images/Register2.JPG)

### Toolbar
The toolbar replaces the default App bar to give us more customization on the design. This toolbar gives users the option to chat or log out of the app.

![ToolBar](/Images/Toolbar.JPG)

### Add Friend
This fragment displays many users, each in a card. Upon swiping the card, another user will be displayed.
<img src="/Images/Swipe_new_pic.jpeg" width=30% height= 30%/>

By swiping left, the previous user will be removed and another user will be displayed.

By swiping right, one of the two events will occur.
- Event 1: If the displayed user has added the current user as a friend, the displayed user will be removed from the current user's pending friend list, and the displayed user will be added to the current user's friend list and vice versa. An alert message will be displayed. By clicking the "Say Hello" button, the user will be brought to the Message activity.
<img src="/Images/AddFriend_Alert.png" width=40% height= 40%/>
<img src="/Images/AddFriend_SayHello.png" width=40% height= 40%/>
- Event 2: If the displayed user has not added the current user as a friend,  the current user will be added to the displayed user's pending friend list. A toast message will appear, stating that the current user is added to the displayed user's pending friend list.
<img src="/Images/AddFriend_Toast.png" width=40% height= 40%/>

### Conversations
This activity allows the logged in user to see who he has chat with.The button "Start a new chat" allows users to start the NewChat activity.By clicking on a friend, user will be brought to the message activity.</br>
<img src="/Images/Screenshot_20200607_132748_sg.MAD.socially.jpg" width=40% height= 40%/>

### New Chat
This activity will allow users to select a friend to start a conversation. By clicking on a friend the user will be brought to the message activity. </br>
<img src="/Images/Screenshot_20200607_161231_sg.MAD.socially.jpg" width=40% height= 40%/>

### Message
This activity allows user to send a message to a friend and read the message send by this friend.
<img src="/Images/Screenshot_20200607_132832_sg.MAD.socially.jpg" width=40% height= 40%/>

### Create an Activity
This activity allows users to create an activity based on the given fields. </br>
<img src="/Images/Screenshot_20200607_132852_sg.MAD.socially.jpg" width=40% height= 40%/>

### Explore
This fragment allows users to view the different interests on the app. By clicking on any of the interests, users will be brought to the display activities activity.
![ExploreFrgament](Images/Screenshot_1591544326.png)

### Display Activities
This activity allows users to view activities that users created for each interest. If users are interested, they can click on the activity.
![DisplayActivities_Activity](Images/Screenshot_1591544332.png)

### Display Selected Activity
This activity allows user to view the activity they selected. 
(Not implemented yet: Button allows interested user to communicate with user who created the selected activity)
![DisplaySelectedActivity_Activity](Images/Screenshot_1591544338.png)

### Notifications
This fragment uses the ViewPager2 and TabLayout. It displays two tabs, Friends and Activity. Upon clicking the tab or swiping left/right, the page will change to its respective fragment.
### Notifications Friends
This fragment displays the new friends that the user has made and displays the duration added to friend's list. 
<img src="/Images/Rv_friends.png" width=40% height= 40%/>

### Phone Notifications
This service allows users to receive notifications shown on their phone notifications sheet. This applies to any Android API level.
### Phone Friends Notifications
When both users swipe right, the one that swiped right first will receive a notification that tells him/her that he made a new friend.
<img src="/Images/Friendsnotification.png" width=40% height= 40%/>
### Phone Reply Notifications
When a user sends the current user a message, he/she will receive a notification that displays the message contents.
<img src="/Images/Messagenotification.png" width=40% height= 40%/></br>
The current user can click the "Reply" button to send a message back without entering the app. Afterwhich, the message he/she sends will be displayed.</br>
<img src="/Images/Replynotification.png" width=40% height= 40%/>

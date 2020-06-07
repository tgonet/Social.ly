# Social.ly

Team members: Aw Joey(s10196528), Yukie Ang(s10195775), Terris Ng(s10197829), Tan Ming Zhe(S10193694)

## Roles and contribution:

### Class file
Yukie: ExploreAdapter.java,ExploreFragment.java,

MingZhe: Chat, Conversation, CreateActivity, GridFriendAdapter, Message, MessageAdapter, NewChat, Useradapter

Aw Joey: User, FriendsAdapter, FriendsFragment, FriendsViewModel

Terris:

#### Layout file

Yukie: 

MingZhe: Activity_conversation.xml, Activity_message, Activity_NewChat.xml, chat_left.xml, chat_right.xml,gridfrienditem.xml,useritem.xml

Aw Joey: fragment_home.xml, fragment_home_addfriend.xml, fragment_home_alert.xml

Terris:


## Relevant appendices

### Add Friend
This fragment displays many users, each in a card. Upon swiping the card, another user will be displayed.

By swiping left, the previous user will be removed and another user will be displayed.

By swiping right, one of the two events will occur.
- Event 1: If the displayed user has added the current user as a friend, the displayed user will be removed from the current user's pending friend list, and the displayed user will be added to the current user's friend list and vice versa. An alert message will be displayed. By clicking the "Say Hello" button, the user will be brought to the Message activity.
- Event 2: If the displayed user has not added the current user as a friend,  the current user will be added to the displayed user's pending friend list. A toast message will appear, stating that the current user is added to the displayed user's pending friend list.

### Conversations
This activity allows the logged in user to see who he has chat with.The button "Start a new chat" allows users to start the NewChat activity.By clicking on a friend, user will be brought to the message activity.
![Conversation Activity](Images/Screenshot_20200607_132748_sg.MAD.socially.jpg)

### New Chat
This activity will allow users to select a friend to start a conversation. By clicking on a friend the user will be brought to the message activity. 
![Newchat Activity](Images/Screenshot_20200607_161231_sg.MAD.socially.jpg)

### Message
This activity allows user to send a message to a friend and read the message send by this friend.
![Message Activity](Images/Screenshot_20200607_132832_sg.MAD.socially.jpg)

### Create an Activity
This activity allows users to create an activity based on the given fields.
![CreateActivity Activity](Images/Screenshot_20200607_132852_sg.MAD.socially.jpg)

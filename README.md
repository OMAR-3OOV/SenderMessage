# SenderMessage (This project outdated)
Spigot / JDA plugin
____________________________

# Plugin name : SenderMessage

# Versions : v1.0.4

# API'S : SPIGOT API & DISCORD API
____________________________

Some important things :
 - plugin will not loaded if there no "Bot token", make sure to put it in `config file`.
 - You might get some errors in console if user do something wrong in discord command.
 - if the plugin doesn't loaded that means there's some information in `config file` is missed.
 - make SURE to put *JDA library*.jar in your `plugins file`.

Some notes you should know about ( Muted / Leaderboard / Commands ) System :
 - if there is some person get muted and they doesn't longer in the server before leaderboard will print them as `null`, its better to don't mute anyone didn't join the server yet.
 - The only messages is availabe to read is the last five messages user sent in log checker.
 - there is five warning only for player, if they type any blacklisted words,overflows this warnings will let them get muted automatically, and the chance will decrease every time by 4 and it will mute them from the first time automatically! keep in your mind ( if server reloaded.. warnings gonna reset )
Note: all blacklisted words working on discord as well, but without the mute system

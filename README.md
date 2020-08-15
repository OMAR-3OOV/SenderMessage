# SenderMessage
Spigot / JDA plugin
____________________________

# Plugin name : SenderMessage

# Versions : v1.0.4

# API'S : SPIGOT API & DISCORD API
____________________________

Some important things :
 - plugin will not loaded if there no "Bot token" connected in `config file`
 - maybe some errors gonna got it in console if user do something wrong in discord command
 - if plugin doesn't loaded that mean there is some informations in `config file` doesn't completed
 - you should use (DISCORD API).jar in your `plugins file`

Some notes you should know it about ( Muted / Leaderboard / Commands ) System :
 - if there is some person is muted and he doesn't longer in the server before leaderboard will print him `null` for some reason in SPIGOT API
 - only the last five lines can read it in check command
 - there is five warning only for any player if he type any blacklisted words, if he overflows this warnings he gonna get muted, and in next time he    got mute from first time! keep in your mind ( if server reloaded.. warnings gonna reset )
 - be attention the all blacklisted words working in discord too but without muted system

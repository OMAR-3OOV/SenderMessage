package system;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class DiscordCore extends ListenerAdapter {

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        super.onReady(event);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isFake()) return;
        if (event.getAuthor().isBot()) return;
        if (event.getTextChannel().getIdLong() != core.getChannelid()) return;
        core.sendMessageToMinecraft(event.getMessage());
        super.onMessageReceived(event);
    }
}

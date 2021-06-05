package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandTicket extends Command {
    private final AccountManager accountManager;

    public CommandTicket(AccountManager accountManager) {
        super(new String[] {"ticket"}, new String[] {"project-name", "bounty"}, null, false);
        this.accountManager = accountManager;
    }

    //Used to create a ticket. When provided the needed information it will create a new channel for the creator to write up and provide the information to our database
    @Override
    public void onUse(Message query, List<String> arguments, CommandManager commandManager) throws Exception {
        String userid = Objects.requireNonNull(query.getMember()).getId();
        if(arguments.size() == 2) {
                ChannelAction<TextChannel> channel = query.getGuild().createTextChannel(arguments.get(1), query.getGuild().getCategoryById("849658284490752041"));
                channel.addMemberPermissionOverride(query.getMember().getIdLong(), Collections.singleton(Permission.MESSAGE_WRITE), null);
                this.accountManager.openTicket(userid, channel.complete().getId());
                query.getTextChannel().sendMessage("Channel created. Please follow the example channel to get the best results.").queue();
        } else {
            query.getTextChannel().sendMessage("This command was used incorrectly. Please specify the name of your project with no spaces. Ex: `/ticket Gateway-Bot`").queue();
        }

    }
}

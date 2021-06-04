package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandTicket extends Command {
    private AccountManager accountManager;
    public CommandTicket(AccountManager accountManager) {
        super(new String[] {"ticket"}, new String[] {"project-name", "bounty"}, null, false);
        this.accountManager = accountManager;
    }

    @Override
    public void onUse(Message query, List<String> arguments, CommandManager commandManager) throws Exception {
        String userid = Objects.requireNonNull(query.getMember()).getId();
        String name = arguments.get(1);
        int bounty = Integer.parseInt(arguments.get(2));
        int balance = this.accountManager.getBalance(userid);
        if(arguments.size() == 3) {
            if(bounty > balance && Integer.parseInt(arguments.get(2)) > 1) {
                this.accountManager.openTicket(userid, name, bounty);
                this.accountManager.updateBalance(userid, -bounty);
                ChannelAction<TextChannel> channel = query.getGuild().createTextChannel(name, query.getGuild().getCategoryById("849658284490752041"));
                channel.addMemberPermissionOverride(query.getMember().getIdLong(), Collections.singleton(Permission.MESSAGE_WRITE), null).queue();
                query.getTextChannel().sendMessage("Channel created. Please follow the example channel to get the best results.").queue();
            }
        } else {
            query.getTextChannel().sendMessage("This command was used incorrectly. Please specify the name of your project with no spaces. Ex: `/ticket Gateway-Bot`").queue();
        }

    }
}

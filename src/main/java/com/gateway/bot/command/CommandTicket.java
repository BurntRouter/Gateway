package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommandTicket extends ListenerAdapter {
    private final AccountManager accountManager;

    public CommandTicket(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    //Used to create a ticket. When provided the needed information it will create a new channel for the creator to write up and provide the information to our database
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if(event.getName().equals("ticket")) {
            event.deferReply().setEphemeral(true).queue();
            String userid = event.getUser().getId();
            if(!event.getOptions().isEmpty()) {
                ChannelAction<TextChannel> channel = event.getGuild().createTextChannel(event.getOption("project-name").getAsString(), event.getGuild().getCategoryById("849658284490752041"));
                channel.addMemberPermissionOverride(event.getMember().getIdLong(), Collections.singleton(Permission.MESSAGE_WRITE), null);
                try {
                    this.accountManager.openTicket(userid, channel.complete().getId());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                event.getHook().sendMessage("Ticket created. Please follow the <#849719421739204608> for the best results.").setEphemeral(true).queue();
            } else {
                event.getHook().sendMessage("This command was used incorrectly. Please specify the name of your project with no spaces. Ex: `/ticket Gateway-Bot`").setEphemeral(true).queue();
            }
        }
    }
}

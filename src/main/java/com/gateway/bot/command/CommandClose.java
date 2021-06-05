package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class CommandClose extends Command {
    private final AccountManager accountManager;

    public CommandClose(AccountManager accountManager) {
        super(new String[] {"close"}, new String[] {"userid (optional)"}, null);
        this.accountManager = accountManager;
    }

    @Override
    public void onUse(Message query, List<String> arguments, CommandManager commandManager) throws Exception {
        String userid = query.getAuthor().getId();
        if(query.getCategory().getId().contains("849658284490752041")) {
            if(this.accountManager.getTicketOwner(query.getTextChannel().getId()).contains(userid)) {
                if(arguments.size() < 3) {
                    query.getTextChannel().sendMessage("Please include a rating on a scale of 1/10 as well as a review. Ex: `/close 10 Router did a great job at writing the bot!`").queue();
                } else {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(query.getAuthor().getAsTag());
                    StringBuilder stringBuilder = new StringBuilder();
                    for(String string : arguments.subList(2, arguments.size())){
                        stringBuilder.append(string + " ");
                    }
                    embedBuilder.setDescription("Review: " + stringBuilder);
                    embedBuilder.setFooter("Rating (1/10): " + arguments.get(1));
                    embedBuilder.setColor(Color.GREEN);
                    query.getGuild().getTextChannelById("850541536936722443").sendMessage(embedBuilder.build()).queue();
                    query.getTextChannel().delete().queue();
                }
            }
        }  else {
                query.getTextChannel().sendMessage("You have either used this command in error or you do not have permission to close the ticket.").queue();
        }
    }
}

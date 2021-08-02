package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;

public class CommandClose extends ListenerAdapter {
    private final AccountManager accountManager;

    public CommandClose(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    //Closes a ticket, sends a review, and pays out if necessary
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String channelid = event.getTextChannel().getId();
        if(this.getTicketOwner(channelid) == event.getUser().getId() && ) {

        }

        if(this.accountManager.getBounty(channelid) > 0) {
            try {
                String creator = this.accountManager.getTicketOwner(channelid);
                String freelancer = this.accountManager.getFreelancer(channelid);
                int bounty = this.accountManager.getBounty(channelid);

                if(event..getId().contains("849658284490752041") && this.accountManager.getBalance(creator) >= bounty) {
                    if(this.accountManager.getTicketOwner(channelid).contains(query.getAuthor().getId())) {
                        if(arguments.size() < 3) {
                            query.getTextChannel().sendMessage("Please include a rating on a scale of 1/10 as well as a review. Ex: `/close 10 Router did a great job at writing the bot!`").queue();
                        } else {
                            this.accountManager.updateBalance(creator, -bounty);
                            this.accountManager.updateBalance(freelancer, bounty);
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle(query.getGuild().getMemberById(freelancer).getAsMention());
                            StringBuilder stringBuilder = new StringBuilder();
                            for(String string : arguments.subList(2, arguments.size())){
                                stringBuilder.append(string + " ");
                            }
                            embedBuilder.setDescription("Review: " + stringBuilder);
                            embedBuilder.setFooter("Rating (1/10): " + arguments.get(1));
                            embedBuilder.setColor(Color.GREEN);
                            embedBuilder.setAuthor(query.getGuild().getMemberById(creator).getAsMention());
                            query.getGuild().getTextChannelById("850541536936722443").sendMessage(embedBuilder.build()).queue();
                            query.getTextChannel().delete().queue();
                        }
                    }
                }  else {
                    query.getTextChannel().sendMessage("You have either used this command in error, you do not have permission to close the ticket, or you do not have enough funds to complete payment.").queue();
                }
            } catch (Exception e) {

            }
        } else {
        query.getTextChannel().delete().queue();
        }

    }

    public String getTicketOwner(String channelid) {
        try {
            return this.accountManager.getTicketOwner(channelid);
        } catch (Exception e) {
            return null;
        }
    }

    public void completePayment(String channelid) {
        try {
            int bounty = this.accountManager.getBounty(channelid);
            String owner = this.getTicketOwner(channelid);
            String freelancer = this.accountManager.getFreelancer(channelid);
            this.accountManager.updateBalance(owner, -bounty);
            this.accountManager.updateBalance(freelancer, bounty);
        } catch (Exception e) {

        }
    }
}

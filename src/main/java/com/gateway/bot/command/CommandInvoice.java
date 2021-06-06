package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandInvoice extends Command{
    private final AccountManager accountManager;

    public CommandInvoice(AccountManager accountManager) {
        super(new String[] {"invoice"}, new String[] {"amount"}, "Creates the amount to be paid out when the ticket is closed.");
        this.accountManager = accountManager;
    }

    @Override
    public void onUse(Message query, List<String> arguments, CommandManager commandManager) throws Exception {
        try {
            int bounty = Integer.valueOf(arguments.get(1));
            String ticketOwner = this.accountManager.getTicketOwner(query.getTextChannel().getId());
            String freelancer = this.accountManager.getFreelancer(query.getTextChannel().getId());
            this.accountManager.setBounty(query.getTextChannel().getId(), bounty);
            if(this.accountManager.getFreelancer(query.getTextChannel().getId()).contains(query.getAuthor().getId())) {
                query.getTextChannel().sendMessage("Attention " + query.getGuild().getMemberById(ticketOwner).getAsMention() + "the bounty has been set by " +
                        query.getGuild().getMemberById(freelancer).getAsMention() + " to be " + bounty + " . " +
                        "To close the ticket and pay this please use `/close [rating 1/10] [review]. " +
                        "If there is an issue with the ticket or payment please ping the managers.").queue();
            } else {
                query.getTextChannel().sendMessage("Only the freelancer that has claimed this ticket may invoice it.").queue();
            }
        } catch (Exception e) {
            query.getTextChannel().sendMessage("This command has been used incorrectly. If you are the freelancer please use `/invoice [amount]`").queue();
        }
    }
}

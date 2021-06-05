package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandClaim extends Command{
    private final AccountManager accountManager;

    public CommandClaim(AccountManager accountManager) {
        super(new String[] {"claim"}, null, "Allows you to claim a ticket to be completed");
        this.accountManager = accountManager;
    }

    //Allows a freelancer to claim a ticket they would like to complete
    @Override
    public void onUse(Message query, List<String> arguments, CommandManager commandManager) throws Exception {
        if(!this.accountManager.getTicketOwner(query.getTextChannel().getId()).contains(query.getAuthor().getId())){
            if(this.accountManager.getFreelancer(query.getTextChannel().getId()) == null) {
                this.accountManager.setFreelancer(query.getTextChannel().getId(), query.getAuthor().getId());
                query.getTextChannel().sendMessage("You have claimed this ticket! To release the claim use `/release`").queue();
            } else {
                query.getTextChannel().sendMessage("This ticket has already been claimed.").queue();
            }
        } else {
                query.getTextChannel().sendMessage("You can't claim your own ticket!").queue();
        }
    }
}

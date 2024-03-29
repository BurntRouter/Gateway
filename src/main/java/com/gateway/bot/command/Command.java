package com.gateway.bot.command;

import java.util.List;

import net.dv8tion.jda.api.entities.Message;

public abstract class Command {

    private final String[] identifiers;

    private final String[] arguments;

    private String description;

    private boolean modCommand;

    private boolean requiresWritePermission = true;

    public String[] getIdentifiers() {
        return identifiers;
    }

    public Command(String[] identifiers, String[] arguments, String description){
        this.identifiers = identifiers;
        this.arguments = arguments;
        this.description = description;
    }

    public Command(String[] identifiers, String [] arguments, String description, Boolean modCommand){
        this.identifiers = identifiers;
        this.arguments = arguments;
        this.description = description;
        this.modCommand = modCommand;
    }

    public boolean identifierMatches(String identifier) {
        for (String potentialMatchIdentifier : this.getIdentifiers()) {
            if (potentialMatchIdentifier.equalsIgnoreCase(identifier)) {
                return true;
            }
        }
        return false;
    }

    public abstract void onUse(Message query, List<String> arguments, CommandManager commandManager) throws Exception;

    public String[] getArguments() {
        return arguments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequiresWritePermission(boolean requiresWritePermission) {
        this.requiresWritePermission = requiresWritePermission;
    }

    public boolean requiresWritePermission() {
        return this.requiresWritePermission;
    }

    public boolean hasArguments() {
        return this.getArguments().length > 0;
    }

    public boolean modCommand() {
        return this.modCommand;
    }

}
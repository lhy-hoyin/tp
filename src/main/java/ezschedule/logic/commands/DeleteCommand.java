package ezschedule.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import ezschedule.commons.core.Messages;
import ezschedule.commons.core.index.Index;
import ezschedule.logic.commands.exceptions.CommandException;
import ezschedule.model.Model;
import ezschedule.model.event.Event;

/**
 * Deletes an event identified using it's displayed index from the scheduler.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the event identified by the index number used in the displayed event list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Event: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Event> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        Event eventToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteEvent(eventToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteCommand // instanceof handles nulls
            && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }
}
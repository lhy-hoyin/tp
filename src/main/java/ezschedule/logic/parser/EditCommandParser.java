package ezschedule.logic.parser;

import static java.util.Objects.requireNonNull;

import ezschedule.commons.core.Messages;
import ezschedule.commons.core.index.Index;
import ezschedule.logic.commands.EditCommand;
import ezschedule.logic.commands.EditCommand.EditEventDescriptor;
import ezschedule.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object.
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_DATE,
                    CliSyntax.PREFIX_START, CliSyntax.PREFIX_END);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE),
                    pe);
        }

        EditEventDescriptor editEventDescriptor = new EditEventDescriptor();
        if (argMultimap.getValue(CliSyntax.PREFIX_NAME).isPresent()) {
            editEventDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(CliSyntax.PREFIX_DATE).isPresent()) {
            editEventDescriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(CliSyntax.PREFIX_DATE).get()));
        }
        if (argMultimap.getValue(CliSyntax.PREFIX_START).isPresent()) {
            editEventDescriptor.setStartTime(ParserUtil.parseTime(argMultimap.getValue(CliSyntax.PREFIX_START).get()));
        }
        if (argMultimap.getValue(CliSyntax.PREFIX_END).isPresent()) {
            editEventDescriptor.setEndTime(ParserUtil.parseTime(argMultimap.getValue(CliSyntax.PREFIX_END).get()));
        }

        if (!editEventDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editEventDescriptor);
    }
}
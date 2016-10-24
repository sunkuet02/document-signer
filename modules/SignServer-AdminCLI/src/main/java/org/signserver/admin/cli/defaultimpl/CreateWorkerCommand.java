package org.signserver.admin.cli.defaultimpl;

import org.signserver.cli.spi.CommandFailureException;
import org.signserver.cli.spi.IllegalCommandArgumentsException;
import org.signserver.cli.spi.UnexpectedCommandFailureException;
import java.util.Properties;

/**
 * Created by sun on 9/27/16.
 */
public class CreateWorkerCommand extends AbstractAdminCommand {
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsages() {
        return "Usage: signserver createworker -name WORKERGENID -defaultkey DefaultKeyName -workerclasspath WorkerClassPath\n";
    }

    @Override
    public int execute(String... args) throws IllegalCommandArgumentsException, CommandFailureException, UnexpectedCommandFailureException {
        if(args.length % 2 != 0) {
            throw new IllegalCommandArgumentsException("Wrong number of arguments");
        }
        try {

            SetPropertiesHelper helper = new SetPropertiesHelper(getOutputStream());
            Properties properties = new Properties();

            int counterPoint = 0 ;
            String key = null, value = null;
            for (String keyValue : args) {
                if (counterPoint %2 ==0 && !keyValue.startsWith("-")) {
                    throw new UnexpectedCommandFailureException("Failed for key Value "+ keyValue, null);
                }
                if(counterPoint % 2 == 0) {
                    key = keyValue.substring(1);
                }
                else {
                    value = keyValue;
                    properties.setProperty(key, value);
                }
                counterPoint ++ ;
            }

            helper.process(properties);
            this.getOutputStream().println("\n\n");

            return 0;
        } catch (Exception e) {
            throw new UnexpectedCommandFailureException(e);
        }
    }
}
